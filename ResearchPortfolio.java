package statuschecker;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static javax.swing.JOptionPane.showMessageDialog;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ResearchPortfolio extends Source{
	
	public ResearchPortfolio(String stringURL) {
		super(stringURL);
	}

	@Override
	public void load(){
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
			int start = 3;
			int end = doc.getNumberOfPages();
			
			String text = "";
			
			for(int i = start; i < end; i++){
				stripper.setStartPage(i);
				stripper.setEndPage(i);
				String pageText = stripper.getText(doc);
				String line = getNumber(pageText) + "  " + getStatus(pageText);
				if(line.length() > 3){
					text += line + "\n";
				}
			}
			
			output = sort(text);
			stream.close();
			doc.close();
			
//			stripper.setStartPage(11);
//			stripper.setEndPage(11); //TODO bug on 11 doesn't find correct title. Finds first. Maybe should find last.
//			System.out.println(stripper.getText(doc));
			
		} catch (IOException e) {
			e.printStackTrace();
			
			String message = "Problem with file\n";
			showMessageDialog(null, message + e.getMessage());
		}
	}
	
	/**
	 * Returns the project I # from the input page text
	 */
	private static String getNumber(String pageText){
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
	private static String getStatus(String pageText){
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
}
