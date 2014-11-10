package nlp.ai.util;

import java.util.ArrayList;

public class SkipGramGenerator {

	private String[] words;

	public SkipGramGenerator(String[] words) {
		this.words = words;
	}

	public ArrayList<String> generate() {
		ArrayList<String> ngrams = new ArrayList<>();
		for (int i = 0; i <= this.words.length - 5; i++) {
			String fiveGram = words[i] + " " + words[i + 1] + " "
					+ words[i + 2] + " " + words[i + 3] + " " + words[i + 4];
			ngrams.add(fiveGram);

			if ((i + 5) < this.words.length) {
				// -,2,3,4,5,6,-
				fiveGram = words[i + 1] + " " + words[i + 2] + " "
						+ words[i + 3] + " " + words[i + 4] + " "
						+ words[i + 5];
				ngrams.add(fiveGram);

				// 1,2,3,-,5,6,-
				fiveGram = words[i] + " " + words[i + 1] + " " + words[i + 2]
						+ " " + words[i + 4] + " " + words[i + 5];
				ngrams.add(fiveGram);

				// 1,2,3,4,-,6,-
				fiveGram = words[i] + " " + words[i + 1] + " " + words[i + 2]
						+ " " + words[i + 3] + " " + words[i + 5];
				ngrams.add(fiveGram);

				// 1,2,-,4,5,6,-
				fiveGram = words[i] + " " + words[i + 1] + " " + words[i + 3]
						+ " " + words[i + 4] + " " + words[i + 5];
				ngrams.add(fiveGram);

				// 1,-,3,4,5,6,-
				fiveGram = words[i] + " " + words[i + 2] + " " + words[i + 3]
						+ " " + words[i + 4] + " " + words[i + 5];
				ngrams.add(fiveGram);
			}

			if ((i + 6) < this.words.length) {
				// -,2,3,4,5,-,7
				fiveGram = words[i + 1] + " " + words[i + 2] + " "
						+ words[i + 3] + " " + words[i + 4] + " "
						+ words[i + 6];
				ngrams.add(fiveGram);

				// 1,2,3,4,-,-,7
				fiveGram = words[i] + " " + words[i + 1] + " " + words[i + 2]
						+ " " + words[i + 3] + " " + words[i + 6];
				ngrams.add(fiveGram);

				// -,2,3,4,-,6,7
				fiveGram = words[i + 1] + " " + words[i + 2] + " "
						+ words[i + 3] + " " + words[i + 5] + " "
						+ words[i + 6];
				ngrams.add(fiveGram);

				// -,2,3,-,5,6,7
				fiveGram = words[i + 1] + " " + words[i + 2] + " "
						+ words[i + 4] + " " + words[i + 5] + " "
						+ words[i + 6];
				ngrams.add(fiveGram);

				// 1,2,-,-,5,6,7
				fiveGram = words[i] + " " + words[i + 1] + " " + words[i + 4]
						+ " " + words[i + 5] + " " + words[i + 6];
				ngrams.add(fiveGram);

				// 1,2,3,-,-,6,7
				fiveGram = words[i] + " " + words[i + 1] + " " + words[i + 2]
						+ " " + words[i + 5] + " " + words[i + 6];
				ngrams.add(fiveGram);

				// 1,2,3,-,5,-,7
				fiveGram = words[i] + " " + words[i + 1] + " " + words[i + 2]
						+ " " + words[i + 4] + " " + words[i + 6];
				ngrams.add(fiveGram);

				// -,2,-,4,5,6,7
				fiveGram = words[i + 1] + " " + words[i + 3] + " "
						+ words[i + 4] + " " + words[i + 5] + " "
						+ words[i + 6];
				ngrams.add(fiveGram);
				
				// 1,2,-,4,-,6,7
				fiveGram = words[i] + " " + words[i + 1] + " " + words[i + 3]
						+ " " + words[i + 5] + " " + words[i + 6];
				ngrams.add(fiveGram);
				
				// 1,2,-,4,5,-,7
				fiveGram = words[i] + " " + words[i + 1] + " " + words[i + 3]
						+ " " + words[i + 4] + " " + words[i + 6];
				ngrams.add(fiveGram);
				
				// -,-,3,4,5,6,7
				fiveGram = words[i + 2] + " " + words[i + 3] + " " + words[i + 4]
						+ " " + words[i + 5] + " " + words[i + 6];
				ngrams.add(fiveGram);

				// 1,-,-,4,5,6,7
				fiveGram = words[i] + " " + words[i + 3] + " " + words[i + 4]
						+ " " + words[i + 5] + " " + words[i + 6];
				ngrams.add(fiveGram);

				// 1,-,3,-,5,6,7
				fiveGram = words[i] + " " + words[i + 2] + " "
						+ words[i + 4] + " " + words[i + 5] + " "
						+ words[i + 6];
				ngrams.add(fiveGram);
				
				// 1,-,3,4,-,6,7
				fiveGram = words[i] + " " + words[i + 2] + " " + words[i + 3]
						+ " " + words[i + 5] + " " + words[i + 6];
				ngrams.add(fiveGram);
				
				// 1,-,3,4,5,-,7
				fiveGram = words[i] + " " + words[i + 2] + " " + words[i + 3]
						+ " " + words[i + 4] + " " + words[i + 6];
				ngrams.add(fiveGram);
			}
		}
		return ngrams;
	}

}
