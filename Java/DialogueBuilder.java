import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public final class DialogueBuilder {
    private final List<DialogueSteps.IStep> steps = new ArrayList<>();
    private Runnable onEnd;

    public DialogueBuilder say(String text) {
        steps.add(new DialogueSteps.Say(text));
        return this;
    }

    public DialogueBuilder pause(Duration duration) {
        steps.add(new DialogueSteps.Pause(duration));
        return this;
    }

    public ChoiceBuilder choice(String prompt) {
        return new ChoiceBuilder(this, prompt);
    }

    public DialogueBuilder endDialogue() {
        steps.add(DialogueSteps.End.INSTANCE);
        return this;
    }

    public DialogueBuilder onEnd(Runnable callback) {
        this.onEnd = callback;
        return this;
    }

    public Dialogue build() {
        return new Dialogue(steps, onEnd);
    }

    DialogueBuilder addStep(DialogueSteps.IStep step) {
        steps.add(step);
        return this;
    }
}