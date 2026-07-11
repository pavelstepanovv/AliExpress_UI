package pages;

import com.codeborne.selenide.SelenideElement;
import elements.Link;

import static com.codeborne.selenide.Selenide.$x;

public class CategoryPage extends BasePage {

    private final Link catalogLink = Link.byText("Каталог");
    private final Link electronicsLink = Link.byText("Электроника");
    private final Link audioVideoLink = Link.byText("Аудио- и видеотехника");

    public void openCatalog() {
        catalogLink.click();
    }

    public void selectElectronics() {
        electronicsLink.click();
    }

    public void selectAudioVideo() {
        audioVideoLink.click();
    }

    public boolean hasSubcategories() {
        SelenideElement subcategories = $x("//div[contains(@class, 'subcategories')]");
        return subcategories.isDisplayed();
    }

    public boolean hasProducts() {
        SelenideElement products = $x("//div[contains(@class, 'product-list')]");
        return products.isDisplayed();
    }

    public String getCategoryTitle() {
        SelenideElement title = $x("//h1[contains(text(), 'Электроника')]");
        return title.getText();
    }
}