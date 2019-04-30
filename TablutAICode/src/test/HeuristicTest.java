package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.State;
import domain.State.Pawn;
import domain.StateTablut;
import strategy.Heuristic;
import util.Node;
import util.PawnClass;
import util.PawnMap;
import util.Position;

class HeuristicTest {

	Node root;
	Node child;
	State state;
	
	
	/*@BeforeEach
	void setUp() throws Exception {
		state = new StateTablut();
		PawnMap.getInstance().createMap(state); 
		root = new Node();
		child = new Node(1, root.cloneState(), root, 1, "", "");
		//per testare cattura pedina nera
		//child.getState().remove(new Position(1,4));
		
		//per testare cattura pedina bianca
		//child.getState().remove(new Position(2,4));
		
		//per testare re protetto su 3 lati
		/*child.getState().remove(new Position(3,4));
		//caso nero
		child.getState().get(new Position(4,5)).setType(Pawn.BLACK);
		child.getState().get(new Position(5,4)).setType(Pawn.BLACK);
		child.getState().get(new Position(4,3)).setType(Pawn.BLACK);*/
		
		//per testare re protetto su 2 lati
		/*child.getState().remove(new Position(3,4));
		child.getState().remove(new Position(4,3));*/
		
		//per testare re protetto su 1 lati
		/*child.getState().remove(new Position(3,4));
		child.getState().remove(new Position(4,3));
		child.getState().remove(new Position(4,5));*/
		
		//per testare re protetto su 0 lati
		/*child.getState().remove(new Position(3,4));
		child.getState().remove(new Position(4,3));
		child.getState().remove(new Position(4,5));
		child.getState().remove(new Position(5,4));*/
	//}

	/*@AfterEach
	void tearDown() throws Exception {
	}*/

	/*@Test
	void testNumberOfPawnCaptured() {
		System.out.println(PawnMap.getInstance().getMap().size());
		Pawn white = Pawn.WHITE;
		//Pawn black = Pawn.BLACK;
		int number = Heuristic.getInstance().numberOfPawnCaptured(child, white);
		assertEquals(1, number);
	}*/
	
	/*
	@Test
	void testKingProtected() {
		Position king = new Position(4,4);
		//Pawn pawn = Pawn.WHITE;
		Pawn black = Pawn.BLACK;
		int[] kingProtected = Heuristic.getInstance().kingProtected(child,king , black);
		//re protetto su 4 lati da pedine bianche
		assertEquals(0, kingProtected[0]);
		assertEquals(0, kingProtected[1]);
		assertEquals(0, kingProtected[2]);
		assertEquals(1, kingProtected[3]);
		
		//re protetto su 3 lati da pedine bianche
		assertEquals(0, kingProtected[0]);
		assertEquals(0, kingProtected[1]);
		assertEquals(1, kingProtected[2]);
		assertEquals(0, kingProtected[3]);
		
		//re protetto su 2 lati da pedine bianche
		assertEquals(0, kingProtected[0]);
		assertEquals(1, kingProtected[1]);
		assertEquals(0, kingProtected[2]);
		assertEquals(0, kingProtected[3]);
		
		//re protetto su 1 lati da pedine bianche
		assertEquals(1, kingProtected[0]);
		assertEquals(0, kingProtected[1]);
		assertEquals(0, kingProtected[2]);
		assertEquals(0, kingProtected[3]);
		
		//re protetto su 0 lati da pedine bianche
		assertEquals(0, kingProtected[0]);
		assertEquals(0, kingProtected[1]);
		assertEquals(0, kingProtected[2]);
		assertEquals(0, kingProtected[3]);
	}*/

	/*
	@Test
	void testKingInEscapePoint() {
		//mi aspetto 0 se nel castello
		//Position king = new Position(4,4);
		
		//metto in un escape point
		Position king = new Position(1,0);
		int actual = Heuristic.getInstance().kingInEscapePoint(king);
		assertEquals(1, actual);
	}*/
	
	/*
	@Test
	void testKingCaptured() {
		//elimino il re dallo stato
		//child.getState().remove(new Position(4,4));
		
		int actual = Heuristic.getInstance().kingCaptured(child);
		
		assertEquals(0, actual);
	}*/
	
	/*@Test
	void testFreeEscapeRoute(){
		child.getState().remove(new Position(4,4));
		Position king = new Position(5,1);
		child.getState().put(king, new PawnClass(5,1, Pawn.KING));
		int actual = Heuristic.getInstance().freeEscapeRoute(child, king);
		assertEquals(1, actual);
	}*/
	
	/*@Test
	void testBlockEscapeRoute() {
		child.getState().remove(new Position(4,4));
		Position king = new Position(2,2);
		child.getState().put(king, new PawnClass(2,2, Pawn.KING));
		child.getState().put(new Position(2,1), new PawnClass(2,1, Pawn.BLACK));
		int actual = Heuristic.getInstance().blockEscapeRoute(child, king);
		assertEquals(3, actual);
	}*/
	/*
	@Test
	void testWhiteHeuristic() {
		root.getState().remove(new Position(4,4));
		int result = Heuristic.getInstance().BlackHeuristic(root);
		System.out.println("result "+result);
		assertEquals(200, result);
	}*/
	
	/*
	@Test
	void testConvertLetterToInt() {
		String position = "f3";
		Position test = Heuristic.getInstance().convertLetterToInt(position);
		Position pos = new Position(2,5);
		System.out.println(test.toString());
		if(pos.equals(test)) {
			System.out.println("Uguali");
		}else {
			System.out.println("Diversi");
		}
	}*/
	
	/*@Test
	void testdistanceBetweenKingEscape() {
		int result = Heuristic.getInstance().distanceBetweenKingEscape(new Position(6,3));
		assertEquals(3,result);
	}*/
	@Test
	void testKingInEscapePoint() {
		Position king = new Position(6,0);
		int expected = Heuristic.getInstance().kingInEscapePoint(king);
		assertEquals(1, expected);
	}
}
