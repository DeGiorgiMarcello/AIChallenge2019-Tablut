package test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.*;

import domain.State.Pawn;
import util.PawnClass;
import util.Position;

public class PawnClassTest {

	/*@Test
	public void testEquals() throws Throwable{
		PawnClass underTest = new PawnClass(1, 1, Pawn.BLACK);
		
		boolean result = underTest.equals(new PawnClass(1, 1, Pawn.WHITE));
		
		assertEquals(false, result);
	}*/
	
	/*@Test
	public void testMaxNumberBoxMoveUp() {
		Map<Position, PawnClass> test = new HashMap<Position, PawnClass>();
		Position pos = new Position(0,4);
		test.put(new Position(1,4), new PawnClass(1,4, Pawn.BLACK));
		test.put(pos, new PawnClass(0,4, Pawn.BLACK));
		
		int cont = test.get(pos).maxNumberBoxMoveUp(test);
		if(cont == 0)
			System.out.println("Non posso muovermi\n");
		for(int i = 1; i <= cont; i++) {
			int row = test.get(pos).getRow()+i;
			System.out.println("Posso spostarla in riga="+row+" colonna="+test.get(pos).getColumn());
		}
		assertEquals(3, cont);
	}*/
	
	/*@Test
	public void testMaxNumberBoxMoveDown() {
		Map<Position, PawnClass> test = new HashMap<Position, PawnClass>();
		Position pos = new Position(0,3);
		test.put(new Position(4,3), new PawnClass(4,3, Pawn.BLACK));
		test.put(pos, new PawnClass(0,3, Pawn.BLACK));
		
		int cont = test.get(pos).maxNumberBoxMoveDown(test);
		if(cont == 0)
			System.out.println("Non posso muovermi\n");
		for(int i = 1; i <= cont; i++) {
			int row = test.get(pos).getRow()+i;
			System.out.println("Posso spostarla in riga="+row+" colonna="+test.get(pos).getColumn());
		}
		assertEquals(3, cont);
	}*/
	
	@Test
	public void testMaxNumberBoxMoveRight() {
		Map<Position, PawnClass> test = new HashMap<Position, PawnClass>();
		Position pos = new Position(1,2);
		//test.put(new Position(2,2), new PawnClass(2,2, Pawn.BLACK));
		test.put(pos, new PawnClass(1,2, Pawn.BLACK));
		
		int cont = test.get(pos).maxNumberBoxMoveRight(test);
		if(cont == 0)
			System.out.println("Non posso muovermi\n");
		for(int i = 1; i <= cont; i++) {
			int column = test.get(pos).getColumn()+i;
			System.out.println("Posso spostarla in riga="+test.get(pos).getRow()+" colonna="+column);
		}
		assertEquals(1, cont);
	}
	
	/*
	@Test
	public void testMaxNumberBoxMoveLeft() {
		Map<Position, PawnClass> test = new HashMap<Position, PawnClass>();
		Position pos = new Position(4,7);
		test.put(new Position(8,7), new PawnClass(8,7, Pawn.BLACK));
		test.put(pos, new PawnClass(4,7, Pawn.BLACK));
		
		int cont = test.get(pos).maxNumberBoxMoveLeft(test);
		if(cont == 0)
			System.out.println("Non posso muovermi\n");
		for(int i = 1; i <= cont; i++) {
			int column = test.get(pos).getColumn()-i;
			System.out.println("Posso spostarla in riga="+test.get(pos).getRow()+" colonna="+column);
		}
		assertEquals(2, cont);
	}*/
}

