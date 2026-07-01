namespace Dialful;

public sealed class ChoiceBuilder
{
    private readonly DialogueBuilder parent;
    private readonly string prompt;
    private readonly List<DialogueOption> options = [];

    internal ChoiceBuilder(DialogueBuilder parent, string prompt)
    {
        this.parent = parent;
        this.prompt = prompt;
    }

    public ChoiceBuilder Option(string label) => AddOption(label, null, null);
    public ChoiceBuilder Option(string label, Action<DialogueBuilder> branch) => AddOption(label, null, branch);
    public ChoiceBuilder Option(string label, Func<bool> condition, Action<DialogueBuilder> branch) => AddOption(label, condition, branch);
    public ChoiceBuilder Option(string label, Func<bool> condition) => AddOption(label, condition, null);

    private ChoiceBuilder AddOption(string label, Func<bool>? condition, Action<DialogueBuilder>? action)
    {
        Dialogue? branch = null;

        if (action != null)
        {
            DialogueBuilder builder = new DialogueBuilder();

            action(builder);
            branch = builder.Build();
        }

        options.Add(new DialogueOption(label, condition, branch));
        return this;
    }

    public DialogueBuilder End()
    {
        parent.AddStep(new ChoiceStep(prompt, options.AsReadOnly()));

        return parent;
    }
}