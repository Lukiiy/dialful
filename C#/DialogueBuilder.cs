namespace Dialful;

public sealed class DialogueBuilder
{
    private readonly List<IDialogueStep> Steps = [];
    private Action? onEnd;

    public DialogueBuilder Say(string text)
    {
        Steps.Add(new SayStep(text));

        return this;
    }

    public DialogueBuilder Pause(TimeSpan duration)
    {
        Steps.Add(new PauseStep(duration));

        return this;
    }

    public ChoiceBuilder Choice(string prompt) => new ChoiceBuilder(this, prompt);

    public DialogueBuilder EndDialogue()
    {
        Steps.Add(EndStep.Instance);

        return this;
    }

    public DialogueBuilder OnEnd(Action callback)
    {
        onEnd = callback;

        return this;
    }

    public Dialogue Build() => new Dialogue(Steps.AsReadOnly(), onEnd);

    internal DialogueBuilder AddStep(IDialogueStep step)
    {
        Steps.Add(step);
        return this;
    }
}