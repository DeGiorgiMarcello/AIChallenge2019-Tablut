package strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import util.Position;

import domain.State.Pawn;
import util.BestNode;
import util.Node;
import util.PawnClass;
import util.PawnMap;

public class Strategy {
	//calcola tutte le possibili mosse a partire da uno stato
	private static Strategy instance;
	private ArrayList nodesList = new ArrayList<Node>();
	private ArrayList firstNodes = new ArrayList<Node>();   //lista dei primi nodi per ogni depth
	private ArrayList hashCodeStateList = new ArrayList<Integer>();
	private String player; 
	private int depth = 0;
	private ArrayList<Position> citadels = new ArrayList<Position>();
	private Position castle = new Position(4,4);
	private Position a1 = new Position(3,4);
	private Position a2 = new Position(4,3);
	private Position a3 = new Position(4,5);
	private Position a4 = new Position(5,4);
	private boolean taken = false; //variabile per tener conto dell'avvenuta cattura e aggiornare i nodi figli
	final int MAXDEPTH = 4;
	private Heuristic heuristicInstance;
	boolean first = true;
		
	private Strategy() {
		initCitadels();
		heuristicInstance.getInstance();
	}
	
	public static Strategy getInstance() {
		if(instance == null)
			instance = new Strategy();
		return instance;
	}
	
	/*A partire dalla mappa, per ogni pedina calcola tutte le possibili mosse*/
	
	public String[] getMove(String player) {
		String[] move = new String[2];
		//Inizializzazione variabili globali
		depth = 0;
		nodesList.clear();
		this.player = player;
		//----------------
		//Node partialTree = generatePartialTree();
		int alfa = -500;  //-infinito
		int beta = 500;   //+infinito
		nodesList.add(new Node());
		BestNode alphaBetaBestNode = alphaBeta(new Node(),0,alfa,beta,true);  //si inizializza con la root
		Node bestNode = alphaBetaBestNode.getNode();
		
		while(bestNode.getDepth() != 1) {
			bestNode = bestNode.getParent();
		}
		move[0] = bestNode.getPawnMoveFrom();
		move[1] = bestNode.getPawnMoveTo();
		
		return move;
	}
	
	/*
	public Node generatePartialTree() {
		String player = this.player;
		Node root = new Node();
		nodesList.add(0,root);
		firstNodes.add(root);
		hashCodeStateList.add(root.getState().hashCode());
		 
		
		while(depth < MAXDEPTH) {
			if(getPlayer().equals("white")) {
				Node actualNode = (Node)nodesList.get(0);
				expandNode(actualNode,Pawn.WHITE);	
				depth++;
				first = true;
				setPlayer("black");
			
			}
		
			else {
				Node actualNode = (Node)nodesList.get(0);
				expandNode(actualNode,Pawn.BLACK);
				depth++;
				first = true;	
				setPlayer("white");
		
			}
		}
		setPlayer(player);
		return root;  //ritorna l'albero intero
	} */
	
