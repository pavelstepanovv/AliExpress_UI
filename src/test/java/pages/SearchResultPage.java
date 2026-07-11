package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class SearchResultPage extends BasePage {

    private final ElementsCollection productCards = $$x("//a[contains(@class, 'universalSnippetLink')]");
    private final SelenideElement maxPriceInput = $x("(//div[contains(@class, 'PriceBlock')]//input)[2]");

    public boolean hasProducts() {
        try {
            productCards.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(15));
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    public int getProductsCount() {
        return productCards.size();
    }

    public boolean hasProductContainingWord(String word) {
        if (productCards.isEmpty()) {
            return false;
        }

        for (SelenideElement card : productCards) {
            String title = card
                    .$x(".//div[@data-type='PartWrap-Text'][@title]")
                    .getAttribute("title");

            if (title != null && title.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // Блок написан Шакуров 4382.
    public SearchResultPage filterByMaxPrice(int maxPrice) {
        // Вводим верхнюю границу цены и ждем, пока сайт применит фильтр к выдаче.
        maxPriceInput.scrollIntoView(true).setValue(String.valueOf(maxPrice)).pressEnter();
        new WebDriverWait(getWebDriver(), Duration.ofSeconds(20))
                .until(driver -> allProductsHavePriceLessThan(maxPrice));
        return this;
    }

    public SelenideElement getFirstProduct() {
        return productCards.first();
    }

    public String getFirstProductTitle() {
        if (productCards.isEmpty()) {
            return "Нет товаров";
        }
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

    public String getFirstProductPrice() {
        if (productCards.isEmpty()) {
            return "Нет цены";
        }
        return productCards.first()
                .$x(".//div[@data-type='PartWrap-Text'][contains(@style, 'font-size: 21px')]")
                .getText();
    }

    // Блок написан Шакуров 4382.
    public boolean allProductsHavePriceLessThan(int maxPrice) {
        if (productCards.isEmpty()) {
            return false;
        }

        int checkedProducts = 0;
        for (SelenideElement card : productCards) {
            String priceText = card
                    .$x(".//div[@data-type='PartWrap-Text'][contains(@style, 'font-size: 21px')]")
                    .getText();

            if (priceText == null || priceText.isEmpty()) {
                continue;
            }

            try {
                int price = extractFirstPrice(priceText);
                checkedProducts++;
                if (price > maxPrice) {
                    return false;
                }
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return checkedProducts > 0;
    }

    // Блок написан Шакуров 4382.
    private int extractFirstPrice(String priceText) {
        // Берем первое число из цены, чтобы скидка или процент не склеивались с ценой.
        Matcher matcher = Pattern.compile("\\d[\\d\\s]*").matcher(priceText);
        if (!matcher.find()) {
            throw new NumberFormatException("Price not found: " + priceText);
        }
        return Integer.parseInt(matcher.group().replaceAll("\\s", ""));
    }

    public ProductPage openFirstProduct() {
        productCards.first().click();
        return new ProductPage();
    }
}
