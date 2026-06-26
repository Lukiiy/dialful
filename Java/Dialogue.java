import java.util.List;

public final class Dialogue {
    private final List<DialogueStep> steps;
    private final Runnable onEnd;

    Dialogue(List<DialogueStep> steps, Runnable onEnd) {
        this.steps = List.copyOf(steps);
        this.onEnd = onEnd;
    }

    public static DialogueBuilder create() {
        return new DialogueBuilder();
    }

    public DialogueResult play(DialogueRenderer renderer) {
        return new DialogueRuntime(renderer).execute(this);
    }

    public void triggerEnd() {
        if (onEnd != null) onEnd.run();
    }

    public List<DialogueStep> steps() {
        return steps;
    }
}