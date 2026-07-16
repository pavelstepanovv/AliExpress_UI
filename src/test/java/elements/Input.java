package elements;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.Keys;

import java.time.Duration;

/**
 * Класс-наследник от BaseElement для работы с полями ввода на странице.
 */
public class Input extends BaseElement {

    // СТАТИЧЕСКИЕ ПОЛЯ (локаторы)
    private static final String INPUT_BY_NAME_XPATH = "//input[@name='%s']";
    private static final String INPUT_BY_ID_XPATH = "//input[@id='%s']";
    private static final String INPUT_BY_TEST_ID_XPATH = "//input[@data-testid='%s']";

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

    /** Создаёт поле ввода по стабильному атрибуту data-testid. */
    public static Input byTestId(String testId) {
        return new Input(INPUT_BY_TEST_ID_XPATH, testId);
    }

    /**
     * Заполняет поле ввода текстом.
     * Очищает поле, затем вводит текст.
     *
     * Параметры: text текст, который нужно ввести
     */
    public void fill(String text) {
        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(text);
        element.shouldHave(Condition.exactValue(text));
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

    /** Ждёт готовое поле, в том числе после ручного прохождения CAPTCHA. */
    public void waitUntilEditable(Duration timeout) {
        element.shouldBe(Condition.editable, timeout);
    }
}
