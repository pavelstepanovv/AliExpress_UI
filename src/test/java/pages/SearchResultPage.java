package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import elements.Button;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class SearchResultPage extends BasePage {

    private final ElementsCollection productCards = $$x("//a[contains(@class, 'universalSnippetLink')]");

    public boolean hasProducts() {
        return !productCards.isEmpty();
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

    public boolean allProductsHavePriceLessThan(int maxPrice) {
        if (productCards.isEmpty()) {
            return false;
        }

        for (SelenideElement card : productCards) {
            String priceText = card
                    .$x(".//div[@data-type='PartWrap-Text'][contains(@style, 'font-size: 21px')]")
                    .getText();

            // getText() никогда не возвращает null, только пустую строку
            if (priceText.isEmpty()) {
                continue;
            }

            try {
                int price = Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
                if (price > maxPrice) {
                    return false;
                }
            } catch (NumberFormatException e) {
                // Если не удалось распарсить цену, пропускаем товар
            }
        }
        return true;
    }

    public ProductPage openFirstProduct() {
        productCards.first().click();
        return new ProductPage();
    }

    public void filterByMaxPrice(int maxPrice) {
        // Находим все поля с классом haze-input
        ElementsCollection priceInputs = $$x("//input[contains(@class, 'haze-input_Input__input')]");

        if (priceInputs.size() < 2) {
            throw new RuntimeException("Не найдено поле 'до' для фильтрации по цене");
        }

        // Берём второе поле (индекс 1) — это поле "до"
        SelenideElement priceToInput = priceInputs.get(1);

        // Скроллим до поля, чтобы оно стало видимым
        priceToInput.scrollIntoView(true);

        // Небольшая пауза после скролла
        Selenide.sleep(1500);

        // Вводим значение
        priceToInput.clear();
        priceToInput.sendKeys(String.valueOf(maxPrice));

        // Нажимаем Enter для применения фильтра
        priceToInput.pressEnter();
    }
}