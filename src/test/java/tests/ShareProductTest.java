package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.ProductPage;
import pages.SearchResultPage;
import pages.SharePage;

import static org.assertj.core.api.Assertions.assertThat;

public class ShareProductTest extends BaseTest {

    @Test
    public void testShareProduct() {
        MainPage mainPage = new MainPage().open();
        SearchResultPage results = mainPage.search(HEADPHONES_QUERY);
        ProductPage productPage = results.openFirstProduct();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SharePage sharePage = productPage.openShareDialog();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean shareDialogDisplayed = sharePage.isShareDialogDisplayed();
        System.out.println("Шторка поделиться отображается: " + shareDialogDisplayed);

        boolean hasCopyButton = sharePage.hasCopyLinkButton();
        System.out.println("Кнопка скопировать ссылку: " + hasCopyButton);

        boolean hasSocial = sharePage.hasSocialIcons();
        System.out.println("Есть иконки соцсетей: " + hasSocial);

        int socialCount = sharePage.getSocialNetworksCount();
        System.out.println("Количество иконок соцсетей: " + socialCount);

        assertThat(shareDialogDisplayed)
                .as("Должна открыться шторка 'Поделиться'")
                .isTrue();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}