# Selenium Automation Testing Framework

Framework kiểm thử tự động áp dụng mô hình Page Object Model (POM) và Data-Driven Testing (DDT).
Dự án được tích hợp CI/CD qua GitHub Actions, chạy song song đa trình duyệt (Chrome, Firefox) qua Selenium Grid và xuất báo cáo bằng Allure Report.

## 🛠 Công nghệ sử dụng
- Java 17
- Selenium WebDriver 4.x
- TestNG
- Maven
- Allure Report
- Docker & Selenium Grid 4
- GitHub Actions (CI/CD)

## 🚀 Cách chạy trên máy cá nhân (Local)
1. Cài đặt Java 17 và Maven.
2. Clone repository này về máy.
3. Mở Terminal và chạy lệnh:
   `mvn clean test`