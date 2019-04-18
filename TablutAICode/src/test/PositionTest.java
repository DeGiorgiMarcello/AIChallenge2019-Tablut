package test;
import static org.junit.Assert.*;
import org.junit.*;

import util.Position;

public class PositionTest {

	@Test
	public void testEquals() throws Throwable{
		Position underTest = new Position(1, 1);
		
		boolean result = underTest.equals(new Position(1,1));
		
		assertEquals(true, result);
	}
}
