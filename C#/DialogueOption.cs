namespace Dialogue;

public sealed class DialogueOption(string label, Func<bool>? condition, Dialogue? branch)
{
    public string Label { get; } = label;
    public Func<bool>? Condition { get; } = condition;
    public Dialogue? Branch { get; } = branch;

    public bool Available => Condition == null || Condition();
}
