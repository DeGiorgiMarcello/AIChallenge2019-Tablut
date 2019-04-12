package strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.text.Position;

import domain.State.Pawn;
import util.Node;
import util.PawnClass;

public class Strategy {
	//calcola tutte le possibili mosse a partire da uno stato
	private static Strategy instance;
	private ArrayList children = new ArrayList<Map<Position, PawnClass>>();
	private int depth = 0;
		
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
				PawnClass pawn = entry.getValue();
				if(pawn.getType().equals(Pawn.WHITE)) {
					
				}
			
			}
			
			
			
		}
		else {
			
		}
		
		
		return move;
	}
	//RISOLVERE IL PROBLEMA DEL moveTo e moveFrom
	
	public void moveLeft(PawnClass pawn,int maxNumberBoxMove,Node parent) {
		for(int i=0;i<pawn.maxNumberBoxMoveLeft();i++) {
			Map actualState = parent.getState();
			Map newState = updateState(actualState,pawn,pawn.getRow(),pawn.getColumn()-i);	//genera un nuovo stato dallo stato vecchio spostando la pedina a sx
			Node child = new Node(depth+1,newState,parent,"","");
		}
	}
	public void moveRight(PawnClass pawn,int maxNumberBoxMove,Node parent) {
		for(int i=0;i<pawn.maxNumberBoxMoveLeft();i++) {
			Map actualState = parent.getState();
			Map newState = updateState(actualState,pawn,pawn.getRow(),pawn.getColumn()+i);	//genera un nuovo stato dallo stato vecchio spostando la pedina a dx
			Node child = new Node(depth+1,newState,parent,"","");
			}
		}
		public void moveUp(PawnClass pawn,int maxNumberBoxMove,Node parent) {
			for(int i=0;i<pawn.maxNumberBoxMoveLeft();i++) {
				Map actualState = parent.getState();
				Map newState = updateState(actualState,pawn,pawn.getRow()-1,pawn.getColumn());	//genera un nuovo stato dallo stato vecchio spostando la pedina su
				Node child = new Node(depth+1,newState,parent,"","");
			}
		}
		public void moveDown(PawnClass pawn,int maxNumberBoxMove,Node parent) {
			for(int i=0;i<pawn.maxNumberBoxMoveLeft();i++) {
				Map actualState = parent.getState();
				Map newState = updateState(actualState,pawn,pawn.getRow()+1,pawn.getColumn());	//genera un nuovo stato dallo stato vecchio spostando la pedina giù
				Node child = new Node(depth+1,newState,parent,"","");
			}
		}
	
	public Map<Position,PawnClass> updateState(Map<Position,PawnClass> state,PawnClass pawn,int newRow,int newColumn){
		
		int oldColumn = pawn.getColumn();
		int oldRow = pawn.getRow();
		Position oldPos = (Position) new util.Position(oldRow,oldColumn);
		Position newPos = (Position) new util.Position(newRow, newColumn);
		state.remove(oldPos);  //rimuove la pedina dallo stato vecchio
		state.put(newPos, new PawnClass(newRow,newColumn,pawn.getType()));  //aggiorna lo stato inserendo la nuova pedina
		return state;
	}
	
}
