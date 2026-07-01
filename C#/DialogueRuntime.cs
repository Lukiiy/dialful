namespace Dialful;

public sealed class DialogueRuntime(IDialogueRenderer renderer)
{
    private readonly IDialogueRenderer Renderer = renderer;

    public DialogueResult Execute(Dialogue dialogue)
    {
        DialogueResult result = new DialogueResult();

        Run(dialogue.Steps, result);
        result.Completed = true;
        dialogue.End();

        return result;
    }

    private bool Run(IReadOnlyList<IDialogueStep> steps, DialogueResult result)
    {
        foreach (IDialogueStep step in steps) if (!Step(step, result)) return false;

        return true;
    }

    private bool Step(IDialogueStep step, DialogueResult result)
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
                Choice(choice.Prompt, choice.Options.ToList(), result);
                return true;

            default:
                return true; // skip unknown types
        }
    }

    private void Choice(string prompt, List<DialogueOption> options, DialogueResult result)
    {
        var available = options.Where(o => o.Available).ToList();
        if (available.Count == 0) return; // skip if no options available

        Renderer.RenderChoice(prompt, available.Select(o => o.Label).ToList());

        DialogueOption selected = available[Renderer.GetChoiceInput(available.Count)];

        result.RecordChoice(prompt, selected.Label);
        if (selected.Branch != null) Run(selected.Branch.Steps, result);
    }
}