	public void expandNode(Node actualNode,Pawn color) {
		Map<Position,PawnClass> actualState = actualNode.getState();
		for(Map.Entry<Position, PawnClass> entry : actualState.entrySet()) {
			PawnClass pawn = entry.getValue();
			if(color.equals(Pawn.KING))
				color = Pawn.WHITE;
			//si prendono tutte le pedine nello stato iniziale e si spostano di tutte le possibili posizioni, generando 
			//nodi figli di root -> i nodi sono quindi posti in testa al nodesList.
			if(pawn.getType().equals(color)) {  
				moveLeft(pawn,actualNode);
				moveRight(pawn,actualNode);
				moveUp(pawn,actualNode);
				moveDown(pawn,actualNode);
			}
		}
	}
public BestNode expandNodeAlphaBeta(Node actualNode, Pawn color,int alphaBetaDepth,double val,double alfa, double beta,boolean max) {
		
		boolean isKing = false;
		BestNode bestNodeMove = new BestNode(actualNode,val); 
		Map<Position,PawnClass> actualState = PawnMap.getInstance().cloneState(actualNode.getState());
		for(Map.Entry<Position, PawnClass> entry : actualState.entrySet()) {
			PawnClass pawn = entry.getValue();
			int captured = actualNode.getCaptured();
			if(pawn.getType().equalsPawn(Pawn.KING.toString()) && color.equalsPawn(Pawn.WHITE.toString())) { //
				//color = Pawn.WHITE;
				isKing = true;
			}
			if(pawn.getType().equals(color) || isKing) {
				
				//MOVE LEFT
				for(int i=1;i<=pawn.maxNumberBoxMoveLeft(actualState);i++) {
					
					Map newState = PawnMap.getInstance().cloneState(actualNode.getState());
					Position oldPos = new Position(pawn.getRow(),pawn.getColumn());
					Position newPos = new Position(pawn.getRow(),pawn.getColumn()-i);
					updateState(newState,pawn,pawn.getRow(),pawn.getColumn()-i);
					captureVerification(newState, newPos);     //questo metodo e updateState sono stati scambiati di posto
					if(taken) {
						captured++;
						taken = false;
					}
					String moveFrom = convertCoordinates(oldPos);
					String moveTo = convertCoordinates(newPos);
					Node child = new Node(alphaBetaDepth+1,newState,actualNode,captured,moveFrom,moveTo);
					int stateHashCode = newState.hashCode();
					if(!hashCodeStateList.contains(stateHashCode)) {   //eliminata condizione che depth<MAXDEPTH e inglobato tutto il resto
						//nodesList.add(child); //nodesList.add(0, child);
						hashCodeStateList.add(stateHashCode);
						nodesList.add(child);
						if(max) {
							BestNode childNode = alphaBeta(child,alphaBetaDepth+1,alfa,beta,!max); //c'era false
							double childVal = childNode.getVal();
							if(childVal >= bestNodeMove.getVal())
								bestNodeMove = childNode;
							
							val = Math.max(val, childVal);
							alfa = Math.max(alfa, val);
							if(beta <= alfa)
								return bestNodeMove ;
						}
						else {
							BestNode childNode = alphaBeta(child,alphaBetaDepth+1,alfa,beta,!max); //c'era false
							double childVal = childNode.getVal();
							if(childVal <= bestNodeMove.getVal())
								bestNodeMove = childNode;
							val = Math.min(val, childVal);
							beta = Math.min(beta, val);
							if(beta <= alfa)
								return bestNodeMove;
						}		
					}
				}
				
				//MOVE RIGHT
				for(int i=1;i<=pawn.maxNumberBoxMoveRight(actualState);i++) {
					Map newState = PawnMap.getInstance().cloneState(actualNode.getState());
					Position oldPos = new Position(pawn.getRow(),pawn.getColumn());
					Position newPos = new Position(pawn.getRow(),pawn.getColumn()+i);
					updateState(newState,pawn,pawn.getRow(),pawn.getColumn()+i);
					captureVerification(newState, newPos);
					if(taken) {
						captured++;
						taken = false;
					}
					String moveFrom = convertCoordinates(oldPos);
					String moveTo = convertCoordinates(newPos);
					Node child = new Node(alphaBetaDepth+1,newState,actualNode,captured,moveFrom,moveTo);
					int stateHashCode = newState.hashCode();
					if(!hashCodeStateList.contains(stateHashCode)) {
						//nodesList.add(child); //nodesList.add(0, child);
						hashCodeStateList.add(stateHashCode);
						nodesList.add(child);
						if(max) { 
							BestNode childNode = alphaBeta(child,alphaBetaDepth+1,alfa,beta,!max); //c'era false
							double childVal = childNode.getVal();
							if(childVal >= bestNodeMove.getVal())
								bestNodeMove = childNode;
							val = Math.max(val, childVal);
							alfa = Math.max(alfa, val);
							if(beta <= alfa)
								return bestNodeMove;
						}
						else {
							BestNode childNode = alphaBeta(child,alphaBetaDepth+1,alfa,beta,!max); //c'era true
							double childVal = childNode.getVal();
							if(childVal <= bestNodeMove.getVal())
								bestNodeMove = childNode;
							val = Math.min(val, childVal);
							beta = Math.min(beta, val);
							if(beta <= alfa)
								return bestNodeMove;
						}
					}
				}
				//MOVE UP
				for(int i=1;i<=pawn.maxNumberBoxMoveUp(actualState);i++) {
					Map newState = PawnMap.getInstance().cloneState(actualNode.getState());
					Position oldPos = new Position(pawn.getRow(),pawn.getColumn());
					Position newPos = new Position(pawn.getRow()-1,pawn.getColumn());
					updateState(newState,pawn,pawn.getRow()-1,pawn.getColumn());
					captureVerification(newState, newPos);
					if(taken) {
						captured++;
						taken = false;
					}
					String moveFrom = convertCoordinates(oldPos);
					String moveTo = convertCoordinates(newPos);
					Node child = new Node(alphaBetaDepth+1,newState,actualNode,captured,moveFrom,moveTo);
					int stateHashCode = newState.hashCode();
					if(!hashCodeStateList.contains(stateHashCode)) {
						//nodesList.add(child); //nodesList.add(0, child);
						hashCodeStateList.add(stateHashCode);
						nodesList.add(child);
						if(max) {
							BestNode childNode = alphaBeta(child,alphaBetaDepth+1,alfa,beta,!max); //c'era false
							double childVal = childNode.getVal();
							if(childVal >= bestNodeMove.getVal())
								bestNodeMove = childNode;
							val = Math.max(val, childVal);
							alfa = Math.max(alfa, val);
							if(beta <= alfa)
								return bestNodeMove;
						}
						else {
							BestNode childNode = alphaBeta(child,alphaBetaDepth+1,alfa,beta,!max); //c'era true
							double childVal = childNode.getVal();
							if(childVal <= bestNodeMove.getVal())
								bestNodeMove = childNode;
							val = Math.min(val, childVal);
							beta = Math.min(beta, val);
							if(beta <= alfa)
								return bestNodeMove;
						}	
					}
				}
				//MOVE DOWN
				for(int i=1;i<=pawn.maxNumberBoxMoveDown(actualState);i++) {
					Map newState = PawnMap.getInstance().cloneState(actualNode.getState());
					Position oldPos = new Position(pawn.getRow(),pawn.getColumn());
					Position newPos = new Position(pawn.getRow()+1,pawn.getColumn());		
					updateState(newState,pawn,pawn.getRow()+1,pawn.getColumn());
					captureVerification(newState, newPos);
					if(taken) {
						captured++;
						taken = false;
					}
					String moveFrom = convertCoordinates(oldPos);
					String moveTo = convertCoordinates(newPos);
					Node child = new Node(alphaBetaDepth+1,newState,actualNode,captured,moveFrom,moveTo);
					int stateHashCode = newState.hashCode();
					if(!hashCodeStateList.contains(stateHashCode)) {
						//nodesList.add(child); //nodesList.add(0, child);
						hashCodeStateList.add(stateHashCode);
						nodesList.add(child);
						if(max) {
							BestNode childNode = alphaBeta(child,alphaBetaDepth+1,alfa,beta,!max); //c'era false
							double childVal = childNode.getVal();
							if(childVal >= bestNodeMove.getVal())
								bestNodeMove = childNode;
							val = Math.max(val, childVal);
							alfa = Math.max(alfa, val);
							if(beta <= alfa) {
								return bestNodeMove;
							}
						}
						else {
							BestNode childNode = alphaBeta(child,alphaBetaDepth+1,alfa,beta,!max); //c'era true
							double childVal = childNode.getVal();
							if(childVal <= bestNodeMove.getVal())
								bestNodeMove = childNode;
							val = Math.min(val, childVal);
							beta = Math.min(beta, val);
							if(beta <= alfa) {
								return bestNodeMove;
							}
						}	
					}
				}
			
			}
			isKing = false;
		}
		return bestNodeMove;
	}
	
public BestNode alphaBeta(Node node,int depth,double alfa,double beta, boolean max) {
	double val;
	int childCounter = 0;
	BestNode bestNodeMove = new BestNode(node, 0);
	Pawn maxColor,minColor;
	if(player.equals("white")) {
		 maxColor = Pawn.WHITE;
		 minColor = Pawn.BLACK;
	}
	else {
		 maxColor = Pawn.BLACK;
		 minColor = Pawn.WHITE;
		
	}
	if(max) {
		val = -500;
		bestNodeMove.setVal(val);
		if(depth == MAXDEPTH ) { //NODI FOGLIA!
			//con la funzione euristica si assegna il valore al nodo
			val = Heuristic.getInstance().evaluateNode(node);
			bestNodeMove = new BestNode(node,val); 
			return bestNodeMove;
		}else{ //se il nodo non è un nodo foglia, prendi tutti i figli del nodo
				/*
			for(Node child : (ArrayList<Node>) nodesList) {
				if(child.getParent() == node) {
					childCounter++;
					BestNode childNode = alphaBeta(child,depth+1,alfa,beta,max); //c'era false
					double childVal = childNode.getVal();
					if(childVal >= bestNodeMove.getVal())
						bestNodeMove = childNode;
					val = Math.max(val, childVal);
					alfa = Math.max(alfa, val);
					if(beta <= alfa) {
						nodesList.remove(child);
						return bestNodeMove;
					}
				}
			} */
			//se nella lista dei nodi da espandere non ci sono figli di questo nodo, espandilo!
			BestNode childNode = expandNodeAlphaBeta(node, maxColor, depth, val, alfa, beta,max); //c'era true e depth -val
			double childVal = childNode.getVal();
			if(childVal >= bestNodeMove.getVal())
				bestNodeMove = childNode;
			val = Math.max(val, childVal);
			alfa = Math.max(alfa, val);
			if(beta <= alfa) {
				return bestNodeMove;
			}
		} 
		
		//DA RIVEDERE!
		/*if(depth == 0 && nodesList.get(0) == node ) {  
			bestNodeMove = new BestNode(node,val);
			return bestNodeMove;
		}*/
		return bestNodeMove;
	}
	else {
		val = +500;
		bestNodeMove.setVal(val);
		if(depth == MAXDEPTH) { //NODI FOGLIA!
			//con la funzione euristica si assegna il valore al nodo
			val = Heuristic.getInstance().evaluateNode(node);  
			return new BestNode(node,val);
		}else { //se il nodo non è un nodo foglia, prendi tutti i figli del nodo
			/*
			for(Node child : (ArrayList<Node>) nodesList) {
				if(child.getParent() == node) {
					BestNode childNode = alphaBeta(child,depth+1,alfa,beta,max); //c'era false
					double childVal = childNode.getVal();
					if(childVal <= bestNodeMove.getVal())
						bestNodeMove = childNode;
					val = Math.max(val, childVal);
					beta = Math.min(beta, val);
					if(beta <= alfa)
						nodesList.remove(child);
						return bestNodeMove;
				}
			} */
			//se nella lista dei nodi da espandere non ci sono figli di questo nodo, espandilo!
			BestNode childNode = expandNodeAlphaBeta(node, minColor, depth, val, alfa, beta,max); //c'era true -
			double childVal = childNode.getVal();
			if(childVal <= bestNodeMove.getVal())
				bestNodeMove = childNode;
			val = Math.max(val, childVal);
			beta = Math.min(beta, val);
			if(beta <= alfa)
				return bestNodeMove;
		}
		return bestNodeMove;
	}
}

