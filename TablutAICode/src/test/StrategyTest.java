package test;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.State;
import domain.State.Pawn;
import domain.StateTablut;
import gui.Gui;
import strategy.Strategy;
import util.BestNode;
import util.Node;
import util.PawnClass;
import util.PawnMap;
import util.Position;

class StrategyTest {
	protected Strategy strategy;
	protected State initState;
	protected Map<Position,PawnClass> state;
	protected Node parent;
	protected PawnClass pawn;
	protected double cost;
	protected int depth,captured;
	protected String moveFrom,moveTo;
	protected Node node;
	
	@BeforeEach
	protected void init() {
		strategy = Strategy.getInstance();
	} 
	

	@Test
	protected void convertCoordinates() {
		Position pos = new Position(3,3); //posizione d4
		assertTrue(strategy.convertCoordinates(pos).equals("d4"));
		Position pos2 = new Position(8,8); //posizione i9
		assertTrue(strategy.convertCoordinates(pos2).equals("i9"));
	}
	
	@BeforeEach
	protected void initMoveTests() {
		initState = new StateTablut();
		PawnMap.getInstance().createMap(initState);
		state = PawnMap.getInstance().getMap();
		parent = new Node();
	}
	/*	
	@Test
	protected void captureVerificationBlack() {
		
		Map<Position,PawnClass> newState = PawnMap.getInstance().cloneState(state);
		
		//Stato di semplice cattura
		
		newState.put(new Position(6,3), new PawnClass(6,3,Pawn.BLACK)); //aggiunto in d7
		newState.put(new Position(6,5), new PawnClass(6,5,Pawn.BLACK)); //aggiunto in f7
		Map<Position,PawnClass> captureVerificationState = PawnMap.getInstance().cloneState(newState);
		strategy.captureVerification(captureVerificationState, new Position(6,5));
		newState.remove(new Position(6,4));
		assertEquals(newState, captureVerificationState);
		
		//Stato di cattura del re non adiacente al castello e fuori dal castello
		Map<Position,PawnClass> newState2 = PawnMap.getInstance().cloneState(state);
		newState2.remove(new Position(4,4));
		newState2.put(new Position(6,6), new PawnClass(6,6,Pawn.KING));
		newState2.put(new Position(7,6), new PawnClass(7,6,Pawn.BLACK));
		newState2.put(new Position(5,6), new PawnClass(5,6,Pawn.BLACK));
		Map<Position,PawnClass> captureVerificationState2 = PawnMap.getInstance().cloneState(newState2);
		strategy.captureVerification(captureVerificationState2, new Position(5,6));
		newState2.remove(new Position(6,6));
		assertEquals(newState2, captureVerificationState2);
		
		//Stato di cattura del re nel castello
		Map<Position,PawnClass> newState3 = PawnMap.getInstance().cloneState(state);
		newState3.remove(new Position(5,4));
		newState3.remove(new Position(3,4));
		newState3.remove(new Position(4,3));
		newState3.remove(new Position(4,5));
		newState3.remove(new Position(4,6));
		newState3.put(new Position(5,4),new PawnClass(5,4,Pawn.BLACK));
		newState3.put(new Position(3,4),new PawnClass(3,4,Pawn.BLACK));
		newState3.put(new Position(4,3),new PawnClass(4,3,Pawn.BLACK));
		newState3.put(new Position(4,5),new PawnClass(4,5,Pawn.BLACK));
		Map<Position,PawnClass> captureVerificationState3 = PawnMap.getInstance().cloneState(newState3);
		strategy.captureVerification(captureVerificationState3, new Position(4,5));
		newState3.remove(new Position(4,4));
		assertEquals(newState3, captureVerificationState3);
		
		//Stato di cattura del re adiacente al castello
		Map<Position,PawnClass> newState4 = PawnMap.getInstance().cloneState(state);
		newState4.remove(new Position(5,4));
		newState4.remove(new Position(6,4));
		newState4.remove(new Position(4,4));
		newState4.put(new Position(5,4), new PawnClass(5,4,Pawn.KING));
		newState4.put(new Position(5,3), new PawnClass(5,3,Pawn.BLACK));
		newState4.put(new Position(5,5), new PawnClass(5,5,Pawn.BLACK));
		newState4.put(new Position(6,4), new PawnClass(6,4,Pawn.BLACK));
		Map<Position,PawnClass> captureVerificationState4 = PawnMap.getInstance().cloneState(newState4);
		strategy.captureVerification(captureVerificationState4, new Position(6,4));
		newState4.remove(new Position(5,4));
		assertEquals(newState4, captureVerificationState4);
	} 
	
	@Test
	protected void captureVerificationWhite() {
		
		Map<Position,PawnClass> newState = PawnMap.getInstance().cloneState(state);
		
		//Stato di semplice cattura
		newState.put(new Position(6,6), new PawnClass(6,6,Pawn.WHITE)); 
		newState.put(new Position(6,5), new PawnClass(6,5,Pawn.BLACK)); 
		Map<Position,PawnClass> captureVerificationState = PawnMap.getInstance().cloneState(newState);
		strategy.captureVerification(captureVerificationState, new Position(6,4));
		newState.remove(new Position(6,5));
		assertEquals(newState, captureVerificationState);
		
		//Cattura con cittadella
		Map<Position,PawnClass> newState2 = PawnMap.getInstance().cloneState(state);
		newState2.put(new Position(7,5), new PawnClass(7,5,Pawn.BLACK)); 
		newState2.put(new Position(6,5), new PawnClass(6,5,Pawn.WHITE)); 
		Map<Position,PawnClass> captureVerificationState2 = PawnMap.getInstance().cloneState(newState2);
		strategy.captureVerification(captureVerificationState2, new Position(6,5));
		newState2.remove(new Position(7,5));
		assertEquals(newState2, captureVerificationState2);
		
	}
	
	/*
	@Test
	protected void moveLeft() {
		pawn = new PawnClass(7,3,Pawn.BLACK); 
		Strategy.getInstance().getNodesList().clear(); //libero la NodeList
		
		Map<Position,PawnClass> state = new HashMap<Position,PawnClass>();
		state.put(new Position(2,2), new PawnClass(2,2,Pawn.KING));
		state.put(new Position(7,2), new PawnClass(7,2,Pawn.WHITE));
		state.put(new Position(7,3), new PawnClass(7,3,Pawn.BLACK));
		parent = new Node(0,state,null,0,null,null);
		
		strategy.moveLeft(pawn, parent);
		ArrayList<Node> nodeList = strategy.getNodesList();
		System.out.println("MOVE LEFT TEST of pawn "+pawn.getType()+" in "+pawn.getRow()+" "+pawn.getColumn());
		for(int i=0;i<nodeList.size();i++) {
			node = nodeList.get(i);
			depth = node.getDepth();
			moveFrom = node.getPawnMoveFrom();
			moveTo = node.getPawnMoveTo();
			captured = node.getCaptured();
			cost = node.getCost();
			System.out.println("Depth: "+depth+" Captured: "+captured+" cost: "+cost+" MoveFrom: "+moveFrom+" MoveTo: "+moveTo);
		}
	}
		
	
	@Test
	protected void moveRight() {
		pawn = new PawnClass(7,3,Pawn.BLACK); 
		Strategy.getInstance().getNodesList().clear(); //libero la NodeList
		
		Map<Position,PawnClass> state = new HashMap<Position,PawnClass>();
		state.put(new Position(2,2), new PawnClass(2,2,Pawn.KING));
		state.put(new Position(7,2), new PawnClass(7,2,Pawn.WHITE));
		state.put(new Position(7,3), new PawnClass(7,3,Pawn.BLACK));
		parent = new Node(0,state,null,0,null,null);
		
		strategy.moveRight(pawn, parent);
		ArrayList<Node> nodeList = strategy.getNodesList();
		System.out.println("MOVE RIGHT TEST of pawn "+pawn.getType()+" in "+pawn.getRow()+" "+pawn.getColumn());
		for(int i=0;i<nodeList.size();i++) {
			node = nodeList.get(i);
			depth = node.getDepth();
			moveFrom = node.getPawnMoveFrom();
			moveTo = node.getPawnMoveTo();
			captured = node.getCaptured();
			cost = node.getCost();
			System.out.println("Depth: "+depth+" Captured: "+captured+" cost: "+cost+" MoveFrom: "+moveFrom+" MoveTo: "+moveTo);
		}
	}
	@Test
	protected void moveUp() {
		pawn = new PawnClass(7,3,Pawn.BLACK); 
		Strategy.getInstance().getNodesList().clear(); //libero la NodeList
		
		Map<Position,PawnClass> state = new HashMap<Position,PawnClass>();
		state.put(new Position(2,2), new PawnClass(2,2,Pawn.KING));
		state.put(new Position(7,2), new PawnClass(7,2,Pawn.WHITE));
		state.put(new Position(7,3), new PawnClass(7,3,Pawn.BLACK));
		parent = new Node(0,state,null,0,null,null);
		
		strategy.moveUp(pawn, parent);
		ArrayList<Node> nodeList = strategy.getNodesList();
		System.out.println("MOVE UP TEST of pawn "+pawn.getType()+" in "+pawn.getRow()+" "+pawn.getColumn());
		for(int i=0;i<nodeList.size();i++) {
			node = nodeList.get(i);
			depth = node.getDepth();
			moveFrom = node.getPawnMoveFrom();
			moveTo = node.getPawnMoveTo();
			captured = node.getCaptured();
			cost = node.getCost();
			System.out.println("Depth: "+depth+" Captured: "+captured+" cost: "+cost+" MoveFrom: "+moveFrom+" MoveTo: "+moveTo);
		}
}
	
	
	@Test
	protected void moveDown() {
		pawn = new PawnClass(7,3,Pawn.BLACK); 
		Strategy.getInstance().getNodesList().clear(); //libero la NodeList
		
		Map<Position,PawnClass> state = new HashMap<Position,PawnClass>();
		state.put(new Position(2,2), new PawnClass(2,2,Pawn.KING));
		state.put(new Position(7,2), new PawnClass(7,2,Pawn.WHITE));
		state.put(new Position(7,3), new PawnClass(7,3,Pawn.BLACK));
		parent = new Node(0,state,null,0,null,null);
		
		strategy.moveDown(pawn, parent);
		ArrayList<Node> nodeList = strategy.getNodesList();
		System.out.println("MOVE DOWN TEST of pawn "+pawn.getType()+" in "+pawn.getRow()+" "+pawn.getColumn());
		for(int i=0;i<nodeList.size();i++) {
			node = nodeList.get(i);
			depth = node.getDepth();
			moveFrom = node.getPawnMoveFrom();
			moveTo = node.getPawnMoveTo();
			captured = node.getCaptured();
			cost = node.getCost();
			System.out.println("Depth: "+depth+" Captured: "+captured+" cost: "+cost+" MoveFrom: "+moveFrom+" MoveTo: "+moveTo);
		}  
	}
	
	
	@Test
	protected void expandNode() {
		
		Map<Position,PawnClass> state = new HashMap<Position,PawnClass>();
		ArrayList<Node> nodeList = strategy.getNodesList();
		nodeList.clear();
		state.put(new Position(2,2), new PawnClass(2,2,Pawn.KING));
		state.put(new Position(7,2), new PawnClass(7,2,Pawn.WHITE));
		state.put(new Position(7,3), new PawnClass(7,3,Pawn.BLACK));
		node = new Node(0,state,null,0,null,null);
		strategy.expandNode(node,Pawn.BLACK);
		System.out.println("EXPAND NODE TEST");
		for(int i=0;i<nodeList.size();i++) {
			node = nodeList.get(i);
			depth = node.getDepth();
			moveFrom = node.getPawnMoveFrom();
			moveTo = node.getPawnMoveTo();
			captured = node.getCaptured();
			cost = node.getCost();
			System.out.println("Depth: "+depth+" Captured: "+captured+" cost: "+cost+" MoveFrom: "+moveFrom+" MoveTo: "+moveTo);
		}
	} 
	
	
	
	@Test 
	protected void generatePartialTree() {
		
		strategy.setPlayer("white");
		ArrayList<Node> nodeList = strategy.getInstance().getNodesList();
		nodeList.clear();
		int count = 0;
		strategy.generatePartialTree();
		for(int i=0;i<nodeList.size();i++) {
			node = nodeList.get(i);
			depth = node.getDepth();
			moveFrom = node.getPawnMoveFrom();
			moveTo = node.getPawnMoveTo();
			System.out.println(" Depth: "+depth+" MoveFrom: "+moveFrom+" MoveTo: "+moveTo);
			count++;
		}
		System.out.println("Count: "+count);
		
		
	} */
	
