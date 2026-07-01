import java.util.List;

public final class DialogueRuntime {
    private final DialogueRenderer renderer;

    public DialogueRuntime(DialogueRenderer renderer) {
        this.renderer = renderer;
    }

    public DialogueResult execute(Dialogue dialogue) {
        DialogueResult result = new DialogueResult();

        run(dialogue.steps(), result);
        result.complete();
        dialogue.end();

        return result;
    }

    private boolean run(List<DialogueSteps.IStep> steps, DialogueResult result) {
        for (DialogueSteps.IStep step : steps) if (!step(step, result)) return false;

        return true;
    }

    private boolean step(DialogueSteps.IStep step, DialogueResult result) {
        return switch (step) {
            case DialogueSteps.Say(var text) -> {
                renderer.renderText(text);
                yield true;
            }

            case DialogueSteps.Pause(var d) -> {
                renderer.wait(d);
                yield true;
            }

            case DialogueSteps.End _ -> false;

            case DialogueSteps.Choice(var prompt, var options) -> {
                choice(prompt, options, result);
                yield true;
            }
        };
    }

    private void choice(String prompt, List<DialogueOption> options, DialogueResult result) {
        var available = options.stream().filter(DialogueOption::available).toList();
        if (available.isEmpty()) return;

        renderer.renderChoice(prompt, available.stream().map(DialogueOption::label).toList());

        DialogueOption selected = available.get(renderer.getChoiceInput(available.size()));

        result.record(prompt, selected.label());
        if (selected.branch() != null) run(selected.branch().steps(), result);
    }
}