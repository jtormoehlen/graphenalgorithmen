package de.uos.inf.ko.ga.graph.impl;

import java.util.ArrayList;
import java.util.List;

import de.uos.inf.ko.ga.graph.Graph;

/**
 * Implementation of a directed graph with a matrix representation of the edges.
 * @author Tobias Oelschlägel
 *
 */
public class DirectedGraphMatrix implements Graph {

	/* edge weight that is used if no edge weight is given */
	private static final double DEFAULT_EDGE_WEIGHT = 1.0;
	/* threshold for testing equality of floating point numbers */
	private static final double EPSILON = 0.0001;

	/* number of vertices */
	private int n = 0;
	/* whether the graph is weighted */
	private boolean weighted = false;
	/* weights of the edges, null if an edge is not present */
	private Double[][] weights = new Double[0][0];
 
	@Override
	public void addEdge(int start, int end) {
		this.addEdge(start, end, DEFAULT_EDGE_WEIGHT);
	}

	@Override
	public void addEdge(int start, int end, double weight) {
		if ((start < 0) || (start >= this.n) || (end < 0) || (end >= this.n)) {
			throw new IllegalArgumentException("cannot add edge with invalid vertex id");
		}

		if (start == end) {
			// throw new IllegalArgumentException("cannot add self-loop");
			return;
		}

		this.weights[start][end] = weight;

		/* if an edge with non-default edge weight is added, then the graph becomes weighted */
		if (Math.abs(weight - DEFAULT_EDGE_WEIGHT) > EPSILON) {
			this.weighted = true;
		}
	}

	@Override
	public void addVertex() {
		this.addVertices(1);
	}
	
	@Override
	public void addVertices(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("cannot add a negative number of vertices");
		} else if (n == 0) {
			return;
		}

		/* copy existing weights into new array of greater size */
		Double[][] newWeights = new Double[this.n + n][this.n + n];
		for (int i = 0; i < this.n; ++i) {
			for (int j = 0; j < this.n; ++j) {
				newWeights[i][j] = this.weights[i][j];
			}
		}
		
		/* replace the old adjacency matrix */
		this.n += n;
		this.weights = newWeights;
	}

	@Override
	public List<Integer> getNeighbors(int v) {
		if ((v < 0) || (v >= this.n)) {
			throw new IllegalArgumentException("cannot determine neighbors of invalid vertex");
		}

		final List<Integer> neighbors = new ArrayList<>();
		for (int i = 0; i < this.n; ++i) {
			if (this.hasEdge(i, v) || this.hasEdge(v, i)) {
				neighbors.add(i);
			}
		}

		return neighbors;
	}

	@Override
	public List<Integer> getPredecessors(int v) {
		if ((v < 0) || (v >= this.n)) {
			throw new IllegalArgumentException("cannot determine predecessors of invalid vertex");
		}

		final List<Integer> neighbors = new ArrayList<>();
		for (int i = 0; i < this.n; ++i) {
			if (this.hasEdge(i, v)) {
				neighbors.add(i);
			}
		}
		
		return neighbors;
	}

	@Override
	public List<Integer> getSuccessors(int v) {
		if ((v < 0) || (v >= this.n)) {
			throw new IllegalArgumentException("cannot determine successors of invalid vertex");
		}
		
		final List<Integer> neighbors = new ArrayList<>();
		for (int i = 0; i < this.n; ++i) {
			if (this.hasEdge(v, i)) {
				neighbors.add(i);
			}
		}
		
		return neighbors;
	}

	@Override
	public int getVertexCount() {
		return this.n;
	}

	@Override
	public double getEdgeWeight(int start, int end) {
		if ((start < 0) || (start >= this.n) || (end < 0) || (end >= this.n)) {
			return Double.POSITIVE_INFINITY;
		}
		
		return (this.weights[start][end] != null) ? this.weights[start][end] : Double.POSITIVE_INFINITY;
	}

	@Override
	public boolean hasEdge(int start, int end) {
		if ((start < 0) || (start >= this.n) || (end < 0) || (end >= this.n)) {
			return false;
		}
		
		return (this.weights[start][end] != null);
	}

	@Override
	public void removeEdge(int start, int end) {
		if ((start < 0) || (start >= this.n) || (end < 0) || (end >= this.n)) {
			throw new IllegalArgumentException("cannot remove edge with invalid vertex id");
		}
		
		this.weights[start][end] = null;
	}

	@Override
	public void removeVertex() {
		if (this.n <= 0) {
			throw new IllegalStateException("cannot remove vertex from empty graph");
		}
		
		this.n--;
		Double[][] newWeights = new Double[this.n][this.n];
		for (int i = 0; i < this.n; ++i) {
			for (int j = 0; j < this.n; ++j) {
				newWeights[i][j] = this.weights[i][j];
			}
		}

		this.weights = newWeights;
	}

	@Override
	public boolean isWeighted() {
		return this.weighted;
	}

	@Override
	public boolean isDirected() {
		return true;
	}

}
