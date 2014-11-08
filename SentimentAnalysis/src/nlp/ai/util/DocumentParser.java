package nlp.ai.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class DocumentParser {

	//private ArrayList<NLPSentence> nlpSentences;
	private Map<String, ArrayList<NLPSentence>> nlpSentenceMap;
	private Properties props;
	private StanfordCoreNLP pipeline;
	
	public DocumentParser(){
		this.nlpSentenceMap = new HashMap<>();
		this.props = new Properties();
		this.props.setProperty("annotators",
				"tokenize, ssplit, pos, lemma, parse, sentiment");
		this.pipeline = new StanfordCoreNLP(props);
	}
	
	public void parseDoc(String folderPath){
		File[] files = new File(folderPath).listFiles();
		
		for (File file: files){
			//parse each file here
			ArrayList<NLPSentence> nlpSentences = new ArrayList<>();
			//for each line {
				String line = "The movie was awful, but the actor was great. I also liked the story, but the action was awful.";
				line = "The actor was good, but the other actor was bad.";
				nlpSentences.add(getNLPSentenceForLine(line));
			//}
			this.nlpSentenceMap.put(file.getName().toLowerCase(), nlpSentences);
		}
		
		
		
	}

	public NLPSentence getNLPSentenceForLine(String line) {
		Annotation annotation = pipeline.process(line);
		CoreMap sentence = annotation
				.get(CoreAnnotations.SentencesAnnotation.class).get(0);
		if (line != null && line.length() > 0) {	
				String text = sentence.get(TextAnnotation.class);
				NLPSentence nlpSentence = new NLPSentence(text);
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

	public void setNlpSentenceMap(Map<String, ArrayList<NLPSentence>> nlpSentenceMap) {
		this.nlpSentenceMap = nlpSentenceMap;
	}
	
}
