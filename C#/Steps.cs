namespace Dialogue;

public interface IDialogueStep {}

public sealed class SayStep(string text) : IDialogueStep
{
    public string Text { get; } = text;
}

public sealed class PauseStep(TimeSpan duration) : IDialogueStep
{
    public TimeSpan Duration { get; } = duration;
}

public sealed class EndStep : IDialogueStep
{
    public static readonly EndStep Instance = new();

    private EndStep() {} // prevent from new instances
}

public sealed class ChoiceStep(string prompt, IReadOnlyList<DialogueOption> options) : IDialogueStep
{
    public string Prompt { get; } = prompt;
    public IReadOnlyList<DialogueOption> Options { get; } = options;
}