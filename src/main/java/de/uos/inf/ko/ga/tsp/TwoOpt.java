package de.uos.inf.ko.ga.tsp;

import de.uos.inf.ko.ga.graph.Graph;

/**
 * @author edited by jtormoehlen
 */
public class TwoOpt {

	/**
	 * Perform a two-opt exchange step of the edges (v[pos1], v[pos1 + 1]) and (v[pos2], v[pos2 + 1]).
	 * The resulting tour visits
	 *   v[0], ..., v[pos1], v[pos2], v[pos2 - 1], ..., v[pos1 + 1], v[pos2 + 1], v[pos2 + 2], ..., v[n - 1].
	 * Special case: if the last edge (v[n - 1], v[0]) is used, then the following tour is created:
	 *   v[0], v[pos1 + 1], v[pos1 + 2], ..., v[n - 1], v[pos1], v[pos1 - 1], ..., v[0]
	 * @param tour Tour
	 * @param pos1 Index of the starting vertex of the first edge
	 * @param pos2 Index of the starting vertex of the second edge
	 * @return tour obtained by performing the edge exchange
	 */
	private static Tour twoOptExchange(Tour tour, int pos1, int pos2) {
		assert(tour != null);
		assert(pos1 >= 0);
		assert(pos2 > pos1 + 1);
		assert(pos2 < tour.getVertices().length);

		/* case is handled */
//		assert(pos1 != (pos2 + 1) % tour.getVertices().length);

		int[] route = tour.getVertices();
		int[] next = new int[route.length];

		/* in case edge {v[n - 1], v[0]} is not used... */
		if (pos2 != route.length - 1) {

			int j = 0;
			for (int i = 0; i <= pos1; i++) {
				next[j++] = route[i];
			}

			next[j++] = route[pos2];

			for (int i = pos2 - 1; i >= pos1 + 1; i--) {
				next[j++] = route[i];
			}

			next[j++] = route[pos2 + 1];

			for (int i = pos2 + 2; i <= route.length - 1; i++) {
				next[j++] = route[i];
			}

			/* ...otherwise special case */
		} else {

			int j = 0;
			next[j++] = route[pos1 + 1];

			for (int i = pos1 + 2; i <= route.length - 1; i++) {
				next[j++] = route[i];
			}

			next[j++] = route[pos1];

			for (int i = pos1 - 1; i >= 1; i--) {
				next[j++] = route[i];
			}

			next[route.length - 1] = route[0];

		}

		return new Tour(tour.getGraph(), next);
	}

	/**
	 * Single step of the Two-Opt neighborhood for the TSP with either
	 * first-fit or best-fit selection of the neighbor.
	 * - First-fit returns the first neighbor that is found that has a
	 *   better objective value than the original tour.
	 * - Best-fit always searches through the whole neighborhood and
	 *   returns one of the tours with the best objective values.
	 * @param tour
	 * @param firstFit whether to use first-fit or best-fit for neighbor selection
	 * @return tour obtained by performing the first or the best improvement
	 */
	private static Tour twoOptNeighborhood(Tour tour, boolean firstFit) {

		Graph graph = tour.getGraph();
		int n = graph.getVertexCount();
		double bestCost = tour.getCosts();

		/* loop through all N^2 neighborhoods */
		for (int i = 0; i < n - 1; i++) {
			for (int j = i + 2; j < n; j++) {

				/* perform the 2-opt edge exchange */
				Tour next = twoOptExchange(tour, i, j);
				double nextCost = next.getCosts();

				if (nextCost < bestCost) {
					tour = next;
					bestCost = nextCost;

					/* return the first improvement if firstfit */
					if (firstFit) {
						return tour;
					}
				}
			}
		}

		return tour;
	}


	/**
	 * Iterative Two-Opt neighborhood for the TSP.
	 * This method calls twoOptNeighborhood iteratively as long as the
	 * tour can be improved.
	 * @param tour Tour to be improved
	 * @param firstFit whether to use first-fit or best-fit for neighbor selection
	 * @return best tour obtained by iteratively applying the two-opt neighborhood
	 */
	public static Tour iterativeTwoOpt(Tour tour, boolean firstFit) {

		Tour current = tour;
		Tour next = twoOptNeighborhood(current, firstFit);

		/* loop until no improvement is possible */
		while (next.getCosts() < current.getCosts() && !firstFit) {
			current = next;
			next = twoOptNeighborhood(current, firstFit);
		}

		return current;
	}
}
