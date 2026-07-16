package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import elements.Button;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class CartPage extends BasePage {

    private static final String URL = "https://aliexpress.ru/cart";

    private final ElementsCollection productContainers = $$x("//*[@data-testid='productContainer']");
    private final Button removeSelectedButton = Button.byTestId("removeSelectedButton");
    private final Button popupRemoveButton = Button.byTestId("popupRemove");
    private final SelenideElement emptyCartTitle = $x(
            "//h3[contains(@class,'EmptyPage__title') and normalize-space()='В корзине ничего нет']");
    private final SelenideElement cartState = $x(
            "//*[@data-testid='productContainer'] | " +
                    "//h3[contains(@class,'EmptyPage__title') and normalize-space()='В корзине ничего нет']");
    private final SelenideElement productTitle = $x("//*[@data-product-title]");
    private final SelenideElement productQuantity = $x("//*[@data-product-quantity]");
    private final SelenideElement productPrice = $x("//*[@data-product-unformatted-price]");
    private final SelenideElement incrementButton = $x("//*[@data-testid='productIncrementButton']");
    private final SelenideElement totalPriceBlock = $x("//*[@id='snow_cart_total_price_anchor']");
    private final SelenideElement totalItemsText = $x("//*[@id='snow_cart_total_price_anchor']//*[contains(normalize-space(), 'товар') and contains(normalize-space(), 'шт.')]");
    private final SelenideElement totalPriceText = $x("(//*[@id='snow_cart_total_price_anchor']//span[contains(normalize-space(), '₽')])[1]");

    /** Открывает корзину независимо от того, есть в ней товары или нет. */
    public CartPage open() {
        open(URL);
        dismissBlockingOverlays(Duration.ofSeconds(2));
        cartState.shouldBe(visible, Duration.ofMinutes(3));
        dismissBlockingOverlays(Duration.ofSeconds(2));
        return this;
    }

    /**
     * Ждет загрузки корзины и товара внутри нее.
     */
    public CartPage waitUntilOpened() {
        dismissBlockingOverlays(Duration.ofSeconds(2));
        activatePage();
        productContainers.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(30));
        productTitle.shouldBe(visible, Duration.ofSeconds(30));
        productQuantity.shouldBe(visible, Duration.ofSeconds(30));
        totalPriceBlock.shouldBe(visible, Duration.ofSeconds(30));
        return this;
    }

    public String getProductTitle() {
        return productTitle.getText();
    }

    public int getProductQuantity() {
        return Integer.parseInt(productQuantity.getAttribute("data-product-quantity"));
    }

    public CartPage increaseProductQuantityTo(int expectedQuantity) {
        while (getProductQuantity() < expectedQuantity) {
            dismissBlockingOverlays();
            int currentQuantity = getProductQuantity();
            incrementButton.shouldBe(visible, Duration.ofSeconds(10)).click();
            productQuantity.shouldHave(attribute("data-product-quantity", String.valueOf(currentQuantity + 1)), Duration.ofSeconds(10));
        }
        return this;
    }

    public int getProductUnitPrice() {
        return Integer.parseInt(productPrice.getAttribute("data-product-unformatted-price"));
    }

    public CartPage waitTotalRecalculatedForQuantity(int expectedQuantity) {
        totalItemsText.shouldHave(text(expectedQuantity + " шт."), Duration.ofSeconds(20));
        totalPriceText.shouldBe(visible, Duration.ofSeconds(20));
        return this;
    }

    public int getTotalPrice() {
        return Integer.parseInt(totalPriceText.getText().replaceAll("[^0-9]", ""));
    }

    /** Удаляет все товары через интерфейс, сохраняя cookie общей сессии. */
    public CartPage removeAllProducts() {
        if (!productContainers.isEmpty()) {
            dismissBlockingOverlays();
            removeSelectedButton.clickWhenEnabled(Duration.ofSeconds(15));
            popupRemoveButton.clickWhenEnabled(Duration.ofSeconds(15));
            productContainers.shouldHave(size(0), Duration.ofSeconds(20));
        }
        emptyCartTitle.shouldBe(visible, Duration.ofSeconds(20));
        return this;
    }

    /** Проверяет видимый экран пустой корзины и отсутствие товарных контейнеров. */
    public boolean isCartEmpty() {
        productContainers.shouldHave(size(0), Duration.ofSeconds(20));
        return emptyCartTitle.is(visible, Duration.ofSeconds(20));
    }
}
