package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    // 1. Định nghĩa các Locator bằng @FindBy
    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // Constructor gọi super(driver) để kế tạo Explicit Wait từ BasePage
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // 2. Các Action Methods
    // Fluent Interface: Đăng nhập thành công trả về trang InventoryPage
    public InventoryPage login(String user, String pass) {
        waitAndType(usernameField, user);
        waitAndType(passwordField, pass);
        waitAndClick(loginButton);
        return new InventoryPage(driver);
    }

    // Đăng nhập biết trước sẽ thất bại (sai pass, rỗng...) trả về chính LoginPage
    public LoginPage loginExpectingFailure(String user, String pass) {
        waitAndType(usernameField, user);
        waitAndType(passwordField, pass);
        waitAndClick(loginButton);
        return this;
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isElementVisible(By.cssSelector("[data-test='error']"));
    }
}