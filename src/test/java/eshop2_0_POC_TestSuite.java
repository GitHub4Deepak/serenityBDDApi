
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;
import static io.cucumber.junit.CucumberOptions.SnippetType.CAMELCASE;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin={"pretty"},
        features = "classpath:features",
        snippets = CAMELCASE,
        tags = {"@api"}
)
public class eshop2_0_POC_TestSuite {
}