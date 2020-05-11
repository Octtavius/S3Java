package ie.home;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{

	private static final String MY_PRIV_BUCKET_NAME = "morcov-bucket";
	private static final String MY_ANOTHER_BUCKET_NAME = "buck2morcov";
	private static final String F1 = "file11.txt";
	private static final String F2 = "file22.txt";
	private static final String F3 = "file33.txt";
	private static final String DIR = "C:\\Users\\I323506\\Documents\\Tutorials\\AWS\\S3\\java-proj\\bucket1";
	private static final String DIR_ALT = "C:\\Users\\I323506\\Documents\\Tutorials\\AWS\\S3\\java-proj\\bucket2";
	
    public static void main( String[] args )
    {

    	/*
    	 * This now is created in the MyS3Client. 
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
    	
//    	MyS3Client myS3Client = new MyS3Client(s3Client);
    	S3Manager myS3manager = new S3Manager();
    	/*
    	List<Bucket> buckets = myS3manager.listBuckets();

    	buckets.forEach(file -> {
    		System.out.println(file);
    	});
         
        //upload file
        myS3manager.uploadFile(MY_PRIV_BUCKET_NAME, F1, DIR, F1);
        
        //download file
        myS3manager.downloadFile(MY_PRIV_BUCKET_NAME, F1, DIR_ALT);
        
        // delete file form s3
        myS3manager.deleteFile(MY_PRIV_BUCKET_NAME, F1);
        */
    	
        List<String> files = myS3manager.listFiles(MY_PRIV_BUCKET_NAME);
        files.stream()
        	.forEach(fileName -> {
        		System.out.println(fileName);
        	});
      
        /*
        myS3manager.copyFile(MY_PRIV_BUCKET_NAME, F1, MY_ANOTHER_BUCKET_NAME);
        
        // before running this method, make sure the bucket is public. so that you can see the result
        myS3manager.blockPublicAccess(MY_ANOTHER_BUCKET_NAME);
        
        // generate a url to read the file. test in incognito to read the file using temp url
        String presignedUrl = myS3manager.createPresignedUrl(MY_PRIV_BUCKET_NAME, F1);
        System.out.println(presignedUrl);
        */
    }
}
