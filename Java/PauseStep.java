import java.time.Duration;

public record PauseStep(Duration duration) implements DialogueStep {}
