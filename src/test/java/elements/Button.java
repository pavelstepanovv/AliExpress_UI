package elements;

import com.codeborne.selenide.Condition;

import java.time.Duration;

/**
 * Класс-наследник от BaseElement для работы с кнопками на странице.
 */
public class Button extends BaseElement {

    // СТАТИЧЕСКИЕ ПОЛЯ (локаторы)
    private static final String BUTTON_BY_TEXT_XPATH =
            "//button[normalize-space()='%1$s' or .//*[normalize-space()='%1$s']]";
    private static final String BUTTON_BY_CLASS_XPATH = "//button[contains(@class, '%s')]";
    private static final String BUTTON_BY_TEST_ID_XPATH = "//button[@data-testid='%s']";

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

    /** Создаёт кнопку по стабильному атрибуту data-testid. */
    public static Button byTestId(String testId) {
        return new Button(BUTTON_BY_TEST_ID_XPATH, testId);
    }

    /**
     * Кликает по кнопке.
     */
    public void click() {
        element.click();
    }

    /** Ждёт видимую кнопку и нажимает её. */
    public void clickWhenVisible(Duration timeout) {
        element.shouldBe(Condition.visible, timeout).click();
    }

    /** Ждёт видимую активную кнопку и нажимает её. */
    public void clickWhenEnabled(Duration timeout) {
        element.shouldBe(Condition.visible, timeout)
                .shouldBe(Condition.enabled, timeout)
                .click();
    }
}
