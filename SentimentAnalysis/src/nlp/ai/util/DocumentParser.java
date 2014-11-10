package nlp.ai.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.util.CoreMap;

public class DocumentParser {

	// private ArrayList<NLPSentence> nlpSentences;
	private Map<String, ArrayList<NLPSentence>> nlpSentenceMap;
	private Properties props;
	private StanfordCoreNLP pipeline;

	public DocumentParser() {
		this.nlpSentenceMap = new HashMap<>();
		this.props = new Properties();
		this.props.setProperty("annotators",
				"tokenize, ssplit, pos, lemma, parse, sentiment");
		this.pipeline = new StanfordCoreNLP(props);
	}

	public void parseDoc(String folderPath) {
		File[] files = new File(folderPath).listFiles();
		try {
			for (File file : files) {

				FileInputStream fis = new FileInputStream(file);
				byte[] data = new byte[(int) file.length()];
				fis.read(data);
				fis.close();

				String content = new String(data, "UTF-8");

				Reader reader = new StringReader(content);
				DocumentPreprocessor dp = new DocumentPreprocessor(reader);
				List<String> sentenceList = new LinkedList<String>();
				Iterator<List<HasWord>> it = dp.iterator();
				while (it.hasNext()) {
					StringBuilder sentenceSb = new StringBuilder();
					List<HasWord> sentence = it.next();
					for (HasWord token : sentence) {
						if (sentenceSb.length() >= 1) {
							sentenceSb.append(" ");
						}
						sentenceSb.append(token);
					}
					sentenceList.add(sentenceSb.toString());
				}

				ArrayList<NLPSentence> nlpSentences = new ArrayList<>();
				for (String line : sentenceList) {
					nlpSentences.add(getNLPSentenceForLine(line));
				}
				this.nlpSentenceMap.put(file.getName().toLowerCase(),
						nlpSentences);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public NLPSentence getNLPSentenceForLine(String line) {
		Annotation annotation = pipeline.process(line);
		CoreMap sentence = annotation.get(
				CoreAnnotations.SentencesAnnotation.class).get(0);
		if (line != null && line.length() > 0) {
			String text = sentence.get(TextAnnotation.class);
			NLPSentence nlpSentence = new NLPSentence(text);
			nlpSentence.calculateSentenceSentiment(sentence);
			nlpSentence.extractSubjectsFromLine(sentence);
			nlpSentence.loadSubjectNgramMap();
			nlpSentence.loadSubjectSentimentsMap(pipeline);
			nlpSentence.calculateSentimentForSubject();
			return nlpSentence;
		}
		return null;
	}

	public Map<String, ArrayList<NLPSentence>> getNlpSentenceMap() {
		return nlpSentenceMap;
	}

	public void setNlpSentenceMap(
			Map<String, ArrayList<NLPSentence>> nlpSentenceMap) {
		this.nlpSentenceMap = nlpSentenceMap;
	}

}