	/*public boolean whiteWin(Node node) {
		boolean result = false;
		Position king;
		for(Map.Entry<Position, PawnClass> entry : node.getState().entrySet()) {
			
		}
	}*/
	
	public String convertCoordinates(Position pos) {
		String result = "";
		int row = pos.getRow()+1;  //le righe partono da 1, non da 0.
		int col = pos.getColumn();
		switch(col) {
		case 0 : result = "a"+Integer.toString(row);
				break;
		case 1 : result = "b"+Integer.toString(row);
				break;
		case 2 : result = "c"+Integer.toString(row);
				break;
		case 3 : result = "d"+Integer.toString(row);
				break;
		case 4 : result = "e"+Integer.toString(row);
				break;
		case 5 : result = "f"+Integer.toString(row);
				break;
		case 6 : result = "g"+Integer.toString(row);
				break;
		case 7 : result = "h"+Integer.toString(row);
				break;
		case 8 : result = "i"+Integer.toString(row);
				break;
		}
		return result;
	}
	
	public void moveLeft(PawnClass pawn,Node parent) {
		taken = false;
		
		int captured = parent.getCaptured();
		
		for(int i=1;i<=pawn.maxNumberBoxMoveLeft(parent.getState());i++) {
			Map newState = PawnMap.getInstance().cloneState(parent.getState());
			Position oldPos = new Position(pawn.getRow(),pawn.getColumn());
			Position newPos = new Position(pawn.getRow(),pawn.getColumn()-i);
			updateState(newState,pawn,pawn.getRow(),pawn.getColumn()-i);	//genera un nuovo stato dallo stato vecchio spostando la pedina a sx
			captureVerification(newState, newPos);
			if(taken) {
				captured++;
				taken = false;
			}
			String moveFrom = convertCoordinates(oldPos);
			String moveTo = convertCoordinates(newPos);
			Node child = new Node(depth+1,newState,parent,captured,moveFrom,moveTo);
			int stateHashCode = newState.hashCode();
			if(depth < MAXDEPTH-1 && !hashCodeStateList.contains(stateHashCode)) {
				nodesList.add(0, child); 
				hashCodeStateList.add(stateHashCode);
			}
			if(first) {
				firstNodes.add(child);
				first = false;
			}
		}
	}
	public void moveRight(PawnClass pawn,Node parent) {
		taken = false;
		int captured = parent.getCaptured();
		
		for(int i=1;i<=pawn.maxNumberBoxMoveRight(parent.getState());i++) {
			Map newState = PawnMap.getInstance().cloneState(parent.getState());
			Position oldPos = new Position(pawn.getRow(),pawn.getColumn());
			Position newPos = new Position(pawn.getRow(),pawn.getColumn()+i);
			updateState(newState,pawn,pawn.getRow(),pawn.getColumn()+i);	//genera un nuovo stato dallo stato vecchio spostando la pedina a dx
			captureVerification(newState, newPos);
			if(taken) {
				captured++;
				taken = false;
			}
			String moveFrom = convertCoordinates(oldPos);
			String moveTo = convertCoordinates(newPos);
			Node child = new Node(depth+1,newState,parent,captured,moveFrom,moveTo);
			int stateHashCode = newState.hashCode();
			if(depth < MAXDEPTH-1 && !hashCodeStateList.contains(stateHashCode)) {
				nodesList.add(0, child); 
				hashCodeStateList.add(stateHashCode);
			}
		}
	}
		public void moveUp(PawnClass pawn,Node parent) {
			taken = false;
			int captured = parent.getCaptured();
			
			for(int i=1;i<=pawn.maxNumberBoxMoveUp(parent.getState());i++) {
				Map newState = PawnMap.getInstance().cloneState(parent.getState());
				Position oldPos = new Position(pawn.getRow(),pawn.getColumn());
				Position newPos = new Position(pawn.getRow()-i,pawn.getColumn());
				updateState(newState,pawn,pawn.getRow()-i,pawn.getColumn());	//genera un nuovo stato dallo stato vecchio spostando la pedina su
				captureVerification(newState, newPos);
				if(taken) {
					captured++;
					taken = false;
				}
				String moveFrom = convertCoordinates(oldPos);
				String moveTo = convertCoordinates(newPos);
				Node child = new Node(depth+1,newState,parent,captured,moveFrom,moveTo);
				int stateHashCode = newState.hashCode();
				if(depth < MAXDEPTH-1 && !hashCodeStateList.contains(stateHashCode)) {
					nodesList.add(0, child); 
					hashCodeStateList.add(stateHashCode);
				}
			}
		}
		public void moveDown(PawnClass pawn,Node parent) {
			taken = false;
			
			int captured = parent.getCaptured();
			
			for(int i=1;i<=pawn.maxNumberBoxMoveDown(parent.getState());i++) {
				Map newState = PawnMap.getInstance().cloneState(parent.getState());
				Position oldPos = new Position(pawn.getRow(),pawn.getColumn());
				Position newPos = new Position(pawn.getRow()+i,pawn.getColumn());
				updateState(newState,pawn,pawn.getRow()+i,pawn.getColumn());	//genera un nuovo stato dallo stato vecchio spostando la pedina giù
				captureVerification(newState, newPos);
				if(taken) {
					captured++;
					taken = false;
				}
				String moveFrom = convertCoordinates(oldPos);
				String moveTo = convertCoordinates(newPos);
				Node child = new Node(depth+1,newState,parent,captured,moveFrom,moveTo);
				int stateHashCode = newState.hashCode();
				if(depth < MAXDEPTH-1 && !hashCodeStateList.contains(stateHashCode)) {
					nodesList.add(0, child); 
					hashCodeStateList.add(stateHashCode);
				}
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
		citadels.add(new Position(0,3));
		citadels.add(new Position(0,4));
		citadels.add(new Position(0,5));
		citadels.add(new Position(1,4));
		citadels.add(new Position(3,0));
		citadels.add(new Position(3,8));
		citadels.add(new Position(4,0));
		citadels.add(new Position(4,1));
		citadels.add(new Position(4,7));
		citadels.add(new Position(4,8));
		citadels.add(new Position(5,0));
		citadels.add(new Position(5,8));
		citadels.add(new Position(7,4));
		citadels.add(new Position(8,3));
		citadels.add(new Position(8,4));
		citadels.add(new Position(8,5));
	}
	
	public Map<Position, PawnClass> captureVerification(Map<Position,PawnClass> map, Position newPawnPosition) {
		/*una volta creato il nuovo stato, viene richiamato quesdto metodo che verifica se la mossa genera la cattura
		 * di una pedina avversaria; in caso affermativo elimina la pedina dalla mappa e ritorna la nuova mappa aggiornata. */
		/*IF ESTERNO VERIFICA SE LA PEDINA CORRENTE è BIANCO O NERA ( CORRISPONDE ANCHE AL GIOCATORE CHE STA EFFETTUANDO LA MOSSA)
		 * SECONDO IF VERIFICA SE DOPO AVER SPOSTATO LA PEDINA:
		 * 1 LA MAPPA CONTIENE UN ELEMENTO NELLA POSIZIONE SUBITO SOPRA A QUELLA DELLA PEDINA SPOSTATA
		 * 2 LA MAPPA CONTIENE UN ELEMENTO DUE POSIZIONI SOPRA LA PEDINA SPOSTATA
		 * 3 SE LA PEDINA SUBITO SOPRA A QUELLA APPENA SPOSTATA è DI TIPO DIVERSO DA QUELLA CONSIDERATA 
		 * 4 SE LA PEDINA 2 POSIZIONI SOPRA QUELLA CONSIDERATA è DELLO STESSO TIPO OPPURE NEL CASO BIANCO è IL RE*/
		PawnClass pawn = map.get(newPawnPosition);
		Pawn type = pawn.getType();
		if(type.equalsPawn(Pawn.WHITE.toString()) || type.equalsPawn(Pawn.KING.toString()) ) {
			return captureVerificationWhite(map, newPawnPosition);
		}else {
			return captureVerificationBlack(map, newPawnPosition);
		}
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
		Position abovePosition1 = new Position(newPawnPosition.getRow() -1, newPawnPosition.getColumn());
		Position abovePosition2 = new Position(newPawnPosition.getRow() -2, newPawnPosition.getColumn());
		
		if(map.containsKey(abovePosition1) && map.containsKey(abovePosition2)){
			PawnClass pawnAbove1 = map.get(abovePosition1);
			PawnClass pawnAbove2 = map.get(abovePosition2);
			if(pawnAbove1.getType().equalsPawn(Pawn.BLACK.toString())
				&& (pawnAbove2.getType().equalsPawn(Pawn.WHITE.toString())
						|| pawnAbove2.getType().equalsPawn(Pawn.KING.toString()))) {	
				map.remove(abovePosition1);
				taken = true;
			}
		}
		
		//CATTURO PEDINA SUBITO SOTTO
		Position belowPosition1 = new Position(newPawnPosition.getRow() +1, newPawnPosition.getColumn());
		Position belowPosition2 = new Position(newPawnPosition.getRow() +2, newPawnPosition.getColumn());
		
		if(map.containsKey(belowPosition1) && map.containsKey(belowPosition2)){
			PawnClass pawnBelow1 = map.get(belowPosition1);
			PawnClass pawnBelow2 = map.get(belowPosition2);
			if(pawnBelow1.getType().equalsPawn(Pawn.BLACK.toString())
				&& pawnBelow2.getType().equalsPawn(Pawn.WHITE.toString())
						|| pawnBelow2.getType().equalsPawn(Pawn.KING.toString())) {
				
				map.remove(belowPosition1);
				taken = true;
			}
		}
		
		//CATTURO PEDINA A DESTRA
		Position rightPosition1 = new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+1);
		Position rightPosition2 = new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+2);
		
