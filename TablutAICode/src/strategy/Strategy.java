package strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import util.Position;

import domain.State.Pawn;
import util.Node;
import util.PawnClass;
import util.PawnMap;

public class Strategy {
	//calcola tutte le possibili mosse a partire da uno stato
	private static Strategy instance;
	private ArrayList children = new ArrayList<Map<Position, PawnClass>>();
	private int depth = 0;
	private ArrayList<Position> citadels = new ArrayList<Position>();
	private Position castle = new Position(4,4);
	private Position a1 = new Position(3,4);
	private Position a2 = new Position(4,3);
	private Position a3 = new Position(4,5);
	private Position a4 = new Position(5,4);
		
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
	
	
	
	
	
	
	public void initCitadels() {
		citadels.add(new Position(3,0));
		citadels.add(new Position(4,0));
		citadels.add(new Position(3,0));
		citadels.add(new Position(4,1));
		citadels.add(new Position(8,3));
		citadels.add(new Position(8,4));
		citadels.add(new Position(8,5));
		citadels.add(new Position(7,4));
		citadels.add(new Position(5,8));
		citadels.add(new Position(4,8));
		citadels.add(new Position(4,7));
		citadels.add(new Position(3,8));
		citadels.add(new Position(0,3));
		citadels.add(new Position(0,4));
		citadels.add(new Position(0,5));
		citadels.add(new Position(0,4));
	}
	
