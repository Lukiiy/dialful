namespace Dialogue;

public interface IDialogueRenderer
{
    void RenderText(string text);
    void RenderChoice(string prompt, IReadOnlyList<string> availableOptions);
    void Clear();
    void Wait(int durationMs);

    /// Block until the player picks an option.
    int GetChoiceInput(int optionCount);
}