	/*
	@Test
	protected void alphaBetaTest() {
		
		Map<Position,PawnClass> state1 = new HashMap<Position,PawnClass>();
		state1.put(new Position(2,2), new PawnClass(2,2,Pawn.KING));
		state1.put(new Position(7,2), new PawnClass(7,2,Pawn.WHITE));
		state1.put(new Position(2,1), new PawnClass(2,1,Pawn.BLACK));
		state1.put(new Position(2,7), new PawnClass(2,7,Pawn.WHITE));
		state1.put(new Position(1,2), new PawnClass(1,2,Pawn.WHITE));
		state1.put(new Position(7,3), new PawnClass(7,3,Pawn.BLACK));
		/*Map<Position,PawnClass> state2  = PawnMap.getInstance().cloneState(state1);
		state2.remove(new Position(7,3));
		state2.put(new Position(6,3), new PawnClass(6,3,Pawn.BLACK));*//*
		ArrayList<Node> nodesList = strategy.getNodesList();
		nodesList.clear();
		
		Node node1 = new Node(0,state1,parent,0,"d5","d8");
	//	Node node2 = new Node(2,state2,node1,0,"d8","d7");
	//	nodesList.add(0,node2);
		nodesList.add(node1);  
		strategy.setPlayer("black");
		//nodesList.add(0,parent);
		BestNode bn = strategy.alphaBeta(node1, 0, -500, 500, true);
		Node prova = bn.getNode();
		
		
		while(prova.getDepth() != 1) {
			prova = prova.getParent();
		}
		
		System.out.println("Valore mossa consigliata "+bn.getVal()+"\nMossa consigliata "+prova.getPawnMoveFrom()+"-"+prova.getPawnMoveTo());
	}*/
	/*
	@Test
	protected void whiteWinTest() {
		Map<Position,PawnClass> state1 = new HashMap<Position,PawnClass>();
		state1.put(new Position(2,0), new PawnClass(2,0,Pawn.KING));
		state1.put(new Position(7,2), new PawnClass(7,2,Pawn.WHITE));
		state1.put(new Position(2,1), new PawnClass(2,1,Pawn.BLACK));
		state1.put(new Position(2,7), new PawnClass(2,7,Pawn.WHITE));
		state1.put(new Position(1,2), new PawnClass(1,2,Pawn.WHITE));
		state1.put(new Position(7,3), new PawnClass(7,3,Pawn.BLACK));
		Node node1 = new Node(0,state1,parent,0,"d5","d8");
		
		boolean result = strategy.whiteWin(node1);
		if(result)
			System.out.println("White win");
		else
			System.out.println("White NOT win");
		assertEquals(true, result);
	}
	
	@Test
	protected void blackWinTest() {
		Map<Position,PawnClass> state1 = new HashMap<Position,PawnClass>();
		state1.put(new Position(7,2), new PawnClass(7,2,Pawn.WHITE));
		state1.put(new Position(2,1), new PawnClass(2,1,Pawn.BLACK));
		state1.put(new Position(2,7), new PawnClass(2,7,Pawn.WHITE));
		state1.put(new Position(1,2), new PawnClass(1,2,Pawn.WHITE));
		state1.put(new Position(7,3), new PawnClass(7,3,Pawn.BLACK));
		Node node1 = new Node(0,state1,parent,0,"d5","d8");
		
		boolean result = strategy.blackWin(node1);
		if(result)
			System.out.println("Black NOT win");
		else
			System.out.println("Black win");
		assertEquals(true, result);
	} */
	
