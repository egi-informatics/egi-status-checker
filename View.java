package statuschecker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class View implements ActionListener{

	JFrame frame;
	
	//Panels
	JPanel rpPanel;
	JPanel jsPanel;
	JPanel stPanel;
	
	//Text
	JTextPane rpText;
	JTextPane jsText;
	JTextPane stText;
	
	//Buttons
	JButton rpButton;
	JButton jsButton;
	JButton stButton;
	
	ResearchPortfolio rp;
	
	static final String rpURL = "http://egi.utah.edu/downloads/research_portfolio/EGI_Research_Portfolio.pdf";
	
	//Frame size
	static final int WIDTH = 900;
	static final int HEIGHT = 640;
	
	static final int TOP = 10;
	static final int BOTTOM = 5;
	static final int SIDE = 5;
	
	static final int TEXT_PADDING = 10;
	static final String FONT = "Courier";

	public View(){
		frame = new JFrame("Project Status Checker");
		frame.setSize(WIDTH, HEIGHT);
		frame.setMinimumSize(frame.getSize());
		frame.setLayout(new GridLayout(1, 3));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Sets location to center of screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		
		setupRP();
		setupJSON();
		setupST();
		
		rp = new ResearchPortfolio(rpURL);
		
		frame.setVisible(true);
	}
	
	public void setupRP(){
		rpPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		configureLayout(layout);
		
		rpPanel.setLayout(layout);
		rpPanel.setBorder(BorderFactory.createEmptyBorder(TOP, SIDE, BOTTOM, SIDE));
		
		rpText = new JTextPane();
		configureText(rpText);
		rpText.setText("Research Portfolio");
		
		rpButton = new JButton("Load Research Portfolio");
		configureButton(rpButton);
		rpButton.addActionListener(this);
		
		rpPanel.add(rpButton, BorderLayout.NORTH);
		rpPanel.add(rpText, BorderLayout.CENTER);
		
		
		frame.add(rpPanel);
	}
	
	public void setupJSON(){
		jsPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		configureLayout(layout);
		
		jsPanel.setLayout(layout);
		jsPanel.setBorder(BorderFactory.createEmptyBorder(TOP, 0, BOTTOM, 0)); // no inner padding
		
		jsText = new JTextPane();
		configureText(jsText);
		jsText.setText("JSON Map");
		
		jsButton = new JButton("Load Map JSON");
		configureButton(jsButton);
		
		jsPanel.add(jsButton, BorderLayout.NORTH);
		jsPanel.add(jsText, BorderLayout.CENTER);
		
		frame.add(jsPanel);
	}
	
	public void setupST(){
		stPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		configureLayout(layout);
		
		stPanel.setLayout(layout);
		stPanel.setBorder(BorderFactory.createEmptyBorder(TOP, SIDE, BOTTOM, SIDE));
		
		stText = new JTextPane();
		configureText(stText);
		stText.setText("Static Project Text");
		
		stButton = new JButton("Load Static Project Text");
		configureButton(stButton);
		
		stPanel.add(stButton, BorderLayout.NORTH);
		stPanel.add(stText, BorderLayout.CENTER);
		
		frame.add(stPanel);
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
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(rpButton)){
			rp.load();
			rpText.setText(rp.getText());
		}
	}
}
