package tests;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Chức năng xác thực")
@Feature("Đăng nhập người dùng")
public class LoginTest extends BaseTest {

    @Test(description = "Kiểm tra đăng nhập thành công với tài khoản chuẩn")
    @Story("UC-001: Đăng nhập hợp lệ")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Hệ thống phải cho phép người dùng đăng nhập khi nhập đúng Username và Password.")
    public void testLoginSuccess() {
        LoginPage loginPage = new LoginPage(getDriver());

        Allure.step("1. Truy cập trang đăng nhập SauceDemo");
        // URL đã được BaseTest mở tự động

        Allure.step("2. Nhập Username và Password bảo mật");
        loginPage.login(ConfigReader.getInstance().getUsername(), ConfigReader.getInstance().getPassword());

        Allure.step("3. Xác minh chuyển hướng sang trang Inventory");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory.html"), "Không vào được trang sản phẩm!");
    }
}