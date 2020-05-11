package ie.home;

import java.util.List;

import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;

public class MyS3Client {

	private final AmazonS3 s3Client;

	/*1. example of passing client in constructor
	 *  
	public MyS3Client(AmazonS3 s3Client) {
		this.s3Client = s3Client;
	}
	 */
	
	public MyS3Client(AWSSessionCredentials credentials) {
		this.s3Client = AmazonS3ClientBuilder.standard()
    			.withCredentials(new AWSStaticCredentialsProvider(credentials))
    			.withRegion(Regions.EU_WEST_1)
    			.build();
	}
	
	
	
	public Bucket createBucket(String name) {
			CreateBucketRequest request = new CreateBucketRequest(name);
			
			return s3Client.createBucket(request);
	}
	
	public List<Bucket> listBuckets() {
		return s3Client.listBuckets();
	}
}
