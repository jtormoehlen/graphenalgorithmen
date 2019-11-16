package de.uos.inf.ko.ga.graph.mst;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import de.uos.inf.ko.ga.graph.Graph;
import de.uos.inf.ko.ga.graph.impl.UndirectedGraphList;

public class Prim {

	/**
	 * Computes a minimum spanning tree of an undirected graph using Prim's algorithm.
	 * Internally this method uses an unsorted list of edges that leave a component.
	 * Each update of one of the edges requires a full scan of the complete list.
	 * @param graph Input graph
	 * @return Minimum spanning tree of the input graph
	 */
	public static Graph minimumSpanningTreeList(Graph graph) {
		assert(graph != null);
		assert(!graph.isDirected());

		final int n = graph.getVertexCount();

		/* create graph to which the edges of the minimum spanning tree will be added */
//		double totalWeight = 0.0;
		final Graph mst = new UndirectedGraphList();
		mst.addVertices(n);

		/* for each vertex 'v': visited[v] == true iff v has been added to some tree */
		int last_unseen = 0;
		final boolean[] visited = new boolean[n];
		
		/* 'outgoing' is a list of edges that leave the current component;
		 * for each neighbor of the component this list contains the cheapest edge */
		final List<WeightedEdge> outgoing = new ArrayList<>();

		while (last_unseen < n) {
			/* find some unseen vertex at which we can start the growth phase */
			if (visited[last_unseen++]) {
				continue;
			}
			
			final int root = last_unseen - 1;
			visited[root] = true;
				
			for (final int v : graph.getNeighbors(root)) {
				outgoing.add(new WeightedEdge(root, v, graph.getEdgeWeight(root, v)));
			}

			/* iterate as long as there are outgoing edges from this component */
			while (!outgoing.isEmpty()) {
				/* find an edge of minimum weight and remove it from the list */
				final int minIdx = findCheapestEdgeInList(outgoing);
				final WeightedEdge edge = outgoing.remove(minIdx);
				final int u = edge.getEnd();

				/* add this edge to the minimum spanning tree */
				visited[u] = true;
//				totalWeight += edge.getWeight();
				
//				 System.out.println("adding edge { " + edge.getStart() + ", " + edge.getEnd() + " } with weight " + edge.getWeight());
				mst.addEdge(edge.getStart(), edge.getEnd(), edge.getWeight());
				
				/* add edges to all neighbors of edge.v that have not been visited and update the existing ones */
				for (final int v : graph.getNeighbors(u)) {
					if (!visited[v]) {
						updateOutgoingEdgeWeight(outgoing, u, v, graph.getEdgeWeight(u, v));
					}
				}
			}
		}
		
//		 System.out.println("total weight: " + totalWeight);

		return mst;
	}

	/**
	 * Determines the index of the edge with the smallest weight inside a list.
	 * @param edgeList List of edges
	 * @return Index of the cheapest edge, -1 if the edge list is empty
	 */
	private static int findCheapestEdgeInList(List<WeightedEdge> edgeList) {
		int min_idx = -1;
		double min_weight = Double.MAX_VALUE;

		int pos = 0;
		for (final WeightedEdge edge : edgeList) {
			if (edge.getWeight() < min_weight) {
				min_idx = pos;
				min_weight = edge.getWeight();
			}

			pos++;
		}

		return min_idx;
	}

	/**
	 * Updates the weight of a cheapest edge to some vertex or inserts the edge if there is no cheaper edge.
	 * Iterates over the complete list and checks whether each edge ends in vertex 'v'. If some edge does,
	 * the weight is replaced if the new weight is lower. If no edge leads to 'v', then a new edge is added
	 * to the list.
	 * @param edgeList List of edges
	 * @param u Vertex inside the component
	 * @param v Vertex outside the component
	 * @param weight Weight of the edge from 'u' to 'v'
	 */
	private static void updateOutgoingEdgeWeight(List<WeightedEdge> edgeList, int u, int v, double weight) {
		/* check whether an edge to 'v' already exists */
		for (WeightedEdge edge : edgeList) {
			if (edge.v == v) {
				/* update the edge weight if necessary */
				if (weight < edge.getWeight()) {
					edge.setStart(u);
					edge.setWeight(weight);
				}

				return;
			}
		}
		
		/* the edge was not found, i.e. 'v' is just becoming reachable via 'u' */
		edgeList.add(new WeightedEdge(u, v, weight));
	}
	
