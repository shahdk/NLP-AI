package nlp.ai.main;

import java.util.ArrayList;
import java.util.Map;

import nlp.ai.util.DocumentParser;
import nlp.ai.util.NLPSentence;

public class Main {	
	
	public static void main(String[] args) {

		DocumentParser docParser = new DocumentParser();
		docParser.parseDoc("docs\\PositiveReviews");
		
		Map<String, ArrayList<NLPSentence>> docNLPMap = docParser.getNlpSentenceMap();
		for (String docName: docNLPMap.keySet()){
			ArrayList<NLPSentence> sentences = docNLPMap.get(docName);
			for (NLPSentence sentence: sentences){
				sentence.printResults();
				break;
			}
			break;
		}	 
	}
}