import java.time.Duration;
import java.util.List;

public class DialogueSteps {
    public interface IStep {}

    public record Say(String text) implements IStep {}

    public record Pause(Duration duration) implements IStep {}

    public record End() implements IStep {
        public static final End INSTANCE = new End();
    }

    public record Choice(String prompt, List<DialogueOption> options) implements IStep {}
}
