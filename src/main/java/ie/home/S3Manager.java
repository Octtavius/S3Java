package ie.home;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class S3Manager {
	private static final Logger log = Logger.getLogger(S3Manager.class.getName());

	private final AmazonS3 s3Client;

	/*
	 * 1. example of passing client in constructor
	 * 
	 * public MyS3Client(AmazonS3 s3Client) { this.s3Client = s3Client; }
	 */

	public S3Manager(AWSSessionCredentials credentials) {
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
		log.info("Downloading from " + bucket + " the file: " + fileName );
		GetObjectRequest request = new GetObjectRequest(bucket, fileName);
		S3Object s3Object = s3Client.getObject(request);

		storeFile(dirAlt, fileName, s3Object);
	}
	
	public void deleteFile(String bucket, String fileName) {
		log.info("Deleting from " + bucket + " the file: " + fileName );
		DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
		s3Client.deleteObject(request);
	}

	private void storeFile(String destination, String name, S3Object s3Object) {
		File file = new File(destination + "\\" + name);

		InputStream in = null;
		OutputStream out = null;

		try {
			in = s3Object.getObjectContent();
			byte[] buf = new byte[1024];
			out = new FileOutputStream(file);

			int count = -1;
			while ((count = in.read(buf)) != -1) {
				if (Thread.interrupted()) {
					
					out.close();
					// when downloading large files, the thread might get interrupted.
					// if that happens, then the download should be stopped
					// no need to download the rest
					throw new InterruptedException();
				}
				out.write(buf, 0, count);
			}
		} catch (InterruptedException e) {
			System.err.println("Thread was interrupted\n" + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Something wrong with writing the data\n" + e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}

				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void displayFile(File file) {
		FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
