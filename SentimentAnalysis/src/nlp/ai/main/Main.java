package nlp.ai.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class Main {

	private static ArrayList<String> subjects = new ArrayList<>();
	private static ArrayList<String> ngrams = new ArrayList<>();
	private static Map<String, ArrayList<String>> subjectMap = new HashMap<>();
	private static Map<String, ArrayList<String>> subjectSentiment = new HashMap<>();
	private static Map<String, String> subjectSentimentResult = new HashMap<>();

	public static void main(String[] args) {
		

		Properties props = new Properties();
		props.setProperty("annotators",
				"tokenize, ssplit, pos, lemma, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		String line = "The movie was awful, but the actor was great. I also liked the story, but the action was awful.";
		line = "@Rain also likes eating sausage.";

		generateNGrams(line);

		extractSubjectsFromLine(pipeline, line);

		loadSubjectNgramMap();

		loadSubjectSentimentsMap(pipeline);

		calculateSentimentForSubject();

		Annotation annotation = pipeline.process(line);
		List<CoreMap> sentences = annotation
				.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			String sentiment = sentence
					.get(SentimentCoreAnnotations.ClassName.class);
			System.out.println("[ "+ sentiment + " ] " + sentence);
		}

		for (String subject : subjectSentimentResult.keySet()) {
			System.out.println(subject + " : "
					+ subjectSentimentResult.get(subject));
		}
	}

	public static void calculateSentimentForSubject() {
		for (String subject : subjectSentiment.keySet()) {
			int positive = 0;
			int negative = 0;
			ArrayList<String> sentiments = subjectSentiment.get(subject);
			for (String sentiment : sentiments) {
				if (sentiment.toLowerCase().contains("positive"))
					positive++;
				else if (sentiment.toLowerCase().contains("negative"))
					negative++;
			}
			if (positive > negative)
				subjectSentimentResult.put(subject, "positive");
			else if (negative > positive)
				subjectSentimentResult.put(subject, "negative");
			else
				subjectSentimentResult.put(subject, "neutral");
		}
	}

	public static void loadSubjectSentimentsMap(StanfordCoreNLP pipeline) {
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

	public static void loadSubjectNgramMap() {
		for (String subject : subjects) {
			ArrayList<String> subjectNGrams = new ArrayList<>();
			if (subjectMap.containsKey(subject)) {
				subjectNGrams = subjectMap.get(subject);
			}
			for (String ngram : ngrams) {
				if (ngram.contains(subject))
					subjectNGrams.add(ngram);
			}
			subjectMap.put(subject, subjectNGrams);
		}
	}

	public static void extractSubjectsFromLine(StanfordCoreNLP pipeline,
			String line) {
		Annotation annotation = pipeline.process(line);

		List<CoreMap> sentences = annotation
				.get(CoreAnnotations.SentencesAnnotation.class);
		if (line != null && line.length() > 0) {
			for (CoreMap sentence : sentences) {

				Tree tr = sentence
						.get(TreeCoreAnnotations.TreeAnnotation.class);

				List<Tree> children = tr.firstChild().getChildrenAsList();
				inOrderTraversal(children);
			}
		}
	}

	public static void inOrderTraversal(List<Tree> children) {
		for (Tree sibling : children) {
			if (sibling.label().value().toLowerCase().equals("np")) {
				getSubject(sibling.getChildrenAsList());
			} else {
				inOrderTraversal(sibling.getChildrenAsList());
			}
		}
	}

	public static void getSubject(List<Tree> children) {
		for (Tree sibling : children) {
			if (sibling.label().value().toLowerCase().contains("nn")) {
				subjects.add(sibling.firstChild().label().value().toLowerCase());
			} else {
				getSubject(sibling.getChildrenAsList());
			}
		}
	}

	public static void generateNGrams(String sentence) {

		// get rid of punctuation
		sentence = sentence.replaceAll("[,]", "");
		sentence = sentence.replaceAll("[^a-zA-Z0-9'-@]", ";");
		sentence = sentence.replaceAll(";", " ");
		String[] words = sentence.split(" ");

		for (int i = 0; i <= words.length - 3; i++) {
			String threeGram = words[i] + " " + words[i + 1] + " "
					+ words[i + 2];
			ngrams.add(threeGram);
			if (words[i].contains("@"))
				subjects.add(words[i]);
		}
	}
}

