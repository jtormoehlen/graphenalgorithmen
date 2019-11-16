package de.uos.inf.ko.ga.graph.shortestpath;

import de.uos.inf.ko.ga.graph.Graph;

/**
 * Shortest-path computation with Bellman-Ford for determining the distances
 * from a vertex to all other vertices in a directed graph.
 * @author Tobias Oelschlägel
 *
 */
public class BellmanFord {

	/**
	 * Computes distances to all vertices in a graph starting at a certain vertex.
	 * The distances returned is Double.POSITIVE_INFINITY if a vertex is not
	 * reachable from the start vertex.
	 * @param graph Input graph
	 * @param start Start vertex for the computation of the distances
	 * @return Array containing the distance d[v] from the start node for each vertex v
	 */
	public static double[] shortestPaths(Graph graph, int start) {
		final int n = graph.getVertexCount();

		double[] d_cur = new double[n];
		double[] d_next = new double[n];

		/* set the initial distances */
		d_cur[start] = 0.0;
		for (int v = 0; v < n; ++v) {
			if (v != start) {
				d_cur[v] = graph.getEdgeWeight(start, v);
			}
		}

		/* flag whether any distance has changed during the last iteration */
		boolean changed;

		for (int k = 1; k < n; ++k) {
			changed = false;

			for (int v = 0; v < n; ++v) {
				/* copy old entries to the new array 'd_next' */
				d_next[v] = d_cur[v];

				for (int u : graph.getPredecessors(v)) {
					final double c = graph.getEdgeWeight(u, v);
	
					/* update the minimum distance */
					if (d_cur[u] + c < d_next[v]) {
						d_next[v] = d_cur[u] + c;
						changed = true;
					}
				}
			}

			/* swap both array */
			final double[] d_tmp = d_cur;
			d_cur = d_next;
			d_next = d_tmp;
			
			/* abort if nothing has changed */
			if (!changed) {
				break;
			}
		}

		return d_cur;
	}

}
