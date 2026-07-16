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

    private static final String AUDIO_VIDEO_URL_PATH = "/category/16046/audio-video";

    private final SelenideElement audioVideoBreadcrumb = $x(
            "//a[contains(@href,'/category/16046/audio-video')]" +
                    "//*[normalize-space()='Аудио- и видеотехника']");
    private final ElementsCollection productCards = $$x(
            "//*[contains(@class,'CommonShelf_Snippet__product')]");

    /** Ждёт нужный URL, хлебные крошки и непустой список товаров. */
    public CategoryPage waitUntilOpened() {
        webdriver().shouldHave(urlContaining(AUDIO_VIDEO_URL_PATH), Duration.ofMinutes(3));
        dismissBlockingOverlays(Duration.ofSeconds(2));
        audioVideoBreadcrumb.shouldBe(visible, Duration.ofSeconds(30));
        productCards.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(30));
        return this;
    }

    /** Возвращает количество отображаемых товарных карточек. */
    public int getProductsCount() {
        return productCards.size();
    }
}
