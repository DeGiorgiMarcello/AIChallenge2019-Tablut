package test;

/*import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;*/
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.*;

import domain.State;
import domain.State.Pawn;
import domain.State.Turn;
import domain.StateTablut;
import util.PawnMap;
import util.Position;
import util.PawnClass;

public class PawnMapTest {
	private PawnMap underTest = PawnMap.getInstance();
	private State state = new StateTablut();
	
	@Test
	public void testCreateMap() throws Throwable{
		
		underTest.createMap(state);
		TestUtil testUtil = new TestUtil();
		if(testUtil.mapCreated.keySet().equals(underTest.getMap().keySet()))
			System.out.println("EQ");
		else
			System.out.println("NOT EQ");
		
		Assert.assertNotNull("Mappa creata è nulla", underTest.getMap());
		Assert.assertEquals("Mappe hanno la stessa dimensione",testUtil.mapCreated.size(), underTest.getMap().size());
	}
	
	
	
	
}
