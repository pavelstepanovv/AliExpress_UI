package elements;

public class Link extends BaseElement {

    private static final String LINK_BY_TEXT_XPATH = "//a[contains(text(), '%s')]";
    private static final String LINK_BY_HREF_XPATH = "//a[contains(@href, '%s')]";

    private Link(String xpath, String value) {
        super(xpath, value);
    }

    public static Link byText(String text) {
        return new Link(LINK_BY_TEXT_XPATH, text);
    }

    public static Link byHref(String href) {
        return new Link(LINK_BY_HREF_XPATH, href);
    }

    public String getHref() {
        return element.getAttribute("href");
    }
}