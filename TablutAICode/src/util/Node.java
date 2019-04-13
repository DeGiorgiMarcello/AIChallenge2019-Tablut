package util;

import java.util.Map;

import domain.State;

public class Node {
	
	private int depth;
	private Map<Position, PawnClass> state = new java.util.HashMap<Position, PawnClass>();
	private Node parent;
	private int captured;
	private String pawnMoveFrom;
	private String pawnMoveTo;
	
	//root
	public Node() {
		this.state = state; 
		depth = 0;
		captured = 0;
		parent = null;
		pawnMoveFrom = "";
		pawnMoveTo = "";
	}
	
	public Node(int depth,Map state, Node parent,int captured, String pawnMoveFrom, String pawnMoveTo ) {
		this.depth = depth;
		this.state = state;
		this.parent = parent;
		this.pawnMoveFrom = pawnMoveFrom;
		this.pawnMoveTo = pawnMoveTo;
		this.captured = captured;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Map getState() {
		return state;
	}

	public void setState(Map state) {
		this.state = state;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public String getPawnMoveFrom() {
		return pawnMoveFrom;
	}

	public void setPawnMoveFrom(String pawnMoveFrom) {
		this.pawnMoveFrom = pawnMoveFrom;
	}

	public String getPawnMoveTo() {
		return pawnMoveTo;
	}

	public void setPawnMoveTo(String pawnMoveTo) {
		this.pawnMoveTo = pawnMoveTo;
	}

	public int getCaptured() {
		return captured;
	}

	public void setCaptured(int captured) {
		this.captured = captured;
	}
	
	

}
