package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import elements.Button;
import elements.Input;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.actions;
import static com.codeborne.selenide.Condition.focused;
public class MainPage extends BasePage {

    private static final String URL = "https://aliexpress.ru";
    private static final String SEARCH_SUGGESTIONS_XPATH = "//div[@ae_button_type='auto_suggestion' and @ae_object_type='keyword']";

    private final Input searchInput = Input.byName("SearchText");
    private final Button searchButton = Button.byText("Найти");
    private final ElementsCollection searchSuggestions = $$x(SEARCH_SUGGESTIONS_XPATH);

    public MainPage open() {
        open(URL);
        return this;
    }

    public SearchResultPage search(String query) {
        searchInput.fill(query);
        searchButton.click();
        return new SearchResultPage();
    }

    public void clearSearch() {
        searchInput.clear();
    }

    public void fillSearch(String query) {
        searchInput.fill(query);
    }

    public MainPage fillSearchAndWaitSuggestions(String query) {
        searchInput.fill(query);
        searchSuggestions.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(10));
        return this;
    }

    public int getSuggestionsCount() {
        return searchSuggestions.size();
    }

    public List<String> getSuggestionValues() {
        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i < searchSuggestions.size(); i++) {
            String suggestion = searchSuggestions.get(i).getAttribute("ae_object_value");
            if (suggestion == null) {
                suggestion = searchSuggestions.get(i).getText();
            }
            suggestions.add(suggestion);
        }
        return suggestions;
    }

    public CategoryPage openSubcategory(String mainCategory, String subCategory) {
        SelenideElement catalogButton = $x("//*[contains(normalize-space(), 'Каталог') and not(.//descendant::*[contains(normalize-space(), 'Каталог')])]");
        catalogButton.shouldBe(visible, Duration.ofSeconds(15)).click();

        SelenideElement mainCategoryLink = $x("//*[contains(@class, 'catalog') or contains(@class, 'menu')]//*[contains(normalize-space(), '" + mainCategory + "')]");
        if (!mainCategoryLink.exists()) {
            mainCategoryLink = $x("//a[contains(normalize-space(), '" + mainCategory + "')]");
        }

        mainCategoryLink.shouldBe(visible, Duration.ofSeconds(15)).hover();

        SelenideElement subCategoryLink = $x("//*[contains(normalize-space(), '" + subCategory + "')]");

        subCategoryLink.shouldBe(visible, Duration.ofSeconds(15)).click();

        return new CategoryPage();
    }

    public DeliveryPage openDeliverySettings() {
        SelenideElement deliveryButton = $x("//span[contains(@class, 'ShipToHeaderItem') or contains(@class, 'GeoTooltip') or contains(@class, 'city')]");
        if (!deliveryButton.exists()) {
            deliveryButton = $x("//*[contains(@data-testid, 'delivery') or contains(@data-testid, 'ship')]");
        }
        if (!deliveryButton.exists()) {
            deliveryButton = $x("//*[contains(@class, 'header')]//*[contains(text(), 'Москва') or contains(text(), 'Санкт-Петербург') or contains(text(), 'Краснодар') or contains(text(), 'Россия')]");
        }
        if (!deliveryButton.exists()) {
            deliveryButton = $x("//*[contains(@class, 'header')]//span[contains(text(), 'Доставка')]");
        }
        deliveryButton.shouldBe(visible, Duration.ofSeconds(15)).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new DeliveryPage();
    }

    public String getDeliveryCity() {
        SelenideElement deliveryCity = $x("//span[contains(@class, 'ShipToHeaderItem') or contains(@class, 'GeoTooltip')]");
        if (!deliveryCity.exists()) {
            deliveryCity = $x("//*[contains(@data-testid, 'delivery')]");
        }
        if (!deliveryCity.exists()) {
            deliveryCity = $x("//*[contains(@class, 'header')]//span[contains(text(), 'Краснодар') or contains(text(), 'Москва') or contains(text(), 'Санкт-Петербург')]");
        }
        return deliveryCity.shouldBe(visible, Duration.ofSeconds(15)).getText();
    }
}