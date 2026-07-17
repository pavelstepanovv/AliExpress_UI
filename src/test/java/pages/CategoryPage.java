package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

/** Page Object страницы товарной подкатегории AliExpress. */
public class CategoryPage extends BasePage {

    // СТАТИЧЕСКИЕ ПОЛЯ (константы)
    private static final int SHORT_TIMEOUT_SEC = 2;
    private static final int LONG_TIMEOUT_SEC = 30;
    private static final int PAGE_LOAD_TIMEOUT_MIN = 3;

    private static final String AUDIO_VIDEO_URL_PATH = "/category/16046/audio-video";

    // ПОЛЯ ЭКЗЕМПЛЯРА
    private final SelenideElement audioVideoBreadcrumb = $x(
            "//a[contains(@href,'/category/16046/audio-video')]" +
                    "//*[normalize-space()='Аудио- и видеотехника']");
    private final ElementsCollection productCards = $$x(
            "//*[contains(@class,'CommonShelf_Snippet__product')]");

    // КОНСТРУКТОРЫ
    public CategoryPage() {
        // пустой конструктор
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ

    public CategoryPage waitUntilOpened() {
        webdriver().shouldHave(urlContaining(AUDIO_VIDEO_URL_PATH), Duration.ofMinutes(PAGE_LOAD_TIMEOUT_MIN));
        dismissBlockingOverlays(Duration.ofSeconds(SHORT_TIMEOUT_SEC));
        audioVideoBreadcrumb.shouldBe(visible, Duration.ofSeconds(LONG_TIMEOUT_SEC));
        productCards.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(LONG_TIMEOUT_SEC));
        return this;
    }

    public int getProductsCount() {
        return productCards.size();
    }
}
