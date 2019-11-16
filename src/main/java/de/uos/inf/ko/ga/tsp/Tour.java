package de.uos.inf.ko.ga.tsp;

import de.uos.inf.ko.ga.graph.Graph;

/**
 * Class representing a tour in a graph.
 * @author Tobias Oelschlägel
 *
 */
public class Tour {

	private final Graph g;
	private final int[] vertices;

	/**
	 * Initializes an empty tour
	 * @param g Graph
	 */
	public Tour(Graph g) {
		this(g, null);
	}

	/**
	 * Creates a copy of a tour.
	 * @param tour Tour to be copied
	 */
	public Tour(Tour tour) {
		this(tour.getGraph(), tour.getVertices());
	}

	/**
	 * Initializes a tour with a given order of the vertices.
	 * @param g Graph
	 * @param vertices Order of the vertices
	 */
	public Tour(Graph g, int[] vertices) {
		this.g = g;

		if (vertices != null) {
			this.vertices = new int[vertices.length];
			System.arraycopy(vertices, 0, this.vertices, 0, vertices.length);
		} else {
			this.vertices = new int[0];
		}
	}

	/**
	 * Computes the total costs of the tour.
	 * @return total sum of costs of edges contained in the tour
	 */
	public double getCosts() {
		double costs = 0.0;
		
		final int n = this.vertices.length;

		for (int i = 0; i < n; ++i) {
			costs += this.g.getEdgeWeight(this.vertices[i], this.vertices[(i + 1) % n]);
		}
		
		return costs;
	}

	/**
	 * Gets the order of the vertices of the tour.
	 * @return order of vertices
	 */
	public int[] getVertices() {
		return this.vertices;
	}

	/**
	 * Gets the graph.
	 * @return graph
	 */
	public Graph getGraph() {
		return this.g;
	}
	
	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		for (int i = 0; i < this.vertices.length; i++){
			s.append(this.vertices[i]);
			s.append(" -> ");
		}
		
		s.append(this.vertices[0]);
		s.append(", Kosten = ");
		s.append(this.getCosts());

		return s.toString();
	}
}
 