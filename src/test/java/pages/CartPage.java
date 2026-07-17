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

    // СТАТИЧЕСКИЕ ПОЛЯ (константы)
    private static final int SHORT_TIMEOUT_SEC = 2;
    private static final int MEDIUM_TIMEOUT_SEC = 10;
    private static final int LONG_TIMEOUT_SEC = 20;
    private static final int EXTRA_LONG_TIMEOUT_SEC = 30;
    private static final int PRODUCT_WAIT_TIMEOUT_SEC = 30;
    private static final int QUANTITY_UPDATE_TIMEOUT_SEC = 10;
    private static final int TOTAL_RECALC_TIMEOUT_SEC = 20;
    private static final int REMOVE_TIMEOUT_SEC = 15;
    private static final int EMPTY_TIMEOUT_SEC = 20;

    private static final String URL = "https://aliexpress.ru/cart";

    // ПОЛЯ ЭКЗЕМПЛЯРА
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

    // КОНСТРУКТОРЫ
    public CartPage() {
        // пустой конструктор
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ

    public CartPage open() {
        open(URL);
        dismissBlockingOverlays(Duration.ofSeconds(SHORT_TIMEOUT_SEC));
        cartState.shouldBe(visible, Duration.ofMinutes(3));
        dismissBlockingOverlays(Duration.ofSeconds(SHORT_TIMEOUT_SEC));
        return this;
    }

    public CartPage waitUntilOpened() {
        dismissBlockingOverlays(Duration.ofSeconds(SHORT_TIMEOUT_SEC));
        activatePage();
        productContainers.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(PRODUCT_WAIT_TIMEOUT_SEC));
        productTitle.shouldBe(visible, Duration.ofSeconds(PRODUCT_WAIT_TIMEOUT_SEC));
        productQuantity.shouldBe(visible, Duration.ofSeconds(PRODUCT_WAIT_TIMEOUT_SEC));
        totalPriceBlock.shouldBe(visible, Duration.ofSeconds(PRODUCT_WAIT_TIMEOUT_SEC));
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
            incrementButton.shouldBe(visible, Duration.ofSeconds(MEDIUM_TIMEOUT_SEC)).click();
            productQuantity.shouldHave(
                    attribute("data-product-quantity", String.valueOf(currentQuantity + 1)),
                    Duration.ofSeconds(QUANTITY_UPDATE_TIMEOUT_SEC)
            );
        }
        return this;
    }

    public int getProductUnitPrice() {
        return Integer.parseInt(productPrice.getAttribute("data-product-unformatted-price"));
    }

    public CartPage waitTotalRecalculatedForQuantity(int expectedQuantity) {
        totalItemsText.shouldHave(text(expectedQuantity + " шт."), Duration.ofSeconds(TOTAL_RECALC_TIMEOUT_SEC));
        totalPriceText.shouldBe(visible, Duration.ofSeconds(TOTAL_RECALC_TIMEOUT_SEC));
        return this;
    }

    public int getTotalPrice() {
        return Integer.parseInt(totalPriceText.getText().replaceAll("[^0-9]", ""));
    }

    public CartPage removeAllProducts() {
        if (!productContainers.isEmpty()) {
            dismissBlockingOverlays();
            removeSelectedButton.clickWhenEnabled(Duration.ofSeconds(REMOVE_TIMEOUT_SEC));
            popupRemoveButton.clickWhenEnabled(Duration.ofSeconds(REMOVE_TIMEOUT_SEC));
            productContainers.shouldHave(size(0), Duration.ofSeconds(EMPTY_TIMEOUT_SEC));
        }
        emptyCartTitle.shouldBe(visible, Duration.ofSeconds(EMPTY_TIMEOUT_SEC));
        return this;
    }

    public boolean isCartEmpty() {
        productContainers.shouldHave(size(0), Duration.ofSeconds(EMPTY_TIMEOUT_SEC));
        return emptyCartTitle.is(visible, Duration.ofSeconds(EMPTY_TIMEOUT_SEC));
    }
}
