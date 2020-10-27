package stepDefinitions;

import io.cucumber.core.internal.gherkin.deps.com.google.gson.JsonObject;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.User;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Patch;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.thucydides.core.util.EnvironmentVariables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import screenplay.FindAUser;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.*;


public class list_of_users_stepDefinition {
    EnvironmentVariables environmentVariables;
    private String theRestApiBaseUrl;
    private final Logger log = LoggerFactory.getLogger(list_of_users_stepDefinition.class);
    Actor james;
    JsonObject jsonObject = new JsonObject();

    @Before
    public void setRestApiBaseUrl(){
        theRestApiBaseUrl = environmentVariables.
                optionalProperty("restApi.baseUrl").
                orElse("https://reqres.in");

        log.info("the rest api url is : "+theRestApiBaseUrl);
    }
    @Given("^(?:.*) is at the base URL")
    public void jamesHasLaunchedReqRes() {
        james = Actor.named("James the customer").whoCan(CallAnApi.at(theRestApiBaseUrl));
    }

    @When("James wants to fetch list of users by page {string}")
    public void jamesWantsToFetchListOfUsersByPage(String count) {

        james.attemptsTo(
                    Get.resource("/api/users?page={page}").
                            with(request -> request.relaxedHTTPSValidation()).
                            with(request -> request.pathParam("page",count)));


        james.should(seeThatResponse("The expected result should be returned : ",response -> response.statusCode(200)
                .body("page",is(Integer.parseInt(count)))));

    }
    @Then("^(?:.*) should be able to list all the users")
    public void JamesShouldBeAbleToListAllTheUsers() {

        int value = SerenityRest.lastResponse().jsonPath().get("page");

        if (value == 1){

            james.should(seeThatResponse("User details should be correct",
                    response -> response.statusCode(200)
                            .body("data.first_name", hasItems("Janet", "George", "Emma", "Eve", "Charles", "Tracey"))));
        }else if(value == 2){
            james.should(seeThatResponse("User details should be correct",
                    response -> response.statusCode(200)
                            .body("data.first_name", hasItems("Michael", "George", "Rachel", "Byron", "Lindsay","Tobias"))));
        }
    }


    @When("James wants to fetch single user")
    public void jamesWantsToFetchSingleUser() {

        james.attemptsTo(FindAUser.withId(2));

        james.should(seeThatResponse("Expected response should be returned : ",response -> response.statusCode(200)));

        User user = SerenityRest.lastResponse().jsonPath().getObject("data", User.class);
        assertThat(user.getFirstName(),is("Janet"));
    }


    @Then("^(?:.*) should be able to get single user")
    public void jamesShouldBeAbleToGetSingleUser() {
        assertThat(SerenityRest.lastResponse().jsonPath().get("data.first_name"),is(instanceOf(String.class)));
    }

    @When("^(?:.*) wants to add new user")
    public void james_wants_to_add_new_user() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name","deepak");
        jsonObject.addProperty("job","tester");

        james.attemptsTo(Post.to("/api/users").with(request->{
            request.body(jsonObject);
            request.header("Content-Type", "application/json");
            return request;
        }));
    }

    @Then("^(?:.*) should be able to add new user")
    public void james_should_be_able_to_add_new_user() {
        james.should(seeThatResponse("Expected response shoul returned : ",request -> request.statusCode(201)));
    }

    @When("^(?:.*) wants to delete a user$")
    public void jamesWantsToDeleteAUser() {
        james.attemptsTo(Delete.from("/api/users/2")
                .with(request->{
                    request.relaxedHTTPSValidation();
                    return request;
                })
        );
    }

    @Then("^(?:.*) should be able to delete the user$")
    public void jamesShouldBeAbleToDeleteTheUser() {
        james.should(seeThatResponse("The expected result be returned : ",response ->
                response.statusCode(204)));
    }

    @When("^(?:.*) wants to alter a user$")
    public void jamesWantsToAlterAUser() {

        jsonObject.addProperty("name","Aanya");
        james.attemptsTo(Patch.to("/api/users?page=2").
                with(request->{
                    request.body(jsonObject);
                    request.header("content-type","application/json");
                    return request;
                })
                );
    }

    @Then("^(?:.*) should be able to alter the user$")
    public void jamesShouldBeAbleToAlterTheUser() {
        james.should(seeThatResponse("Expected result should returned : ",response ->
                response.statusCode(200)));
    }
}

