package logger;

public interface Logger {

	boolean add(String content);
	public String retrieveAll();
	public boolean makeEmpty();
}
