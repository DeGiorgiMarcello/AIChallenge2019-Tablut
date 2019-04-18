package test;

import static org.junit.Assert.*;

import org.junit.*;

import domain.State.Pawn;
import util.PawnClass;

public class PawnClassTest {

	@Test
	public void testEquals() throws Throwable{
		PawnClass underTest = new PawnClass(1, 1, Pawn.BLACK);
		
		boolean result = underTest.equals(new PawnClass(1, 1, Pawn.WHITE));
		
		assertEquals(false, result);
	}
}
