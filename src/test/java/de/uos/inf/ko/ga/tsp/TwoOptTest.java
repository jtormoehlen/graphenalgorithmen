package de.uos.inf.ko.ga.tsp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.*;

import de.uos.inf.ko.ga.graph.impl.UndirectedGraphMatrix;
import de.uos.inf.ko.ga.graph.util.GraphGenerator;
import org.junit.Test;

import de.uos.inf.ko.ga.graph.Graph;
import de.uos.inf.ko.ga.graph.reader.GraphReader;

public class TwoOptTest {

	private static final List<String> GRAPHS = Arrays.asList(
			"tsp_01.gra",
			"tsp_02.gra",
			"tsp_03.gra"
	);

	@Test
	public void testRunTwoOptOnTestGraphs() {
		for (final String filename : GRAPHS) {
			final File fileGraph = new File("src/test/resources/" + filename);

			try {
				final Graph graph = GraphReader.readUndirectedGraph(fileGraph);
				assertNotNull(graph);

				Tour next = generateRandomTSPTourFrom(graph);
				Tour maxTour, minTour;
				minTour = maxTour = next;
				double average = 0.0d;

				/* set the maximal number of random generations */
				final int counter = 100;

				for (int i = 0; i < counter; i++) {
//					System.out.println("----------------------------------------------------------");
//					System.out.println(next.toString());

					Tour solution = TwoOpt.iterativeTwoOpt(next, true);

					/* track the maximum */
					if (solution.getCosts() > maxTour.getCosts()) {
						maxTour = solution;
					}

					/* track the minimum */
					if (solution.getCosts() < minTour.getCosts()) {
						minTour = solution;
					}

					next = generateRandomTSPTourFrom(graph);

					average += solution.getCosts();

//					System.out.println(solution.toString());
//					System.out.println("----------------------------------------------------------");
				}

				/* output some report (min, max ) */
				System.out.println("Best: " + minTour.toString());
				System.out.println("Min: " + minTour.getCosts() + " | Max: " + maxTour.getCosts() + " | Avg: " + (average /= counter));
				System.out.println("##########################################################");


			} catch (Exception ex) {
				fail("caught an exception while computing TSP tours: " + ex);
			}
		}
	}

	/**
	 * Fills an array of size n with unique random
	 * integers from set {0,...,n-1} similar to the
	 * Collections.shuffle() method.
	 * @param graph initialize size of array with numbers of vertices |V| in G
	 * @return new Tour with graph and random route order
	 */
	private static Tour generateRandomTSPTourFrom(Graph graph) {
		List<Integer> arrayList = new ArrayList<>();
		Random random = new Random();
		int n = graph.getVertexCount();
		int[] order = new int[n];

		/* put all possible integers in a list */
		for (int i = 0; i < n; i++) {
			arrayList.add(new Integer(i));
		}

		/* as long as there are integers left */
		int i = 0;
		while (!arrayList.isEmpty()) {
			/* choose a random index to pick integer */
			int randomIndex = random.nextInt(n - 1);

			/* and put it in array if possible */
			if (randomIndex <= arrayList.size() - 1) {
				order[i++] = arrayList.get(randomIndex);
				arrayList.remove(randomIndex);
			}
		}

		return new Tour(graph, order);
	}
}
