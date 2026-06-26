import java.time.Duration;
import java.util.List;

public interface DialogueRenderer {
    void renderText(String text);
    void renderChoice(String prompt, List<String> options);
    void clear();
    void wait(Duration duration);

    int getChoiceInput(int optionCount);
}