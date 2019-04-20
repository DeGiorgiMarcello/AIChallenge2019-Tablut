package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.State;
import domain.StateTablut;
import domain.State.Pawn;
import strategy.Strategy;
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
		pawn = new PawnClass(5,4,Pawn.WHITE); //bianco in e6
	}
	/*
	@Test
	protected void moveLeft() {
		strategy.moveLeft(pawn, parent);
		ArrayList<Node> nodeList = strategy.getNodesList();
		State stato = (State) nodeList.get(0).getState();
		stato.boardString();
		
		
	} */
	
	@Test
	protected void captureVerificationBlack() {
		
		Map<Position,PawnClass> newState = PawnMap.getInstance().cloneState(state);
		
		//Stato di semplice cattura
		
		newState.put(new Position(6,3), new PawnClass(6,3,Pawn.BLACK)); //aggiunto in d7
		newState.put(new Position(6,5), new PawnClass(6,5,Pawn.BLACK)); //aggiunto in f7
		Map<Position,PawnClass> captureVerificationState = strategy.captureVerification(newState, new Position(6,5));
		newState.remove(new Position(6,4));
		
		assertEquals(newState, captureVerificationState);
		
		//Stato di cattura del re non adiacente al castello e fuori dal castello
		Map<Position,PawnClass> newState2 = PawnMap.getInstance().cloneState(state);
		newState2.remove(new Position(4,4));
		newState2.put(new Position(6,6), new PawnClass(6,6,Pawn.KING));
		newState2.put(new Position(7,6), new PawnClass(7,6,Pawn.BLACK));
		newState2.put(new Position(5,6), new PawnClass(5,6,Pawn.BLACK));
		Map<Position,PawnClass> captureVerificationState2 = strategy.captureVerification(newState2, new Position(5,6));
		newState2.remove(new Position(6,6));
		assertEquals(newState2, captureVerificationState2);
		
		//Stato di cattura del re nel castello
		Map<Position,PawnClass> newState3 = PawnMap.getInstance().cloneState(state);
		newState3.remove(new Position(5,4));
		newState3.remove(new Position(3,4));
		newState3.remove(new Position(4,3));
		newState3.remove(new Position(4,5));
		newState3.put(new Position(5,4),new PawnClass(5,4,Pawn.BLACK));
		newState3.put(new Position(3,4),new PawnClass(3,4,Pawn.BLACK));
		newState3.put(new Position(4,3),new PawnClass(4,3,Pawn.BLACK));
		newState3.put(new Position(4,5),new PawnClass(4,5,Pawn.BLACK));
		Map<Position,PawnClass> captureVerificationState3 = strategy.captureVerification(newState3, new Position(4,5));
		newState3.remove(new Position(4,4));
		assertEquals(newState3, captureVerificationState3);
		
		//Stato di cattura del re adiacente al castello
		Map<Position,PawnClass> newState4 = PawnMap.getInstance().cloneState(state);
		newState4.remove(new Position(5,4));
		newState4.remove(new Position(6,4));
		newState4.put(new Position(5,4), new PawnClass(5,4,Pawn.KING));
		newState4.put(new Position(5,3), new PawnClass(5,3,Pawn.BLACK));
		newState4.put(new Position(5,5), new PawnClass(5,5,Pawn.BLACK));
		newState4.put(new Position(6,4), new PawnClass(6,4,Pawn.BLACK));
		Map<Position,PawnClass> captureVerificationState4 = strategy.captureVerification(newState4, new Position(5,4));
		newState4.remove(new Position(5,4));
		assertEquals(newState4, captureVerificationState4);
		
		//C'è da risolvere il bug con le cittadelle
		
	}

}
