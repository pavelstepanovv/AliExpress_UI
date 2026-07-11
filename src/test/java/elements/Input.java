package elements;

public class Input extends BaseElement {

    private static final String INPUT_BY_NAME_XPATH = "//input[@name='%s']";
    private static final String INPUT_BY_ID_XPATH = "//input[@id='%s']";

    private Input(String xpath, String value) {
        super(xpath, value);
    }

    public void fill(String text) {
        // Блок написан Шакуров 4382: setValue стабильнее работает с React-полями AliExpress, чем clear + sendKeys.
        element.setValue(text);
    }

    public static Input byName(String name) {
        return new Input(INPUT_BY_NAME_XPATH, name);
    }

    public static Input byId(String id) {
        return new Input(INPUT_BY_ID_XPATH, id);
    }

    public void clear() {
        element.clear();
    }
}
