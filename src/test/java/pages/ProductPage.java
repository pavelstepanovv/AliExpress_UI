package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import elements.Button;
import elements.Link;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;

/**
 * Page Object страницы товара AliExpress.
 * Автор: Шакуров 4382.
 */
public class ProductPage extends BasePage {

    private final SelenideElement titleElement = $x("//h1[string-length(normalize-space()) > 0]");
    private final ElementsCollection priceElements = $$x("//*[@data-testid='HazeProductPrice']");
    private final ElementsCollection addToCartButtons = $$x("//button[@data-testid='toCartBtn' or @data-button-add-to-cart='true']");
    private final ElementsCollection understoodButtons = $$x("//button[normalize-space()='Понятно' or .//*[normalize-space()='Понятно']]");
    private final SelenideElement cartLink = $x("//a[.//span[normalize-space()='Корзина'] or contains(normalize-space(), 'Корзина')]");
    private final Button shareButton = Button.byText("Поделиться");
    private final Link sellerLink = Link.byText("Продавец");

    /**
     * Ждет загрузки основных элементов карточки товара.
     */
    public ProductPage waitUntilOpened() {
        titleElement.shouldBe(visible, Duration.ofSeconds(30));
        getVisiblePriceElement().shouldBe(visible, Duration.ofSeconds(30));
        getVisibleAddToCartButton().shouldBe(visible, Duration.ofSeconds(30));
        return this;
    }

    /**
     * Проверяет, отображается ли название товара.
     */
    public boolean isTitleDisplayed() {
        return titleElement.isDisplayed();
    }

    /**
     * Возвращает название товара.
     */
    public String getTitleText() {
        return titleElement.getText();
    }

    /**
     * Проверяет, отображается ли цена товара.
     */
    public boolean isPriceDisplayed() {
        return getVisiblePriceElement().isDisplayed();
    }

    /**
     * Возвращает цену товара.
     */
    public String getPrice() {
        return getVisiblePriceElement().getText();
    }

    /**
     * Нажимает кнопку добавления товара в корзину.
     */
    public void addToCart() {
        closeVisibleHints();
        getVisibleAddToCartButton().scrollIntoView(true).click();
    }

    /**
     * Открывает корзину через иконку в шапке сайта.
     */
    public CartPage openCart() {
        cartLink.shouldBe(visible, Duration.ofSeconds(20)).click();
        return new CartPage().waitUntilOpened();
    }

    /**
     * Открывает окно "Поделиться".
     */
    public void clickShare() {
        shareButton.click();
    }

    /**
     * Открывает страницу продавца.
     */
    public void clickSeller() {
        sellerLink.click();
    }

    /**
     * Проверяет наличие кнопки добавления в корзину.
     */
    public boolean hasAddToCartButton() {
        return getVisibleAddToCartButton().isDisplayed();
    }

    /**
     * Выбирает количество товара.
     */
    public void selectQuantity(int quantity) {
        SelenideElement quantityInput = $x("//input[contains(@class, 'quantity')]");
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(quantity));
    }

    /**
     * Открывает вкладку отзывов.
     */
    public void clickReviewsTab() {
        SelenideElement reviewsTab = $x("//div[contains(text(), 'Отзывы')]");
        reviewsTab.click();
    }

    /**
     * Проверяет, отображается ли блок отзывов.
     */
    public boolean isReviewsBlockDisplayed() {
        SelenideElement reviewsBlock = $x("//div[contains(@class, 'reviews')]");
        return reviewsBlock.isDisplayed();
    }

    /**
     * Проверяет, отображается ли рейтинг товара.
     */
    public boolean isRatingDisplayed() {
        SelenideElement rating = $x("//div[contains(@class, 'rating')]");
        return rating.isDisplayed();
    }

    /**
     * Проверяет наличие текстовых отзывов.
     */
    public boolean hasTextReviews() {
        ElementsCollection reviews = $$x("//div[contains(@class, 'review-text')]");
        return !reviews.isEmpty();
    }

    /**
     * Проверяет, что отзывы содержат блок со звездами.
     */
    public boolean allReviewsHaveStars() {
        ElementsCollection reviews = $$x("//div[contains(@class, 'review-item')]");
        for (SelenideElement review : reviews) {
            SelenideElement stars = review.$x(".//div[contains(@class, 'stars')]");
            if (!stars.isDisplayed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверяет, отображается ли модальное окно "Поделиться".
     */
    public boolean isShareModalDisplayed() {
        SelenideElement modal = $x("//div[contains(@class, 'share-modal')]");
        return modal.isDisplayed();
    }

    /**
     * Проверяет наличие кнопки копирования ссылки.
     */
    public boolean hasCopyLinkButton() {
        SelenideElement copyButton = $x("//button[contains(text(), 'Скопировать ссылку')]");
        return copyButton.isDisplayed();
    }

    /**
     * Проверяет наличие иконок социальных сетей.
     */
    public boolean hasSocialIcons() {
        ElementsCollection icons = $$x("//div[contains(@class, 'social-icon')]");
        return !icons.isEmpty();
    }

    /**
     * Возвращает имя продавца.
     */
    public String getSellerName() {
        return sellerLink.getText();
    }

    /**
     * Возвращает видимый блок цены товара.
     */
    private SelenideElement getVisiblePriceElement() {
        return priceElements.findBy(visible);
    }

    /**
     * Возвращает видимую кнопку добавления товара в корзину.
     */
    private SelenideElement getVisibleAddToCartButton() {
        return addToCartButtons.findBy(visible);
    }

    /**
     * Закрывает видимые подсказки и баннеры, которые могут перекрывать кнопку корзины.
     */
    private void closeVisibleHints() {
        for (int i = 0; i < 2; i++) {
            SelenideElement button = understoodButtons.findBy(visible);
            if (!button.exists()) {
                return;
            }
            executeJavaScript("arguments[0].click();", button);
        }
    }
}
