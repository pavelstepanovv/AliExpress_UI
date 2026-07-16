package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.ProductPage;
import pages.SearchResultPage;
import pages.SharePopup;

import static org.assertj.core.api.Assertions.assertThat;

/** Тест 9: открытие окна "Поделиться" на странице товара. */
public class ProductShareTest extends BaseTest {

    @Test
    public void testOpenSharePopup() {
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();
        SearchResultPage results = mainPage.search(HEADPHONES_QUERY);
        ProductPage productPage = results.openFirstProduct();
        SharePopup sharePopup = productPage.openSharePopup();

        assertThat(sharePopup.isDisplayed())
                .as("Окно 'Поделиться' должно отображаться")
                .isTrue();
        assertThat(sharePopup.isCopyLinkButtonDisplayed())
                .as("В окне должна отображаться кнопка 'Скопировать ссылку'")
                .isTrue();
        assertThat(sharePopup.getVisibleSocialIconsCount())
                .as("В окне должны отображаться иконки социальных сетей")
                .isGreaterThan(0);
    }
}