		if(map.containsKey(rightPosition1) && map.containsKey(rightPosition2)) {
			PawnClass pawnRight1 = map.get(rightPosition1);
			PawnClass pawnRight2 = map.get(rightPosition2);
			if(pawnRight1.getType().equalsPawn(Pawn.BLACK.toString())
					&& pawnRight2.getType().equalsPawn(Pawn.WHITE.toString())
						|| pawnRight2.getType().equalsPawn(Pawn.KING.toString())) {
			
					map.remove(rightPosition1);
					taken = true;
				}
		}
		
		//CATTURO A SINISTRA
		Position leftPosition1 = new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-1);
		Position leftPosition2 = new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-2);
		
		if(map.containsKey(leftPosition1) && map.containsKey(leftPosition2)){
			PawnClass pawnLeft1 = map.get(leftPosition1);
			PawnClass pawnLeft2 = map.get(leftPosition2);
				
			if(pawnLeft1.getType().equalsPawn(Pawn.BLACK.toString())
				&& (pawnLeft2.getType().equalsPawn(Pawn.WHITE.toString())
						|| pawnLeft2.getType().equalsPawn(Pawn.KING.toString()))) {
				
				map.remove(leftPosition1);
				taken = true;
			}
		}
		
		
		
		//bianco-nero-citadels && bianco-nero-castle
		/*per verificare la cattura verifico se citadels un elemnto che si trova due posizioni sopra la pedina
		 * considerata*/
		//CATTURO LA PEDINA SUBITO SOPRA
		
		if(map.containsKey(abovePosition1) && (citadels.contains(abovePosition2) || castle.equals(abovePosition2))){
			PawnClass pawnAbove1 = map.get(abovePosition1);
							
			if(pawnAbove1.getType().equalsPawn(Pawn.BLACK.toString()) && !citadels.contains(abovePosition1)) {
				map.remove(abovePosition1);
				taken = true;
			}
		}	
		
		//CATTURO PEDINA SUBITO SOTTO
		if(map.containsKey(belowPosition1) && (citadels.contains(belowPosition2) || castle.equals(belowPosition2))){
			PawnClass pawnBelow1 = map.get(belowPosition1);
			
			if(pawnBelow1.getType().equalsPawn(Pawn.BLACK.toString()) && !citadels.contains(belowPosition1)) {
				map.remove(belowPosition1);
				taken = true;
			}
		}
		
		//CATTURO PEDINA A DESTRA
		if(map.containsKey(rightPosition1) && (citadels.contains(rightPosition2)|| castle.equals(rightPosition2))){
			PawnClass pawnRight1 = map.get(rightPosition1);
			if(pawnRight1.getType().equalsPawn(Pawn.BLACK.toString()) && !citadels.contains(rightPosition1)) {
				map.remove(rightPosition1);
				taken = true;
			}
		}
		
		//CATTURO A SINISTRA
		if(map.containsKey(leftPosition1) && (citadels.contains(leftPosition2) || castle.equals(rightPosition2))){
			PawnClass pawnLeft1 = map.get(leftPosition1);
			if(pawnLeft1.getType().equalsPawn(Pawn.BLACK.toString()) && !citadels.contains(leftPosition1)) {
				map.remove(leftPosition1);
			taken = true;
			}
		}
		//bianco-nero-castle
		return map;
	}
	
	
	public Map<Position, PawnClass> captureVerificationBlack(Map<Position,PawnClass> map, Position newPawnPosition) {
		Position kingPosition = PawnMap.getInstance().findKingPosition(map);
		if(kingPosition != null) {
			//black-white/king-black
			Position abovePosition1 = new Position(newPawnPosition.getRow() -1, newPawnPosition.getColumn());
			Position abovePosition2 = new Position(newPawnPosition.getRow() -2, newPawnPosition.getColumn());
			
			//CATTURO PEDINA SOPRA
			if(map.containsKey(abovePosition1) && map.containsKey(abovePosition2)) {
					
				PawnClass pawnAbove1 = map.get(abovePosition1);
				PawnClass pawnAbove2 = map.get(abovePosition2);
				
				if(pawnAbove1.getType().equalsPawn(Pawn.KING.toString()) 
						&& (kingPosition.equals(castle)
						|| !kingNotAdjacent(kingPosition))) {
					//Se quello sopra è il re, si trova nel castello oppure è adiacente al castello, 
					//non fare niente (l'altro metodo dopo). L'else if permette di mangiare il Re nell'altro caso
					}
				else if (pawnAbove1.getType().equalsPawn(Pawn.WHITE.toString()) 
						|| pawnAbove1.getType().equalsPawn(Pawn.KING.toString())
						&& pawnAbove2.getType().equalsPawn(Pawn.BLACK.toString())) {
					map.remove(abovePosition1);					
					taken = true;
				}
			}
			
			//CATTURO PEDINA SOTTO
			Position belowPosition1 = new Position(newPawnPosition.getRow() +1, newPawnPosition.getColumn());
			Position belowPosition2 = new Position(newPawnPosition.getRow() +2, newPawnPosition.getColumn());
			
			if(map.containsKey(belowPosition1) && map.containsKey(belowPosition2)){
				
				PawnClass pawnBelow1 = map.get(belowPosition1);
				PawnClass pawnBelow2 = map.get(belowPosition2);
				
				if(pawnBelow1.getType().equalsPawn(Pawn.KING.toString()) 
						&& (kingPosition.equals(castle)
						|| !kingNotAdjacent(kingPosition))) {}
				else if(pawnBelow1.getType().equalsPawn(Pawn.WHITE.toString())
						|| pawnBelow1.getType().equalsPawn(Pawn.KING.toString()) 
						&& pawnBelow2.getType().equalsPawn(Pawn.BLACK.toString())) {
							
					map.remove(belowPosition1);
					taken = true;
					}
			}
			
			//CATTURO PEDINA A DESTRA
			Position rightPosition1 = new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+1);
			Position rightPosition2 = new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+2);
			
			if(map.containsKey(rightPosition1) && map.containsKey(rightPosition2)){
				PawnClass pawnRight1 = map.get(rightPosition1);
				PawnClass pawnRight2 = map.get(rightPosition2);
				
				if(pawnRight1.getType().equalsPawn(Pawn.KING.toString()) 
						&& (kingPosition.equals(castle)
						|| !kingNotAdjacent(kingPosition))) {}
				else if(pawnRight1.getType().equalsPawn(Pawn.WHITE.toString())
						|| pawnRight1.getType().equalsPawn(Pawn.KING.toString())
						&& pawnRight2.getType().equalsPawn(Pawn.BLACK.toString())){
							
						map.remove(rightPosition1);
						taken = true;
					}
			}
			
			//CATTURO PEDINA A SINISTRA
			Position leftPosition1 = new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-1);
			Position leftPosition2 = new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-2);
			
			if(map.containsKey(leftPosition1) && map.containsKey(leftPosition2)){
				PawnClass pawnLeft1 = map.get(leftPosition1);
				PawnClass pawnLeft2 = map.get(leftPosition2);
				
				if(pawnLeft1.getType().equalsPawn(Pawn.KING.toString()) 
						&& (kingPosition.equals(castle)
						|| !kingNotAdjacent(kingPosition))) {}	
				else if(pawnLeft1.getType().equalsPawn(Pawn.WHITE.toString())
						|| pawnLeft1.getType().equalsPawn(Pawn.KING.toString())
						&& pawnLeft2.getType().equalsPawn(Pawn.BLACK.toString())){
							
					map.remove(leftPosition1);
					taken = true;
				}
			}
		
			
			//black-white-citadels/castle
			//CATTURO PEDINA SOPRA
			
			if(map.containsKey(abovePosition1) && (citadels.contains(abovePosition2) || castle.equals(abovePosition2)
					&& kingNotAdjacent(kingPosition) && !kingPosition.equals(castle))){
				PawnClass pawnAbove1 = map.get(abovePosition1);
								
				if(pawnAbove1.getType().equalsPawn(Pawn.WHITE.toString()) 
						|| pawnAbove1.getType().equalsPawn(Pawn.KING.toString())) {
					map.remove(abovePosition1);
					taken = true;
				}
			}	
					
				
			
			//CATTURO PEDINA SOTTO
					if(map.containsKey(belowPosition1) && (citadels.contains(belowPosition2) || castle.equals(belowPosition2)
						&& kingNotAdjacent(kingPosition) && !kingPosition.equals(castle))){
						PawnClass pawnBelow1 = map.get(belowPosition1);
						
						if(pawnBelow1.getType().equalsPawn(Pawn.WHITE.toString())
								|| pawnBelow1.getType().equalsPawn(Pawn.KING.toString())){
							map.remove(belowPosition1);
							taken = true;
						}
					}
		
			
			//CATTURO PEDINA A DESTRA
					
					if(map.containsKey(rightPosition1) && (citadels.contains(rightPosition2)|| castle.equals(rightPosition2)
							&& kingNotAdjacent(kingPosition) && !kingPosition.equals(castle))){
						PawnClass pawnRight1 = map.get(rightPosition1);
						if(pawnRight1.getType().equalsPawn(Pawn.WHITE.toString())
								|| pawnRight1.getType().equalsPawn(Pawn.KING.toString())) {
							map.remove(rightPosition1);
							taken = true;
						}
					}
					
					//CATTURO A SINISTRA
					if(map.containsKey(leftPosition1) && (citadels.contains(leftPosition2) || castle.equals(rightPosition2)
							&& kingNotAdjacent(kingPosition) && !kingPosition.equals(castle))){
						PawnClass pawnLeft1 = map.get(leftPosition1);
						if(pawnLeft1.getType().equalsPawn(Pawn.WHITE.toString())
								|| pawnLeft1.getType().equalsPawn(Pawn.KING.toString())) {
							map.remove(leftPosition1);
						taken = true;
						}
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
				taken = true;
				
				/*PARTITA VINTA*/
			}
			
			//black-kingAdjacent
			if(kingPosition.equals(a1) && map.containsKey(new Position(3,3)) && map.containsKey(new Position(3,5)) && map.containsKey(new Position(2,5))
					&& map.get(new Position(3,3)).getType().equalsPawn(Pawn.BLACK.toString())
					&& map.get(new Position(3,5)).getType().equalsPawn(Pawn.BLACK.toString())
					&& map.get(new Position(2,5)).getType().equalsPawn(Pawn.BLACK.toString())) {
				map.remove(kingPosition);
				taken = true;
				/*partita vinta*/
			}
			if(kingPosition.equals(a2) && map.containsKey(new Position(3,3)) && map.containsKey(new Position(5,3)) && map.containsKey(new Position(4,2))
					&& map.get(new Position(3,3)).getType().equalsPawn(Pawn.BLACK.toString())
					&& map.get(new Position(5,3)).getType().equalsPawn(Pawn.BLACK.toString())
					&& map.get(new Position(4,2)).getType().equalsPawn(Pawn.BLACK.toString())) {
				map.remove(kingPosition);
				taken = true;
				/*partita vinta*/
			}
			if(kingPosition.equals(a3) && map.containsKey(new Position(5,5)) && map.containsKey(new Position(3,5)) && map.containsKey(new Position(4,6))
					&& map.get(new Position(5,5)).getType().equalsPawn(Pawn.BLACK.toString())
					&& map.get(new Position(3,5)).getType().equalsPawn(Pawn.BLACK.toString())
					&& map.get(new Position(4,6)).getType().equalsPawn(Pawn.BLACK.toString())) {
				map.remove(kingPosition);
				taken = true;
				/*partita vinta*/
			}
			if(kingPosition.equals(a4) && map.containsKey(new Position(5,3)) && map.containsKey(new Position(5,5)) && map.containsKey(new Position(6,4))
					&& map.get(new Position(5,3)).getType().equalsPawn(Pawn.BLACK.toString())
					&& map.get(new Position(5,5)).getType().equalsPawn(Pawn.BLACK.toString())
					&& map.get(new Position(6,4)).getType().equalsPawn(Pawn.BLACK.toString())) {
				map.remove(kingPosition);
				taken = true;
				/*partita vinta*/
			}
			return map;
		}
		else return map;  //anche se il re non c'è più ritorna la mappa senza verificare nulla -> potrebbe causare altre esplorazioni nei nodi figli
	}
	
	
	public boolean kingNotAdjacent(Position kingPosition) {
		/*ritorna true se il re non è adiacente al castello*/
		if(!kingPosition.equals(a1) && !kingPosition.equals(a2) && !kingPosition.equals(a3) && !kingPosition.equals(a4)){
			return true;
		}else
			return false;
	}

	public ArrayList getNodesList() {
		return nodesList;
	}

	public void setNodesList(ArrayList nodesList) {
		this.nodesList = nodesList;
	}


	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public ArrayList<Position> getCitadels() {
		return citadels;
	}

	public void setCitadels(ArrayList<Position> citadels) {
		this.citadels = citadels;
	}

	public Position getCastle() {
		return castle;
	}

	public void setCastle(Position castle) {
		this.castle = castle;
	}
	
	

}