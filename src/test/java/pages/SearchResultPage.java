package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import elements.Item;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Page Object страницы результатов поиска AliExpress.
 * Автор: Шакуров 4382.
 */
public class SearchResultPage extends BasePage {

    // СТАТИЧЕСКИЕ ПОЛЯ (константы)
    private static final int PRODUCT_LOAD_TIMEOUT_SEC = 15;
    private static final int PRICE_INPUT_TIMEOUT_SEC = 15;

    private static final String PRODUCT_CARDS_XPATH =
            "//a[contains(@class, 'universalSnippetLink')]";
    private static final String PRODUCT_TITLE_XPATH =
            ".//div[@data-type='PartWrap-Text'][@title]";
    private static final String PRODUCT_TITLE_FALLBACK_XPATH =
            ".//div[@data-type='PartWrap-Text'][contains(@style, 'line-clamp: 2')]";
    private static final String PRODUCT_PRICE_XPATH =
            ".//div[@data-type='PartWrap-Text'][contains(@style, 'font-size: 21px')]";
    private static final String MAX_PRICE_INPUT_XPATH =
            "(//div[contains(@class, 'PriceBlock')]//input)[2]";
    private static final String PRICE_ASC_SORT_TYPE = "price_asc";

    // ПОЛЯ ЭКЗЕМПЛЯРА
    private final ElementsCollection productCards = $$x(PRODUCT_CARDS_XPATH);
    private final SelenideElement maxPriceInput = $x(MAX_PRICE_INPUT_XPATH);
    private final Item sortDropdown = Item.byClass("Select__select");
    private final SelenideElement selectedSort = $x(
            "//*[contains(@class, 'Select__select')]//*[@ae_button_type='sort by']");
    private final Item cheapFirstOption = Item.byClassAndText(
            "Select__option",
            "Сначала дешёвые");

    // КОНСТРУКТОРЫ
    public SearchResultPage() {
        // пустой конструктор
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ

    public boolean hasProducts() {
        try {
            waitUntilProductsLoaded();
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    public SearchResultPage waitUntilProductsLoaded() {
        waitForProductCards();
        return this;
    }

    public int getProductsCount() {
        waitUntilProductsLoaded();
        return getProductCardsCount();
    }

    public List<String> getProductTitles() {
        waitUntilProductsLoaded();
        return extractProductTitles();
    }

    public String getFirstProductTitle() {
        waitUntilProductsLoaded();
        return extractFirstProductTitle();
    }

    public String getFirstProductPrice() {
        waitUntilProductsLoaded();
        return extractFirstProductPrice();
    }

    public List<Integer> getProductPrices() {
        waitUntilProductsLoaded();
        return extractProductPrices();
    }

    public ProductPage openFirstProduct() {
        waitUntilProductsLoaded();
        dismissBlockingOverlays();
        prepareAndClickFirstProduct();
        switchToProductWindow("/item/");
        return new ProductPage().waitUntilOpened();
    }

    public SearchResultPage filterByMaxPrice(int maxPrice) {
        dismissBlockingOverlays();
        enterMaxPrice(maxPrice);
        applyPriceFilter();
        return this;
    }

    public SearchResultPage sortByPriceAscending() {
        waitUntilProductsLoaded();
        dismissBlockingOverlays();
        openSortDropdown();
        selectCheapFirstOption();
        waitForSortApplied();
        waitUntilProductsLoaded();
        return this;
    }

    public String getSelectedSortType() {
        return selectedSort.shouldBe(visible)
                .getAttribute("ae_object_value");
    }

    public String getSelectedSortText() {
        return selectedSort.shouldBe(visible).getText();
    }

    // ПРИВАТНЫЕ МЕТОДЫ

    private void waitForProductCards() {
        productCards.shouldHave(
                sizeGreaterThan(0),
                Duration.ofSeconds(PRODUCT_LOAD_TIMEOUT_SEC)
        );
    }

    private int getProductCardsCount() {
        return productCards.size();
    }

    private List<String> extractProductTitles() {
        List<String> titles = new ArrayList<>();
        for (SelenideElement card : productCards) {
            String title = card.$x(PRODUCT_TITLE_XPATH)
                    .getAttribute("title");
            if (title != null && !title.isEmpty()) {
                titles.add(title);
            }
        }
        return titles;
    }

    private String extractFirstProductTitle() {
        SelenideElement firstCard = productCards.first();
        String title = firstCard.$x(PRODUCT_TITLE_XPATH)
                .getAttribute("title");
        if (title == null || title.isEmpty()) {
            title = firstCard.$x(PRODUCT_TITLE_FALLBACK_XPATH)
                    .getText();
        }
        return title;
    }

    private String extractFirstProductPrice() {
        return productCards.first()
                .$x(PRODUCT_PRICE_XPATH)
                .getText();
    }

    private List<Integer> extractProductPrices() {
        List<Integer> prices = new ArrayList<>();
        for (SelenideElement card : productCards) {
            String priceText = card.$x(PRODUCT_PRICE_XPATH)
                    .getText();
            if (priceText == null || priceText.isEmpty()) {
                continue;
            }
            try {
                prices.add(extractFirstPrice(priceText));
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return prices;
    }

    private void prepareAndClickFirstProduct() {
        Set<String> oldWindowHandles = getWebDriver().getWindowHandles();
        SelenideElement firstProduct = productCards.first()
                .scrollIntoView(true);
        executeJavaScript(
                "arguments[0].removeAttribute('target');",
                firstProduct
        );
        firstProduct.click();
    }

    private void switchToProductWindow(String urlPart) {
        Set<String> oldWindowHandles = getWebDriver().getWindowHandles();
        switchToNewWindowIfOpened(oldWindowHandles, urlPart);
    }

    private void enterMaxPrice(int maxPrice) {
        maxPriceInput.shouldBe(visible, Duration.ofSeconds(PRICE_INPUT_TIMEOUT_SEC))
                .scrollIntoView(true)
                .setValue(String.valueOf(maxPrice));
    }

    private void applyPriceFilter() {
        maxPriceInput.pressEnter();
    }

    private void openSortDropdown() {
        sortDropdown.click();
    }

    private void selectCheapFirstOption() {
        cheapFirstOption.click();
    }

    private void waitForSortApplied() {
        webdriver().shouldHave(
                urlContaining("SortType=price_asc")
        );
        selectedSort.shouldHave(
                attribute("ae_object_value", PRICE_ASC_SORT_TYPE)
        );
    }

    private int extractFirstPrice(String priceText) {
        Matcher matcher = Pattern.compile("\\d[\\d\\s]*")
                .matcher(priceText);
        if (!matcher.find()) {
            throw new NumberFormatException("Price not found: " + priceText);
        }
        return Integer.parseInt(matcher.group().replaceAll("\\s", ""));
    }
}
