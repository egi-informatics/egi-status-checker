package statuschecker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class View {

	JFrame frame;
	JPanel rpPanel;
	JPanel jsPanel;
	JPanel stPanel;
	JTextPane rpText;
	JTextPane jsText;
	JTextPane stText;
	
	static final int innerPadding = 20;
	static final int TOP = 0;
	static final int BOTTOM = 0;

	public View(){
		frame = new JFrame("Project Status Checker");
		frame.setSize(1200, 600);
		frame.setMinimumSize(frame.getSize());
		frame.setLayout(new GridLayout(1, 3));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Sets location to center of screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		
		setupRP();
		setupJSON();
		setupST();
		
		frame.setVisible(true);
	}
	
	public void setupRP(){
		rpPanel = new JPanel();
		rpPanel.setLayout(new BorderLayout());
		rpPanel.setBorder(BorderFactory.createEmptyBorder(TOP, 0, BOTTOM, 0)); // no inner padding
		
		rpText = new JTextPane();
		rpText.setEditable(false);
		//rpText.setText("Research Portfolio");
		
		JButton rpButton = new JButton("Load Research Portfolio");
		
		rpPanel.add(rpButton, BorderLayout.NORTH);
		rpPanel.add(rpText, BorderLayout.CENTER);
		
		
		frame.add(rpPanel);
	}
	
	public void setupJSON(){
		jsPanel = new JPanel();
		jsPanel.setLayout(new BorderLayout());
		jsPanel.setBorder(BorderFactory.createEmptyBorder(TOP, innerPadding, BOTTOM, innerPadding));
		
		jsText = new JTextPane();
		jsText.setEditable(false);
		//jsText.setText("JSON Map");
		
		JButton jsButton = new JButton("Load Map JSON");
		
		jsPanel.add(jsButton, BorderLayout.NORTH);
		jsPanel.add(jsText, BorderLayout.CENTER);
		
		frame.add(jsPanel);
	}
	
	public void setupST(){
		stPanel = new JPanel();
		stPanel.setLayout(new BorderLayout());
		stPanel.setBorder(BorderFactory.createEmptyBorder(TOP, 0, BOTTOM, 0)); // no inner padding
		
		stText = new JTextPane();
		stText.setEditable(false);
		//stText.setText("Static Project Text");
		
		JButton stButton = new JButton("Load Static Project Text");
		
		stPanel.add(stButton, BorderLayout.NORTH);
		stPanel.add(stText, BorderLayout.CENTER);
		
		frame.add(stPanel);
	}
}
