package GenerateUrl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import GenerateUrl.App;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class AppTest {

  @Test
  public void environmentVariable() {
    assertEquals(System.getenv("BUCKET_NAME"), "demo-bucket");
  }

  @Test
  public void successfulResponse() {
    App app = new App();
    
    APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
    HashMap<String, String> queryStringParameters = new HashMap<>();
    queryStringParameters.put("filename", "my-profile-image.jpg");
    event.setQueryStringParameters(queryStringParameters);
    
    APIGatewayProxyResponseEvent result = app.handleRequest(event, null);
    assertEquals(200, result.getStatusCode().intValue());
    assertEquals("application/json", result.getHeaders().get("Content-Type"));
    String content = result.getBody();
    assertNotNull(content);
    assertTrue(content.contains("\"presignedUrl\""));
  }
}
