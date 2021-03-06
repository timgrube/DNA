package dna.graph.generators.zalando;

import dna.graph.datastructures.GraphDataStructure;
import dna.graph.datastructures.zalando.ZalandoGraphDataStructure;
import dna.graph.nodes.Node;
import dna.graph.nodes.zalando.ZalandoNode;

public class CustomersProductsGraphGenerator extends
		ZalandoEqualityGraphGenerator {

	/**
	 * Initializes the {@link CustomersProductsGraphGenerator}.
	 * 
	 * @param gds
	 *            The {@link GraphDataStructure} of the graph to generate.
	 * @param timestampInit
	 *            The time right before start creating the graph.
	 * @param maxNumberOfEvents
	 *            The maximum number of {@code Event}s used for graph
	 *            generation. It is the <u>maximum</u> number because the log
	 *            file may have fewer lines.
	 * @param eventsFilepath
	 *            The full path of the Zalando log file. Will be passed to
	 *            {@link EventReader}.
	 */
	public CustomersProductsGraphGenerator(ZalandoGraphDataStructure gds,
			long timestampInit, int maxNumberOfEvents, String eventsFilepath) {
		super("CustomersProducts", gds, timestampInit, null, maxNumberOfEvents,
				eventsFilepath, new EventColumn[] {
						EventColumn.PERMANENTCOOKIEID, EventColumn.FAMILYSKU },
				true, new EventColumn[] { EventColumn.PERMANENTCOOKIEID,
						EventColumn.FAMILYSKU }, true, false);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <b>Because the "customers products graph" is bipartite, only edges from
	 * products to customers are added, not between customers or between
	 * products.</b>
	 */
	@Override
	void addEdgesForColumns(Event event) {
		int nodeForEventIndex, mappingForColumnGroup;
		nodeForEventIndex = this.mappings.getMapping(
				this.columnGroupsToAddAsNodes[0], event);

		for (EventColumn[] columnGroup : this.columnGroupsToCheckForEquality) {
			mappingForColumnGroup = this.mappings
					.getMapping(columnGroup, event);

			for (int otherNodeIndex : this.nodesSortedByColumnGroupsToCheckForEquality
					.getNodes(mappingForColumnGroup, nodeForEventIndex)) {
				// add edges only between nodes of different columns (bipartite
				// graph)

				final Node nodeForEvent = this.graph.getNode(nodeForEventIndex);
				final Node otherNode = this.graph.getNode(otherNodeIndex);
				if (!ZalandoNode.equalType(nodeForEvent, otherNode))
					this.addBidirectionalEdge(nodeForEvent, otherNode, 1);
			}
		}
	}

}
