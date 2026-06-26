import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class DialogueResult {
    private final Map<String, String> choices = new LinkedHashMap<>();
    private boolean completed;

    public boolean completed() {
        return completed;
    }

    public void setCompleted(boolean value) {
        this.completed = value;
    }

    public String choice(String prompt) {
        return choices.get(prompt);
    }

    public Map<String, String> allChoices() {
        return Collections.unmodifiableMap(choices);
    }

    void recordChoice(String prompt, String selectedLabel) {
        choices.put(prompt, selectedLabel);
    }
}