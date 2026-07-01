namespace DialogueSystem;

public sealed class Dialogue
{
    public IReadOnlyList<IDialogueStep> Steps { get; }
    private readonly Action? onEnd;

    internal Dialogue(IReadOnlyList<IDialogueStep> steps, Action? onEnd)
    {
        this.Steps = steps;
        this.onEnd = onEnd;
    }

    public static DialogueBuilder Create() => new DialogueBuilder();
    public DialogueResult Play(IDialogueRenderer renderer) => new DialogueRuntime(renderer).Execute(this);

    internal void TriggerEnd() => onEnd?.Invoke();
}
