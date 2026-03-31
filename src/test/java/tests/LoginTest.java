package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Kiểm tra đăng nhập thành công")
    public void testLoginSuccess() {
        // BaseTest đã tự gọi getDriver().get(URL) trong hàm @BeforeMethod
        LoginPage loginPage = new LoginPage(getDriver());
        
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Assert chỉ nằm trong class Test
        Assert.assertTrue(inventoryPage.isLoaded(), "Trang Inventory chưa được load thành công!");
    }

    @Test(description = "Kiểm tra đăng nhập sai mật khẩu")
    public void testLoginWrongPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        
        loginPage.loginExpectingFailure("standard_user", "wrong_pass");
        
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Không hiện thông báo lỗi!");
        Assert.assertTrue(loginPage.getErrorMessage().contains("do not match"), "Sai nội dung lỗi!");
    }

    @Test(description = "Kiểm tra tài khoản bị khóa")
    public void testLoginLockedUser() {
        LoginPage loginPage = new LoginPage(getDriver());
        
        loginPage.loginExpectingFailure("locked_out_user", "secret_sauce");
        
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"), "Lỗi khóa tài khoản sai!");
    }
}