package getproductstokped;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class GrabDataProdukTokped {
  WebDriver driver;
  int limitScroll = 180;
  int limitPage = 7;
  
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
      }
      
      WebElement next = driver.findElement(By.xpath("//li/button[@aria-label='Laman berikutnya']"));
      js.executeScript("arguments[0].click()", next);
      this.scroll();
    }
    
    driver.close();
  }
  
  public static void main(String[] args) {
    GrabDataProdukTokped tokped = new GrabDataProdukTokped();
    tokped.getData();
  }
}
