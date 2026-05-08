package utils;

import com.microsoft.playwright.Page;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.nio.file.Paths;

//import io.qameta.allure.Allure;
//import java.nio.file.Files;
//import java.nio.file.Paths;

public class ScreenshotListener implements ITestListener {

  @Override
  public void onTestFailure(ITestResult result) {

    System.out.println("🔥 TEST FAILED → ambil screenshot");

    Object testClass = result.getInstance();

    try {
      Page page = (Page) testClass.getClass().getField("page").get(testClass);

      String dir = "screenshots";
      new java.io.File(dir).mkdirs();

      java.io.File file = new java.io.File(
    		  dir + "/" + result.getName() + "_" + System.currentTimeMillis() + ".png"
    		);

    		String filePath = file.getAbsolutePath();

    		page.screenshot(new Page.ScreenshotOptions()
    		  .setPath(Paths.get(filePath)));

      System.out.println("📸 Screenshot saved: " + filePath);
      
      /*// 🔥 BARU attach ke Allure
      byte[] screenshotBytes = Files.readAllBytes(Paths.get(filePath));

      Allure.getLifecycle().addAttachment(
          "Screenshot",
          "image/png",
          "png",
          screenshotBytes
      );*/

      // 🔥 MASUKKAN KE REPORT
      String fileUrl = file.toURI().toString();

      Reporter.log("<br><a href='" + fileUrl + "' target='_blank'>📸 View Screenshot</a><br>", true);

      // preview gambar
      Reporter.log("<br><img src='" + fileUrl + "' height='200'/><br>", true);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}