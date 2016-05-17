package statuschecker;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProjectPages extends Source {
	
	public static final int TIMEOUT = 14_000;
	private String html = "";

	public ProjectPages(String stringURL) {// https://egi.utah.edu/all/
		super(stringURL);
	}

	@Override
	public void load() {
		String projects = "";

		Document doc;
		try {
			URL url = new URL(this.stringURL);
			InputStream stream = url.openStream();
			Scanner s = new Scanner(stream);
			
			while(s.hasNextLine()){
				html += s.nextLine() + "\n";
			}
			
			doc = Jsoup.parse(html);
			Elements links = doc.select("#projects a");
			
			for (int i = 0; i < links.size(); i++){
				loadPage(links.get(i).attr("href"));
			}
			
			s.close();
			// System.out.println(projects);

		} catch (IOException e) {
			if(e instanceof SocketTimeoutException){
				output = "Timed out";
			}
			e.printStackTrace();
		}
	}

	private void loadPage(String link){
		String[] temp = link.split("/");
		String num = temp[temp.length - 1].toUpperCase();
		
		try {
			Document doc = Jsoup.parse(new URL("http://egi.utah.edu/research/current-projects/" + num + "?mode=quick"), TIMEOUT);
			Elements projectName = doc.select("h1 > span + span");
			if(num.length() < 7){
				num += "  ";
			}
			String text = num + "  " + projectName.text() + "\n";
			output += text;
			System.out.println(text.trim());
		} catch (IOException e) {
			if(e instanceof SocketTimeoutException){
				String text = num + "  " + "Timed out" + "\n";
				output += text;
				System.out.println(text.trim());
				return;
			}
			e.printStackTrace();
		}
	}
	
	private String filter(String text){
		if(text.equals("Timed out")){
			return text;
		}
		
		Scanner s = new Scanner(text);
		String filtered = "";
		
		while(s.hasNextLine()){
			String line = s.nextLine();
			if(line.contains("Q") && line.contains("Book")){
				continue;
			}
			line = line.replaceFirst("I", "I ");
			line = line.replace((char)160, (char)32);
			filtered += line + "\n";
		}
		s.close();
		return filtered;
	}
	
	@Override
	public String getText(){
		output = filter(output);
		return super.getText();
	}
}
