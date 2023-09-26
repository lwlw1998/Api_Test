package stepDefination;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class FoodicsApiSteps{
    private WebDriver driver;
    private String baseUrl = "https://pay2.foodics.dev/login";
    private String email;
    private String password;
    private String token;
    private Response response;

    @Given("I have a valid email and password")
    public void setCredentials() {
        email = "merchant@foodics.com";
        password = "123456";
    }

    @When("I send a POST request to {string}")
    public void sendPostRequest(String endpoint) {
        response = RestAssured.given()
                .contentType("application/json")
                .body("{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}")
                .when()
                .post(baseUrl + endpoint);
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Then("the response body should contain a token")
    public void verifyTokenInResponseBody() {
        token = response.jsonPath().getString("token");
        Assert.assertNotNull(token);
    }

    @When("I send a GET request to {string}")
    public void sendGetRequest(String endpoint) {
        response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + endpoint);
    }

    @Then("the response body should contain the user email")
    public void verifyUserEmailInResponseBody() {
        String userEmail = response.jsonPath().getString("email");
        Assert.assertNotNull(userEmail);
        Assert.assertEquals(email, userEmail);
    }

    @Given("I am on the login page")
    public void navigateToLoginPage() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        driver.get(baseUrl + "/login");
    }

    @When("I enter the email and password")
    public void enterEmailAndPassword() {
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
    }

    @When("I click on Login button")
    public void clickLoginButton() {
        driver.findElement(By.xpath("//button[contains(text(), 'Login')]")).click();
    }

    @Then("I should be logged in successfully")
    public void verifySuccessfulLogin() {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/dashboard"));
    }
}


