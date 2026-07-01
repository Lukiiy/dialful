namespace DialogueSystem;

public sealed class DialogueResult
{
    private readonly Dictionary<string, string> Choices = [];
    public bool Completed { get; internal set; }

    public string? Choice(string prompt) => Choices.TryGetValue(prompt, out string? val) ? val : null;

    public IReadOnlyDictionary<string, string> AllChoices => Choices;

    internal void RecordChoice(string prompt, string selectedLabel) => Choices[prompt] = selectedLabel;
}