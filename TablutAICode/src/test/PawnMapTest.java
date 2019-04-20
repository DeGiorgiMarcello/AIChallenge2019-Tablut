package test;

/*import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;*/
import java.util.HashMap;
import java.util.Map;

import org.junit.*;

import domain.State;
import domain.State.Pawn;
import domain.State.Turn;
import domain.StateTablut;
import util.PawnMap;
import util.Position;
import util.PawnClass;

public class PawnMapTest {
	PawnMap underTest;
	State state;
	TestUtil testUtil;
	
	@Before
	public void init() {
		underTest = PawnMap.getInstance();
		state = new StateTablut();
		testUtil = new TestUtil();
	}
	
	@Test
	public void testCreateMap(){
		
		underTest.createMap(state);
		
		Assert.assertNotNull("Mappa creata è nulla", underTest.getMap());
		Assert.assertEquals("Mappe hanno la stessa dimensione",testUtil.mapCreated.size(), underTest.getMap().size());
		Assert.assertEquals("Key Set diversi", testUtil.mapCreated.keySet(), underTest.getMap().keySet());
		Assert.assertTrue(testUtil.mapCreated.equals(underTest.getMap()));
	}
	
	
	
	
}
