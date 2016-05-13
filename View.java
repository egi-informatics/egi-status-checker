package statuschecker;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class View implements ActionListener{

	JFrame frame;
	
	//Panels
	JPanel top;
	JPanel bottom;
	
	JPanel rpPanel;
	JPanel jsPanel;
	JPanel stPanel;
	
	//Text
	JTextPane rpTextPane;
	JTextPane jsTextPane;
	JTextPane stText;
	
	//Buttons
	JButton rpButton;
	JButton jsButton;
	JButton stButton;
	JButton compareButton;
	
	ResearchPortfolio rp;
	MapJS js;
	
	static final String rpURL = "http://egi.utah.edu/downloads/research_portfolio/EGI_Research_Portfolio.pdf";
	static final String jsURL = "http://egi.utah.edu/api/research.json";
	
	//Frame size
	static final int WIDTH = 900;
	static final int HEIGHT = 900;
	
	static final int TOP = 10;
	static final int BOTTOM = 5;
	static final int SIDE = 5;
	
	static final int TEXT_PADDING = 10;
	static final String FONT = "Courier";

	public View(){
		frame = new JFrame("Project Status Checker");
		frame.setSize(WIDTH, HEIGHT);
		frame.setMinimumSize(frame.getSize());
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Sets location to center of screen
		frame.setLocationRelativeTo(null);
		
		top = new JPanel();
		setupTop();
		
		bottom = new JPanel();
		setupBottom();
		
		
		rp = new ResearchPortfolio(rpURL);
		js = new MapJS(jsURL);
		
		frame.setVisible(true);
	}
	
	private void setupBottom() {
		frame.add(bottom, BorderLayout.SOUTH);
	}

	private void setupTop() {
		top.setLayout(new GridLayout(1, 3));
		setupRP();
		setupJSON();
		setupST();
		frame.add(top, BorderLayout.CENTER);
	}

	public void setupRP(){
		rpPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		configureLayout(layout);
		
		rpPanel.setLayout(layout);
		rpPanel.setBorder(BorderFactory.createEmptyBorder(TOP, SIDE, BOTTOM, SIDE));
		
		rpTextPane = new JTextPane();
		configureText(rpTextPane);
		//rpText.setText("Research Portfolio");
		
		rpButton = new JButton("Load Research Portfolio");
		configureButton(rpButton);
		
		rpPanel.add(rpButton, BorderLayout.NORTH);
		rpPanel.add(rpTextPane, BorderLayout.CENTER);
		
		top.add(rpPanel);
	}
	
	public void setupJSON(){
		jsPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		configureLayout(layout);
		
		jsPanel.setLayout(layout);
		jsPanel.setBorder(BorderFactory.createEmptyBorder(TOP, 0, BOTTOM, 0)); // no inner padding
		
		jsTextPane = new JTextPane();
		configureText(jsTextPane);
		//jsText.setText("JSON Map");
		
		jsButton = new JButton("Load Map JSON");
		configureButton(jsButton);
		
		jsPanel.add(jsButton, BorderLayout.NORTH);
		jsPanel.add(jsTextPane, BorderLayout.CENTER);
		
		top.add(jsPanel);
	}
	
	public void setupST(){
		stPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		configureLayout(layout);
		
		stPanel.setLayout(layout);
		stPanel.setBorder(BorderFactory.createEmptyBorder(TOP, SIDE, BOTTOM, SIDE));
		
		stText = new JTextPane();
		configureText(stText);
		//stText.setText("Static Project Text");
		
		stButton = new JButton("Load Static Project Text");
		configureButton(stButton);
		
		stPanel.add(stButton, BorderLayout.NORTH);
		stPanel.add(stText, BorderLayout.CENTER);
		
		top.add(stPanel);
	}
	
	private void configureLayout(BorderLayout layout) {
		layout.setVgap(10);
	}

	private void configureText(JTextPane pane) {
		pane.setEditable(false);
		//pane.setContentType("text/html");
		pane.setBorder(BorderFactory.createEmptyBorder(TEXT_PADDING, TEXT_PADDING, TEXT_PADDING, TEXT_PADDING));
		pane.setFont(Font.getFont(FONT));
	}
	
	private void configureButton(JButton button) {
		button.setFocusable(false);
		button.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(rpButton)){
			rp.load();
			rpTextPane.setText(rp.getText());
			return;
		}
		
		if(e.getSource().equals(jsButton)){
			js.load();
			jsTextPane.setText(js.getText());
			compare(rpTextPane, jsTextPane);
			return;
		}
		
	}

	private void compare(JTextPane rpTextPane, JTextPane jsTextPane) {
		String rpText = rpTextPane.getText();
		String jsText = jsTextPane.getText();
		
		if(rpText.isEmpty()){
			return;
		}
		
		append(jsTextPane, "\n\nAdd:\n");
		
		Scanner rps = new Scanner(rpText);
		
		while(rps.hasNextLine()){
			String line = rps.nextLine();
			String num = getNum(line);
			boolean hasNum = false;
			
			Scanner jps = new Scanner(jsText);
			
			while(jps.hasNextLine()){
				String jsLine = jps.nextLine();
				if(jsLine.contains(num)){
					hasNum = true;
					break;
				}
			}
			if(!hasNum){
				append(jsTextPane, line + "\n");
			}
		}
	}
	
	private void append(JTextPane pane, String text){
		Document doc = pane.getDocument();
		try {
			doc.insertString(doc.getLength(), text, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private String getNum(String line){
		return line.split(" ")[1];
	}
}
