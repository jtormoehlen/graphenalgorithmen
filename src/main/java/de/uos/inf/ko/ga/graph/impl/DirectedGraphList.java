package de.uos.inf.ko.ga.graph.impl;

import java.util.ArrayList;
import java.util.List;

import de.uos.inf.ko.ga.graph.Graph;

/**
 * Implementation of a directed graph with a list representation of the edges.
 * @author Tobias Oelschlägel
 *
 */
public class DirectedGraphList implements Graph {

	/* edge weight that is used if no edge weight is given */
	private static final double DEFAULT_EDGE_WEIGHT = 1.0;
	/* threshold for testing equality of floating point numbers */
	private static final double EPSILON = 0.0001;

	/* number of vertices */
	private int n = 0;
	/* whether the graph is weighted */
	private boolean weighted = false;
	/* weights of the edges, null if an edge is not present */
	private List<List<Edge>> edges = new ArrayList<>();

	@Override
	public void addEdge(int start, int end) {
		this.addEdge(start, end, DEFAULT_EDGE_WEIGHT);
	}

	@Override
	public void addEdge(int start, int end, double weight) {
		if ((start < 0) || (end < 0) || (start >= this.n) || (end >= this.n)) {
			throw new IllegalArgumentException("cannot add edge with an invalid vertex id");
		}

		if (start == end) {
			// throw new IllegalArgumentException("cannot add self-loop");
			return;
		}

		if (Math.abs(weight - DEFAULT_EDGE_WEIGHT) > EPSILON) {
			this.weighted = true;
		}
		
		final Edge edge = new Edge(start, end, weight);
		this.edges.get(start).add(edge);
		this.edges.get(end).add(edge);
	}

	@Override
	public void addVertex() {
		this.addVertices(1);
	}

	@Override
	public void addVertices(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("cannot add a negative number of vertices");
		}

		if (n == 0) {
			return;
		}

		for (int i = 0; i < n; i++) {
			this.edges.add(new ArrayList<>());
		}

		this.n += n;
	}

	@Override
	public List<Integer> getNeighbors(int v) {
		final List<Integer> vertexList = this.getPredecessors(v);
		for (final Integer neighbor : this.getSuccessors(v)) {
			if (!vertexList.contains(neighbor)) {
				vertexList.add(neighbor);
			}
		}
		return vertexList;
	}

	@Override
	public List<Integer> getPredecessors(int v) {
		if ((v < 0) || (v >= this.n)) {
			throw new IllegalArgumentException("cannot get list of predecessors for invalid vertex id");
		}

		final List<Integer> vertexList = new ArrayList<>();
		for (final Edge edge : this.edges.get(v)) {
			if (v == edge.v) {
				vertexList.add(edge.u);
			}
		}
		
		return vertexList;
	}

	@Override
	public List<Integer> getSuccessors(int v) {
		if ((v < 0) || (v >= this.n)) {
			throw new IllegalArgumentException("cannot get list of successors for invalid vertex id");
		}

		final List<Integer> vertexList = new ArrayList<>();
		for (final Edge edge : this.edges.get(v)) {
			if (v == edge.u) {
				vertexList.add(edge.v);
			}
		}
		
		return vertexList;
	}

	@Override
	public int getVertexCount() {
		return this.n;
	}

	@Override
	public double getEdgeWeight(int start, int end) {
		if ((start < 0) || (end < 0) || (start >= this.n) || (end >= this.n)) {
			return Double.POSITIVE_INFINITY;
		}
		
		for (final Edge edge : this.edges.get(start)) {
			if ((start == edge.u) && (end == edge.v)) {
				return edge.weight;
			}
		}
		
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public boolean hasEdge(int start, int end) {
		if ((start < 0) || (end < 0) || (start >= this.n) || (end >= this.n)) {
			return false;
		}
		
		for (final Edge edge : this.edges.get(start)) {
			if ((start == edge.u) && (end == edge.v)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void removeEdge(int start, int end) {
		if ((start < 0) || (end < 0) || (start >= this.n) || (end >= this.n)) {
			throw new IllegalArgumentException("cannot remove edge with an invalid vertex id");
		}

		int pos = 0;
		for (final Edge edge : this.edges.get(start)) {
			if ((start == edge.u) && (end == edge.v)) {
				this.edges.get(start).remove(pos);
				break;
			}

			pos++;
		}
		
		pos = 0;
		for (final Edge edge : this.edges.get(end)) {
			if ((start == edge.u) && (end == edge.v)) {
				this.edges.get(end).remove(pos);
				break;
			}

			pos++;
		}
	}

	@Override
	public void removeVertex() {
		if (this.n <= 0) {
			throw new IllegalStateException("cannot remove vertex from empty graph");
		}

		final int v = this.n - 1;

		/* as long as there exists an edge incident to this vertex, remove it */
		final List<Edge> edgeList = this.edges.get(v);
		while (!edgeList.isEmpty()) {
			final Edge edge = edgeList.get(0);
			this.removeEdge(edge.u, edge.v);
		}

		/* remove the adjacency list */
		this.edges.remove(v);
		this.n--;
	}

	@Override
	public boolean isWeighted() {
		return this.weighted;
	}

	@Override
	public boolean isDirected() {
		return true;
	}

	private static class Edge {
		public int u;
		public int v;
		public double weight;
		
		public Edge(int u, int v, double weight) {
			this.u = u;
			this.v = v;
			this.weight = weight;
		}
	}
}
