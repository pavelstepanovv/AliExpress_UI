package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Page Object страницы результатов поиска AliExpress.
 * Автор: Шакуров 4382.
 */
public class SearchResultPage extends BasePage {

    private final ElementsCollection productCards = $$x("//a[contains(@class, 'universalSnippetLink')]");
    private final SelenideElement maxPriceInput = $x("(//div[contains(@class, 'PriceBlock')]//input)[2]");

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
            String title = card
                    .$x(".//div[@data-type='PartWrap-Text'][@title]")
                    .getAttribute("title");

            if (title != null && !title.isEmpty()) {
                titles.add(title);
            }
        }
        return titles;
    }

    /**
     * Возвращает первую карточку товара.
     */
    public SelenideElement getFirstProduct() {
        waitUntilProductsLoaded();
        return productCards.first();
    }

    /**
     * Возвращает название первого товара.
     */
    public String getFirstProductTitle() {
        waitUntilProductsLoaded();
        SelenideElement firstCard = productCards.first();
        String title = firstCard
                .$x(".//div[@data-type='PartWrap-Text'][@title]")
                .getAttribute("title");

        if (title == null || title.isEmpty()) {
            title = firstCard
                    .$x(".//div[@data-type='PartWrap-Text'][contains(@style, 'line-clamp: 2')]")
                    .getText();
        }
        return title;
    }

    /**
     * Возвращает цену первого товара.
     */
    public String getFirstProductPrice() {
        waitUntilProductsLoaded();
        return productCards.first()
                .$x(".//div[@data-type='PartWrap-Text'][contains(@style, 'font-size: 21px')]")
                .getText();
    }

    /**
     * Возвращает распознанные цены загруженных товаров.
     */
    public List<Integer> getProductPrices() {
        waitUntilProductsLoaded();
        List<Integer> prices = new ArrayList<>();
        for (SelenideElement card : productCards) {
            String priceText = card
                    .$x(".//div[@data-type='PartWrap-Text'][contains(@style, 'font-size: 21px')]")
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
     * Вводит верхнюю границу цены и ждет применения фильтра к выдаче.
     */
    public SearchResultPage filterByMaxPrice(int maxPrice) {
        maxPriceInput.scrollIntoView(true).setValue(String.valueOf(maxPrice)).pressEnter();
        new WebDriverWait(getWebDriver(), Duration.ofSeconds(20))
                .until(driver -> {
                    List<Integer> prices = getProductPrices();
                    return !prices.isEmpty() && prices.stream().allMatch(price -> price <= maxPrice);
                });
        return this;
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
