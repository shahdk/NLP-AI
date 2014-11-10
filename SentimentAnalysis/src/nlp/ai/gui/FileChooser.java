package nlp.ai.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FileChooser extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel inputFileLabel;

	private JTextField inputTextField;

	private JPanel inputPanel;

	private JButton inputButton;
	private JButton runButton;

	public FileChooser() {
		super("Corpus Selector");
		setSize(450, 200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.initComponents();
		this.setLayout(new GridLayout(3, 1));
		this.add(this.inputFileLabel, 0);
		this.add(this.inputPanel, 1);
		this.add(this.runButton, 2);
	}

	public void initComponents() {
		this.inputFileLabel = new JLabel("Select Corpus Folder:");

		this.inputPanel = new JPanel();
		this.inputPanel.setLayout(new BorderLayout());

		this.inputTextField = new JTextField("");
		this.inputTextField.setSize(20, 130);

		this.inputButton = new JButton("Browse..");
		this.runButton = new JButton("Hit it!");
		this.inputButton.setSize(20, 50);
		this.inputButton.addActionListener(this);
		this.runButton.addActionListener(this);

		this.inputPanel.add(this.inputTextField, BorderLayout.CENTER);
		this.inputPanel.add(this.inputButton, BorderLayout.EAST);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.runButton)){
			
			SentimentDisplay results = new SentimentDisplay(this.inputTextField.getText());
			results.setVisible(true);
		}
		else{
			JFileChooser chooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			chooser.setCurrentDirectory(workingDirectory);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int option = chooser.showOpenDialog(FileChooser.this);
			if (option == JFileChooser.APPROVE_OPTION) {
				this.inputTextField
						.setText((chooser.getSelectedFile() != null) ? chooser
								.getSelectedFile().getPath() : "nothing");
			} else {
				this.inputTextField.setText("");
			}
		}
		
	}

}
