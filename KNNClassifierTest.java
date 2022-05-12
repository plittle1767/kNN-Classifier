package a7;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

class KNNClassifierTest {

	Sample2D sampleA = new Sample2D(0, 0, "a");
	Sample2D sampleB = new Sample2D(1, 0, "b");
	Sample2D sampleC = new Sample2D(2, 0, "c");
	Sample2D sampleD = new Sample2D(3, 0, "d");
	Sample2D sampleE = new Sample2D(4, 0, "e");

	@Test
	void testFindKClosest() {
		ArrayList<Sample2D> samples = new ArrayList<>();

		samples.add(sampleA);
		samples.add(sampleB);
		samples.add(sampleC);
		samples.add(sampleD);

		KNNClassifier testClassifier = new KNNClassifier(3, samples);

		ArrayList<Neighbor> neighbors = testClassifier.computeNeighbors(sampleE);

		ArrayList<Neighbor> kClosestNeighbor = testClassifier.findKClosest(neighbors);

		assertEquals(sampleD, kClosestNeighbor.get(0).getSample(), "Test failed. Expected to get '3, 0, 'd'' but got " 
		+ kClosestNeighbor.get(0).getSample());
	}

	@Test
	void testVoteOnClassification() {
//		HashMap<String, Integer> voteMap = new HashMap<>();
//
//		ArrayList<Neighbor> samples = new ArrayList<>();
//
//		samples.add(sampleA);
//		samples.add(sampleB);
//		samples.add(sampleC);
//		samples.add(sampleD);
//
//		voteMap.put("smiling", 3);
//		voteMap.put("neutral", 1);
//		voteMap.put("surpirsed", 2);
//
//		KNNClassifier testClassifier = new KNNClassifier(3, samples);
//		String votes = testClassifier.voteOnClassification(voteMap);
//
//		assertEquals("smiling", votes, "Test failed. Expected to get *blank* but got *blank*");
	}

	@Test
	void testGetHighestVotedClassification() {
		HashMap<String, Integer> voteMap = new HashMap<>();

		ArrayList<Sample2D> samples = new ArrayList<>();
		
		samples.add(sampleA);
		samples.add(sampleB);
		samples.add(sampleC);
		samples.add(sampleD);

		voteMap.put("smiling", 3);
		voteMap.put("neutral", 1);
		voteMap.put("surpirsed", 2);

		KNNClassifier testClassifier = new KNNClassifier(3, samples);
		String votes = testClassifier.getHighestVotedClassification(voteMap);

		assertEquals("smiling", votes, "Test failed. Expected to get smiling but got " + votes);
	}

	@Test
	void testCompareTo() {
		Neighbor tester = new Neighbor(sampleA, sampleB);
		Neighbor tester2 = new Neighbor(sampleC, sampleD);
		
		int compareTest = tester.compareTo(tester2);
		
		assertEquals(0, compareTest, "Test failed. Expected to get 0 but got " + compareTest);
	}

}
