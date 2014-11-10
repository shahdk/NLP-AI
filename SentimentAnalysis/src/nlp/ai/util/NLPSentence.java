package nlp.ai.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class NLPSentence {

	private String sentence;
	private String sentenceSentiment;
	private ArrayList<String> subjects;
	private Map<String, ArrayList<String>> subjectMap;
	private Map<String, ArrayList<String>> subjectSentiment;
	private Map<String, String> subjectSentimentResult;
	private ArrayList<String> ngrams;
	private Map<String, Integer> sentimentScoreMap;

	public NLPSentence(String sentence) {
		this.sentence = sentence;
		this.sentenceSentiment = "";
		this.subjects = new ArrayList<>();
		this.subjectMap = new HashMap<>();
		this.subjectSentiment = new HashMap<>();
		this.subjectSentimentResult = new HashMap<>();
		this.ngrams = new ArrayList<String>();
		this.sentimentScoreMap = new HashMap<>();
		this.loadSentimentScoreMap();
		this.generateNGrams(sentence);
	}

	public void loadSentimentScoreMap() {
		String[] sentiments = new String[] { "very negative", "negative",
				"neutral", "positive", "very positive" };
		int[] scores = new int[]{-2, -1, 0, 1, 2};
		
		for (int i = 0; i<sentiments.length; i++){
			this.sentimentScoreMap.put(sentiments[i], scores[i]);
		}
	}

	public void calculateSentenceSentiment(CoreMap sentence) {
		this.sentenceSentiment = sentence
				.get(SentimentCoreAnnotations.ClassName.class);
	}

	public void generateNGrams(String sentence) {

		// get rid of punctuation
		sentence = sentence.replaceAll("[,]", "");
		sentence = sentence.replaceAll("[^a-zA-Z0-9'-@]", ";");
		sentence = sentence.replaceAll("[\\s.]", ";");
		sentence = sentence.replaceAll(";", " ");
		String[] words = sentence.split(" ");

		for (int i = 0; i <= words.length - 5; i++) {
			String threeGram = words[i] + " " + words[i + 1] + " "
					+ words[i + 2] + " " + words[i + 3] + " " + words[i + 4];
			ngrams.add(threeGram);
		}
	}

	public void extractSubjectsFromLine(CoreMap coreMapSentence) {
		Tree tr = coreMapSentence.get(TreeCoreAnnotations.TreeAnnotation.class);
		List<Tree> children = tr.firstChild().getChildrenAsList();
		inOrderTraversal(children);
	}

	public void inOrderTraversal(List<Tree> children) {
		for (Tree sibling : children) {
			if (sibling.label().value().toLowerCase().equals("np")) {
				getSubject(sibling.getChildrenAsList());
			} else {
				inOrderTraversal(sibling.getChildrenAsList());
			}
		}
	}

	public void getSubject(List<Tree> children) {
		String subject = "";
		for (Tree sibling : children) {
			subject += sibling.firstChild().label().value().toLowerCase() + " ";
		}
		subjects.add(subject.trim());
	}

	public void loadSubjectNgramMap() {
		for (String subject : subjects) {
			ArrayList<String> subjectNGrams = new ArrayList<>();
			if (subjectMap.containsKey(subject)) {
				subjectNGrams = subjectMap.get(subject);
			}
			for (String ngram : ngrams) {
				String[] words = ngram.split(" ");
				String[] subs = subject.split(" ");
				int counter = 0;
				for (int i = 0; i < subs.length; i++) {
					for (int j = 0; j < words.length; j++) {
						if (words[j].toLowerCase().equals(subs[i]))
							counter++;
					}
					if (counter == subs.length)
						subjectNGrams.add(ngram);
				}
			}
			subjectMap.put(subject, subjectNGrams);
		}
	}

	public void loadSubjectSentimentsMap(StanfordCoreNLP pipeline) {
		for (String subject : subjectMap.keySet()) {
			ArrayList<String> sentiments = new ArrayList<>();
			if (subjectSentiment.containsKey(subject)) {
				sentiments = subjectSentiment.get(subject);
			}

			for (String ngram : subjectMap.get(subject)) {
				Annotation annotation = pipeline.process(ngram);
				List<CoreMap> sentences = annotation
						.get(CoreAnnotations.SentencesAnnotation.class);
				for (CoreMap sentence : sentences) {
					String sentiment = sentence
							.get(SentimentCoreAnnotations.ClassName.class);
					sentiments.add(sentiment);
				}
				subjectSentiment.put(subject, sentiments);
			}
		}
	}

	public void calculateSentimentForSubject() {
		for (String subject : subjectSentiment.keySet()) {
			int score = 0;
			ArrayList<String> sentiments = subjectSentiment.get(subject);
			for (String sentiment : sentiments) {
				sentiment = sentiment.toLowerCase();
				score += this.sentimentScoreMap.get(sentiment);
			}
			if (score > 0)
				subjectSentimentResult.put(subject, "positive");
			else if (score < 0)
				subjectSentimentResult.put(subject, "negative");
			else
				subjectSentimentResult.put(subject, "neutral");
		}
	}

	public void printResults() {
		System.out.println("[ " + this.sentenceSentiment + " ] "
				+ this.sentence);
		for (String subject : subjectSentimentResult.keySet()) {
			System.out.println(subject + " : "
					+ subjectSentimentResult.get(subject));
		}
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getSentenceSentiment() {
		return sentenceSentiment;
	}

	public void setSentenceSentiment(String sentenceSentiment) {
		this.sentenceSentiment = sentenceSentiment;
	}

	public ArrayList<String> getSubjects() {
		return subjects;
	}

	public void setSubjects(ArrayList<String> subjects) {
		this.subjects = subjects;
	}

	public Map<String, String> getSubjectSentimentResult() {
		return subjectSentimentResult;
	}

	public void setSubjectSentimentResult(
			Map<String, String> subjectSentimentResult) {
		this.subjectSentimentResult = subjectSentimentResult;
	}

	public Map<String, ArrayList<String>> getSubjectSentiment() {
		return subjectSentiment;
	}

	public void setSubjectSentiment(
			Map<String, ArrayList<String>> subjectSentiment) {
		this.subjectSentiment = subjectSentiment;
	}

	public Map<String, ArrayList<String>> getSubjectMap() {
		return subjectMap;
	}

	public void setSubjectMap(Map<String, ArrayList<String>> subjectMap) {
		this.subjectMap = subjectMap;
	}

	public ArrayList<String> getNgrams() {
		return ngrams;
	}

	public void setNgrams(ArrayList<String> ngrams) {
		this.ngrams = ngrams;
	}

}
