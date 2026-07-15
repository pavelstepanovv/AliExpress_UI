package elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;

/**
 * Класс-наследник от BaseElement для работы со ссылками (тег a).
 */
public class Link extends BaseElement {

    // СТАТИЧЕСКИЕ ПОЛЯ (локаторы)
    private static final String LINK_BY_CLASS_XPATH = "//a[contains(@class, '%s')]";

    // КОНСТРУКТОРЫ
    private Link(String xpath, String value) {
        super(xpath, value);
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ
    /**
     * Создаёт объект ссылки по CSS-классу.
     *
     * Параметры: className название класса
     * Возвращает: объект Link
     */
    public static Link byClass(String className) {
        return new Link(LINK_BY_CLASS_XPATH, className);
    }

    /**
     * Кликает по ссылке через JavaScript.
     * Нужен для ссылок, по которым обычный клик не срабатывает.
     */
    public void clickViaJs() {
        // ждём, пока ссылка появится на странице, и кликаем по ней через JS
        element.should(Condition.exist);
        Selenide.executeJavaScript("arguments[0].click();", element.toWebElement());
    }
}
