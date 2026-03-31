package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class InventoryPage extends BasePage {

    @FindBy(css = ".inventory_list")
    private WebElement inventoryList;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = ".inventory_item button")
    private List<WebElement> addToCartButtons;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isElementVisible(By.cssSelector(".inventory_list"));
    }

    // Fluent Interface: Thêm xong vẫn ở trang này -> return this
    public InventoryPage addFirstItemToCart() {
        waitAndClick(addToCartButtons.get(0));
        return this;
    }

    // Tìm động theo tên sản phẩm (Cho phép dùng findElement bên TONG Page Object)
    public InventoryPage addItemByName(String name) {
        String xpath = "//div[text()='" + name + "']/ancestor::div[@class='inventory_item']//button";
        WebElement btn = driver.findElement(By.xpath(xpath));
        waitAndClick(btn);
        return this;
    }

    public int getCartItemCount() {
        try {
            return Integer.parseInt(getText(cartBadge));
        } catch (Exception e) {
            return 0; // Xử lý an toàn khi giỏ hàng rỗng (không có badge)
        }
    }

    // Fluent Interface: Chuyển trang
    public CartPage goToCart() {
        waitAndClick(cartLink);
        return new CartPage(driver);
    }
}