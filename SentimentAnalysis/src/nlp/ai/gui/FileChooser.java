package nlp.ai.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import nlp.ai.util.OSDetector;

public class FileChooser extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JLabel inputFileLabel;
	private JProgressBar progressBar;
	private JTextField inputTextField;

	private JPanel inputPanel;
	private JPanel runPanel;

	private JButton inputButton;
	private JButton runButton;

	public FileChooser() {
		super("Corpus Selector");
		this.initComponents();
		this.initFrame();
	}

	private void initFrame() {
		setSize(450, 180);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.add(this.inputFileLabel, BorderLayout.NORTH);
		this.add(this.inputPanel, BorderLayout.CENTER);
		this.add(this.runPanel, BorderLayout.SOUTH);
	}

	public void initComponents() {
		initProgressBar();

		initInputLabel();

		initInputTextField();

		initInputButton();

		initRunButton();

		initInputPanel();

		initRunPanel();
	}

	private void initRunButton() {
		this.runButton = new JButton("Hit It!");
		this.runButton.setPreferredSize(new Dimension(450, 50));
		this.runButton.addActionListener(this);
	}

	private void initInputButton() {
		this.inputButton = new JButton("Browse..");
		this.inputButton.setPreferredSize(new Dimension(100, 30));
		this.inputButton.addActionListener(this);
	}

	private void initInputTextField() {
		if (OSDetector.isWindows())
			this.inputTextField = new JTextField("docs\\PositiveReviews");
		else
			this.inputTextField = new JTextField("docs/PositiveReviews");
		this.inputTextField.setPreferredSize(new Dimension(350, 30));
	}

	private void initRunPanel() {
		this.runPanel = new JPanel();
		this.runPanel.setLayout(new GridLayout(2, 1));
		this.runPanel.setPreferredSize(new Dimension(450, 75));
		this.runPanel.add(this.runButton);
		this.runPanel.add(this.progressBar);
	}

	private void initInputPanel() {
		this.inputPanel = new JPanel();
		this.inputPanel.setLayout(new BorderLayout());
		this.inputPanel.setPreferredSize(new Dimension(450, 30));
		this.inputPanel.add(this.inputTextField, BorderLayout.CENTER);
		this.inputPanel.add(this.inputButton, BorderLayout.EAST);
	}

	private void initInputLabel() {
		this.inputFileLabel = new JLabel("Select Corpus Folder:");
		this.inputFileLabel.setPreferredSize(new Dimension(450, 30));
	}

	private void initProgressBar() {
		this.progressBar = new JProgressBar();
		this.progressBar.setMinimum(0);
		this.progressBar.setMaximum(100);
		this.progressBar.setPreferredSize(new Dimension(450, 30));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.runButton)) {
			this.progressBar.setIndeterminate(true);
			displaySentimentResult();
		} else {
			chooseFile();
		}

	}

	private void displaySentimentResult() {
		Thread t = new Thread() {
			public void run() {

				SentimentDisplay results = new SentimentDisplay(
						inputTextField.getText(), progressBar);
				results.setVisible(true);
				dispose();

			}
		};
		t.start();
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
