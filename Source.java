package statuschecker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public abstract class Source {
	
	String output = "";
	String stringURL;
	
	public Source(String stringURL){
		this.stringURL = stringURL;
	}
	
	/**
	 * Loads source to be parsed
	 */
	public abstract void load();
	
	/**
	 * Returns parsed project text from the source 
	 */
	public String getText(){
		return sort(output);
	}
	
	/**
	 * Sorts the text by line
	 */
	protected String sort(String text){
		Scanner sc = new Scanner(text);
		ArrayList<String> list = new ArrayList<>();
		
		String result = "";
		
		while(sc.hasNext()){
			list.add(sc.nextLine());
		}

		Comparator<String> order = new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		};
		
		list.sort(order);
		
		for(String line : list){
			result += line + "\n";
		}
		
		sc.close();
		return result.trim();
	}

}
