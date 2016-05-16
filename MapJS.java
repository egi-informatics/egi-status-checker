package statuschecker;

import static javax.swing.JOptionPane.showMessageDialog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MapJS extends Source{
	
	public MapJS(String stringURL) {
		super(stringURL);
	}

	@Override
	public void load() {
		this.output = "";
		
		URL url = null;
		try {
			url = new URL(stringURL);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		try {
			InputStream stream = url.openStream();
			Scanner s = new Scanner(stream);
			while(s.hasNextLine()){
				read(s.nextLine());
			}
			
			output = cleanUp(output);
			
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
			
			if(e instanceof FileNotFoundException){
				String message = "File not found";
				showMessageDialog(null, message + e.getMessage());
				return;
			}
			
			String message = "Problem with file\n";
			showMessageDialog(null, message + e.getMessage());
		}
	}
	
	private void read(String line){
		String number = "";
		String status = "";
		
		if(line.contains("\"id\" : \"I0")){
			number = getNumber(line);
			output += number + "  ";
			return;
		}
		if(line.contains("\"status\" :")){
			status = getStatus(line);
			output += status + "\n";
			return;
		}
	}
	
	private String getNumber(String line){
		
		line = line.replaceAll("    \"id\" : \"", "");
		line = line.replaceAll("\",", "");
		line = line.replaceAll("I0", "I 0");
		
		if(line.length() == 7){
			return line + "  ";
		}
		
		return line;
	}
	
	private String getStatus(String line){
		line = line.replaceAll("    \"status\" : \"", "");
		line = line.replaceAll("\",", "");
		if(line.contains("Complete")){
			return "Completed";
		}
		return line;
	}
	
	private String cleanUp(String text){
		Scanner s = new Scanner(text);
		String result = "";
		while(s.hasNext()){
			String line = s.nextLine();
			if(line.contains("I 0")){
				result += line + "\n";
			}
		}
		s.close();
		return result;
	}
}
