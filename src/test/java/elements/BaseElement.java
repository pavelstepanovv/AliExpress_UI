package elements;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class BaseElement {
    protected final SelenideElement element;

    protected BaseElement(String xpath, String value) {
        this.element = $x(String.format(xpath, value));
    }

    protected BaseElement(SelenideElement element) {
        this.element = element;
    }

    public boolean isDisplayed() {
        return element.isDisplayed();
    }

    public String getText() {
        return element.getText();
    }

    public void click() {
        element.click();
    }
}