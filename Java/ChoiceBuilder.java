import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ChoiceBuilder {
    private final DialogueBuilder parent;
    private final String prompt;
    private final List<DialogueOption> options = new ArrayList<>();

    ChoiceBuilder(DialogueBuilder parent, String prompt) {
        this.parent = parent;
        this.prompt = prompt;
    }

    public ChoiceBuilder option(String label) {
        return addOption(label, null, null);
    }

    public ChoiceBuilder option(String label, Consumer<DialogueBuilder> branch) {
        return addOption(label, null, branch);
    }

    public ChoiceBuilder option(String label, Supplier<Boolean> condition, Consumer<DialogueBuilder> branch) {
        return addOption(label, condition, branch);
    }

    public ChoiceBuilder option(String label, Supplier<Boolean> condition) {
        return addOption(label, condition, null);
    }

    private ChoiceBuilder addOption(String label, Supplier<Boolean> condition, Consumer<DialogueBuilder> action) {
        Dialogue branch = null;

        if (action != null) {
            DialogueBuilder builder = new DialogueBuilder();

            action.accept(builder);
            branch = builder.build();
        }

        options.add(new DialogueOption(label, condition, branch));

        return this;
    }

    public DialogueBuilder end() {
        parent.addStep(new DialogueSteps.Choice(prompt, List.copyOf(options)));

        return parent;
    }
}