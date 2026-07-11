package elements;

public class Button extends BaseElement {

    private static final String BUTTON_BY_TEXT_XPATH = "//button[contains(text(), '%s')]";
    private static final String BUTTON_BY_CLASS_XPATH = "//button[contains(@class, '%s')]";
    private static final String BUTTON_BY_TYPE_XPATH = "//button[@type='%s']";

    private Button(String xpath, String value) {
        super(xpath, value);
    }

    public static Button byText(String text) {
        return new Button(BUTTON_BY_TEXT_XPATH, text);
    }

    public static Button byClass(String className) {
        return new Button(BUTTON_BY_CLASS_XPATH, className);
    }

    public static Button byType(String type) {
        return new Button(BUTTON_BY_TYPE_XPATH, type);
    }
}