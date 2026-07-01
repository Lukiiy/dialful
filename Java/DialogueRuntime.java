import java.time.Duration;
import java.util.List;

public final class DialogueRuntime {
    private final DialogueRenderer renderer;

    public DialogueRuntime(DialogueRenderer renderer) {
        this.renderer = renderer;
    }

    public DialogueResult execute(Dialogue dialogue) {
        DialogueResult result = new DialogueResult();

        runSteps(dialogue.steps(), result);

        result.setCompleted(true);
        dialogue.triggerEnd();

        return result;
    }

    private void runSteps(List<DialogueSteps.IStep> steps, DialogueResult result) {
        for (DialogueSteps.IStep step : steps) if (!runStep(step, result)) return;
    }

    private boolean runStep(DialogueSteps.IStep step, DialogueResult result) {
        if (step instanceof DialogueSteps.Say(String text)) {
            renderer.renderText(text);
            return true;
        }

        if (step instanceof DialogueSteps.Pause(Duration duration)) {
            renderer.wait(duration);
            return true;
        }

        if (step instanceof DialogueSteps.End) return false;

        if (step instanceof DialogueSteps.Choice choice) {
            runChoice(choice, result);
            return true;
        }

        return true;
    }

    private void runChoice(DialogueSteps.Choice choice, DialogueResult result) {
        List<DialogueOption> available = choice.options().stream().filter(DialogueOption::isAvailable).toList();
        if (available.isEmpty()) return;

        renderer.renderChoice(choice.prompt(), available.stream().map(DialogueOption::label).toList());

        DialogueOption selected = available.get(renderer.getChoiceInput(available.size()));

        result.recordChoice(choice.prompt(), selected.label());

        if (selected.branch() != null) runSteps(selected.branch().steps(), result);
    }
}