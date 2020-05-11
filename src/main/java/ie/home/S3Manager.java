package ie.home;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PublicAccessBlockConfiguration;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.SetPublicAccessBlockRequest;

public class S3Manager {
	private static final Logger log = Logger.getLogger(S3Manager.class.getName());

	private static final String AWS_ACCESS_KEY = "AWS_ACCESS_KEY";
	private static final String AWS_SECRET_KEY = "AWS_SECRET_KEY";
	AWSSessionCredentials credentials;
	private final AmazonS3 s3Client;

	/*
	 * 1. example of passing client in constructor
	 * 
	 * public MyS3Client(AmazonS3 s3Client) { this.s3Client = s3Client; }
	 */

	public S3Manager() {
		String accessKey = System.getenv(AWS_ACCESS_KEY);
		String secretKey = System.getenv(AWS_SECRET_KEY);

		credentials = new BasicSessionCredentials(accessKey, secretKey, "");
		this.s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.EU_WEST_1).build();
	}

	public void uploadFile(String bucket, String localFile, String localDirectory, String key) {
		File file = new File(localDirectory + "\\" + localFile);
		log.info("Uploading file: " + (localDirectory + "\\" + localFile));

		PutObjectRequest request = new PutObjectRequest(bucket, key, file);
		s3Client.putObject(request);
	}

	public Bucket createBucket(String name) {
		log.info("Creating bucket: " + name);
		CreateBucketRequest request = new CreateBucketRequest(name);

		return s3Client.createBucket(request);
	}

	public List<Bucket> listBuckets() {
		return s3Client.listBuckets();
	}

	public void downloadFile(String bucket, String fileName, String dirAlt) {
		log.info("Downloading from " + bucket + " the file: " + fileName);
		GetObjectRequest request = new GetObjectRequest(bucket, fileName);
		S3Object s3Object = s3Client.getObject(request);

		FileManager fm = new FileManager();
		
		fm.storeFile(dirAlt, fileName, s3Object);
	}

	public void deleteFile(String bucket, String fileName) {
		log.info("Deleting from " + bucket + " the file: " + fileName);
		DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
		s3Client.deleteObject(request);
	}

	public List<String> listFiles(String bucket) {
		ListObjectsRequest request = new ListObjectsRequest();
		request.setBucketName(bucket);
		ObjectListing result = s3Client.listObjects(request);
		log.info("Fetched " + result.getObjectSummaries().size() + " objects from bucket " + bucket);

		return result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
	}

	public void copyFile(String sourceBucket, String sourceKey, String targetBucket) {
		CopyObjectRequest request = new CopyObjectRequest(sourceBucket, sourceKey, targetBucket, sourceKey);
		s3Client.copyObject(request);
	}
	
	public void blockPublicAccess(String bucketName) {
		PublicAccessBlockConfiguration config = new PublicAccessBlockConfiguration();
		config
			.withBlockPublicAcls(true)
			.withBlockPublicPolicy(true)
			.withIgnorePublicAcls(true)
			.withRestrictPublicBuckets(true);
		
		SetPublicAccessBlockRequest request = new SetPublicAccessBlockRequest();
		request
			.withBucketName(bucketName)
			.withPublicAccessBlockConfiguration(config);
		
		s3Client.setPublicAccessBlock(request);
	}

	public String createPresignedUrl(String bucketName, String key) {
		Date expiration = new Date();
		long seconds30 = 1000 * 30;
		expiration.setTime(expiration.getTime() + seconds30);
		
		GeneratePresignedUrlRequest request =  new GeneratePresignedUrlRequest(bucketName, key);
		request.withMethod(HttpMethod.GET)
		.withExpiration(expiration);
		
		return s3Client.generatePresignedUrl(request).toString();
	}

	public void deleteBucket(String bucketName) {
		DeleteBucketRequest request = new DeleteBucketRequest(bucketName);
		s3Client.deleteBucket(request);
	}
	
	
}
