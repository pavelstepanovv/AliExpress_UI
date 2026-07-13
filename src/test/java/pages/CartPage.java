package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;

/**
 * Page Object корзины AliExpress.
 * Автор: Шакуров 4382.
 */
public class CartPage extends BasePage {

    private final ElementsCollection productContainers = $$x("//*[@data-testid='productContainer']");
    private final SelenideElement productTitle = $x("//*[@data-product-title]");
    private final SelenideElement productQuantity = $x("//*[@data-product-quantity]");
    private final SelenideElement productPrice = $x("//*[@data-product-unformatted-price]");
    private final SelenideElement incrementButton = $x("//*[@data-testid='productIncrementButton']");
    private final SelenideElement totalPriceBlock = $x("//*[@id='snow_cart_total_price_anchor']");
    private final SelenideElement totalItemsText = $x("//*[@id='snow_cart_total_price_anchor']//*[contains(normalize-space(), 'товар') and contains(normalize-space(), 'шт.')]");
    private final SelenideElement totalPriceText = $x("(//*[@id='snow_cart_total_price_anchor']//span[contains(normalize-space(), '₽')])[1]");

    /**
     * Ждет загрузки корзины и товара внутри нее.
     */
    public CartPage waitUntilOpened() {
        activatePage();
        productContainers.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(30));
        productTitle.shouldBe(visible, Duration.ofSeconds(30));
        productQuantity.shouldBe(visible, Duration.ofSeconds(30));
        totalPriceBlock.shouldBe(visible, Duration.ofSeconds(30));
        return this;
    }

    /**
     * Возвращает название товара в корзине.
     */
    public String getProductTitle() {
        return productTitle.getText();
    }

    /**
     * Возвращает текущее количество товара.
     */
    public int getProductQuantity() {
        return Integer.parseInt(productQuantity.getAttribute("data-product-quantity"));
    }

    /**
     * Увеличивает количество товара до указанного значения.
     */
    public CartPage increaseProductQuantityTo(int expectedQuantity) {
        while (getProductQuantity() < expectedQuantity) {
            int currentQuantity = getProductQuantity();
            incrementButton.shouldBe(visible, Duration.ofSeconds(10)).click();
            productQuantity.shouldHave(attribute("data-product-quantity", String.valueOf(currentQuantity + 1)), Duration.ofSeconds(10));
        }
        return this;
    }

    /**
     * Возвращает цену товара за одну штуку.
     */
    public int getProductUnitPrice() {
        return Integer.parseInt(productPrice.getAttribute("data-product-unformatted-price"));
    }

    /**
     * Ждет пересчета итогового блока под нужное количество товара.
     */
    public CartPage waitTotalRecalculatedForQuantity(int expectedQuantity) {
        totalItemsText.shouldHave(text(expectedQuantity + " шт."), Duration.ofSeconds(20));
        totalPriceText.shouldBe(visible, Duration.ofSeconds(20));
        return this;
    }

    /**
     * Возвращает итоговую стоимость корзины числом.
     */
    public int getTotalPrice() {
        return Integer.parseInt(totalPriceText.getText().replaceAll("[^0-9]", ""));
    }
}
