package elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$x;

/**
 * Класс-наследник от BaseElement для простых элементов страницы,
 * у которых нет отдельного класса: div, span, li, label и т.д.
 */
public class Item extends BaseElement {

    // СТАТИЧЕСКИЕ ПОЛЯ (локаторы)
    private static final String ITEM_BY_CLASS_XPATH = "//*[contains(@class, '%s')]";
    private static final String ITEM_BY_TEST_ID_XPATH = "//*[@data-testid='%s']";
    private static final String ITEM_BY_CLASS_AND_TEXT_XPATH = "//*[contains(@class, '%s')][normalize-space()='%s']";
    private static final String BUTTON_BY_TEST_ID_XPATH = "//button[@data-testid='%s'][not(contains(@class, '%s'))]";
    private static final String BUTTON_BY_TEST_ID_IN_CONTAINER_XPATH =
            "//div[contains(@class, '%s')][not(contains(@class, '%s'))]//button[@data-testid='%s']";
    private static final String ITEM_BY_CLASS_IN_CONTAINER_XPATH =
            "//div[contains(@class, '%s')][not(contains(@class, '%s'))]//*[contains(@class, '%s')]";

    // КОНСТРУКТОРЫ
    private Item(String xpath, String value) {
        super(xpath, value);
    }

    private Item(SelenideElement element) {
        super(element);
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ
    /**
     * Создаёт объект элемента по CSS-классу.
     *
     * Параметры: className название класса
     * Возвращает: объект Item
     */
    public static Item byClass(String className) {
        return new Item(ITEM_BY_CLASS_XPATH, className);
    }

    /** Создаёт простой элемент по стабильному атрибуту data-testid. */
    public static Item byTestId(String testId) {
        return new Item(ITEM_BY_TEST_ID_XPATH, testId);
    }

    /**
     * Создаёт объект элемента по CSS-классу и видимому тексту.
     * Нужен, когда на странице несколько элементов с одним классом.
     *
     * Параметры: className название класса, text видимый текст элемента
     * Возвращает: объект Item
     */
    public static Item byClassAndText(String className, String text) {
        return new Item($x(String.format(ITEM_BY_CLASS_AND_TEXT_XPATH, className, text)));
    }

    /**
     * Создаёт объект кнопки по атрибуту data-testid,
     * исключая кнопки с указанным классом.
     *
     * Параметры: testId значение data-testid, excludedClass класс, который надо исключить
     * Возвращает: объект Item
     */
    public static Item buttonByTestIdExcludingClass(String testId, String excludedClass) {
        return new Item($x(String.format(BUTTON_BY_TEST_ID_XPATH, testId, excludedClass)));
    }

    /**
     * Создаёт объект кнопки по data-testid внутри контейнера с указанным классом,
     * исключая контейнеры с другим указанным классом.
     *
     * Параметры: containerClass класс контейнера, excludedContainerClass класс, который надо исключить,
     * testId значение data-testid кнопки
     * Возвращает: объект Item
     */
    public static Item buttonByTestIdInContainer(String containerClass, String excludedContainerClass, String testId) {
        return new Item($x(String.format(BUTTON_BY_TEST_ID_IN_CONTAINER_XPATH,
                containerClass, excludedContainerClass, testId)));
    }

    /**
     * Создаёт объект элемента по CSS-классу внутри контейнера с указанным классом,
     * исключая контейнеры с другим указанным классом.
     *
     * Параметры: containerClass класс контейнера, excludedContainerClass класс, который надо исключить,
     * className класс самого элемента
     * Возвращает: объект Item
     */
    public static Item byClassInContainer(String containerClass, String excludedContainerClass, String className) {
        return new Item($x(String.format(ITEM_BY_CLASS_IN_CONTAINER_XPATH,
                containerClass, excludedContainerClass, className)));
    }

    /**
     * Кликает по элементу.
     */
    public void click() {
        element.click();
    }

    /** Ждёт, пока элемент станет видимым. */
    public void waitUntilVisible(Duration timeout) {
        element.shouldBe(Condition.visible, timeout);
    }

    /**
     * Ждёт, пока элемент исчезнет со страницы.
     * Это динамическое ожидание вместо статической паузы.
     */
    public void waitUntilHidden() {
        element.should(Condition.disappear);
    }

    /**
     * Ждёт, пока текст элемента изменится и перестанет быть равен старому значению.
     * Это динамическое ожидание для элементов, которые обновляет скрипт страницы.
     *
     * Параметры: oldText старое значение текста
     */
    public void waitUntilTextChanges(String oldText) {
        element.should(Condition.match("text is not '" + oldText + "'",
                el -> !el.getAttribute("textContent").trim().equals(oldText)));
    }

    /**
     * Возвращает текстовое содержимое элемента через атрибут textContent.
     * Нужен для элементов, у которых обычный getText возвращает пустую строку.
     *
     * Возвращает: текст элемента без пробелов по краям
     */
    public String getTextContent() {
        String text = element.getAttribute("textContent");
        return text == null ? "" : text.trim();
    }
}
