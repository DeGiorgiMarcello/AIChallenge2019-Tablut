package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.State;
import domain.StateTablut;
import util.Node;
import util.PawnMap;

class HeuristicTest {

	Node root;
	Node child;
	State state;
	@BeforeEach
	void setUp() throws Exception {
		state = new StateTablut();
		PawnMap.getInstance().createMap(state);
		root = new Node();
		//child = new Node(1, root.getState(), parent, captured, pawnMoveFrom, pawnMoveTo)
	}

	/*@AfterEach
	void tearDown() throws Exception {
	}*/

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
