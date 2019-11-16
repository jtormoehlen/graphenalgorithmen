package de.uos.inf.ko.ga.graph.impl;

import java.util.ArrayList;
import java.util.List;

import de.uos.inf.ko.ga.graph.Graph;

/**
 * Implementation of a undirected graph with a matrix representation of the edges.
 * A symmetric graph can be stored as a linear array of size n*(n - 1)/2 if only
 * the lower or upper triangular portion of the adjacency matrix is retained (without
 * the diagonal). In this implementation only the lower triangular portion is stored.
 *
 * The entries of the array are (a_{10}, a_{20}, a_{21}, a_{30}, a_{31}, a_{32}, ... ),
 * i.e. (using zero-indexing), the position of entry a_{ij} is
 *   j + \sum_{k = 0}^{i - 1} k = j + i*(i - 1)/2
 * By using the lower triangular part, adding/removing the last vertex is simply resizing
 * the array. If the upper triangular part is used, the addition/removal of a vertex
 * requires a complete rearrangement of the entries. As Java does not allow dynamic
 * resizing, we need to copy the matrix anyway, but rearrangement is not necessary.
 * @author Tobias Oelschlägel
 *
 */
public class UndirectedGraphMatrix implements Graph {

	/* edge weight that is used if no edge weight is given */
	private static final double DEFAULT_EDGE_WEIGHT = 1.0;
	/* threshold for testing equality of floating point numbers */
	private static final double EPSILON = 0.0001;

	/* number of vertices */
	private int n = 0;
	/* whether the graph is weighted */
	private boolean weighted = false;
	/* weights of the edges, null if an edge is not present */
	private Double[] weights = new Double[0];

	private static int pos(int start, int end) {
		final int u = Math.max(start, end);
		final int v = Math.min(start, end);
		return v + u * (u - 1) / 2;
	}
	
	private static int matrixSize(int n) {
		return n * (n + 1) / 2;
	}

	@Override
	public void addEdge(int start, int end) {
		this.addEdge(start, end, DEFAULT_EDGE_WEIGHT);
	}

	@Override
	public void addEdge(int start, int end, double weight) {
		if ((start < 0) || (start >= this.n) || (end < 0) || (end >= this.n)) {
			throw new IllegalArgumentException("cannot add edge with invalid vertex ids");
		}
		
		if (start == end) {
			// throw new IllegalArgumentException("cannot add self-loop");
			return;
		}

		this.weights[pos(start, end)] = weight;
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
		final int oldSize = matrixSize(this.n);
		final int newSize = matrixSize(this.n + n);

		Double[] newWeights = new Double[newSize];
		for (int i = 0; i < oldSize; ++i) {
			newWeights[i] = this.weights[i];
		}
		
		for (int i = oldSize; i < newSize; ++i) {
			newWeights[i] = null;
		}

		this.n += n;
		this.weights = newWeights;
	}

	@Override
	public List<Integer> getNeighbors(int v) {
		final List<Integer> list = new ArrayList<>();

		for (int u = 0; u < this.n; ++u) {
			if (u != v) {
				if (this.hasEdge(u, v)) {
					list.add(u);
				}
			}
		}
		
		return list;
	}

	@Override
	public List<Integer> getPredecessors(int v) {
		return this.getNeighbors(v);
	}

	@Override
	public List<Integer> getSuccessors(int v) {
		return this.getNeighbors(v);
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
		
		if (start == end) {
			return Double.POSITIVE_INFINITY;
		}
		
		final int idx = pos(start, end);
		return (this.weights[idx] != null) ? this.weights[idx] : Double.POSITIVE_INFINITY;
	}

	@Override
	public boolean hasEdge(int start, int end) {
		if ((start < 0) || (start >= this.n) || (end < 0) || (end >= this.n)) {
			return false;
		}
		
		if (start == end) {
			return false;
		}

		final int idx = pos(start, end);
		return (this.weights[idx] != null);
	}

	@Override
	public void removeEdge(int start, int end) {
		if ((start < 0) || (start >= this.n) || (end < 0) || (end >= this.n)) {
			throw new IllegalArgumentException("cannot remove edge with invalid vertex ids");
		}

		final int idx = pos(start, end);
		this.weights[idx] = null;
	}

	@Override
	public void removeVertex() {
		if (this.n <= 0) {
			throw new IllegalStateException("cannot remove vertex from empty graph");
		}

		this.n--;
		Double[] newWeights = new Double[matrixSize(this.n)];
		for (int i = 0; i < matrixSize(this.n); ++i) {
			newWeights[i] = this.weights[i];
		}

		this.weights = newWeights;
	}

	@Override
	public boolean isWeighted() {
		return this.weighted;
	}

	@Override
	public boolean isDirected() {
		return false;
	}

}
