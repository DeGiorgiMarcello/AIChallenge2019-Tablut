package test;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import strategy.Strategy;
import util.Position;

class StrategyTest {
	
	Strategy strategy = Strategy.getInstance();

	@Test
	void convertCoordinates() {
		Position pos = new Position(3,3); //posizione d4
		assertTrue(strategy.convertCoordinates(pos).equals("d4"));
		Position pos2 = new Position(8,8); //posizione i9
		assertTrue(strategy.convertCoordinates(pos2).equals("i9"));
	}

}
