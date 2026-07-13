package elements;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

/**
 * Базовый класс UI-элемента, от которого наследуются конкретные элементы.
 * Автор: Шакуров 4382.
 */
public class BaseElement {

    protected final SelenideElement element;

    protected BaseElement(String xpath, String value) {
        this.element = $x(String.format(xpath, value));
    }

    protected BaseElement(SelenideElement element) {
        this.element = element;
    }

    /**
     * Проверяет, отображается ли элемент на странице.
     */
    public boolean isDisplayed() {
        return element.isDisplayed();
    }

    /**
     * Возвращает текст элемента.
     */
    public String getText() {
        return element.getText();
    }

    /**
     * Выполняет клик по элементу.
     */
    public void click() {
        element.click();
    }
}
