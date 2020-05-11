package ie.home;

import java.util.List;

import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final String AWS_ACCESS_KEY = "AWS_ACCESS_KEY_ID";
	private static final String AWS_SECRET_KEY = "AWS_SECRET_ACCESS_KEY";
	private static final String MY_PRIV_BUCKET_NAME = "morcov-bucket";
	private static final String MY_ANOTHER_BUCKET_NAME = "temp-morcov-bucket";
	private static final String F1 = "file1.txt";
	private static final String F2 = "file2.txt";
	private static final String F3 = "file3.txt";
	private static final String DIR = "C:\\Users\\I323506\\Documents\\Tutorials\\AWS\\S3\\java-proj\\bucket1";
	private static final String DIR_ALT = "C:\\Users\\I323506\\Documents\\Tutorials\\AWS\\S3\\java-proj\\bucket2";
	
    public static void main( String[] args )
    {
    	String accessKey = System.getenv("AWS_ACCESS_KEY");
    	String secretKey = System.getenv("AWS_SECRET_KEY");
    	
    	AWSSessionCredentials credentials = new BasicSessionCredentials(accessKey, secretKey, "");

//    	MyS3Client myS3Client = new MyS3Client(s3Client);
    	MyS3Client myS3Client = new MyS3Client(credentials);

    	List<Bucket> buckets = myS3Client.listBuckets();
        System.out.println(buckets.size());
    	/*
    	 * This is created in the MyS3Client. 
    	 * 
    	 *AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
    			.withCredentials(new AWSStaticCredentialsProvider(credentials))
    			.withRegion(Regions.EU_WEST_1)
    			.build(); 
    	 */
    	
    	
    	/* this will read the credentials set in aws cli. the example above is just to show how to specificaly
    	 * pass credentials.
    	 * 
    	 * AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).build();
    	 */ 
    }
}
