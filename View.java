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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class View implements ActionListener{

	double version = 0.2;
	JFrame frame;
	
	//Panels
	private JPanel top;
	private JPanel bottom;
	
	private JPanel rpPanel;
	private JPanel jsPanel;
	private JPanel stPanel;
	
	//Text
	JTextPane rpTextPane;
	JTextPane jsTextPane;
	JTextPane stTextPane;
	
	//Buttons
	private JButton rpButton;
	private JButton jsButton;
	private JButton stButton;
	
	ResearchPortfolio rp;
	MapJS js;
	
	static final String rpURL = "http://egi.utah.edu/downloads/research_portfolio/EGI_Research_Portfolio.pdf";
	static final String jsURL = "http://egi.utah.edu/api/research.json";
	
	//Frame size
	static final int WIDTH = 800;
	static final int HEIGHT = 800;
	
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
		bottom.setLayout(new BorderLayout());
		JLabel versionText = new JLabel(version + "");
		bottom.setBorder(BorderFactory.createEmptyBorder(0, SIDE, SIDE, SIDE));
		bottom.add(versionText, BorderLayout.EAST);
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
		
		rpButton = new JButton("Load Research Portfolio");
		rpTextPane = new JTextPane();
		configureButton(rpButton, rpPanel);
		configureText(rpTextPane, rpPanel);
		
		top.add(rpPanel);
	}
	
	public void setupJSON(){
		jsPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		configureLayout(layout);
		
		jsPanel.setLayout(layout);
		jsPanel.setBorder(BorderFactory.createEmptyBorder(TOP, 0, BOTTOM, 0)); // no inner padding
		
		jsTextPane = new JTextPane();
		jsButton = new JButton("Load Map JSON");
		configureText(jsTextPane, jsPanel);
		configureButton(jsButton, jsPanel);
		
		top.add(jsPanel);
	}
	
	public void setupST(){
		stPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		configureLayout(layout);
		
		stPanel.setLayout(layout);
		stPanel.setBorder(BorderFactory.createEmptyBorder(TOP, SIDE, BOTTOM, SIDE));
		
		stTextPane = new JTextPane();
		stButton = new JButton("Load Static Project Text");
		configureText(stTextPane, stPanel);
		configureButton(stButton, stPanel);
		
		top.add(stPanel);
	}
	
	private void configureLayout(BorderLayout layout) {
		layout.setVgap(10);
	}

	private void configureText(JTextPane pane, JPanel panel) {
		//pane.setEditable(false);
		//pane.setContentType("text/html");
		pane.setBorder(BorderFactory.createEmptyBorder(TEXT_PADDING, TEXT_PADDING, TEXT_PADDING, TEXT_PADDING));
		pane.setFont(Font.getFont(FONT));
		
		JScrollPane scroll = new JScrollPane(pane);
		panel.add(scroll, BorderLayout.CENTER);
	}
	
	private void configureButton(JButton button, JPanel panel) {
		button.setFocusable(false);
		button.addActionListener(this);
		
		panel.add(button, BorderLayout.NORTH);
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
		
		if(rpText.isEmpty() || !rpText.contains("I 0")){
			return;
		}

		// Shows what needs to be added to Map JSON
		append(jsTextPane, "\n\nAdd:\n");
		Scanner rps = new Scanner(rpText);

		while (rps.hasNextLine()) {
			String line = rps.nextLine();
			String num = getNum(line);
			boolean hasNum = false;

			Scanner jps = new Scanner(jsText);

			while (jps.hasNextLine()) {
				String jsLine = jps.nextLine();
				if (jsLine.contains(num)) {
					hasNum = true;
					break;
				}
			}
			if (!hasNum) {
				append(jsTextPane, line + "\n");
			}
			jps.close();
		}
		rps.close();

		// Shows what needs to be removed
		append(jsTextPane, "\nRemove:\n");
		Scanner jps = new Scanner(jsText);

		while (jps.hasNextLine()) {
			String line = jps.nextLine();
			String num = getNum(line);
			boolean hasNum = false;

			rps = new Scanner(rpText);

			while (rps.hasNextLine()) {
				String rpLine = rps.nextLine();
				if (rpLine.contains(num)) {
					hasNum = true;
					break;
				}
			}
			if (!hasNum) {
				append(jsTextPane, line + "\n");
			}
			rps.close();
		}
		jps.close();

		// Shows what needs to be modified in the Map JSON
		append(jsTextPane, "\nModify:\n");
		rps = new Scanner(rpText);

		while (rps.hasNextLine()) {
			String rpLine = rps.nextLine();
			String num = getNum(rpLine);
			boolean hasNum = false;
			boolean statusMatch = false;

			jps = new Scanner(jsText);

			while (jps.hasNextLine()) {
				String jsLine = jps.nextLine();
				if (jsLine.contains(num)) {
					hasNum = true;
					if(rpLine.equals(jsLine)){
						statusMatch = true;
					}
					break;
				}
			}
			if (hasNum && !statusMatch) {
				append(jsTextPane, rpLine + "\n");
			}
			jps.close();
		}
		rps.close();
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
