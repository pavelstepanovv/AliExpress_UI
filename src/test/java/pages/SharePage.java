package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;

public class SharePage extends BasePage {

    private final SelenideElement sharePanel = $x("//div[contains(@class, 'share') or contains(@class, 'Share') or contains(@data-testid, 'share')]");
    private final SelenideElement copyLinkButton = $x("//button[contains(text(), 'Скопировать ссылку') or contains(text(), 'Copy link') or contains(@class, 'copy')]");
    private final ElementsCollection socialIcons = $$x("//a[contains(@href, 'facebook') or contains(@href, 'twitter') or contains(@href, 'vk') or contains(@href, 'telegram') or contains(@href, 'whatsapp') or contains(@class, 'social')]");

    public boolean isShareDialogDisplayed() {
        try {
            return sharePanel.isDisplayed() || socialIcons.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasCopyLinkButton() {
        return copyLinkButton.isDisplayed();
    }

    public boolean hasSocialIcons() {
        return socialIcons.size() > 0;
    }

    public int getSocialNetworksCount() {
        return socialIcons.size();
    }
}