package test;

import java.util.HashMap;
import java.util.Map;

import domain.State.Pawn;
import util.PawnClass;
import util.Position;

public class TestUtil {
	Map<Position, PawnClass> mapCreated = new HashMap();
	
	public TestUtil() {
		initMapCreated();
	}
	
	public void initMapCreated() {
		mapCreated.put(new Position(0,3), new PawnClass(0, 3, Pawn.BLACK));
		mapCreated.put(new Position(0,4), new PawnClass(0, 4, Pawn.BLACK));
		mapCreated.put(new Position(0,5), new PawnClass(0, 5, Pawn.BLACK));
		mapCreated.put(new Position(1,4), new PawnClass(1, 4, Pawn.BLACK));
		mapCreated.put(new Position(2,4), new PawnClass(2, 4, Pawn.WHITE));
		mapCreated.put(new Position(3,0), new PawnClass(3, 0, Pawn.BLACK));
		mapCreated.put(new Position(3,4), new PawnClass(3, 4, Pawn.WHITE));
		mapCreated.put(new Position(3,8), new PawnClass(3, 8, Pawn.BLACK));
		mapCreated.put(new Position(4,0), new PawnClass(4, 0, Pawn.BLACK));
		mapCreated.put(new Position(4,1), new PawnClass(4, 1, Pawn.BLACK));
		mapCreated.put(new Position(4,2), new PawnClass(4, 2, Pawn.WHITE));
		mapCreated.put(new Position(4,3), new PawnClass(4, 3, Pawn.WHITE));
		mapCreated.put(new Position(4,4), new PawnClass(4, 4, Pawn.KING));
		mapCreated.put(new Position(4,5), new PawnClass(4, 5, Pawn.WHITE));
		mapCreated.put(new Position(4,6), new PawnClass(4, 6, Pawn.WHITE));
		mapCreated.put(new Position(4,7), new PawnClass(4, 7, Pawn.BLACK));
		mapCreated.put(new Position(4,8), new PawnClass(4, 8, Pawn.BLACK));
		mapCreated.put(new Position(5,0), new PawnClass(5, 0, Pawn.BLACK));
		mapCreated.put(new Position(5,4), new PawnClass(5, 4, Pawn.WHITE));
		mapCreated.put(new Position(5,8), new PawnClass(5, 8, Pawn.BLACK));
		mapCreated.put(new Position(6,4), new PawnClass(6, 4, Pawn.WHITE));
		mapCreated.put(new Position(7,4), new PawnClass(7, 4, Pawn.BLACK));
		mapCreated.put(new Position(8,3), new PawnClass(8, 3, Pawn.BLACK));
		mapCreated.put(new Position(8,4), new PawnClass(8, 4, Pawn.BLACK));
		mapCreated.put(new Position(8,5), new PawnClass(8, 5, Pawn.BLACK));
	}
	
}
