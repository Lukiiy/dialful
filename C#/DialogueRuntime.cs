namespace Dialogue;

public sealed class DialogueRuntime(IDialogueRenderer renderer)
{
    private readonly IDialogueRenderer Renderer = renderer;

    public DialogueResult Execute(Dialogue dialogue)
    {
        DialogueResult result = new DialogueResult();

        RunSteps(dialogue.Steps, result);

        result.Completed = true;
        dialogue.TriggerEnd();

        return result;
    }

    private void RunSteps(IReadOnlyList<IDialogueStep> steps, DialogueResult result)
    {
        foreach (IDialogueStep step in steps)
        {
            if (!RunStep(step, result)) return;
        }
    }

    private bool RunStep(IDialogueStep step, DialogueResult result)
    {
        switch (step)
        {
            case SayStep say:
                Renderer.RenderText(say.Text);
                return true;

            case PauseStep pause:
                Renderer.Wait(pause.Duration);
                return true;

            case EndStep:
                return false;

            case ChoiceStep choice:
                RunChoice(choice, result);
                return true;

            default:
                return true; // skip unknown types
        }
    }

    private void RunChoice(ChoiceStep choice, DialogueResult result)
    {
        List<DialogueOption> available = choice.Options.Where(o => o.IsAvailable).ToList();

        if (available.Count == 0) return; // auto-advance if no options available

        Renderer.RenderChoice(choice.Prompt, available.Select(o => o.Label).ToList());

        DialogueOption selected = available[Renderer.GetChoiceInput(available.Count)];

        result.RecordChoice(choice.Prompt, selected.Label);

        if (selected.Branch != null) RunSteps(selected.Branch.Steps, result);
    }
}
