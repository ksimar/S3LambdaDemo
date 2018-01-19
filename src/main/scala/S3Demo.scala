import java.io.File

import com.amazonaws.services.lambda.runtime.events.S3Event
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.{BucketNotificationConfiguration, LambdaConfiguration}

import scala.io.Source

class S3Lambda extends RequestHandler[S3Event, Int] {

  private val amazonS3Client = AmazonS3ClientBuilder.defaultClient()

  override def handleRequest(event: S3Event, context: Context): Int = {
    event.getRecords.size()
  }

  def createBucket(bucketName: String) = {
    amazonS3Client.createBucket(bucketName)
  }

  def uploadToS3(bucket: String, fileName: String, file: File) = {
    amazonS3Client.putObject(bucket, "myFile", file)
  }

  def readFromS3(bucket: String, file: String) = {
    val s3Object = amazonS3Client.getObject(bucket, file)
    Source.fromInputStream(s3Object.getObjectContent).getLines()
  }

  /**
    *
    * @param lambdaFuncARN Amazon Resource Name(ARN) of the Lambda function
    * @param event an S3 Event like "s3:ObjectCreated:*", and any one of the S3 Events available in enum S3Event
    * @return
    */
  def setBucketNotification(bucketName: String, lambdaFuncARN: String, event: String) = {
    val conf = new BucketNotificationConfiguration()
    conf.addConfiguration("lambdaConfig", new LambdaConfiguration(lambdaFuncARN, event))
    amazonS3Client.setBucketNotificationConfiguration(bucketName, conf)
  }

}

object S3Demo extends App {

  private val myFile = "myFile"
  val fileToUpload = new File(myFile)
  val bucketName = "myBucket"
  val s3LambdaClient = new S3Lambda
  s3LambdaClient.setBucketNotification(bucketName, "", "s3:ObjectCreated:*")
  val data = s3LambdaClient.readFromS3(bucketName, myFile)
  s3LambdaClient.createBucket(bucketName)
  s3LambdaClient.uploadToS3(bucketName, myFile, fileToUpload)

  for (line <- data) {
    println(s"$line")
  }

}
