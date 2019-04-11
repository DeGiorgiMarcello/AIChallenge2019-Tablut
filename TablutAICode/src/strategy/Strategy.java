package strategy;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.text.Position;

import util.Node;
import util.PawnClass;

public class Strategy {
	//calcola tutte le possibili mosse a partire da uno stato
	private static Strategy instance;
	private ArrayList children = new ArrayList<Map<Position, PawnClass>>();
		
	private Strategy() {
		
	}
	
	public static Strategy getInstance() {
		if(instance == null)
			instance = new Strategy();
		return instance;
	}
	
	/*A partire dalla mappa, per ogni pedina bianca calcola tutte le possibili mosse*/
	
	public String[] getMove(String player) {
		String[] move = new String[2];
		Node root = new Node();
		Map<Position,PawnClass> initState = root.getState();
		if(player.equals("white")) {
			for(Map.Entry<Position, PawnClass> entry : initState.entrySet()) {
			
			}
			
			
			
		
			
			
			
			
			
			
		}
		else {
			
		}
		
		
		return move;
	}
	
}
