package pages;

import com.codeborne.selenide.ElementsCollection;
import elements.Item;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;

/** Компонент окна с вариантами отправки ссылки на товар. */
public class SharePopup extends BasePage {

    // СТАТИЧЕСКИЕ ПОЛЯ (константы)
    private static final int POPUP_TIMEOUT_SEC = 15;

    // ПОЛЯ ЭКЗЕМПЛЯРА
    private final Item popup = Item.byClass("SharingPdpTooltip__showTooltip__");
    private final Item copyLinkButton = Item.byClass("SharingPdpCopyBlock__copyButton__");
    private final ElementsCollection socialIcons = $$x(
            "//*[contains(@class,'SharingPdpTooltip__showTooltip__')]" +
                    "//*[contains(@class,'SharingPdpSocialElements__socialIcon__')]");

    // КОНСТРУКТОРЫ
    public SharePopup() {
        // пустой конструктор
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ

    public SharePopup waitUntilOpened() {
        popup.waitUntilVisible(Duration.ofSeconds(POPUP_TIMEOUT_SEC));
        copyLinkButton.waitUntilVisible(Duration.ofSeconds(POPUP_TIMEOUT_SEC));
        socialIcons.filterBy(visible)
                .shouldHave(sizeGreaterThan(0), Duration.ofSeconds(POPUP_TIMEOUT_SEC));
        return this;
    }

    public boolean isDisplayed() {
        return popup.isDisplayed();
    }

    public boolean isCopyLinkButtonDisplayed() {
        return copyLinkButton.isDisplayed();
    }

    public int getVisibleSocialIconsCount() {
        return socialIcons.filterBy(visible).size();
    }
}
