package statuschecker;

public interface Source {
	
	/**
	 * Loads source to be parsed
	 */
	public abstract void load();
	
	/**
	 * Returns parsed project text from the source 
	 */
	public String getText();

}