	@Test
	protected void prova() {
		Map<Position,PawnClass> state1 = new HashMap<Position,PawnClass>();
		state1.put(new Position(0,2), new PawnClass(0,2,Pawn.BLACK));
		state1.put(new Position(0,3), new PawnClass(0,3,Pawn.BLACK));
		state1.put(new Position(0,8), new PawnClass(0,8,Pawn.BLACK));
		state1.put(new Position(1,2), new PawnClass(1,2,Pawn.BLACK));
		state1.put(new Position(1,8), new PawnClass(1,8,Pawn.BLACK));
		state1.put(new Position(2,6), new PawnClass(2,6,Pawn.KING));
		state1.put(new Position(4,5), new PawnClass(4,5,Pawn.WHITE));
		state1.put(new Position(4,8), new PawnClass(4,8,Pawn.BLACK));
		state1.put(new Position(5,4), new PawnClass(5,4,Pawn.WHITE));
		state1.put(new Position(6,5), new PawnClass(6,5,Pawn.WHITE));
		state1.put(new Position(6,8), new PawnClass(6,8,Pawn.BLACK));
		state1.put(new Position(7,2), new PawnClass(7,2,Pawn.WHITE));
		state1.put(new Position(7,8), new PawnClass(7,8,Pawn.BLACK));
		state1.put(new Position(8,0), new PawnClass(8,0,Pawn.BLACK));
		state1.put(new Position(8,5), new PawnClass(8,5,Pawn.BLACK));
		strategy.setPlayer("white");
		Node node1 = new Node(0,state1,parent,0,"d5","d8");
		//PawnMap.getInstance().printMap(state1);
		
	/*	BestNode bn = strategy.alphaBeta(node1, 0, -500, 500, true);
		Node bestNode = bn.getNode();
		//La mossa migliore è g3-g1 oppure g3-i3
		System.out.println(bestNode.getPawnMoveFrom()+"-"+bestNode.getPawnMoveTo());
		//System.out.println(bn.getVal()); */
	}
}
