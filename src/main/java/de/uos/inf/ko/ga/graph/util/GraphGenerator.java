package de.uos.inf.ko.ga.graph.util;

import java.util.Random;

import de.uos.inf.ko.ga.graph.Graph;

public class GraphGenerator {

	/**
	 * Generates a random graph. A given number of vertices is added to the graph
	 * and each possible edge is added with probability p. The edge weights are
	 * distributed uniformly in [0, 1].
	 * 
	 * @param graph  Empty graph
	 * @param n      Number of vertices to be added to the graph
	 * @param random Pseudo random number generator
	 * @param p      Probability of existence of an edge
	 */
	public static void generateRandomGraph(Graph graph, int n, Random random, double p) {
		assert (graph != null);

		graph.addVertices(n);

		if (graph.isDirected()) {
			for (int u = 0; u < n; ++u) {
				for (int v = 0; v < n; ++v) {
					if (u != v) {
						if (random.nextDouble() <= p) {
							graph.addEdge(u, v, random.nextDouble());
						}
					}
				}
			}
		} else {
			for (int u = 0; u < n; ++u) {
				for (int v = u + 1; v < n; ++v) {
					if (random.nextDouble() <= p) {
						graph.addEdge(u, v, random.nextDouble());
					}
				}
			}
		}
	}

	/**
	 * Generates a random graph and plants a spanning tree. A given number of
	 * vertices is added to the graph and each possible edge is added with
	 * probability p. The edge weights are distributed uniformly in [0, 1].
	 * To ensure that the graph is connected, a spanning tree is planted after
	 * adding the edges independently. For this, the vertices are shuffled randomly
	 * into the order v_1, ..., v_n, and then for each i in { 2, ..., n } a vertex
	 * is chosen randomly from the set v_1, ..., v_{i - 1} and connected to vertex v_i
	 * by an edge with weight chosen randomly from [0, 1].
	 * 
	 * @param graph  Empty graph
	 * @param n      Number of vertices to be added to the graph
	 * @param random Pseudo random number generator
	 * @param p      Probability of existence of an edge
	 */
	public static void generateRandomConnectedGraph(Graph graph, int n, Random random, double p) {
		assert (graph != null);

		graph.addVertices(n);

		if (graph.isDirected()) {
			for (int u = 0; u < n; ++u) {
				for (int v = 0; v < n; ++v) {
					if (u != v) {
						if (random.nextDouble() <= p) {
							graph.addEdge(u, v, random.nextDouble());
						}
					}
				}
			}
		} else {
			for (int u = 0; u < n; ++u) {
				for (int v = u + 1; v < n; ++v) {
					if (random.nextDouble() <= p) {
						graph.addEdge(u, v, random.nextDouble());
					}
				}
			}
		}

		/*
		 * plant a spanning tree inside: shuffle the list of vertices and then add an
		 * edge from vertex (i) to some random vertex from the set { 0, ..., i - 1 }.
		 */
		int vertices[] = new int[n];
		for (int v = 0; v < n; ++v) {
			vertices[v] = v;
		}

		shuffleArray(vertices, random);

		for (int v = 1; v < n; ++v) {
			/* get some vertex from the set { vertices[0], ..., vertices[v - 1] } */
			final int u = vertices[random.nextInt(v)];
			graph.addEdge(u, v, random.nextDouble());
		}
	}

	private static void shuffleArray(int[] array, Random random) {
		for (int i = array.length - 1; i > 0; i--) {
			final int index = random.nextInt(i + 1);
			if (index != i) {
				final int tmp = array[i];
				array[i] = array[index];
				array[index] = tmp;
			}
		}
	}
}
