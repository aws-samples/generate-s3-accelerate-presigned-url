# Generate S3 Presigned URL with S3 Transfer Acceleration

This project demonstrates how to generate an [Amazon S3](https://aws.amazon.com/s3/) presigned URL with [S3 Transfer Acceleration](https://aws.amazon.com/s3/transfer-acceleration/), using [Amazon API Gateway](https://aws.amazon.com/api-gateway/) REST API and [AWS Lambda](https://aws.amazon.com/lambda/) function. The Lambda function, composed in Java 21, is responsible for generating a presigned URL to allow customers to upload a single file into S3, with S3 Transfer Acceleration enabled, to speed up content transfers to Amazon S3 securely, over long distances. The API is protected by IAM authentication, to protect against non-authenticated users.

You can deploy this application with the [AWS Serverless Application Model (SAM) CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/what-is-sam.html).

## Prerequisites

- [AWS CLI](https://aws.amazon.com/cli/) installed and configured with your AWS credentials
- [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html) installed
- [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) installed

## Deployment

1. Clone the repository:

   ```bash
   git clone https://github.com/aws-samples/generate-s3-accelerate-presigned-url.git
   ```

2. Change to the project directory:

   ```bash
   cd generate-s3-accelerate-presigned-url
   ```

3. Build the application:

   ```bash
   sam build
   ```

4. Deploy the application:

   ```bash
   sam deploy --guided
   ```

   During the deployment process, you will be prompted to provide the following parameters:

   - `S3BucketName`: The name of the S3 bucket where the uploaded files will be stored.

   The SAM CLI will guide you through the rest of the deployment process, including creating an AWS CloudFormation stack and deploying the necessary resources.

## Usage

After the deployment is complete, the SAM CLI will output the API endpoint URL. You can use this endpoint to generate a presigned URL for uploading files to the specified S3 bucket with S3 Transfer Acceleration enabled.

To generate a presigned URL, send a `POST` request to the `/upload` endpoint with the `filename` query parameter, like this:

```
POST https://<api-id>.execute-api.<region>.amazonaws.com/Prod/upload?filename=example.txt
```

The response will contain the presigned URL, which you can use to upload the file to S3 with Transfer Acceleration enabled.

Here's an example using `curl`:

```bash
curl -X POST https://<api-id>.execute-api.<region>.amazonaws.com/Prod/upload?filename=example.txt
```

This will return a JSON response with the presigned URL:

```json
{
  "presignedUrl": "https://my-bucket.s3-accelerate.amazonaws.com/example.txt?....."
}
```

You can then use the returned presigned URL to upload the file to the specified S3 bucket.

## Security

The API is protected by IAM authentication to prevent unauthorized access. Make sure to configure the necessary IAM permissions for your application to access the API and the S3 bucket.

## License

This project is licensed under the [MIT-0 License](LICENSE).
