package nlp.ai.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import nlp.ai.util.DocumentParser;
import nlp.ai.util.NLPSentence;

public class SentimentDisplay extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private String corpusDir;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem newAnalysisItem;
	private JMenuItem quitItem;
	private JLabel comboLabel;
	private JComboBox<String> fileCombo;
	private Map<String, String> sentimentColorMap;
	private JPanel comboPanel;
	private JScrollPane resultPane;
	private JPanel resultPanel;
	private Map<String, ArrayList<NLPSentence>> docNLPMap;

	public SentimentDisplay(String corpusDirectory, JProgressBar progressBar) {
		super("NLP Results");
		setSize(950, 600);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.sentimentColorMap = new HashMap<>();
		this.loadSentimentColorMap();
		this.corpusDir = corpusDirectory;

		this.initComponents(progressBar);

		this.setLayout(new BorderLayout());
		this.add(this.comboPanel, BorderLayout.NORTH);
		this.add(this.resultPane, BorderLayout.CENTER);
		this.setJMenuBar(this.menuBar);
		setLocationRelativeTo(null);
	}

	public void loadSentimentColorMap() {
		String[] sentiments = new String[] { "very positive", "positive",
				"very negative", "negative", "neutral" };
		String[] sentimentColors = new String[] { "green", "green", "red",
				"red", "black" };

		for (int i = 0; i < sentiments.length; i++) {
			this.sentimentColorMap.put(sentiments[i], sentimentColors[i]);
		}
	}

	public void initComponents(JProgressBar progressBar) {
		this.comboLabel = new JLabel("Select Document: ");
		this.resultPanel = new JPanel();
		this.resultPane = new JScrollPane(this.resultPanel);
		this.comboPanel = new JPanel();
		this.fileCombo = new JComboBox<String>();

		this.menuBar = new JMenuBar();
		this.fileMenu = new JMenu("File");

		this.newAnalysisItem = new JMenuItem("New Analysis");
		this.newAnalysisItem.addActionListener(this);
		this.quitItem = new JMenuItem("Quit");
		this.quitItem.addActionListener(this);

		this.menuBar.add(this.fileMenu);
		this.fileMenu.add(this.newAnalysisItem);
		this.fileMenu.add(this.quitItem);

		DocumentParser docParser = new DocumentParser();
		docParser.parseDoc(this.corpusDir, progressBar);

		this.docNLPMap = docParser.getNlpSentenceMap();

		for (String docName : docNLPMap.keySet()) {
			this.fileCombo.addItem(docName);
		}
		this.fileCombo.addActionListener(this);

		this.comboPanel.add(this.comboLabel);
		this.comboPanel.add(this.fileCombo);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.newAnalysisItem)) {
			FileChooser sfc = new FileChooser();
			sfc.setVisible(true);
		} else if (e.getSource().equals(this.quitItem)) {
			System.exit(0);
		} else {
			displayResults();
		}
	}

	private void displayResults() {
		ArrayList<NLPSentence> sentences = docNLPMap
				.get((String) this.fileCombo.getSelectedItem());

		this.resultPanel.setLayout(new GridLayout(sentences.size(), 1));

		for (NLPSentence nlpSentence : sentences) {

			String resultSentence = nlpSentence.getSentence().toLowerCase();
			Map<String, String> subjectMap = nlpSentence
					.getSubjectSentimentResult();
			String result = "<html>";
			for (String subjectName : subjectMap.keySet()) {
				String[] words = resultSentence.split(" ");
				String sentence = "";
				String sentiment = subjectMap.get(subjectName);
				String coloredSubject = "";

				coloredSubject += "<strong><font color='"
						+ this.sentimentColorMap.get(sentiment.toLowerCase())
						+ "'>" + subjectName + "</font></strong>";

				int len = subjectName.split(" ").length;
				int i = 0;
				while (i < words.length) {
					String word = "";
					for (int j = 0; j < len; j++) {
						if (i < words.length)
							word += (words[i] + " ");
						i++;
					}
					word = word.trim();
					if (word.equals(subjectName.toLowerCase()))
						sentence += (coloredSubject + " ");
					else
						sentence += (word + " ");
				}
				resultSentence = sentence.trim();
			}
			result += ("[ <font color='"
					+ this.sentimentColorMap.get(nlpSentence
							.getSentenceSentiment().toLowerCase()) + "'>"
					+ nlpSentence.getSentenceSentiment() + "</font> ] --- "
					+ resultSentence + "</html>");
			JLabel resultLabel = new JLabel(result);
			Font font = new Font("Helvetica", Font.PLAIN, 18);
			resultLabel.setFont(font);
			this.resultPanel.add(resultLabel);
		}
		this.resultPane.revalidate();
		this.resultPane.repaint();
	}
}
