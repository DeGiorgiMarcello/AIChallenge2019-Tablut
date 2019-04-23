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
	
	@Test
	public void testMaxNumberBoxMoveUp() {
		Map<Position, PawnClass> test = new HashMap<Position, PawnClass>();
		Position pos = new Position(6,4);
		test.put(pos, new PawnClass(6, 4, Pawn.BLACK));
		
		int cont = test.get(pos).maxNumberBoxMoveUp(test);
		if(cont == 0)
			System.out.println("Non posso muovermi\n");
		for(int i = 1; i <= cont; i++) {
			int row = test.get(pos).getRow()-i;
			System.out.println("Posso spostarla in riga="+row+" colonna="+test.get(pos).getColumn());
		}
		assertEquals(2, cont);
	}
}
