package util;

public class BestNode {
	
	private Node node;
	private double val;
	private double alfa;
	private double beta;
	private double alfaBetaVal;
	
	public BestNode(Node node, double val) {
		this.node = node;
		this.val = val;
	}
	
	public BestNode(Node node, double val, double alfa, double beta,double alfaBetaVal) {
		this.node = node;
		this.val = val;
		this.alfa = alfa;
		this.beta = beta;
		this.alfaBetaVal = alfaBetaVal;
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

	public double getAlfa() {
		return alfa;
	}

	public void setAlfa(double alfa) {
		this.alfa = alfa;
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	public double getAlfaBetaVal() {
		return alfaBetaVal;
	}

	public void setAlfaBetaVal(double alfaBetaVal) {
		this.alfaBetaVal = alfaBetaVal;
	}
}
