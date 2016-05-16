package statuschecker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static javax.swing.JOptionPane.showMessageDialog;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ResearchPortfolio extends Source{
	
	private String timestamp = "";
	
	public ResearchPortfolio(String stringURL) {
		super(stringURL);
	}

	@Override
	public void load(){
		this.clear();
		
		URL url = null;
		try {
			url = new URL(stringURL);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		try {
			InputStream stream = url.openStream();
			PDDocument doc = PDDocument.load(stream);

			PDFTextStripper stripper = new PDFTextStripper();
			loadTimestamp(stripper, doc);
			int start = 3;
			int end = doc.getNumberOfPages();
			
			String text = "";
			String pageText = "";
			
			for(int i = start; i <= end; i++){
				stripper.setStartPage(i);
				stripper.setEndPage(i);
				pageText = stripper.getText(doc);
				String line = getNumber(pageText) + "  " + getStatus(pageText);
				if(line.length() > 3){
					text += line + "\n";
				}
			}
			
//			stripper.setStartPage(11);
//			stripper.setEndPage(11); //TODO bug on 11 doesn't find correct title. Finds first. Maybe should find last.
//			System.out.println(pageText);
			
			output = sort(text);
			//addTimeStamp();
			stream.close();
			doc.close();
			
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
	
	/**
	 * Returns the project I # from the input page text
	 */
	private String getNumber(String pageText){
		Scanner sc = new Scanner(pageText);
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			if(line.startsWith("I 0")){
				sc.close();
				line = line.trim();
				if(line.length() == 7){
					return line + "  ";
				}
				return line;
			}
		}
		sc.close();
		return "";
	}
	
	/**
	 * Returns the project status from the input page text
	 */
	private String getStatus(String pageText){
		Scanner sc = new Scanner(pageText);
		
		String dev = "In Development";
		String prog = "In Progress";
		String comp = "Completed";
		
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			if(line.contains(dev)){
				sc.close();
				return dev;
			}
			if(line.contains(prog)){
				sc.close();
				return prog;
			}
			if(line.contains(comp)){
				sc.close();
				return comp;
			}
		}
		sc.close();
		return "";
	}
	
	public String getTimestamp(){
		return timestamp;
	}
	
	private void loadTimestamp(PDFTextStripper stripper, PDDocument doc) throws IOException{
		int page = 1;
		stripper.setStartPage(page);
		stripper.setEndPage(page);
		
		String pageText = stripper.getText(doc);
		Scanner s = new Scanner(pageText);
		
		while(s.hasNextLine()){
			String line = s.nextLine();
			
			if(line.contains(":") && (line.contains("AM") || line.contains("PM"))){
				timestamp = line;
				break;
			}
		}
		s.close();
	}
}
