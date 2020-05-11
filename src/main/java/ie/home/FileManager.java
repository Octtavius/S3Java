package ie.home;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.services.s3.model.S3Object;

public class FileManager {


	public void storeFile(String destination, String name, S3Object s3Object) {
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
