package de.uos.inf.ko.ga.graph.shortestpath;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import de.uos.inf.ko.ga.graph.Graph;
import de.uos.inf.ko.ga.graph.impl.DirectedGraphList;

/**
 * Implementation of Dijkstra for computing distances from some vertex
 * to all other vertices in a directed weighted graph that does not
 * contain cycles of negative length.
 * @author Tobias Oelschlägel
 *
 */
public class Dijkstra {

	private static final double EPS = 0.0001;

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

		final boolean seen[] = new boolean[n];
		final double dist[] = new double[n];

		/* list of edges going out of the set of visited vertices */
		final WeightedEdge[] outgoing = new WeightedEdge[n];
		
		/* priority queue sorted by the weights of the edges in ascending order */
		final PriorityQueue<WeightedEdge> queueOutgoing = new PriorityQueue<>(n, new Comparator<WeightedEdge>() {
			@Override
			public int compare(WeightedEdge o1, WeightedEdge o2) {
				if (o1.getWeight() < o2.getWeight()) {
					return -1;
				} else if (o1.getWeight() > o2.getWeight()) {
					return 1;
				}

				return 0;
			}
		});

		for (int v = 0; v < n; ++v) {
			dist[v] = Double.POSITIVE_INFINITY;
		}
		
		seen[start] = true;
		dist[start] = 0.0;
		
		/* the start vertex is visited first; insert all outgoing edges into the priority queue */
		for (int v : graph.getSuccessors(start)) {
			final double distance = graph.getEdgeWeight(start, v);
			outgoing[v] = new WeightedEdge(start, v, distance);
			queueOutgoing.add(outgoing[v]);
		}

		while (!queueOutgoing.isEmpty()) {
			/* get the cheapest edge that leaves the set of visited vertices */
			final WeightedEdge edge = queueOutgoing.poll();
			final int u = edge.getStart();
			final int v = edge.getEnd();

			/* the shortest path from 'start' to 'v' passes through 'u' */
			seen[v] = true;
			dist[v] = dist[u] + graph.getEdgeWeight(u, v);

			/* test whether the distances to the successors of 'v' can be updated */
			for (int w : graph.getSuccessors(v)) {
				if (!seen[w]) {
					/* vertex 'w' has not been visited yet */
					final double weight = graph.getEdgeWeight(v, w);
					final double distance = dist[v] + weight;

					if (outgoing[w] != null) {
						/* vertex 'w' is aleady reachable via some other vertex */
						if (distance < outgoing[w].getWeight()) {
							/* path from 'start' to 'v' to 'w' is shorter than the path that was known before */
							queueOutgoing.remove(outgoing[w]);
							outgoing[w] = new WeightedEdge(v, w, distance);
							queueOutgoing.add(outgoing[w]);
						}
					} else {
						/* vertex 'w' is currently only reachable via 'u'; add a new entry to the priority queue */
						outgoing[w] = new WeightedEdge(v, w, distance);
						queueOutgoing.add(outgoing[w]);
					}
				}
			}
		}

		return dist;
	}
	
	/**
	 * Constructs a directed shortest-path tree from the distances of a shortest-path computation.
	 * This method determines the edges used by shortest paths between the start vertex and
	 * the remaining vertices. At first, it adds all edges outgoing from the start vertex, and
	 * then does this for the newly visited vertices, and so on.
	 * @param graph Input graph
	 * @param start Start vertex
	 * @param dist distances of the vertices from the start vertex
	 * @return Graph containing edges of the shortest paths starting at the start vertex
	 */
	public static Graph shortestPathGraphFromDistances(Graph graph, int start, double dist[]) {
		final int n = graph.getVertexCount();

		/* construct new graph with the same set of vertices */
		final Graph shortestPathTree = new DirectedGraphList();
		shortestPathTree.addVertices(n);

		/* queue containing the vertices that have been seen yet */
		final boolean seen[] = new boolean[n];
		final Queue<Integer> queue = new LinkedList<>();

		/* add the start vertex to the queue */
		seen[start] = true;
		queue.add(start);
		
		while (!queue.isEmpty()) {
			final int u = queue.poll();

			/* determine the neighbors of 'u' that are reachable from 'start' via 'u' in a shortest path */
			for (final int v : graph.getSuccessors(u)) {
				if (!seen[v]) {
					/* test whether the value dist[v] obtained its value from the value dist[u] */
					if (Math.abs(dist[u] + graph.getEdgeWeight(u, v) - dist[v]) <= EPS) {
						seen[v] = true;
						shortestPathTree.addEdge(u, v, graph.getEdgeWeight(u, v));
						queue.add(v);
					}
				}
			}
		}
		
		return shortestPathTree;
	}

	private static class WeightedEdge {
		private final int u;
		private final int v;
		private final double weight;
		
		public WeightedEdge(int u, int v, double weight) {
			this.u = u;
			this.v = v;
			this.weight = weight;
		}
		
		public int getStart() {
			return this.u;
		}
		
		public int getEnd() {
			return this.v;
		}
		
		public double getWeight() {
			return this.weight;
		}
	}
}
