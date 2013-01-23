package dynamicGraphs.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import dynamicGraphs.graph.Edge;
import dynamicGraphs.graph.Graph;

public class GraphWriter {
	public static boolean write(Graph g) {
		return GraphWriter.write(g, g.getFilename());
	}

	public static boolean write(Graph g, String filename) {
		return GraphWriter.write(g, filename, true);
	}

	public static boolean write(Graph g, String filename, boolean sortEdges) {
		Writer writer = null;
		try {
			writer = new Writer(filename);

			writer.writeKeyword(Keywords.graphName);
			writer.writeln(g.getName());

			writer.writeKeyword(Keywords.graphNodes);
			writer.writeln(g.getNodes().length);

			writer.writeKeyword(Keywords.graphEdges);
			writer.writeln(g.getEdges().size());

			writer.writeKeyword(Keywords.graphTimestamp);
			writer.writeln(g.getTimestamp());

			writer.writeKeyword(Keywords.graphListOfEdges);
			if (sortEdges) {
				ArrayList<Edge> sorted = new ArrayList<Edge>(new TreeSet<Edge>(
						g.getEdges()));
				for (Edge e : sorted) {
					writer.writeln(e.getStringRepresentation());
				}
			} else {
				for (Edge e : g.getEdges()) {
					writer.writeln(e.getStringRepresentation());
				}
			}

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
}
