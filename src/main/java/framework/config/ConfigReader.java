package framework.config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    private static ConfigReader instance;
    private Properties props = new Properties();

    // Singleton Pattern: Đảm bảo chỉ tạo 1 đối tượng duy nhất
    private ConfigReader() {
        // Lấy giá trị biến môi trường env (mặc định là 'dev')
        String env = System.getProperty("env", "dev");
        String path = "src/test/resources/config-" + env + ".properties";
        
        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
            System.out.println("[ConfigReader] Đang dùng môi trường: " + env);
        } catch (Exception e) {
            throw new RuntimeException("Không tìm thấy file config: " + path);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) instance = new ConfigReader();
        return instance;
    }
        public String getPassword() {
        String password = System.getenv("APP_PASSWORD");
        if (password == null || password.isBlank()) {
            return props.getProperty("password", "secret_sauce"); // Fallback
        }
        return password;
    }

    public String getUsername() {
        String username = System.getenv("APP_USERNAME");
        if (username == null || username.isBlank()) {
            return props.getProperty("username", "standard_user");
        }
        return username;
    }

    public String getBaseUrl() { return props.getProperty("base.url", "https://www.saucedemo.com"); }

    
    public String getBrowser() { return props.getProperty("browser", "chrome"); }
    public int getExplicitWait() { return Integer.parseInt(props.getProperty("explicit.wait", "15")); }
    public int getRetryCount() { return Integer.parseInt(props.getProperty("retry.count", "1")); }
}