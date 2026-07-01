import java.util.function.Supplier;

public record DialogueOption(String label, Supplier<Boolean> condition, Dialogue branch) {
    public boolean available() {
        return condition == null || condition.get();
    }
}