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
		
		Map<Position, PawnClass> map = new HashMap();
		map.put(new Position(4,0), new PawnClass(4, 0, Pawn.BLACK));
		map.put(new Position(3,8), new PawnClass(3, 8, Pawn.BLACK));
		map.put(new Position(5,4), new PawnClass(5, 4, Pawn.WHITE));
		map.put(new Position(4,4), new PawnClass(4, 4, Pawn.KING));
		map.put(new Position(5,0), new PawnClass(5, 0, Pawn.BLACK));
		map.put(new Position(8,4), new PawnClass(8, 4, Pawn.BLACK));
		map.put(new Position(5,8), new PawnClass(5, 8, Pawn.BLACK));
		map.put(new Position(3,0), new PawnClass(3, 0, Pawn.BLACK));
		map.put(new Position(2,4), new PawnClass(2, 4, Pawn.WHITE));
		map.put(new Position(4,1), new PawnClass(4, 1, Pawn.BLACK));
		map.put(new Position(7,4), new PawnClass(7, 4, Pawn.BLACK));
		map.put(new Position(1,4), new PawnClass(1, 4, Pawn.BLACK));
		map.put(new Position(8,3), new PawnClass(8, 3, Pawn.BLACK));
		map.put(new Position(4,2), new PawnClass(4, 2, Pawn.WHITE));
		map.put(new Position(4,7), new PawnClass(4, 7, Pawn.BLACK));
		map.put(new Position(4,8), new PawnClass(4, 8, Pawn.BLACK));
		map.put(new Position(8,5), new PawnClass(8, 5, Pawn.BLACK));
		map.put(new Position(4,3), new PawnClass(4, 3, Pawn.WHITE));
		map.put(new Position(3,4), new PawnClass(3, 4, Pawn.WHITE));
		map.put(new Position(0,4), new PawnClass(0, 4, Pawn.BLACK));		
		map.put(new Position(0,3), new PawnClass(0, 3, Pawn.BLACK));
		map.put(new Position(4,5), new PawnClass(4, 5, Pawn.WHITE));
		map.put(new Position(6,4), new PawnClass(6, 4, Pawn.WHITE));
		map.put(new Position(4,6), new PawnClass(4, 6, Pawn.WHITE));
		map.put(new Position(0,5), new PawnClass(0, 5, Pawn.BLACK));
				
		underTest.printMap();
		Assert.assertNotNull("Mappa creata è nulla", underTest.getMap());
		Assert.assertEquals("Mappe hanno la stessa dimensione",map.size(), underTest.getMap().size());
	}
	
	
	
	
}
