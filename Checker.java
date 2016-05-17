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

public class Checker implements ActionListener{
	
	public static void main(String[] args){
		new Checker();
	}

	double version = 0.3;
	JFrame frame;
	
	//Panels
	private JPanel top;
	private JPanel bottom;
	
	private JPanel rpPanel;
	private JPanel jsPanel;
	private JPanel stPanel;
	private JPanel ppPanel;
	
	//Text
	JTextPane rpTextPane;
	JTextPane jsTextPane;
	JTextPane stTextPane;
	JTextPane ppTextPane;
	
	//Buttons
	private JButton rpButton;
	private JButton jsButton;
	private JButton stButton;
	private JButton ppButton;
	
	JLabel rpTimestamp;
	
	ResearchPortfolio rp;
	MapJS js;
	MapStatic st;
	ProjectPages pp;
	
	static final String rpURL = "http://egi.utah.edu/downloads/research_portfolio/EGI_Research_Portfolio.pdf";
	static final String jsURL = "http://egi.utah.edu/api/research.json";
	static final String stURL = "http://egi.utah.edu/research/current-projects/?mode=quick";
	static final String ppURL = "http://egi.utah.edu/all/?mode=quick";
	
	//Frame size
	static final int WIDTH = 1000;
	static final int HEIGHT = 800;
	
	static final int TOP = 10;
	static final int BOTTOM = 5;
	static final int SIDE = 5;
	
	static final int TEXT_PADDING = 10;
	static final String FONT = "Courier";

	public Checker(){
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
		st = new MapStatic(stURL);
		pp = new ProjectPages(ppURL);
		
		frame.setVisible(true);
	}
	
	private void setupTop() {
		top.setLayout(new GridLayout(1, 3));
		setupRP();
		setupJSON();
		setupST();
		setupPP();
		frame.add(top, BorderLayout.CENTER);
	}
	
	private void setupBottom() {
		bottom.setLayout(new BorderLayout());
		JLabel versionText = new JLabel(version + "");
		rpTimestamp = new JLabel("");
		bottom.setBorder(BorderFactory.createEmptyBorder(0, SIDE, SIDE, SIDE));
		bottom.add(versionText, BorderLayout.EAST);
		bottom.add(rpTimestamp, BorderLayout.WEST);
		frame.add(bottom, BorderLayout.SOUTH);
	}

	public void setupRP(){
		rpPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		configureLayout(layout);
		
		rpPanel.setLayout(layout);
		rpPanel.setBorder(BorderFactory.createEmptyBorder(TOP, SIDE, BOTTOM, 0));
		
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
		jsPanel.setBorder(BorderFactory.createEmptyBorder(TOP, SIDE, BOTTOM, 0)); // no inner padding
		
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
		stPanel.setBorder(BorderFactory.createEmptyBorder(TOP, SIDE, BOTTOM, 0));
		
		stTextPane = new JTextPane();
		stButton = new JButton("Load Static Text Below Map");
		configureText(stTextPane, stPanel);
		configureButton(stButton, stPanel);
		
		top.add(stPanel);
	}
	
	public void setupPP(){
		ppPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		configureLayout(layout);
		
		ppPanel.setLayout(layout);
		ppPanel.setBorder(BorderFactory.createEmptyBorder(TOP, SIDE, BOTTOM, SIDE));
		
		ppTextPane = new JTextPane();
		ppButton = new JButton("Load All Project Pages");
		configureText(ppTextPane, ppPanel);
		configureButton(ppButton, ppPanel);
		
		top.add(ppPanel);
	}
	
	private void configureLayout(BorderLayout layout) {
		layout.setVgap(10);
	}

	private void configureText(JTextPane pane, JPanel panel) {
		pane.setEditable(false);
		//pane.setContentType("text/html");
		pane.setBorder(BorderFactory.createEmptyBorder(TEXT_PADDING, TEXT_PADDING, TEXT_PADDING, TEXT_PADDING));
		pane.setFont(Font.getFont(FONT));
		
		JScrollPane scroll = new JScrollPane(pane);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		panel.add(scroll, BorderLayout.CENTER);
	}
	
	private void configureButton(JButton button, JPanel panel) {
		button.setFocusable(false);
		button.addActionListener(this);
		
		panel.add(button, BorderLayout.NORTH);
	}

