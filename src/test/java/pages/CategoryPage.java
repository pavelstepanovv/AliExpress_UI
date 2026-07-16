package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;

public class CategoryPage extends BasePage {

    private final SelenideElement categoryTitle = $x("//h1");
    private final ElementsCollection productItems = $$x("//a[contains(@class, 'universalSnippetLink')]");

    public String getCategoryTitle() {
        return categoryTitle.shouldBe(visible, Duration.ofSeconds(10)).getText();
    }

    public int getProductsCount() {
        productItems.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(15));
        return productItems.size();
    }
}