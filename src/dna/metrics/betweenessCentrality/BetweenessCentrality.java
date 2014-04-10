package dna.metrics.betweenessCentrality;

import java.util.Arrays;
import java.util.HashMap;

import dna.graph.Graph;
import dna.graph.IElement;
import dna.graph.nodes.DirectedNode;
import dna.graph.nodes.Node;
import dna.graph.nodes.UndirectedNode;
import dna.metrics.Metric;
import dna.series.data.BinnedDistributionDouble;
import dna.series.data.Distribution;
import dna.series.data.NodeNodeValueList;
import dna.series.data.NodeValueList;
import dna.series.data.Value;
import dna.updates.batch.Batch;

public abstract class BetweenessCentrality extends Metric {

	protected NodeValueList bCC;
	protected BinnedDistributionDouble binnedBC;
	protected double bCSum;
	protected int spSum = 0;
	


	public BetweenessCentrality(String name, ApplicationType type) {
		super(name, type, MetricType.exact);
	}

	@Override
	public void init_() {
		this.bCC = new NodeValueList("BC_Score",
				new double[this.g.getMaxNodeIndex() + 1]);
		this.bCSum = 0d;
		this.spSum=0;
		
		this.binnedBC = new BinnedDistributionDouble("BinnedBCScore", 0.01, new double[100]);
	}

	@Override
	public void reset_() {
		this.bCC = new NodeValueList("BC_Score",
				new double[this.g.getMaxNodeIndex() + 1]);
		this.bCSum = 0d;
		this.spSum=0;
	}

	

	

	@Override
	public Value[] getValues() {
		// Value v1 = new Value("median", getMedian());
		Value v2 = new Value("avg_bc", bCSum / (double) g.getNodeCount());
		return new Value[] { v2 };
	}

	private double getMedian() {
		double[] sortedArray = bCC.getValues();
		Arrays.sort(sortedArray);
		double median;
		if (sortedArray.length % 2 == 0) {
			median = ((double) sortedArray[sortedArray.length / 2] + (double) sortedArray[sortedArray.length / 2 + 1]) / 2;
		} else {
			median = (double) sortedArray[sortedArray.length / 2];
		}
		return median;

	}

	@Override
	public Distribution[] getDistributions() {
		// Distribution d1 = new Distribution("BetweenessCentrality",
		// getDistribution(this.bC));
		
		
		if(spSum==0)
			return new Distribution[]{};
		
		BinnedDistributionDouble bc = new BinnedDistributionDouble("BC-binned", 0.01, new double[100]);
		
		for(double d : this.bCC.getValues()){
			double b = d/spSum;
			System.out.println("bin: " + b);
			bc.incr(b);
		}
		
		return new Distribution[] {bc};

	}

	@Override
	public NodeValueList[] getNodeValueLists() {
//		this.bCC.toString();
		return new NodeValueList[] { this.bCC };
	}

	@Override
	public NodeNodeValueList[] getNodeNodeValueLists() {
		return new NodeNodeValueList[] {};
	}

	private double[] getDistribution(
			HashMap<Node, Double> betweeneesCentralityScore2) {
		double[] temp = new double[betweeneesCentralityScore2.size()];
		int counter = 0;
		for (Node i : betweeneesCentralityScore2.keySet()) {
			temp[counter] = betweeneesCentralityScore2.get(i);
			counter++;
		}
		return temp;
	}

	@Override
	public boolean isComparableTo(Metric m) {
		return m != null && m instanceof BetweenessCentrality;
	}

	@Override
	public boolean isApplicable(Graph g) {
		// currently only on directed graphs
		return DirectedNode.class.isAssignableFrom(g
						.getGraphDatastructures().getNodeType());
		
		
//		return UndirectedNode.class.isAssignableFrom(g.getGraphDatastructures()
//				.getNodeType())
//				|| DirectedNode.class.isAssignableFrom(g
//						.getGraphDatastructures().getNodeType());
	}

	@Override
	public boolean isApplicable(Batch b) {
		// currently only on directed graphs
		return DirectedNode.class.isAssignableFrom(g
				.getGraphDatastructures().getNodeType());

//		return UndirectedNode.class.isAssignableFrom(g.getGraphDatastructures()
//			.getNodeType())
//			|| DirectedNode.class.isAssignableFrom(g
//				.getGraphDatastructures().getNodeType());
	}
	
	@Override
	public boolean equals(Metric m) {
		if (!(m instanceof BetweenessCentrality)) {
			return false;
		}
		boolean success = true;
		BetweenessCentrality bc = (BetweenessCentrality) m;

		// compare bc-score
		for (IElement ie : g.getNodes()) {
			Node n = (Node) ie;
			if (Math.abs(this.bCC.getValue(n.getIndex())
					- bc.bCC.getValue(n.getIndex())) > 0.0001) {
				System.out.println("diff at Node n " + n + " expected Score "
						+ this.bCC.getValue(n.getIndex()) + " is "
						+ bc.bCC.getValue(n.getIndex()));
				success = false;
			}

		}
		return success;
	}

}
