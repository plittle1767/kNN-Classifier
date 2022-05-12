package a7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Implements a kNN classifier on Face samples.
 * 
 * @author David Johnson
 *
 */
public class KNNClassifier {

	private int k; // The number of nearest neighbors used
	private ArrayList<? extends Sample2D> samples; // The List of known samples

	/**
	 * Construct the classifier object with a k value and samples.
	 * 
	 * @param k   the number of neighbors to use to classify
	 * @param the samples that are already classified
	 */
	public KNNClassifier(int k, ArrayList<? extends Sample2D> samples) {
		this.k = k;
		this.samples = samples;
	}

	/**
	 * Every sample can be considered a Neighbor to the unknown sample. Go through
	 * the list of known samples and make a Neighbor object for each one. The
	 * Neighbor holds the distance between the sample and the unknown sample.
	 * 
	 * @param unknown the unknown Sample
	 * @return a list of all the Neighbors to the unknown sample.
	 */
	public ArrayList<Neighbor> computeNeighbors(Sample2D unknown) {
		ArrayList<Neighbor> neighbors = new ArrayList<>();
		for (Sample2D sample : samples) {
			Neighbor neighbor = new Neighbor(sample, unknown);
			neighbors.add(neighbor);
		}
		return neighbors;
	}

	/**
	 * Compute the k closest Neighbor objects. A Neighbor has a compareTo so a list
	 * of Neighbors can be sorted. You can assume there are at least k items in
	 * samples.
	 * 
	 * @param neighbors the list of neighbors.
	 * @return the ArrayList of the k closest Neighbor objects in neighbors.
	 */
	public ArrayList<Neighbor> findKClosest(ArrayList<Neighbor> neighbors) {
		neighbors.sort(null);

		ArrayList<Neighbor> sortedNieghborArray = new ArrayList<>();

		for (int i = 0; i < k; i++) {
			sortedNieghborArray.add(neighbors.get(i));
		}

		return sortedNieghborArray;
	}

	/**
	 * Given the kClosest Neighbor objects, count up how many times each known
	 * classification appears in the k closest. As an example, if the kClosest have
	 * classifications "smiling", "surprised", "smiling", then the result should be
	 * {"smiling" = 2, "surprised" = 1}.
	 * 
	 * @param a list of the kClosest Neighbor objects.
	 * @return a HashMap of classifications and their count.
	 */
	public HashMap<String, Integer> voteOnClassification(ArrayList<Neighbor> kClosest) {
		HashMap<String, Integer> classiciationVote = new HashMap<String, Integer>();
		for (int i = 0; i < kClosest.size(); i++) {
			String classification = kClosest.get(i).sample.getClassification();
			if (classiciationVote.containsKey(classification)) 
				classiciationVote.put(classification, classiciationVote.get(classification) + 1);
			else
				classiciationVote.put(classification, 1);
		}

		return classiciationVote;
	}

	/**
	 * Given a HashMap of classifications and their votes, pick the classification
	 * with the most votes. For a tie, the first appearing classification (as
	 * defined by the order they appear in the keySet() of the HashMap) with that
	 * count should be picked. For a votes map of {"smiling" = 2, "surprised" = 1},
	 * the returned classification would be "smiling".
	 * 
	 * @param votes with entries of classifications as keys and number of votes as
	 *              values.
	 * @return the classification with the most votes.
	 */
	public String getHighestVotedClassification(HashMap<String, Integer> votes) {
		String highestVotedExpress = "";
		Integer highestVotes = 0;

		for (String expression : votes.keySet()) {
			if (highestVotes < votes.get(expression)) {
				highestVotes = votes.get(expression);
				highestVotedExpress = expression;
			}
		}

		return highestVotedExpress;
	}

	/**
	 * The main method to find a classification. The code calls methods to enact
	 * each of the needed steps.
	 * 
	 * @param unknown sample
	 * @return the classification
	 */
	public String classifyUnknownSample(Sample2D unknown) {
		ArrayList<Neighbor> neighbors = computeNeighbors(unknown);
		ArrayList<Neighbor> kClosest = findKClosest(neighbors);
		HashMap<String, Integer> votes = voteOnClassification(kClosest);
		System.out.println(votes);
		String classification = getHighestVotedClassification(votes);
		return classification;
	}
}
