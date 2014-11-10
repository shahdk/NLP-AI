package nlp.ai.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nlp.ai.util.DocumentParser;
import nlp.ai.util.NLPSentence;

public class SentimentDisplay extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private String corpusDir;

	private JLabel comboLabel;
	private JComboBox<String> fileCombo;

	private JPanel comboPanel;
	private JScrollPane resultPanel;
	private Map<String, ArrayList<NLPSentence>> docNLPMap;

	public SentimentDisplay(String corpusDirectory) {
		super("NLP Results");
		setSize(450, 200);

		this.corpusDir = corpusDirectory;
		this.initComponents();

		this.setLayout(new GridLayout(2, 1));
		this.add(this.comboPanel, 0);
		this.add(this.resultPanel, 1);
	}

	public void initComponents() {
		this.comboLabel = new JLabel("Select Document: ");
		this.resultPanel = new JScrollPane();
		this.comboPanel = new JPanel();
		this.fileCombo = new JComboBox<String>();

		DocumentParser docParser = new DocumentParser();
		docParser.parseDoc(this.corpusDir);

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
		this.resultPanel.removeAll();
		ArrayList<NLPSentence> sentences = docNLPMap
				.get((String) this.fileCombo.getSelectedItem());
		for (NLPSentence sentence : sentences) {
			
			String resultSentence = sentence.getSentence();
			Map<String, String> subjectMap = sentence.getSubjectSentimentResult();
			for(String subjectName : subjectMap.keySet()){
				String sentiment = subjectMap.get(subjectName);
				String coloredSubject ="";
				if (sentiment.toLowerCase().equals("positive")){
					coloredSubject += "<html><strong><font color='green'>" + subjectName + "</font></strong></html>";
					resultSentence = resultSentence.replaceAll(subjectName, coloredSubject);
				}
				else if (sentiment.toLowerCase().equals("negative")){
					coloredSubject += "<html><strong><font color='red'>" + subjectName + "</font></strong></html>";
					resultSentence = resultSentence.replaceAll(subjectName, coloredSubject);
				}
				else{
					coloredSubject += "<html><strong>" + subjectName + "</strong></html>";
					resultSentence = resultSentence.replaceAll(subjectName, coloredSubject);
				}
				
			}
			String result = sentence.getSentenceSentiment() +" : "+ resultSentence;
			JLabel resultLabel = new JLabel(result);
			this.resultPanel.add(resultLabel);
		}
	}
}
