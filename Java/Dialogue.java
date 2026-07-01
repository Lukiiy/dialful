import java.util.List;

public final class Dialogue {
    private final List<DialogueSteps.IStep> steps;
    private final Runnable onEnd;

    Dialogue(List<DialogueSteps.IStep> steps, Runnable onEnd) {
        this.steps = List.copyOf(steps);
        this.onEnd = onEnd;
    }

    public static DialogueBuilder create() {
        return new DialogueBuilder();
    }

    public List<DialogueSteps.IStep> steps() {
        return steps;
    }

    public DialogueResult play(DialogueRenderer renderer) {
        return new DialogueRuntime(renderer).execute(this);
    }

    void end() {
        if (onEnd != null) onEnd.run();
    }
}