	public Map<Position, PawnClass> captureVerification(Map<Position,PawnClass> map, Position newPawnPosition) {
		initCitadels();
		/*una volta creato il nuovo stato, viene richiamato quesdto metodo che verifica se la mossa genera la cattura
		 * di una pedina avversaria; in caso affermativo elimina la pedina dalla mappa e ritorna la nuova mappa aggiornata. */
		/*IF ESTERNO VERIFICA SE LA PEDINA CORRENTE è BIANCO O NERA ( CORRISPONDE ANCHE AL GIOCATORE CHE STA EFFETTUANDO LA MOSSA)
		 * SECONDO IF VERIFICA SE DOPO AVER SPOSTATO LA PEDINA:
		 * 1 LA MAPPA CONTIENE UN ELEMENTO NELLA POSIZIONE SUBITO SOPRA A QUELLA DELLA PEDINA SPOSTATA
		 * 2 LA MAPPA CONTIENE UN ELEMENTO DUE POSIZIONI SOPRA LA PEDINA SPOSTATA
		 * 3 SE LA PEDINA SUBITO SOPRA A QUELLA APPENA SPOSTATA è DI TIPO DIVERSO DA QUELLA CONSIDERATA 
		 * 4 SE LA PEDINA 2 POSIZIONI SOPRA QUELLA CONSIDERATA è DELLO STESSO TIPO OPPURE NEL CASO BIANCO è IL RE*/
		if(map.get(newPawnPosition).getType().equalsPawn(Pawn.WHITE.toString()) || map.get(newPawnPosition).getType().equalsPawn(Pawn.KING.toString()) ) {
			return captureVerificationWhite(map, newPawnPosition);
		}else {
			//CATTURO PEDINA SOPRA
			if(map.containsKey(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn()))
					&& map.containsKey(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn()))
					&& (map.get(new Position(newPawnPosition.getRow()-1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.WHITE.toString())
							|| map.get(new Position(newPawnPosition.getRow()-1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.KING.toString()))
					&& map.get(new Position(newPawnPosition.getRow()-2,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.BLACK.toString())) {
					/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
					&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
				map.remove(new Position(newPawnPosition.getRow()-1, newPawnPosition.getColumn()));
			}
			
			//CATTURO PEDINA SOTTO
			if(map.containsKey(new Position(newPawnPosition.getRow() + 1, newPawnPosition.getColumn()))
					&& map.containsKey(new Position(newPawnPosition.getRow() + 2, newPawnPosition.getColumn()))
					&& (map.get(new Position(newPawnPosition.getRow()+1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.WHITE.toString())
							|| map.get(new Position(newPawnPosition.getRow()+1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.KING.toString()))
					&& map.get(new Position(newPawnPosition.getRow()+2,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.BLACK.toString())) {
					/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
					&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
				map.remove(new Position(newPawnPosition.getRow()+1, newPawnPosition.getColumn()));
			}
			
			//CATTURO PEDINA A DESTRA
			if(map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+1))
					&& map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+2))
					&& (map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()+1)).getType().equalsPawn(Pawn.WHITE.toString())
							|| map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()+1)).getType().equalsPawn(Pawn.KING.toString()))
					&& map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()+2)).getType().equalsPawn(Pawn.BLACK.toString())) {
					/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
					&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
				map.remove(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+2));
			}
			
			//CATTURO PEDINA A SINISTRA
			if(map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-1))
					&& map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-2))
					&& (map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()-1)).getType().equalsPawn(Pawn.WHITE.toString())
							|| map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()-1)).getType().equalsPawn(Pawn.KING.toString()))
					&& map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()-2)).getType().equalsPawn(Pawn.BLACK.toString())) {
					/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
					&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
				map.remove(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-2));
			}
		}
		return map;
	}
	
	
	
	public Map<Position, PawnClass> captureVerificationWhite(Map<Position,PawnClass> map, Position newPawnPosition) {
		//bianco-nero-bianco
		/*una volta creato il nuovo stato, viene richiamato quesdto metodo che verifica se la mossa genera la cattura
		 * di una pedina avversaria; in caso affermativo elimina la pedina dalla mappa e ritorna la nuova mappa aggiornata. */
		/*IF ESTERNO VERIFICA SE LA PEDINA CORRENTE è BIANCO O NERA ( CORRISPONDE ANCHE AL GIOCATORE CHE STA EFFETTUANDO LA MOSSA)
		 * SECONDO IF VERIFICA SE DOPO AVER SPOSTATO LA PEDINA:
		 * 1 LA MAPPA CONTIENE UN ELEMENTO NELLA POSIZIONE SUBITO SOPRA A QUELLA DELLA PEDINA SPOSTATA
		 * 2 LA MAPPA CONTIENE UN ELEMENTO DUE POSIZIONI SOPRA LA PEDINA SPOSTATA
		 * 3 SE LA PEDINA SUBITO SOPRA A QUELLA APPENA SPOSTATA è DI TIPO DIVERSO DA QUELLA CONSIDERATA 
		 * 4 SE LA PEDINA 2 POSIZIONI SOPRA QUELLA CONSIDERATA è DELLO STESSO TIPO OPPURE NEL CASO BIANCO è IL RE*/
		//CATTURO LA PEDINA SUBITO SOPRA
		if(map.containsKey(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn()))
				&& map.containsKey(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn()))
				//&& !map.get(newPawnPosition).getType().equalsPawn(Pawn.KING.toString())
				&& map.get(new Position(newPawnPosition.getRow()-1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.BLACK.toString())
				&& (map.get(new Position(newPawnPosition.getRow()-2,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.WHITE.toString())
						|| map.get(new Position(newPawnPosition.getRow()-2,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.KING.toString()))) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow()-1, newPawnPosition.getColumn()));
		}
		
		//CATTURO PEDINA SUBITO SOTTO
		if(map.containsKey(new Position(newPawnPosition.getRow() + 1, newPawnPosition.getColumn()))
				&& map.containsKey(new Position(newPawnPosition.getRow() + 2, newPawnPosition.getColumn()))
				//&& !map.get(newPawnPosition).getType().equalsPawn(Pawn.KING.toString())
				&& map.get(new Position(newPawnPosition.getRow()+1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.BLACK.toString())
				&& (map.get(new Position(newPawnPosition.getRow()+2,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.WHITE.toString())
						|| map.get(new Position(newPawnPosition.getRow()+2,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.KING.toString()))) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow()+1, newPawnPosition.getColumn()));
		}
		
		//CATTURO PEDINA A DESTRA
		if(map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn() + 1))
				&& map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+2))
				//&& !map.get(newPawnPosition).getType().equalsPawn(Pawn.KING.toString())
				&& map.get(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+1)).getType().equalsPawn(Pawn.BLACK.toString())
				&& (map.get(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+2)).getType().equalsPawn(Pawn.WHITE.toString())
						|| map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()+2)).getType().equalsPawn(Pawn.KING.toString()))) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+1));
		}
		
		//CATTURO A SINISTRA
		if(map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn() - 1))
				&& map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-2))
				//&& !map.get(newPawnPosition).getType().equalsPawn(Pawn.KING.toString())
				&& map.get(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-1)).getType().equalsPawn(Pawn.BLACK.toString())
				&& (map.get(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-2)).getType().equalsPawn(Pawn.WHITE.toString())
						|| map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()-2)).getType().equalsPawn(Pawn.KING.toString()))) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-1));
		}
		
		
		
		//bianco-nero-citadels && bianco-nero-castle
		/*per verificare la cattura verifico se citadels un elemnto che si trova due posizioni sopra la pedina
		 * considerata*/
		//CATTURO LA PEDINA SUBITO SOPRA
		if(map.containsKey(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn()))
				&& (citadels.contains(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn()))
						|| castle.equals(new Position(newPawnPosition.getRow()-2, newPawnPosition.getColumn())))
				//&& !map.get(newPawnPosition).getType().equalsPawn(Pawn.KING.toString())
				&& map.get(new Position(newPawnPosition.getRow()-1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.BLACK.toString())) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow()-1, newPawnPosition.getColumn()));
		}
		
		//CATTURO PEDINA SUBITO SOTTO
		if(map.containsKey(new Position(newPawnPosition.getRow() + 1, newPawnPosition.getColumn()))
				&& (citadels.contains(new Position(newPawnPosition.getRow() + 2, newPawnPosition.getColumn()))
						|| castle.equals(new Position(newPawnPosition.getRow() + 2, newPawnPosition.getColumn())))
				//&& !map.get(newPawnPosition).getType().equalsPawn(Pawn.KING.toString())
				&& map.get(new Position(newPawnPosition.getRow()+1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.BLACK.toString())) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow()+1, newPawnPosition.getColumn()));
		}
		
		//CATTURO PEDINA A DESTRA
		if(map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn() + 1))
				&& (citadels.contains(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+2))
						|| castle.equals(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+2)))
				//&& !map.get(newPawnPosition).getType().equalsPawn(Pawn.KING.toString())
				&& map.get(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+1)).getType().equalsPawn(Pawn.BLACK.toString())) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+1));
		}
		
		//CATTURO A SINISTRA
		if(map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn() - 1))
				&& (citadels.contains(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-2))
						|| castle.equals(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-2)))
				//&& !map.get(newPawnPosition).getType().equalsPawn(Pawn.KING.toString())
				&& map.get(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-1)).getType().equalsPawn(Pawn.BLACK.toString())) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-1));
		}
		//bianco-nero-castle
		return map;
	}
	
	
	public void captureVerificationBlack(Map<Position,PawnClass> map, Position newPawnPosition) {
		Position kingPosition = PawnMap.getInstance().findKingPosition(map);
		//black-white/king-black
		//CATTURO PEDINA SOPRA
		if(map.containsKey(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn()))
				&& map.containsKey(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn()))
				&& kingNotAdjacent(kingPosition) && !kingPosition.equals(castle)
				&& (map.get(new Position(newPawnPosition.getRow()-1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.WHITE.toString())
						|| map.get(new Position(newPawnPosition.getRow()-1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.KING.toString()))
				&& map.get(new Position(newPawnPosition.getRow()-2,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.BLACK.toString())) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow()-1, newPawnPosition.getColumn()));
		}
		
		//CATTURO PEDINA SOTTO
		if(map.containsKey(new Position(newPawnPosition.getRow() + 1, newPawnPosition.getColumn()))
				&& map.containsKey(new Position(newPawnPosition.getRow() + 2, newPawnPosition.getColumn()))
				&& kingNotAdjacent(kingPosition) && !kingPosition.equals(castle)
				&& (map.get(new Position(newPawnPosition.getRow()+1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.WHITE.toString())
						|| map.get(new Position(newPawnPosition.getRow()+1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.KING.toString()))
				&& map.get(new Position(newPawnPosition.getRow()+2,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.BLACK.toString())) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow()+1, newPawnPosition.getColumn()));
		}
		
		//CATTURO PEDINA A DESTRA
		if(map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+1))
				&& map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+2))
				&& kingNotAdjacent(kingPosition) && !kingPosition.equals(castle)
				&& (map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()+1)).getType().equalsPawn(Pawn.WHITE.toString())
						|| map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()+1)).getType().equalsPawn(Pawn.KING.toString()))
				&& map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()+2)).getType().equalsPawn(Pawn.BLACK.toString())) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+2));
		}
		
		//CATTURO PEDINA A SINISTRA
		if(map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-1))
				&& map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-2))
				&& kingNotAdjacent(kingPosition) && !kingPosition.equals(castle)
				&& (map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()-1)).getType().equalsPawn(Pawn.WHITE.toString())
						|| map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()-1)).getType().equalsPawn(Pawn.KING.toString()))
				&& map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()-2)).getType().equalsPawn(Pawn.BLACK.toString())) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-2));
		}
		
		//black-white-citadels/castle
		//CATTURO PEDINA SOPRA
		if(map.containsKey(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn()))
				&& (citadels.contains(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn()))
						|| castle.equals(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())))
				&& kingNotAdjacent(kingPosition) && !kingPosition.equals(castle)
				&& (map.get(new Position(newPawnPosition.getRow()-1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.WHITE.toString()))
					|| map.get(new Position(newPawnPosition.getRow()-1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.KING.toString())) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow()-1, newPawnPosition.getColumn()));
		}
		
		//CATTURO PEDINA SOTTO
		if(map.containsKey(new Position(newPawnPosition.getRow() + 1, newPawnPosition.getColumn()))
				&& (citadels.contains(new Position(newPawnPosition.getRow() + 2, newPawnPosition.getColumn()))
						|| castle.equals(new Position(newPawnPosition.getRow() + 2, newPawnPosition.getColumn())))
				&& kingNotAdjacent(kingPosition) && !kingPosition.equals(castle)
				&& (map.get(new Position(newPawnPosition.getRow()+1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.WHITE.toString())
					|| map.get(new Position(newPawnPosition.getRow()+1,  newPawnPosition.getColumn())).getType().equalsPawn(Pawn.KING.toString()))) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow()+1, newPawnPosition.getColumn()));
		}
		
		//CATTURO PEDINA A DESTRA
		if(map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+1))
				&& (citadels.contains(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+2))
						|| castle.equals(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+2)))
				&& kingNotAdjacent(kingPosition) && !kingPosition.equals(castle)
				&& (map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()+1)).getType().equalsPawn(Pawn.WHITE.toString())
					|| map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()+1)).getType().equalsPawn(Pawn.KING.toString()))) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+2));
		}
		
		//CATTURO PEDINA A SINISTRA
		if(map.containsKey(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-1))
				&& (citadels.contains(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-2))
						|| castle.equals(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-2)))
				&& kingNotAdjacent(kingPosition) && !kingPosition.equals(castle)
				&& (map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()-1)).getType().equalsPawn(Pawn.WHITE.toString())
					|| map.get(new Position(newPawnPosition.getRow(),  newPawnPosition.getColumn()-1)).getType().equalsPawn(Pawn.KING.toString()))) {
				/*&& !map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 1, newPawnPosition.getColumn())).getType().toString())
				&& map.get(newPawnPosition).getType().equalsPawn(map.get(new Position(newPawnPosition.getRow() - 2, newPawnPosition.getColumn())).getType().toString())) {*/
			map.remove(new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-2));
		}
		
		
		/*black-king-black
		 * già fatto sopra*/
		
		//black-kingInCastle
		/*devo circondare il re sui 4 lati*/
		if(kingPosition.equals(castle) && map.containsKey(a1) && map.containsKey(a2) && map.containsKey(a3) && map.containsKey(a4)
				&& map.get(a1).getType().equalsPawn(Pawn.BLACK.toString())
				&& map.get(a2).getType().equalsPawn(Pawn.BLACK.toString())
				&& map.get(a3).getType().equalsPawn(Pawn.BLACK.toString())
				&& map.get(a4).getType().equalsPawn(Pawn.BLACK.toString())) {
			map.remove(kingPosition);
			/*PARTITA VINTA*/
		}
		
		//black-kingAdjacent
		if(kingPosition.equals(a1) && map.containsKey(new Position(3,3)) && map.containsKey(new Position(3,5)) && map.containsKey(new Position(2,5))
				&& map.get(new Position(3,3)).getType().equalsPawn(Pawn.BLACK.toString())
				&& map.get(new Position(3,5)).getType().equalsPawn(Pawn.BLACK.toString())
				&& map.get(new Position(2,5)).getType().equalsPawn(Pawn.BLACK.toString())) {
			map.remove(kingPosition);
			/*partita vinta*/
		}
		if(kingPosition.equals(a2) && map.containsKey(new Position(3,3)) && map.containsKey(new Position(5,3)) && map.containsKey(new Position(4,2))
				&& map.get(new Position(3,3)).getType().equalsPawn(Pawn.BLACK.toString())
				&& map.get(new Position(5,3)).getType().equalsPawn(Pawn.BLACK.toString())
				&& map.get(new Position(4,2)).getType().equalsPawn(Pawn.BLACK.toString())) {
			map.remove(kingPosition);
			/*partita vinta*/
		}
		if(kingPosition.equals(a3) && map.containsKey(new Position(5,5)) && map.containsKey(new Position(3,5)) && map.containsKey(new Position(4,6))
				&& map.get(new Position(5,5)).getType().equalsPawn(Pawn.BLACK.toString())
				&& map.get(new Position(3,5)).getType().equalsPawn(Pawn.BLACK.toString())
				&& map.get(new Position(4,6)).getType().equalsPawn(Pawn.BLACK.toString())) {
			map.remove(kingPosition);
			/*partita vinta*/
		}
		if(kingPosition.equals(a4) && map.containsKey(new Position(5,3)) && map.containsKey(new Position(5,5)) && map.containsKey(new Position(6,4))
				&& map.get(new Position(5,3)).getType().equalsPawn(Pawn.BLACK.toString())
				&& map.get(new Position(5,5)).getType().equalsPawn(Pawn.BLACK.toString())
				&& map.get(new Position(6,4)).getType().equalsPawn(Pawn.BLACK.toString())) {
			map.remove(kingPosition);
			/*partita vinta*/
		}
	}
	
	
	public boolean kingNotAdjacent(Position kingPosition) {
		/*ritorna true se il re non è adiacente al castello*/
		if(!kingPosition.equals(a1) && !kingPosition.equals(a2) && !kingPosition.equals(a3) && !kingPosition.equals(a4)){
			return true;
		}else
			return false;
	}
}