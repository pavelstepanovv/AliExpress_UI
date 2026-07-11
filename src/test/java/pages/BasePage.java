package pages;

import com.codeborne.selenide.Selenide;

public class BasePage {

    public static void open(String url) {
        Selenide.open(url);
    }

    public void refresh() {
        Selenide.refresh();
    }

    public String getTitle() {
        return Selenide.title();
    }
}