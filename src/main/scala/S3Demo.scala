import java.io.File

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.lambda.runtime.events.S3Event
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.amazonaws.services.s3.AmazonS3ClientBuilder

import scala.io.Source

class S3Lambda extends RequestHandler[S3Event, Int]{

  val AWS_ACCESS_KEY = ""
  val AWS_SECRET_KEY = ""

  val aWSCredentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
  val amazonS3Client = AmazonS3ClientBuilder.defaultClient()

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

}

object S3Demo extends App {

  private val myFile = "myFile"

  val fileToUpload = new File("myFile")
  val bucketName = "myBucket"
  val s3LambdaClient = new S3Lambda
  s3LambdaClient.createBucket(bucketName)

  val data = s3LambdaClient.readFromS3(bucketName, myFile)
  s3LambdaClient.uploadToS3(bucketName, myFile, fileToUpload)

  for (line <- data) {
    println(s"$line")
  }

}
