package tests;

import framework.base.BaseTest;
import framework.pages.CartPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    @Test(description = "Kiểm tra Fluent Interface: Login -> Add item -> Vào giỏ hàng")
    public void testAddFirstItemAndGoToCart() {
        LoginPage loginPage = new LoginPage(getDriver());
        
        // Sức mạnh của Fluent Interface (Viết code thành 1 chuỗi liên kết)
        CartPage cartPage = loginPage.login("standard_user", "secret_sauce")
                                     .addFirstItemToCart()
                                     .goToCart();
        
        Assert.assertEquals(cartPage.getItemCount(), 1, "Giỏ hàng phải có 1 sản phẩm!");
    }

    @Test(description = "Kiểm tra thêm sản phẩm theo tên")
    public void testAddItemByName() {
        LoginPage loginPage = new LoginPage(getDriver());
        
        CartPage cartPage = loginPage.login("standard_user", "secret_sauce")
                                     .addItemByName("Sauce Labs Bike Light")
                                     .goToCart();
        
        Assert.assertTrue(cartPage.getItemNames().contains("Sauce Labs Bike Light"), 
            "Không tìm thấy sản phẩm đúng tên trong giỏ!");
    }

    @Test(description = "Kiểm tra xóa sản phẩm khỏi giỏ hàng")
    public void testRemoveItemFromCart() {
        LoginPage loginPage = new LoginPage(getDriver());
        
        CartPage cartPage = loginPage.login("standard_user", "secret_sauce")
                                     .addFirstItemToCart()
                                     .goToCart();
        
        // Xóa sản phẩm
        cartPage.removeFirstItem();
        
        // Kiểm tra an toàn: số lượng = 0, không văng lỗi Exception
        Assert.assertEquals(cartPage.getItemCount(), 0, "Giỏ hàng phải trống sau khi xóa!");
    }
}