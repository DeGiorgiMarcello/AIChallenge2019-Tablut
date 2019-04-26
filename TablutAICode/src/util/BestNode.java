package util;

public class BestNode {
	
	private Node node;
	private double val;
	
	public BestNode(Node node, double val) {
		this.node = node;
		this.val = val;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public double getVal() {
		return val;
	}

	public void setVal(double val) {
		this.val = val;
	}
}
