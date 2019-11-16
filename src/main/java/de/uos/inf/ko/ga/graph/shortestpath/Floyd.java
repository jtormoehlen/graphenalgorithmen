package de.uos.inf.ko.ga.graph.shortestpath;

import de.uos.inf.ko.ga.graph.Graph;

/**
 * Shortest-path computation with Floyd for determining the distances between
 * all pairs of vertices in a directed graph.
 * @author Tobias Oelschlägel
 *
 */
public class Floyd {

	/**
	 * Computes distances between all pairs of vertices.
	 * If a vertex is not reachable from another vertex, the corresponding entry
	 * of the distance matrix is Double.POSITIVE_INFINITY.
	 * @param graph Input graph
	 * @return Matrix of dimension n times n with entry d[i][j] being the distance from vertex i to vertex j
	 */
	public static double[][] shortestPaths(Graph graph) {
		final int n = graph.getVertexCount();
		double[][] d = new double[n][n];

		for (int u = 0; u < n; ++u) {
			d[u][u] = 0.0;

			for (int v = 0; v < n; ++v) {
				if (u != v) {
					d[u][v] = graph.getEdgeWeight(u, v);
				}
			}
		}
		
		for (int k = 0; k < n; ++k) {
			for (int u = 0; u < n; ++u) {
				for (int v = 0; v < n; ++v) {
					final double w = d[u][k] + d[k][v];
					if (w < d[u][v]) {
						d[u][v] = w;
					}
				}
			}
		}

		return d;
	}
}
