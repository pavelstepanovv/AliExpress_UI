package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import elements.Item;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Page Object страницы результатов поиска AliExpress.
 * Автор: Шакуров 4382.
 */
public class SearchResultPage extends BasePage {

    private static final String PRODUCT_CARDS_XPATH = "//a[contains(@class, 'universalSnippetLink')]";
    private static final String PRODUCT_TITLE_XPATH = ".//div[@data-type='PartWrap-Text'][@title]";
    private static final String PRODUCT_TITLE_FALLBACK_XPATH = ".//div[@data-type='PartWrap-Text'][contains(@style, 'line-clamp: 2')]";
    private static final String PRODUCT_PRICE_XPATH = ".//div[@data-type='PartWrap-Text'][contains(@style, 'font-size: 21px')]";
    private static final String MAX_PRICE_INPUT_XPATH = "(//div[contains(@class, 'PriceBlock')]//input)[2]";

    private final ElementsCollection productCards = $$x(PRODUCT_CARDS_XPATH);
    private final SelenideElement maxPriceInput = $x(MAX_PRICE_INPUT_XPATH);
    private final Item sortDropdown = Item.byClass("Select__select");
    // настоящая радиокнопка внутри опции скрыта, поэтому кликаем по label с текстом
    private final Item cheapFirstOption = Item.byClassAndText("Select__option", "Сначала дешёвые");

    /**
     * Проверяет, что в поисковой выдаче появились товары.
     */
    public boolean hasProducts() {
        try {
            waitUntilProductsLoaded();
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    /**
     * Ждет появления товаров в поисковой выдаче.
     */
    public SearchResultPage waitUntilProductsLoaded() {
        productCards.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(15));
        return this;
    }

    /**
     * Возвращает количество загруженных карточек товаров.
     */
    public int getProductsCount() {
        waitUntilProductsLoaded();
        return productCards.size();
    }

    /**
     * Возвращает названия загруженных товаров.
     */
    public List<String> getProductTitles() {
        waitUntilProductsLoaded();
        List<String> titles = new ArrayList<>();
        for (SelenideElement card : productCards) {
            String title = card.$x(PRODUCT_TITLE_XPATH).getAttribute("title");

            if (title != null && !title.isEmpty()) {
                titles.add(title);
            }
        }
        return titles;
    }

    /**
     * Возвращает название первого товара.
     */
    public String getFirstProductTitle() {
        waitUntilProductsLoaded();
        SelenideElement firstCard = productCards.first();

        String title = firstCard.$x(PRODUCT_TITLE_XPATH).getAttribute("title");

        if (title == null || title.isEmpty()) {
            title = firstCard.$x(PRODUCT_TITLE_FALLBACK_XPATH).getText();
        }

        return title;
    }

    /**
     * Возвращает цену первого товара.
     */
    public String getFirstProductPrice() {
        waitUntilProductsLoaded();
        return productCards.first().$x(PRODUCT_PRICE_XPATH).getText();
    }

    /**
     * Возвращает распознанные цены загруженных товаров.
     */
    public List<Integer> getProductPrices() {
        waitUntilProductsLoaded();
        List<Integer> prices = new ArrayList<>();
        for (SelenideElement card : productCards) {
            String priceText = card.$x(PRODUCT_PRICE_XPATH).getText();

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

    /**
     * Открывает первый товар из поисковой выдачи.
     */
    public ProductPage openFirstProduct() {
        waitUntilProductsLoaded();
        Set<String> oldWindowHandles = getWebDriver().getWindowHandles();
        productCards.first().scrollIntoView(true).click();
        switchToNewWindowIfOpened(oldWindowHandles);
        return new ProductPage().waitUntilOpened();
    }

    /**
     * Вводит верхнюю границу цены в фильтр.
     */
    public SearchResultPage filterByMaxPrice(int maxPrice) {
        maxPriceInput.shouldBe(visible, Duration.ofSeconds(15))
                .scrollIntoView(true)
                .setValue(String.valueOf(maxPrice))
                .pressEnter();
        return this;
    }

    /**
     * Открывает список сортировки и выбирает вариант "Сначала дешёвые".
     */
    public void sortByPriceAscending() {
        // запоминаем цену первого товара до сортировки: нельзя читать
        // цены, пока список реально не пересортирован
        waitUntilProductsLoaded();
        SelenideElement firstPrice = productCards.first().$x(PRODUCT_PRICE_XPATH);
        String priceBeforeSort = firstPrice.getText();

        sortDropdown.click();
        cheapFirstOption.click();

        // динамическое ожидание: после клика в URL появляется параметр сортировки
        webdriver().shouldHave(urlContaining("SortType=price_asc"));
        // динамическое ожидание: первая цена должна измениться -
        // это значит, что список перерисован в новом порядке (сначала дешёвые)
        firstPrice.shouldNotHave(exactText(priceBeforeSort));
    }

    /**
     * Извлекает первое число из строки цены.
     */
    private int extractFirstPrice(String priceText) {
        Matcher matcher = Pattern.compile("\\d[\\d\\s]*").matcher(priceText);
        if (!matcher.find()) {
            throw new NumberFormatException("Price not found: " + priceText);
        }
        return Integer.parseInt(matcher.group().replaceAll("\\s", ""));
    }

    /**
     * Переключается на новую вкладку товара, если сайт открыл карточку в отдельном окне.
     */
    private void switchToNewWindowIfOpened(Set<String> oldWindowHandles) {
        new WebDriverWait(getWebDriver(), Duration.ofSeconds(10))
                .until(driver -> driver.getWindowHandles().size() > oldWindowHandles.size()
                        || !driver.getCurrentUrl().contains("wholesale"));

        for (String windowHandle : getWebDriver().getWindowHandles()) {
            if (!oldWindowHandles.contains(windowHandle)) {
                switchTo().window(windowHandle);
                return;
            }
        }
    }
}
