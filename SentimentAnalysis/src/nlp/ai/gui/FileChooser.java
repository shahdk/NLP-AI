package nlp.ai.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nlp.ai.util.OSDetector;

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
		setSize(450, 250);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.initComponents();
		this.setLayout(new BorderLayout());
		this.add(this.inputFileLabel, BorderLayout.NORTH);
		this.add(this.inputPanel, BorderLayout.CENTER);
		this.add(this.runButton, BorderLayout.SOUTH);
	}

	public void initComponents() {
		this.inputFileLabel = new JLabel("Select Corpus Folder:");
		this.inputFileLabel.setPreferredSize(new Dimension(450, 30));
		this.inputPanel = new JPanel();
		this.inputPanel.setLayout(new BorderLayout());
		this.inputPanel.setPreferredSize(new Dimension(450, 30));
		if (OSDetector.isWindows())
			this.inputTextField = new JTextField("docs\\PositiveReviews");
		else
			this.inputTextField = new JTextField("docs/PositiveReviews");
		this.inputTextField.setPreferredSize(new Dimension(350, 30));

		this.inputButton = new JButton("Browse..");
		this.runButton = new JButton("Hit It!");
		this.inputButton.setPreferredSize(new Dimension(100, 30));
		this.runButton.setPreferredSize(new Dimension(450, 140));
		
		this.inputButton.addActionListener(this);
		this.runButton.addActionListener(this);

		this.inputPanel.add(this.inputTextField, BorderLayout.CENTER);
		this.inputPanel.add(this.inputButton, BorderLayout.EAST);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.runButton)) {
			this.runButton.setText("");
			if (OSDetector.isWindows())
				this.runButton.setIcon(new ImageIcon("assets\\loading.gif"));
			else
				this.runButton.setIcon(new ImageIcon("assets/loading.gif"));

			Thread t = new Thread() {
				public void run() {
					synchronized (this) {
						SentimentDisplay results = new SentimentDisplay(
								inputTextField.getText());
						results.setVisible(true);
						dispose();
					}
				}
			};
			t.start();
		} else {
			chooseFile();
		}

	}

	private void chooseFile() {
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