	/**
	 * Appends text to the specified text pane
	 */
	private void append(JTextPane pane, String text){
		Document doc = pane.getDocument();
		try {
			doc.insertString(doc.getLength(), text, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the number from the formatted line of text 
	 */
	private String getNum(String line){
		return line.split(" ")[1];
	}
	
	/**
	 * Compares two panes. Outputs the result to the second pane.<br>
	 * Results include projects that need to be added, removed, or modified.
	 */
	private void compare(JTextPane rpTextPane, JTextPane secondPane) {
		String rpText = rpTextPane.getText();
		String secondText = secondPane.getText();
		
		if(rpText.isEmpty() || !rpText.contains("I 0")){
			return;
		}
		
		if(secondText.isEmpty() || !rpText.contains("I 0")){
			return;
		}
		
		if(secondText.equals("Timed out")){
			return;
		}

		// Shows what needs to be added to Map JSON
		append(secondPane, "\n\nAdd:\n");
		Scanner rps = new Scanner(rpText);

		while (rps.hasNextLine()) {
			String line = rps.nextLine();
			String num = getNum(line);
			boolean hasNum = false;

			Scanner ss = new Scanner(secondText);

			while (ss.hasNextLine()) {
				String jsLine = ss.nextLine();
				if (jsLine.contains(num)) {
					hasNum = true;
					break;
				}
			}
			if (!hasNum) {
				append(secondPane, line + "\n");
			}
			ss.close();
		}
		rps.close();

		// Shows what needs to be removed
		append(secondPane, "\nRemove:\n");
		Scanner ss = new Scanner(secondText);

		while (ss.hasNextLine()) {
			String line = ss.nextLine();
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
				append(secondPane, line + "\n");
			}
			rps.close();
		}
		ss.close();

		// Shows what needs to be modified in the Map JSON
		append(secondPane, "\nModify:\n");
		rps = new Scanner(rpText);

		while (rps.hasNextLine()) {
			String rpLine = rps.nextLine();
			String num = getNum(rpLine);
			boolean hasNum = false;
			boolean statusMatch = false;

			ss = new Scanner(secondText);

			while (ss.hasNextLine()) {
				String jsLine = ss.nextLine();
				if (jsLine.contains(num)) {
					hasNum = true;
					if(rpLine.equals(jsLine)){
						statusMatch = true;
					}
					break;
				}
			}
			if (hasNum && !statusMatch) {
				append(secondPane, rpLine + "\n");
			}
			ss.close();
		}
		rps.close();
	}
	
	public void compareOnlyNumber(JTextPane rpTextPane, JTextPane stTextPane){
		String rpText = rpTextPane.getText();
		String stText = stTextPane.getText();

		if (rpText.isEmpty() || !rpText.contains("I 0")) {
			return;
		}

		// Shows what needs to be added to Map JSON
		append(stTextPane, "\n\nAdd:\n");
		Scanner rps = new Scanner(rpText);

		while (rps.hasNextLine()) {
			String line = rps.nextLine();
			String num = getNum(line);
			boolean hasNum = false;

			Scanner sts = new Scanner(stText);

			while (sts.hasNextLine()) {
				String sLine = sts.nextLine();
				if (sLine.contains(num)) {
					hasNum = true;
					break;
				}
			}
			if (!hasNum) {
				append(stTextPane, "I " + num + "\n");
			}
			sts.close();
		}
		rps.close();

		// Shows what needs to be removed
		append(stTextPane, "\nRemove:\n");
		Scanner sts = new Scanner(stText);

		while (sts.hasNextLine()) {
			String line = sts.nextLine();
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
				append(stTextPane, line + "\n");
			}
			rps.close();
		}
		sts.close();
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(rpButton)){
			rp.load();
			rpTextPane.setText(rp.getText());
			String timestamp = rp.getTimestamp();
			rpTimestamp.setText(timestamp);
			return;
		}
		
		if(e.getSource().equals(jsButton)){
			js.load();
			jsTextPane.setText(js.getText());
			compare(rpTextPane, jsTextPane);
			return;
		}
		
		if(e.getSource().equals(stButton)){
			st.load();
			stTextPane.setText(st.getText());
			compareOnlyNumber(rpTextPane, stTextPane);
			return;
		}
		
		if(e.getSource().equals(ppButton)){
			pp.load();
			ppTextPane.setText(pp.getText());
			compare(rpTextPane, ppTextPane);
			return;
		}
	}
}
