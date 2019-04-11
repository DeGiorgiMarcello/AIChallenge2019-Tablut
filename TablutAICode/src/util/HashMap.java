package util;

import java.util.Map;

import domain.State;
import domain.State.Pawn;

public class HashMap {
	private static HashMap instance;
	
	private Map<Position, PawnClass> map = new java.util.HashMap<Position, PawnClass>();
	
	private HashMap() {
		
	}
	
	public static HashMap getInstance() {
		if(instance == null) 
			instance = new HashMap();
		return instance;
	}
	
	public void createMap(State state) {
		if(!map.isEmpty()) {
			map.clear();
		}
		for(int i = 0; i < state.getBoard().length; i++) {
			for(int j = 0; j < state.getBoard()[0].length; j++) {
				switch(state.getBoard()[i][j]) {
				case WHITE:
					map.put(new Position(i,j), new PawnClass(i, j, Pawn.WHITE));
					break;
				case BLACK:
					map.put(new Position(i,j), new PawnClass(i, j, Pawn.BLACK));
					break;
				case KING:
					map.put(new Position(i,j), new PawnClass(i, j, Pawn.KING));
					break;
				}
			}
		}
	}

	
	public void printMap() {
		map.forEach((position, pawn) -> System.out.println(pawn.toString() + "In position "+position.toString()));
	}
	
	public Map<Position, PawnClass> getMap() {
		return map;
	}

	public void setMap(Map<Position, PawnClass> map) {
		this.map = map;
	}
	
	
	
}
