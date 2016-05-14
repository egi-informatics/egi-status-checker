package statuschecker;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MapStatic extends Source{

	public MapStatic(String stringURL) {
		super(stringURL);
	}

	@Override
	public void load() {
		this.clear();
		
		URL url = null;
		try {
			url = new URL(stringURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		InputStream stream = null;
		try {
			stream = url.openStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Scanner s = new Scanner(stream);
		while(s.hasNextLine()){
			String line = s.nextLine();
			if(line.contains(">Project Page</a>") && !line.contains("Q")){
				String num = line.split("/")[3];
				num = num.replaceAll("i", "I");
				num = num.replaceAll("I", "I ");
				
				this.output += num + "\n";
			}
		}
		output = sort(output);
		s.close();
	}

}
