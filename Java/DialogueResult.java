import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class DialogueResult {
    private final Map<String, String> choices = new LinkedHashMap<>();
    private boolean completed;

    public boolean completed() {
        return completed;
    }

    void complete() {
        completed = true;
    }

    public String choice(String prompt) {
        return choices.get(prompt);
    }

    public Map<String, String> choices() {
        return Collections.unmodifiableMap(choices);
    }

    void record(String prompt, String value) {
        choices.put(prompt, value);
    }
}