	/**
	 * Computes a minimum spanning tree of an undirected graph using Prim's algorithm.
	 * @param graph Input graph
	 * @return Minimum spanning tree of the input graph
	 */
	public static Graph minimumSpanningTreeHeap(Graph graph) {
		assert(graph != null);
		assert(!graph.isDirected());

		final int n = graph.getVertexCount();

		/* create graph to which the edges of the minimum spanning tree will be added */
//		double totalWeight = 0.0;
		final Graph mst = new UndirectedGraphList();
		mst.addVertices(n);

		/* for each vertex 'v': visited[v] == true iff v has been added to some tree */
		int last_unseen = 0;
		final boolean[] visited = new boolean[n];
		
		final WeightedEdge[] outgoing = new WeightedEdge[n];
		final PriorityQueue<WeightedEdge> outgoingQueue = new PriorityQueue<>(n, new Comparator<WeightedEdge>() {

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

		while (last_unseen < n) {
			/* find some unseen vertex at which we can start the growth phase */
			if (visited[last_unseen++]) {
				continue;
			}
			
			final int root = last_unseen - 1;
			visited[root] = true;
				
			for (final int v : graph.getNeighbors(root)) {
				outgoing[v] = new WeightedEdge(root, v, graph.getEdgeWeight(root, v));
				outgoingQueue.add(outgoing[v]);
			}

			/* iterate as long as there are outgoing edges from this component */
			while (!outgoingQueue.isEmpty()) {
				/* find an edge of minimum weight and remove it from the list */
				final WeightedEdge edge = outgoingQueue.poll();
				final int u = edge.getEnd();

				/* add this edge to the minimum spanning tree */
				visited[u] = true;
//				totalWeight += edge.getWeight();
				
//				 System.out.println("adding edge { " + edge.getStart() + ", " + edge.getEnd() + " } with weight " + edge.getWeight());
				mst.addEdge(edge.getStart(), edge.getEnd(), edge.getWeight());
				
				/* add edges to all neighbors of edge.v that have not been visited and update the existing ones */
				for (final int v : graph.getNeighbors(u)) {
					if (!visited[v]) {
						final double weight = graph.getEdgeWeight(u, v);

						if (outgoing[v] == null) {
							/* found edge that connects new vertex 'v' to the current component */
							outgoing[v] = new WeightedEdge(u, v, weight);
							outgoingQueue.add(outgoing[v]);
						} else if (weight < outgoing[v].getWeight()) {
							/* weight of the existing edge needs to be decreased; remove the old edge from the heap before inserting the new one */
							outgoingQueue.remove(outgoing[v]);
							outgoing[v] = new WeightedEdge(u, v, weight);
							outgoingQueue.add(outgoing[v]);
						}
					}
				}
			}
		}
		
//		 System.out.println("total weight: " + totalWeight);

		return mst;
	}

	private static class WeightedEdge {
		private int u;
		private int v;
		private double weight;
		
		public WeightedEdge(int u, int v, double weight) {
			this.u = u;
			this.v = v;
			this.weight = weight;
		}
		
		public int getStart() {
			return this.u;
		}
		
		public void setStart(int u) {
			this.u = u;
		}
		
		public int getEnd() {
			return this.v;
		}
		
		public double getWeight() {
			return this.weight;
		}
		
		public void setWeight(double weight) {
			this.weight = weight;
		}
	}
}
