package test;

import static org.junit.Assert.assertTrue;

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
	protected void captureVerificationWhite() {
		System.out.println(state.toString());
		strategy.captureVerification(state, new Position(3,4)); //white
		
		State newState = initState.clone();
		newState.
		
	}

}
