package statuschecker;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ResearchPortfolio {

	public ResearchPortfolio(String _url){
		URL url = null;
		try {
			url = new URL(_url);
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
				String tempText = stripper.getText(doc);
				String line = getNumber(tempText) + "  " + getStatus(tempText);
				if(line.length() > 3){
					text += line + "\n";
				}
			}
			
			System.out.println(sort(text));
			
//			stripper.setStartPage(11);
//			stripper.setEndPage(11); //TODO bug on 11 doesn't find correct title. Finds first. Maybe should find last.
//			System.out.println(stripper.getText(doc));
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getNumber(String text){
		Scanner sc = new Scanner(text);
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
	
	private static String getTitle(String text){
		Scanner sc = new Scanner(text);
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			if(Character.isDigit(line.charAt(0)) && line.length() > 4){
				sc.close();
				return line.substring(2).trim();
			}
		}
		sc.close();
		return null;
	}
	
	private static String getStatus(String text){
		Scanner sc = new Scanner(text);
		
		String dev = "In Development";
		String prog = "In Progress";
		String comp = "Completed";
		
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			if(line.contains(dev)){
				return dev;
			}
			if(line.contains(prog)){
				return prog;
			}
			if(line.contains(comp)){
				return comp;
			}
		}
		sc.close();
		return "";
	}
	
	private static String onlyTop(String text){
		Scanner sc = new Scanner(text);
		String result = "";
		
		while(sc.hasNextLine()){
			String line = sc.nextLine();
//			if(line.contains("Timetable:"))
			if(line.contains("Timetable:")){
				break;
			}
			result += line + "\n";
		}
		return result;
	}

	private static String onlyProjects(String text) {
		Scanner sc = new Scanner(text);
		String result = "";
		
		while(sc.hasNext()){
			String line = sc.nextLine();
			if(line.length() > 10 && hasNumbers(line) && hasLetters(line) && notTimeStamp(line)){
				result += line + "\n";
			}
		}
		sc.close();
		return result;
	}
	
	private static String cleanUp(String text) {
		Scanner sc = new Scanner(text);
		String result = "";
		
		while(sc.hasNext()){
			String line = sc.nextLine();
			line = line.replaceAll("\\((.*?)\\)", ""); // Removes anything inside ( )
			result += line.substring(2, line.length()).trim() + "\n";
		}
		sc.close();
		return result.trim();
	}
	
	private static String sort(String text){
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
	
	private static boolean hasNumbers(String line){
		for(int i = 0; i < line.length(); i++){
			char ch = line.charAt(i);
			if(Character.isDigit(ch)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean hasLetters(String line){
		for(int i = 0; i < line.length(); i++){
			char ch = line.charAt(i);
			if(Character.isLetter(ch)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean notTimeStamp(String line){
		return !line.contains(":");
	}
}
