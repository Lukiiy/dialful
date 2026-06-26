import java.util.List;

public record ChoiceStep(String prompt, List<DialogueOption> options) implements DialogueStep {}