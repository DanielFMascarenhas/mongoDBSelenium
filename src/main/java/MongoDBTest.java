import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;



public class MongoDBTest {

    WebDriver driver;
    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> test_execution_info;
    Document doc;

    String baseURL = "https://www.lambdatest.com/selenium-playground/simple-form-demo";
    String enterMessage = "//input[@id='user-message']";
    String getCheckedValue = "//button[@id='showInput']";
    String yourMessage = "(//div[@id='user-message']/p)[1]";

    // This function will initialize MongoClient and will establish DB connection with MongoDB. 
    @BeforeClass
    public void connectToMongoDB(){
        // Connect to MongoDB by giving {MongoDBHost}
        mongoClient = new MongoClient("{MongoDBHost}", 27017);
        // Get Database handle by replacing {MongoDBName} with DB name
        database = mongoClient.getDatabase("{MongoDBName}");
        // Get Database handle by giving collection name created in MongoDB. 
        test_execution_info = database.getCollection("test_execution_details");
        // Initialise an instance of document
        doc = new Document();
    }

    // This function will initialize Selenium webdrivers in headless manner. 
    private void browserSetup(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(120, TimeUnit.MILLISECONDS);
    }

    // This test will showcase how MongoDB can be utilised to push test results
    @Test
    public void Test1(){

        browserSetup();
      
        // Create one MongoDB document to store test results of this test, Test1
        doc = new Document("testname", "MongoDBTest 1");

        // Lets append, some fields and value pairs into MongoDB document as below
        doc.append("testname", "MongoDBTest 1");
        doc.append("description", "Detailed description of a positive test, test 1");
        doc.append("author","Guru");

        //Login to Lambdatest demo url
        driver.get(baseURL);
        driver.findElement(By.xpath(enterMessage)).sendKeys("MongoDBTest MongoDB");
        driver.findElement(By.xpath((getCheckedValue))).click();

        // Lets set the test result accordingly into MongoDB document
        if (driver.findElement(By.xpath(yourMessage)).isDisplayed()){
            doc.append("status","PASS");
        } else{
            doc.append("status","FAIL");
        }
      
        // Finally, Lets now insert document into collection
        test_execution_info.insertOne(doc);
    }

    // This test will showcase how MongoDB can be utilised to push test results
    @Test
    public void Test2(){

        browserSetup();
      
        // Create one MongoDB document to store test results of this test, Test2
        doc = new Document("testname", "MongoDBTest 2");

        // Lets append, some fields and value pairs into MongoDB document as below
        doc.append("testname", "MongoDBTest 2");
        doc.append("description", "Detailed description of a negative test, MongoDBTest 2");
        doc.append("author","Geeta");

        // Login to Lambdatest demo url
        driver.get(baseURL);
        driver.findElement(By.xpath(enterMessage)).sendKeys("MongoDBTest MongoDB");
        driver.findElement(By.xpath((getCheckedValue))).click();

        // Lets set the test result accordingly into MongoDB document
        if (!driver.findElement(By.xpath(yourMessage)).isDisplayed()){
            doc.append("status","FAIL");
        } else{
            doc.append("status","PASS");
        }

        // Finally, Lets now insert document into collection
        test_execution_info.insertOne(doc);

    }

  // This test will showcase how MongoDB can be utilised to perform any CRUD operations on MongoDB
  @Test
    public void otherCRUDOperations(){

        // Create one simple test & append its result into MongoDB
        doc = new Document("testname", "MongoDB update test");
        doc.append("testname", "MongoDB test");
        doc.append("description", "Some description");
        doc.append("author","Gopal");

        //Some test steps here
        //Some test steps here
        //Some test steps here

        doc.append("status","PASS");

        test_execution_info.insertOne(doc);

        //Now Lets update the record of same document

        //Retrieve the id of that particular document
        ObjectId docId  = doc.getObjectId("_id");

        //Construct a MongoDB query where id is equals to particular id. Using Filters we can construct desired query
        Bson query = Filters.eq("_id", docId);

        //Create a set of values to be updated,
        BasicDBObject set = new BasicDBObject("$set", new BasicDBObject("testname", "MongoDB test Updated"));

        //This method will first execute the query. It would then update first record (of search results) with below set of values
        test_execution_info.updateOne(query,set);

        //We can also delete the document
        test_execution_info.deleteOne(query);
    }



}
