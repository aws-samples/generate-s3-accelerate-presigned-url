package GenerateUrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.S3Configuration;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    // Get environment variable BUCKET_NAME
    private static String BUCKET_NAME = System.getenv("BUCKET_NAME");

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

         try {
            // get filename from query params
            String filename = input.getQueryStringParameters().get("filename");

            // generate presigned url
            String presignedUrl = generatePresignedUrl(BUCKET_NAME, filename);

            // return response with presigned url and pageContents as body
            String output = String.format("{ \"presignedUrl\": \"%s\" }", presignedUrl);

            return response
                    .withStatusCode(200)
                    .withBody(output);
         } catch (NullPointerException e) {
            return response
                    .withBody("{}")
                    .withStatusCode(500);
         }
    }

    // Generate a presigned S3 URL, using S3 acceleration
    private String generatePresignedUrl(String bucketName, String objectKey) {
        // Create a new S3Presigner
        S3Presigner presigner = S3Presigner.builder()
                                            .serviceConfiguration(S3Configuration.builder()
                                                                                 .checksumValidationEnabled(false)
                                                                                 .accelerateModeEnabled(true)
                                                                                 .build())
                                            .build();
        // Set the presigned URL to expire after one hour
        java.time.Duration expiration = java.time.Duration.ofHours(1);

        // Generate the presigned URL
        String presignedUrl = presigner.presignGetObject(builder ->
                builder.signatureDuration(expiration)
                        .getObjectRequest(builder1 ->
                                builder1.bucket(bucketName).key(objectKey))).url().toString();

        return presignedUrl;
    }
}
