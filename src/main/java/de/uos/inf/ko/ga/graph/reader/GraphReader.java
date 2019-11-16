package de.uos.inf.ko.ga.graph.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import de.uos.inf.ko.ga.graph.Graph;
import de.uos.inf.ko.ga.graph.impl.DirectedGraphMatrix;
import de.uos.inf.ko.ga.graph.impl.UndirectedGraphMatrix;

public class GraphReader {

	public static Graph readDirectedGraph(File f) throws NumberFormatException, IOException {
		final Graph graph = new DirectedGraphMatrix();
		final BufferedReader reader = new BufferedReader(new FileReader(f));

		try {
			String s;
			while ((s = reader.readLine()) != null) {
				/* remove leading and trailing spaces */
				s = s.trim();
	
				if (s.startsWith("#") || s.isEmpty()) {
					continue;
				}
	
				if (s.startsWith("n")) {
					/* FORMAT: n <Anzahl Knoten> */
					int n = -1;
					s = s.replace("n", "").trim();
					n = Integer.parseInt(s);
					
					graph.addVertices(n);
				} else {
					// unterteile in Startknoten und Rest
					String[] split = s.split(":");
					// lese die ID des Startknotens aus
					String idstart = split[0].trim();
					int ids = Integer.parseInt(idstart);
	
					// splitte den Rest und durchlaufe ihn
					// erzeuge Kanten mit gegebenen Kosten
					split = split[1].trim().split("\\s+");
					for (int i = 0; i < split.length; i++) {
						if (!"x".equals(split[i])) {
							double c_se = Double.parseDouble(split[i]);
							graph.addEdge(ids, i, c_se);
						}
					}
				}
			}
		} finally {
			reader.close();
		}

		return graph;
	}

	/**
	 * Converts the content of the given file to an abstract undirected graph
	 * 
	 * @param f File with the content to be converted
	 * @return resulting abstract undirected graph
	 * @throws NumberFormatException thrown in case that the file contains letters
	 *                               in case a number is expected
	 * @throws IOException           thrown in case of an input error
	 */
	public static Graph readUndirectedGraph(File f) throws NumberFormatException, IOException {
		Graph graph = new UndirectedGraphMatrix();
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(f));
		String s;
		while ((s = reader.readLine()) != null) {
			// schneide vorne und hinten Leerzeichen ab
			s = s.trim();
			if (!s.startsWith("#") && !s.isEmpty()) {
				// lese n ein
				if (s.startsWith("n")) {
					int n = -1;
					s = s.replace("n", "").trim();
					n = Integer.parseInt(s);
					graph.addVertices(n);
				} else {
					// unterteile in Startknotenund Rest
					String[] split = s.split(":");
					// lese die ID des Startknotens aus
					String idstart = split[0].trim();
					int ids = Integer.parseInt(idstart);

					// splitte den Rest unddurchlaufe ihn
					// erzeuge Kanten mit gegebenen Kosten
					split = split[1].trim().split("\\s+");
					for (int i = 0; i < split.length; i++) {
						if (!"x".equals(split[i])) {
							double c_se = Double.parseDouble(split[i]);
							
							if (ids < i) {
								graph.addEdge(ids, i, c_se);
							}
						}
					}
				}
			}
		}
		reader.close();
		return graph;
	}

}
