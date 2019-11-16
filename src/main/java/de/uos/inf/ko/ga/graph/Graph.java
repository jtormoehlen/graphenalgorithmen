package de.uos.inf.ko.ga.graph;

import java.util.List;

/**
 * Interface defining the operations a graph must provide
 * 
 * @author Mareike Paul
 * @version 15.10.2012
 */
public interface Graph {

	/**
	 * Method to add the edge from start to end to the graph.
	 * Adding self-loops is not allowed.
	 * @param start start vertex
	 * @param end   end vertex
	 */
	public void addEdge(int start, int end);

	/**
	 * Method to add the edge with the given weight to the graph from start to end.
	 * Adding self-loops is not allowed.
	 * @param start  number of start vertex
	 * @param end    number of end vertex
	 * @param weight weight of edge
	 */
	public void addEdge(int start, int end, double weight);

	/**
	 * Method to add a vertex to the graph.
	 */
	public void addVertex();
	
	/**
	 * Method to add multiple vertices to the graph.
	 * @param n number of vertices to add
	 */
	public void addVertices(int n);

	/**
	 * Returns all neighbors of the given vertex v (all vertices i with {i,v} in E or (i,v) or (v,i) in E).
	 * @param v vertex whose neighbors shall be returned
	 * @return List of vertices adjacent to v
	 */
	public List<Integer> getNeighbors(int v);

	/**
	 * Returns a list containing all predecessors of v. In an undirected graph all neighbors are returned.
	 * @param v vertex id
	 * @return list containing all predecessors of v
	 */
	public List<Integer> getPredecessors(int v);

	/**
	 * Returns a list containing all successors v. In an undirected graph all neighbors are returned.
	 * @param v vertex id
	 * @return list containing all edges starting in v
	 */
	public List<Integer> getSuccessors(int v);

	/**
	 * Method to get the number of vertices.
	 * @return number of vertices
	 */
	public int getVertexCount();

	/**
	 * Method to get the weight of the edge {start, end} / the arc (start, end).
	 * @param start start vertex of edge / arc
	 * @param end   end vertex of edge / arc
	 * @return Double.POSITIVE_INFINITY, if the edge does not exist, c_{start, end} otherwise
	 */
	public double getEdgeWeight(int start, int end);

	/**
	 * Method to test whether the graph contains the edge {start, end} / the arc (start, end).
	 * @param start start vertex of the edge
	 * @param end   end vertex of the edge
	 */
	public boolean hasEdge(int start, int end);

	/**
	 * Method to remove an edge from the graph, defined by the vertices start and end.
	 * @param start start vertex of the edge
	 * @param end   end vertex of the edge
	 */
	public void removeEdge(int start, int end);

	/**
	 * Method to remove the last vertex from the graph.
	 */
	public void removeVertex();

	/**
	 * Returns whether the graph is weighted.
	 * A graph is weighted if an edge with weight different from 1.0 has been added to the graph.
	 * @return true if the graph is weighted
	 */
	public boolean isWeighted();

	/**
	 * Returns whether the graph is directed.
	 * @return true if the graph is directed
	 */
	public boolean isDirected();

}
