import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import com.despegar.sparkjava.test.SparkServer;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import spark.servlet.SparkApplication;

import static org.junit.Assert.assertEquals;

public class MainTest {

    public static class WebAppTestSparkApp implements SparkApplication {
        public void init() {
            String[] arr = {};
            Main.main(arr);
        }
    }

    @ClassRule
    public static SparkServer<WebAppTestSparkApp> testServer = new SparkServer<>(WebAppTestSparkApp.class, 8080);

    @Test
    public void serverRespondsSuccessfully() throws HttpClientException {
        GetMethod request = testServer.get("/", false);
        HttpResponse httpResponse = testServer.execute(request);
        assertEquals(200, httpResponse.code());
    }
}
