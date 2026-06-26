using System.Collections.Generic;

namespace DialogueSystem;

public interface IDialogueStep {}

public sealed class SayStep(string text) : IDialogueStep
{
    public string Text { get; } = text;
}

public sealed class PauseStep(int duration) : IDialogueStep
{
    public int Duration { get; } = duration; // in milliseconds
}

public sealed class EndStep : IDialogueStep
{
    public static readonly EndStep Instance = [];

    private EndStep() {} // prevent from new instances
}

public sealed class ChoiceStep(string prompt, IReadOnlyList<DialogueOption> options) : IDialogueStep
{
    public string Prompt { get; } = prompt;
    public IReadOnlyList<DialogueOption> Options { get; } = options;
}