package elements;

/**
 * Класс-наследник от BaseElement для работы с полями ввода на странице.
 */
public class Input extends BaseElement {

    // СТАТИЧЕСКИЕ ПОЛЯ (локаторы)
    private static final String INPUT_BY_NAME_XPATH = "//input[@name='%s']";
    private static final String INPUT_BY_ID_XPATH = "//input[@id='%s']";

    // КОНСТРУКТОРЫ
    private Input(String xpath, String value) {
        super(xpath, value);
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ
    /**
     * Создаёт объект поля ввода по атрибуту name.
     *
     * Параметры: name значение атрибута name
     * Возвращает: объект Input
     */
    public static Input byName(String name) {
        return new Input(INPUT_BY_NAME_XPATH, name);
    }

    /**
     * Создаёт объект поля ввода по атрибуту id.
     *
     * Параметры: id значение атрибута id
     * Возвращает: объект Input
     */
    public static Input byId(String id) {
        return new Input(INPUT_BY_ID_XPATH, id);
    }

    /**
     * Заполняет поле ввода текстом.
     * Очищает поле, затем вводит текст.
     *
     * Параметры: text текст, который нужно ввести
     */
    public void fill(String text) {
        clear();
        sendKeys(text);
    }

    /**
     * Очищает поле ввода.
     */
    public void clear() {
        super.clear();
    }

    /**
     * Возвращает текущее значение поля ввода.
     * У тега input введённый текст лежит в атрибуте value, а не в тексте тега.
     *
     * Возвращает: текст, который сейчас введён в поле
     */
    public String getValue() {
        return element.getValue();
    }
}
