package de.uos.inf.ko.ga.graph.converter;

import de.uos.inf.ko.ga.graph.Graph;
import de.uos.inf.ko.ga.graph.impl.DirectedGraphList;
import de.uos.inf.ko.ga.graph.impl.DirectedGraphMatrix;
import de.uos.inf.ko.ga.graph.impl.UndirectedGraphList;
import de.uos.inf.ko.ga.graph.impl.UndirectedGraphMatrix;

/**
 * Methods for converting between list and matrix representations of graphs.
 * @author Tobias Oelschlägel
 */
public class GraphConverter {

	/**
	 * Constructs a graph represented by adjacency lists.
	 * Outputs an instance of DirectedGraphList if the input graph is directed, and outputs an instance of UndirectedGraphList otherwise.
	 * The weights of the edges of the new graph are the same as the ones of the input graph.
	 * @param graph Graph to be converted
	 * @return graph Graph represented by adjacency list
	 */
	public static Graph toList(Graph graph) {
		if (graph.isDirected()) {
			return copyDirectedGraph(graph, new DirectedGraphList());
		} else {
			return copyUndirectedGraph(graph, new UndirectedGraphList());
		}
	}

	/**
	 * Constructs a graph represented by an adjacency matrix.
	 * Outputs an instance of DirectedGraphMatrix if the input graph is directed, and outputs an instance of UndirectedGraphMatrix otherwise.
	 * The weights of the edges of the new graph are the same as the ones of the input graph.
	 * @param graph Graph to be converted
	 * @return graph Graph represented by an adjacency matrix
	 */
	public static Graph toMatrix(Graph graph) {
		if (graph.isDirected()) {
			return copyDirectedGraph(graph, new DirectedGraphMatrix());
		} else {
			return copyUndirectedGraph(graph, new UndirectedGraphMatrix());
		}
	}
	
	private static Graph copyDirectedGraph(Graph input, Graph output) {
		output.addVertices(input.getVertexCount());
		
		for (int u = 0; u < input.getVertexCount(); ++u) {
			for (Integer v : input.getSuccessors(u)) {
				final double weight = input.getEdgeWeight(u, v);
				output.addEdge(u, v, weight);
			}
		}

		return output;
	}
	
	private static Graph copyUndirectedGraph(Graph input, Graph output) {
		output.addVertices(input.getVertexCount());
		
		for (int u = 0; u < input.getVertexCount(); ++u) {
			for (Integer v : input.getNeighbors(u)) {
				if (u < v) {
					/* only consider each edge once */
					final double weight = input.getEdgeWeight(u, v);
					output.addEdge(u, v, weight);
				}
			}
		}
		
		return output;
	}

}
