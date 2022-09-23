package logger;
import java.io.BufferedWriter;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.Scanner;


public class FileLogger implements Logger{

	private static FileLogger instance;
	private String fileName;

	public boolean add(String content) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true));
			writer.append(content);
			writer.newLine();
			writer.close();
			return true;
		} catch (IOException e) {
			System.out.println("Exception writing to file.");
			e.printStackTrace();
			return false;
		}
	}

	public String retrieveAll() {
		String result = "";
		 try {
		      File file = new File(fileName);
		      Scanner reader = new Scanner(file);
		      while (reader.hasNextLine()) {
		        String data = reader.nextLine();
		        result+= data+"\n";
		      }
		      reader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return result;
	}
	
	public boolean makeEmpty() {
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.write("");
			writer.close();
			return true;
		} catch (IOException e) {
			System.out.println("Exception writing to file.");
			e.printStackTrace();
			return false;
		}
		
	}

	private FileLogger(String fileName) {
		File file = new File(fileName);
		this.fileName = fileName;
	};
	
	public static FileLogger getInstance() {
		if(instance==null) {
			return  new FileLogger("log.txt");
		}else {
			return instance;
		}
	}

	public static FileLogger getInstance(String fileName) {
		if(instance==null) {
			return  new FileLogger(fileName);
		}else {
			return instance;
		}
	}

}





