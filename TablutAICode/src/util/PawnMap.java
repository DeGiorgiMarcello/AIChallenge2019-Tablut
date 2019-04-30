package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import domain.State;
import domain.State.Pawn;

public class PawnMap {
	private static PawnMap instance;
	
	private Map<Position, PawnClass> map = new HashMap<Position, PawnClass>();
	
	private PawnMap() {
		
	}
	
	public static PawnMap getInstance() {
		if(instance == null) 
			instance = new PawnMap();
		return instance;
	}
	
	public Map<Position, PawnClass> cloneState(Map<Position, PawnClass> state){
		Map<Position, PawnClass> cloned = new HashMap<Position, PawnClass>();
		state.forEach((position, pawn) -> {
			cloned.put(position, pawn);
		});
		return cloned;
	}
	
	public void createMap(State state) {
		if(!map.isEmpty()) {
			map.clear();
		}
		for(int i = 0; i < state.getBoard().length; i++) {
			for(int j = 0; j < state.getBoard().length; j++) {
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
		map.forEach((position, pawn) -> System.out.println(pawn.toString() + " In position "+position.toString()));
	}
	
	public Map<Position, PawnClass> getMap() {
		return map;
	}

	public void setMap(Map<Position, PawnClass> map) {
		this.map = map;
	}
	
	public void printMap(Map<Position,PawnClass> state) {
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(state.containsKey((new Position(i,j)))) {
					if(state.get(new Position(i,j)).getType().equalsPawn(Pawn.BLACK.toString()))
					System.out.print("B ");
					else if(state.get(new Position(i,j)).getType().equalsPawn(Pawn.WHITE.toString())) {
						System.out.print("W ");
					}
					else
						System.out.print("K ");
				}
				else System.out.print("- ");		
			}
			if(i!=9) System.out.println(i+1+" ");
		}
		System.out.println("\na b c d e f g h i");
		
	}
	
	public Position findKingPosition(Map<Position, PawnClass> map) {
		ArrayList<Position> list = new ArrayList();
		map.forEach((position, pawnClass) -> {
			if(pawnClass.getType().equalsPawn(Pawn.KING.toString()))
				list.add(position);
		});
		if(list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
		
	}
	
	
	
}
