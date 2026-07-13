package elements;

/**
 * Класс поля ввода, наследуемый от базового класса элемента.
 * Автор: Шакуров 4382.
 */
public class Input extends BaseElement {

    private static final String INPUT_BY_NAME_XPATH = "//input[@name='%s']";
    private static final String INPUT_BY_ID_XPATH = "//input[@id='%s']";

    private Input(String xpath, String value) {
        super(xpath, value);
    }

    /**
     * Очищает поле и вводит новый текст.
     */
    public void fill(String text) {
        element.setValue(text);
    }

    /**
     * Создает поле ввода по атрибуту name.
     */
    public static Input byName(String name) {
        return new Input(INPUT_BY_NAME_XPATH, name);
    }

    /**
     * Создает поле ввода по атрибуту id.
     */
    public static Input byId(String id) {
        return new Input(INPUT_BY_ID_XPATH, id);
    }

    /**
     * Очищает поле ввода.
     */
    public void clear() {
        element.clear();
    }
}
