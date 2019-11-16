package de.uos.inf.ko.ga.graph.impl;

import java.util.ArrayList;
import java.util.List;

import de.uos.inf.ko.ga.graph.Graph;

/**
 * Implementation of an undirected graph with a list representation of the edges.
 * @author Tobias Oelschlägel
 *
 */
public class UndirectedGraphList implements Graph {

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

		this.edges.get(start).add(new Edge(end, weight));
		this.edges.get(end).add(new Edge(start, weight));
	}

	@Override
	public void addVertex() {
		this.addVertices(1);
	}

	@Override
	public void addVertices(int n) {
		this.n += n;
		for (int i = 0; i < n; ++i) {
			this.edges.add(new ArrayList<>());
		}
	}

	@Override
	public List<Integer> getNeighbors(int v) {
		final List<Integer> list = new ArrayList<>();
		for (final Edge edge : this.edges.get(v)) {
			list.add(edge.v);
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
		if ((start < 0) || (end < 0) || (start >= this.n) || (end >= this.n) || (start == end)) {
			return Double.POSITIVE_INFINITY;
		}

		for (final Edge edge : this.edges.get(start)) {
			if (edge.v == end) {
				return edge.weight;
			}
		}
		
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public boolean hasEdge(int start, int end) {
		if ((start < 0) || (end < 0) || (start >= this.n) || (end >= this.n) || (start == end)) {
			return false;
		}

		for (final Edge edge : this.edges.get(start)) {
			if (edge.v == end) {
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
		
		if (start == end) {
			throw new IllegalArgumentException("cannot remove self-loop");
		}
		
		this.edges.get(start).removeIf(e -> e.v == end);
		this.edges.get(end).removeIf(e -> e.v == start);
	}

	@Override
	public void removeVertex() {
		if (this.n <= 0) {
			throw new IllegalStateException("cannot remove vertex from empty graph");
		}

		final int v = this.n - 1;
		final List<Edge> edges = this.edges.get(v); 
		while (!edges.isEmpty()) {
			final Edge edge = edges.get(0);
			this.removeEdge(v, edge.v);
		}
		
		this.n--;
		this.edges.remove(this.n);
	}

	@Override
	public boolean isWeighted() {
		return this.weighted;
	}

	@Override
	public boolean isDirected() {
		return false;
	}

	private static class Edge {
		public int v;
		public double weight;
		
		public Edge(int v, double weight) {
			this.v = v;
			this.weight = weight;
		}
	}
}
