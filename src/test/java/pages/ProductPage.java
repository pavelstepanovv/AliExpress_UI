package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import elements.Button;
import elements.Item;
import elements.Link;

import java.time.Duration;
import java.util.Set;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class ProductPage extends BasePage {

    // СТАТИЧЕСКИЕ ПОЛЯ (константы)
    private static final int LONG_TIMEOUT_SEC = 20;
    private static final int EXTRA_LONG_TIMEOUT_SEC = 30;
    private static final int QUANTITY_WAIT_TIMEOUT_SEC = 20;
    private static final int CART_LINK_TIMEOUT_SEC = 20;
    private static final int MAX_HINT_CLOSE_ATTEMPTS = 2;

    private static final String TITLE_XPATH =
            "//h1[string-length(normalize-space()) > 0]";
    private static final String PRICE_XPATH =
            "//*[@data-testid='HazeProductPrice']";
    private static final String ADD_TO_CART_BUTTON_XPATH =
            "//button[@data-testid='toCartBtn' or @data-button-add-to-cart='true']";
    private static final String UNDERSTOOD_BUTTON_XPATH =
            "//button[normalize-space()='Понятно' or .//*[normalize-space()='Понятно']]";
    private static final String CART_LINK_XPATH =
            "//a[.//span[normalize-space()='Корзина'] or contains(normalize-space(), 'Корзина')]";
    private static final String REVIEWS_ANCHOR_XPATH =
            "//*[@id='reviews_anchor']";
    private static final String ALL_REVIEWS_BUTTON_XPATH =
            "//button[@aria-label='allReviewsButton']";

    // ПОЛЯ ЭКЗЕМПЛЯРА
    private final SelenideElement titleElement = $x(TITLE_XPATH);
    private final ElementsCollection priceElements = $$x(PRICE_XPATH);
    private final ElementsCollection addToCartButtons = $$x(ADD_TO_CART_BUTTON_XPATH);
    private final ElementsCollection understoodButtons = $$x(UNDERSTOOD_BUTTON_XPATH);
    private final SelenideElement cartLink = $x(CART_LINK_XPATH);
    private final SelenideElement reviewsAnchor = $x(REVIEWS_ANCHOR_XPATH);
    private final SelenideElement allReviewsButton = $x(ALL_REVIEWS_BUTTON_XPATH);
    private final Button shareButton = Button.byText("Поделиться");
    private final Item quantityCounter = Item.byClassInContainer(
            "HazePriceButton__counter",
            "stickyOfferPlaced",
            "HazePriceButton__counterText");
    private final Item incrementButton = Item.buttonByTestIdInContainer(
            "HazePriceButton__counter",
            "stickyOfferPlaced",
            "incrementBtn");
    private final Link sellerNameLink = Link.byClass(
            "HazeProductDescription_HazeProductDescription__storeNameLink");

    // КОНСТРУКТОРЫ
    public ProductPage() {
        // пустой конструктор
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ

    public ProductPage waitUntilOpened() {
        waitForTitle();
        waitForPrice();
        return this;
    }

    public boolean isTitleDisplayed() {
        return titleElement.isDisplayed();
    }

    public String getTitleText() {
        return titleElement.getText();
    }

    public boolean isPriceDisplayed() {
        return getVisiblePriceElement().isDisplayed();
    }

    public String getPrice() {
        return getVisiblePriceElement().getText();
    }

    public boolean hasAddToCartButton() {
        return getVisibleAddToCartButton().isDisplayed();
    }

    public void addToCart() {
        dismissBlockingOverlays();
        closeVisibleHints();
        clickAddToCartButton();
        waitForQuantityCounter();
    }

    public CartPage openCart() {
        dismissBlockingOverlays();
        clickCartLink();
        return new CartPage().waitUntilOpened();
    }

    public String getCurrentQuantity() {
        return quantityCounter.getTextContent();
    }

    public void incrementQuantity() {
        dismissBlockingOverlays();
        String oldValue = getCurrentQuantity();
        clickIncrementButton();
        waitForQuantityChange(oldValue);
    }

    public SellerPage openSellerPage() {
        dismissBlockingOverlays();
        clickSellerLinkViaJs();
        return new SellerPage();
    }

    public ReviewsPage openAllReviews() {
        dismissBlockingOverlays();
        scrollToReviewsAnchor();
        clickAllReviewsButton();
        return new ReviewsPage().waitUntilOpened();
    }

    public SharePopup openSharePopup() {
        dismissBlockingOverlays();
        clickShareButton();
        return new SharePopup().waitUntilOpened();
    }

    // ПРИВАТНЫЕ МЕТОДЫ

    private void waitForTitle() {
        titleElement.shouldBe(visible, Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC));
    }

    private void waitForPrice() {
        getVisiblePriceElement().shouldBe(
                visible,
                Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC)
        );
    }

    private SelenideElement getVisiblePriceElement() {
        return priceElements.findBy(visible);
    }

    private SelenideElement getVisibleAddToCartButton() {
        return addToCartButtons.findBy(visible);
    }

    private void clickAddToCartButton() {
        getVisibleAddToCartButton().scrollIntoView(true).click();
    }

    private void waitForQuantityCounter() {
        quantityCounter.waitUntilVisible(Duration.ofSeconds(QUANTITY_WAIT_TIMEOUT_SEC));
    }

    private void clickCartLink() {
        cartLink.shouldBe(visible, Duration.ofSeconds(CART_LINK_TIMEOUT_SEC))
                .click();
    }

    private void clickIncrementButton() {
        incrementButton.click();
    }

    private void waitForQuantityChange(String oldValue) {
        quantityCounter.waitUntilTextChanges(oldValue);
    }

    private void clickSellerLinkViaJs() {
        sellerNameLink.clickViaJs();
    }

    private void scrollToReviewsAnchor() {
        reviewsAnchor.shouldBe(exist, Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC))
                .scrollIntoView(true);
    }

    private void clickAllReviewsButton() {
        Set<String> oldWindowHandles = getWebDriver().getWindowHandles();
        allReviewsButton.shouldBe(visible, Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC))
                .click();
        switchToNewWindowIfOpened(oldWindowHandles, "/reviews");
    }

    private void clickShareButton() {
        shareButton.clickWhenVisible(Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC));
    }

    private void closeVisibleHints() {
        for (int i = 0; i < MAX_HINT_CLOSE_ATTEMPTS; i++) {
            SelenideElement button = understoodButtons.findBy(visible);
            if (!button.exists()) {
                return;
            }
            executeJavaScript("arguments[0].click();", button);
        }
    }
}
