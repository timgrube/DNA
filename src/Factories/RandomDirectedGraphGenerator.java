package Factories;

import DataStructures.GraphDataStructure;
import Graph.DirectedEdge;
import Graph.Graph;
import Graph.Node;
import Utils.Rand;
import Utils.parameters.Parameter;

public class RandomDirectedGraphGenerator extends DirectedGraphGenerator {

	public RandomDirectedGraphGenerator(String name, long timestampInit, Parameter[] params,
			GraphDataStructure gds,
			int nodesInit, int edgesInit) {
		super(name, params, gds, timestampInit, nodesInit, edgesInit);
	}

	@Override
	public Graph generate() {
		Graph graph = this.newGraphInstance();

		for (int i = 0; i < this.nodesInit; i++) {
			Node node = this.gds.newNodeInstance(i);
			graph.addNode(node);
		}

		while (graph.getEdgeCount() < this.edgesInit) {
			int src = Rand.rand.nextInt(graph.getNodeCount());
			int dst = Rand.rand.nextInt(graph.getNodeCount());
			if (src != dst) {
				DirectedEdge edge = (DirectedEdge) this.gds.newEdgeInstance(graph.getNode(src), graph.getNode(dst));
				graph.addEdge(edge);
				edge.getSrc().addEdge(edge);
				edge.getDst().addEdge(edge);
			}
		}

		return graph;
	}

}
