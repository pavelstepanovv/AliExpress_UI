package elements;

/**
 * Класс-наследник от BaseElement для работы с кнопками на странице.
 */
public class Button extends BaseElement {

    // СТАТИЧЕСКИЕ ПОЛЯ (локаторы)
    private static final String BUTTON_BY_TEXT_XPATH = "//button[contains(text(), '%s')]";
    private static final String BUTTON_BY_CLASS_XPATH = "//button[contains(@class, '%s')]";

    // КОНСТРУКТОРЫ
    private Button(String xpath, String value) {
        super(xpath, value);
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ
    /**
     * Создаёт объект кнопки по тексту на ней.
     *
     * Параметры: text текст кнопки
     * Возвращает: объект Button
     */
    public static Button byText(String text) {
        return new Button(BUTTON_BY_TEXT_XPATH, text);
    }

    /**
     * Создаёт объект кнопки по CSS-классу.
     *
     * Параметры: className название класса
     * Возвращает: объект Button
     */
    public static Button byClass(String className) {
        return new Button(BUTTON_BY_CLASS_XPATH, className);
    }

    /**
     * Кликает по кнопке.
     */
    public void click() {
        element.click();
    }
}
