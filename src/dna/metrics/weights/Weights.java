package dna.metrics.weights;

import dna.graph.weights.DoubleWeight;
import dna.graph.weights.IntWeight;
import dna.graph.weights.Weight;
import dna.metrics.IMetricNew;
import dna.metrics.MetricNew;
import dna.series.data.BinnedDistributionInt;
import dna.series.data.Distribution;
import dna.series.data.NodeNodeValueList;
import dna.series.data.NodeValueList;
import dna.series.data.Value;
import dna.util.ArrayUtils;
import dna.util.parameters.DoubleParameter;

public abstract class Weights extends MetricNew {

	protected double binSize;

	protected BinnedDistributionInt distr;

	public Weights(String name, MetricType metricType, double binSize) {
		super(name, metricType, new DoubleParameter("BinSize", binSize));
		this.binSize = binSize;
	}

	@Override
	public Value[] getValues() {
		Value avg = new Value("AverageWeight", this.binSize
				* this.distr.computeAverage());
		Value max = new Value("MaxWeight", this.binSize * this.distr.getMax());
		Value min = new Value("MinWeight", this.binSize * this.distr.getMin());
		return new Value[] { avg, max, min };
	}

	@Override
	public Distribution[] getDistributions() {
		return new Distribution[] { this.distr };
	}

	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[] {};
	}

	@Override
	public NodeNodeValueList[] getNodeNodeValueLists() {
		return new NodeNodeValueList[] {};
	}

	@Override
	public boolean equals(IMetricNew m) {
		if (m == null || !(m instanceof Weights)) {
			return false;
		}
		return ArrayUtils.equals(this.distr.getIntValues(),
				((Weights) m).distr.getIntValues(), "Weights.Distribution");
	}

	protected double getWeight(Weight w) {
		if (w instanceof IntWeight) {
			return (double) ((IntWeight) w).getWeight();
		} else if (w instanceof DoubleWeight) {
			return ((DoubleWeight) w).getWeight();
		} else {
			return Double.NaN;
		}
	}

}