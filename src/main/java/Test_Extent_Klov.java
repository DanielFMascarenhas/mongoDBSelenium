import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentKlovReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.Test;

public class Test_Extent_Klov {

    WebDriver driver;

    ExtentReports extent;
    ExtentTest extentTest;

    String baseURL = "https://www.lambdatest.com/selenium-playground/simple-form-demo";
    String enterMessage = "//input[@id='user-message']";
    String getCheckedValue = "//button[@id='showInput']";
    String yourMessage = "(//div[@id='user-message']/p)[1]";

    //This method will establish connection with MongoDB and would initialize Klov server. 
    // Provide below connection parameters
    //{MongoDBHost}: IP Address of machine where MongoDB is hosted. Same as earlier
    //{KlovHost}: Centos machine host name, where Klov docker image is hosted.
    //{KlovPort}: Port where Klov Server is listening as mentioned in Docker-compose.yml file. By default, it is 80. Give 80.
    @BeforeClass
    private void klovSetup(){
        ExtentKlovReporter klov = new ExtentKlovReporter("LambdaTest");
        klov.initMongoDbConnection("{MongoDBHost}",27017);
        klov.initKlovServerConnection("http://{KlovHost}:{KlovPort}");
      
        //Create an instance of ExtentReports.
        extent = new ExtentReports();
        // Attach Extent Reports to Klov Server. This will publish the Extent reports of all the tests of this class to Klov server
        extent.attachReporter(klov);
    }

    // This method will intialize Chrome browser in an headless manner
    private void browserSetup(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(120, TimeUnit.MILLISECONDS);
    }

    // Lets look at how Test1 execution details can be pushed to Extent reports and then to Klov server 
    @Test
    public void Test1(){

        browserSetup();

        // Create entry for this test into extent report. By giving key details, such as Test name, description, author etc. 
        extentTest = extent.createTest("MongoDBTest 1", "Detailed description of test 1");
        extentTest.assignAuthor("Guru");

        // Log information about your test into the extent report
        extentTest.info("Started with MongoDBTest 1");
        driver.get(baseURL);
        driver.findElement(By.xpath(enterMessage)).sendKeys("MongoDBTest MongoDB");
        driver.findElement(By.xpath((getCheckedValue))).click();

        // Based on testcase checkpoints, append final execution status into extent Report
        try {
            Assert.assertEquals(driver.findElement(By.xpath(yourMessage)).isDisplayed(), true, "Message is not matched.");
            extentTest.pass("PASS");
        }catch(AssertionError e){
            extentTest.fail("FAIL: "+e.getMessage());
        }

    }

    @Test
    public void Test2(){
        browserSetup();

        // Create entry for this test into extent report. By giving key details, such as Test name, description, author etc
        extentTest = extent.createTest("MongoDBTest 2", "Detailed description of test 2");
        extentTest.assignAuthor("Geeta");

        // Log information about your test into the extent report
        extentTest.info("Started with MongoDBTest 2");
        driver.get(baseURL);
        driver.findElement(By.xpath(enterMessage)).sendKeys("MongoDBTest MongoDB");
        driver.findElement(By.xpath((getCheckedValue))).click();

        // Based on testcase checkpoints, append final execution status into extent Report
        try {
            Assert.assertEquals(driver.findElement(By.xpath(yourMessage)).isDisplayed(), false, "Message is matched.");
            extentTest.pass("PASS");
        }catch(AssertionError e){
            extentTest.fail("FAIL: "+e.getMessage());
        }

    }


}
