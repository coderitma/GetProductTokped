package getproductstokped;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class GrabDataProdukTokped {
  WebDriver driver;
  int limitScroll = 180;
  int limitPage = 3;
  
  public GrabDataProdukTokped() {
    System.setProperty(
        "webdriver.chrome.driver", 
        "C:\\webdrivers\\chromedriver.exe");
    
    driver = new ChromeDriver();
    
    driver.get("https://www.tokopedia.com/search?st=product&q=laptop&srp_component_id=02.01.00.00&srp_page_id=&srp_page_title=&navsource=");
  }
  
  public void scroll() {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    for (int i = 0; i <= limitScroll; i++) {
      js.executeScript("window.scrollBy(0, " + i + ")");
    }
  }
  
  public void getData() {
    String sql = "INSERT INTO produk (id, nama, harga, toko) "
        + "VALUES (NULL, ?, ?, ?)";
    
    try (Connection conn = DBUtil.getConnection()) {
      PreparedStatement ps = conn.prepareStatement(sql);
      
      this.scroll();
      
      JavascriptExecutor js = (JavascriptExecutor) driver;
      
      for (int i = 0; i < limitPage; i++) {
        List<WebElement> daftarNamaProduk = driver.findElements(By.xpath("//div[@data-testid='spnSRPProdName']"));
        List<WebElement> daftarHargaProduk = driver.findElements(By.xpath("//div[@data-testid='spnSRPProdPrice']"));
        List<WebElement> daftarNamaToko = driver.findElements(
            By.xpath("//span[@data-testid='spnSRPProdTabShopLoc']/following-sibling::span"));
        
        for (int x = 0; x < daftarNamaProduk.size(); x++) {
          System.out.println(daftarNamaProduk.get(x).getText() 
              + " " 
              + daftarHargaProduk.get(x).getText() 
              + " " 
              + daftarNamaToko.get(x).getText());
          
          int harga = Integer.parseInt(daftarHargaProduk.get(x).getText()
                .replace("Rp", "").replace(".", ""));
          
          ps.setString(1, daftarNamaProduk.get(x).getText());
          ps.setInt(2, harga);
          ps.setString(3, daftarNamaToko.get(x).getText());
          ps.addBatch();
          
        }
        
        WebElement next = driver.findElement(By.xpath("//li/button[@aria-label='Laman berikutnya']"));
        js.executeScript("arguments[0].click()", next);
        this.scroll();
      }
      
      driver.close();
      ps.executeBatch();
      ps.close();
      conn.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    
    
  }
  
  public static void main(String[] args) {
    GrabDataProdukTokped tokped = new GrabDataProdukTokped();
    tokped.getData();
  }
}
