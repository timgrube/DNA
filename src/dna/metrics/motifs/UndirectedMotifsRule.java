package dna.metrics.motifs;

import dna.series.data.distributions.DistributionLong;

public interface UndirectedMotifsRule {
	public void execute(DistributionLong motifs, boolean add);
}
