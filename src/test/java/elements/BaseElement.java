package elements;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

/**
 * Базовый абстрактный класс для всех элементов на странице.
 * От него наследуются конкретные элементы: кнопки, поля ввода, ссылки и т.д.
 */
public abstract class BaseElement {
    // ПОЛЯ ЭКЗЕМПЛЯРА
    /**
     * Сам элемент, которым управляет Selenide.
     * Через него выполняются все действия: клик, ввод текста и т.д.
     */
    protected final SelenideElement element;

    // КОНСТРУКТОРЫ
    /**
     * Конструктор для создания элемента по XPath.
     * Используется в наследниках (Button, Input и т.п.).
     *
     * Параметры: xpath шаблон XPath с %s для подстановки,
     * value значение, которое подставляется в XPath
     */
    protected BaseElement(String xpath, String value) {
        this.element = $x(String.format(xpath, value));
    }

    /**
     * Конструктор для случаев, когда SelenideElement уже найден.
     * Используется редко, в основном для сложных составных элементов.
     *
     * Параметры: element уже готовый объект SelenideElement
     */
    protected BaseElement(SelenideElement element) {
        this.element = element;
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ
    /**
     * Проверяет, отображается ли элемент на странице в данный момент.
     * Используется в тестах для проверки наличия элементов.
     *
     * Возвращает true, если элемент виден на странице
     */
    public boolean isDisplayed() {
        return element.isDisplayed();
    }

    /**
     * Возвращает текстовое содержимое элемента.
     * Используется для получения названий товаров, цен, заголовков и т.д.
     */
    public String getText() {
        return element.getText();
    }

    // ЗАЩИЩЁННЫЕ МЕТОДЫ (только для наследников)
    /**
     * Очищает поле ввода от старого текста.
     * Используется в классе Input перед вводом нового значения.
     */
    protected void clear() {
        element.clear();
    }

    /**
     * Вводит текст в поле.
     * Используется в классе Input для заполнения полей на странице.
     *
     * Параметры: keys текст, который нужно ввести
     */
    protected void sendKeys(CharSequence... keys) {
        element.sendKeys(keys);
    }
}
