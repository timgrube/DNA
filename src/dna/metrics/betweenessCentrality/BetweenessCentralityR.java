package dna.metrics.betweenessCentrality;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import dna.graph.IElement;
import dna.graph.edges.DirectedEdge;
import dna.graph.edges.UndirectedEdge;
import dna.graph.nodes.DirectedNode;
import dna.graph.nodes.Node;
import dna.graph.nodes.UndirectedNode;
import dna.metrics.Metric;
import dna.updates.batch.Batch;
import dna.updates.update.Update;

public class BetweenessCentralityR extends BetweenessCentrality {

	
	public BetweenessCentralityR() {
		super("BetweenessCentralityR", ApplicationType.Recomputation);
	}

	
	@Override
	public void init_() {
		super.init_();
	}
	
	@Override
	public void reset_(){
		super.reset_();
	}
	
	
	@Override
	public boolean applyBeforeBatch(Batch b) {
		return false;
	}

	@Override
	public boolean applyAfterBatch(Batch b) {
		return false;
	}

	@Override
	public boolean applyBeforeUpdate(Update u) {
		return false;
	}

	@Override
	public boolean applyAfterUpdate(Update u) {
		return false;
	}
	
	@Override
	public boolean compute() {
		
		// Stage 0 - global initialization
		this.init_(); 
		
		Queue<Node> q = new LinkedList<Node>();
		Stack<Node> s = new Stack<Node>();

		for (IElement ie : g.getNodes()) {
			Node n = (Node) ie;
			// stage 1 - local initialization
			s.clear();
			q.clear();
			HashMap<Node, HashSet<Node>> p = new HashMap<Node, HashSet<Node>>();
			HashMap<Node, Integer> d = new HashMap<Node, Integer>();
			HashMap<Node, Integer> spc = new HashMap<Node, Integer>();
			HashMap<Node, Double> sums = new HashMap<Node, Double>();

			for (IElement ieE : g.getNodes()) {
				Node t = (Node) ieE;
				if (t == n) {
					d.put(t, 0);
					spc.put(t, 1);
				} else {
					spc.put(t, 0);
					d.put(t, Integer.MAX_VALUE);
				}
				sums.put(t, 0d);
				p.put(t, new HashSet<Node>());
			}

			q.add(n);

			// stage 2 - bfs traversal
				// undirected
			if (DirectedNode.class.isAssignableFrom(this.g
					.getGraphDatastructures().getNodeType())) {
				while (!q.isEmpty()) {
					DirectedNode v = (DirectedNode) q.poll();
					s.push(v);
					for (IElement iEdges : v.getOutgoingEdges()) {
						DirectedEdge edge = (DirectedEdge) iEdges;
						DirectedNode w = edge.getDifferingNode(v);

						if (d.get(w).equals(Integer.MAX_VALUE)) {
							q.add(w);
							d.put(w, d.get(v) + 1);
						}
						if (d.get(w).equals(d.get(v) + 1)) {
							spc.put(w, spc.get(w) + spc.get(v));
							p.get(w).add(v);
						}
					}
				}
				// undirected
			} else if (UndirectedNode.class.isAssignableFrom(this.g
					.getGraphDatastructures().getNodeType())) {
				while (!q.isEmpty()) {
					UndirectedNode v = (UndirectedNode) q.poll();
					s.push(v);

					for (IElement iEdges : v.getEdges()) {
						UndirectedEdge edge = (UndirectedEdge) iEdges;
						UndirectedNode w = edge.getDifferingNode(v);

						if (d.get(w).equals(Integer.MAX_VALUE)) {
							q.add(w);
							d.put(w, d.get(v) + 1);
						}
						if (d.get(w).equals(d.get(v) + 1)) {
							spc.put(w, spc.get(w) + spc.get(v));
							p.get(w).add(v);
						}
					}
				}
			}

			// stage 3 - dependency accumulation
			while (!s.isEmpty()) {
				Node w = s.pop();
				for (Node parent : p.get(w)) {
					double sumForCurretConnection = spc.get(parent) / spc.get(w) * (1 + sums.get(w)) ;
					sums.put(parent, sums.get(parent) + sumForCurretConnection);
				}
				if (w != n) {
					double currentScore = this.bCC.getValue(w.getIndex());
					// this.bC.get(w);
					// this.bC.put(w, currentScore + sums.get(w));
					this.bCC.setValue(w.getIndex(), currentScore + sums.get(w));
					this.bCSum += sums.get(w);
				}
			}
		}
		
		bcdistribution();

		return true;
	}
	
	private void bcdistribution() { // TODO
//		for(double d : this.bCC.getValues()){
//			binnedBC.incr(d);
//		}
		
	}


	@Override
	public boolean equals(Metric m) {
		boolean success =  super.equals(m);
		
		// the bc score is compared in super.equals(m)
		
		return success;
	}

}
