package pages;

import com.codeborne.selenide.SelenideElement;
import elements.Link;

import static com.codeborne.selenide.Selenide.$x;

/**
 * Page Object страницы категорий AliExpress.
 * Автор: Шакуров 4382.
 */
public class CategoryPage extends BasePage {

    private final Link catalogLink = Link.byText("Каталог");
    private final Link electronicsLink = Link.byText("Электроника");
    private final Link audioVideoLink = Link.byText("Аудио- и видеотехника");

    /**
     * Открывает каталог товаров.
     */
    public void openCatalog() {
        catalogLink.click();
    }

    /**
     * Выбирает категорию электроники.
     */
    public void selectElectronics() {
        electronicsLink.click();
    }

    /**
     * Выбирает подкатегорию аудио- и видеотехники.
     */
    public void selectAudioVideo() {
        audioVideoLink.click();
    }

    /**
     * Проверяет наличие подкатегорий.
     */
    public boolean hasSubcategories() {
        SelenideElement subcategories = $x("//div[contains(@class, 'subcategories')]");
        return subcategories.isDisplayed();
    }

    /**
     * Проверяет наличие товаров в категории.
     */
    public boolean hasProducts() {
        SelenideElement products = $x("//div[contains(@class, 'product-list')]");
        return products.isDisplayed();
    }

    /**
     * Возвращает заголовок категории.
     */
    public String getCategoryTitle() {
        SelenideElement title = $x("//h1[contains(text(), 'Электроника')]");
        return title.getText();
    }
}
