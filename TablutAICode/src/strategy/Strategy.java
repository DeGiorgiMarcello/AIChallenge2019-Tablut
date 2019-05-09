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
	private ArrayList hashCodeStateList = new ArrayList<Integer>();
	private String player; 
	private ArrayList<Position> citadels = new ArrayList<Position>();
	private Position castle = new Position(4,4);
	private Position a1 = new Position(3,4);
	private Position a2 = new Position(4,3);
	private Position a3 = new Position(4,5);
	private Position a4 = new Position(5,4);
	private boolean taken = false; //variabile per tener conto dell'avvenuta cattura e aggiornare i nodi figli
	private Heuristic heuristicInstance;
	private long startTime;
	private long actualTime;
	private long elapsedTime;
	

	final int MAXDEPTH = 3;
	final int MAXTIME = 58;
		
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
		startTime = System.currentTimeMillis();
		String[] move = new String[2];
		int depth = 0;
		this.player = player;
		int alfa = -3000;  //-infinito
		int beta = 3000;   //+infinito
		Node root = new Node();
		int stateHashCode = root.getState().hashCode(); 
		hashCodeStateList.add(stateHashCode);
		BestNode alphaBetaBestNode = alphaBeta(root,depth,alfa,beta,true);  //si inizializza con la root
		Node bestNode = alphaBetaBestNode.getNode();
		
		while(bestNode.getDepth() != 1) {
			bestNode = bestNode.getParent();
		}
		move[0] = bestNode.getPawnMoveFrom();
		move[1] = bestNode.getPawnMoveTo();
		
		return move;
	}
	
	public boolean isCutable(double alfa,double beta) {
		if(beta <= alfa)
			return true;
		else
			return false;
	}
	
	public Node movePawnLeft(PawnClass pawn,Node actualNode,int alphaBetaDepth,int captured,int i) {
		Map newState = PawnMap.getInstance().cloneState(actualNode.getState());
		Position oldPos = new Position(pawn.getRow(),pawn.getColumn());
		Position newPos = new Position(pawn.getRow(),pawn.getColumn()-i);
		updateState(newState,pawn,pawn.getRow(),pawn.getColumn()-i);
		captureVerification(newState, newPos);    
		if(taken) {
			captured++;
			taken = false;
		}
		String moveFrom = convertCoordinates(oldPos);
		String moveTo = convertCoordinates(newPos);
		Node child = new Node(alphaBetaDepth+1,newState,actualNode,captured,moveFrom,moveTo);
		return child;
	}
	public Node movePawnRight(PawnClass pawn,Node actualNode,int alphaBetaDepth,int captured,int i) {
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
		return child;
	}
	public Node movePawnDown(PawnClass pawn,Node actualNode,int alphaBetaDepth,int captured,int i) {
		Map newState = PawnMap.getInstance().cloneState(actualNode.getState());
		Position oldPos = new Position(pawn.getRow(),pawn.getColumn());
		Position newPos = new Position(pawn.getRow()+i,pawn.getColumn());
		updateState(newState,pawn,pawn.getRow()+i,pawn.getColumn());
		captureVerification(newState, newPos);    
		if(taken) {
			captured++;
			taken = false;
		}
		String moveFrom = convertCoordinates(oldPos);
		String moveTo = convertCoordinates(newPos);
		Node child = new Node(alphaBetaDepth+1,newState,actualNode,captured,moveFrom,moveTo);
		return child;
	}
	public Node movePawnUp(PawnClass pawn,Node actualNode,int alphaBetaDepth,int captured,int i) {
		Map newState = PawnMap.getInstance().cloneState(actualNode.getState());
		Position oldPos = new Position(pawn.getRow(),pawn.getColumn());
		Position newPos = new Position(pawn.getRow()-i,pawn.getColumn());
		updateState(newState,pawn,pawn.getRow()-i,pawn.getColumn());
		captureVerification(newState, newPos);    
		if(taken) {
			captured++;
			taken = false;
		}
		String moveFrom = convertCoordinates(oldPos);
		String moveTo = convertCoordinates(newPos);
		Node child = new Node(alphaBetaDepth+1,newState,actualNode,captured,moveFrom,moveTo);
		return child;
	}
	
	public BestNode updateAlfaBetaValues(double alfa,double beta, double val,double childVal,BestNode bestNodeMove,BestNode childNode, boolean max) {
			
		if(max) {											
			if(childVal > bestNodeMove.getVal())
				bestNodeMove = childNode;
			val = Math.max(val, childVal);
			alfa = Math.max(alfa, val);	
		}
		else {
			if(childVal < bestNodeMove.getVal())
				bestNodeMove = childNode;
			val = Math.min(val, childVal);
			beta = Math.min(beta, val);
		}
		
		return new BestNode(bestNodeMove.getNode(),bestNodeMove.getVal(),alfa,beta,val);
	}
	
	public BestNode expandNodeAlphaBeta(Node actualNode, Pawn color,int alphaBetaDepth,double val,double alfa, double beta,boolean max) {
			
		boolean isKing = false;
		BestNode bestNodeMove = new BestNode(actualNode,val); 
		Map<Position,PawnClass> actualState = PawnMap.getInstance().cloneState(actualNode.getState());
		double childVal;
		
		for(Map.Entry<Position, PawnClass> entry : actualState.entrySet()) {	
		
			PawnClass pawn = entry.getValue();
			int captured = actualNode.getCaptured();
			if(pawn.getType().equalsPawn(Pawn.KING.toString()) && color.equalsPawn(Pawn.WHITE.toString())) 
				isKing = true;
			if(pawn.getType().equals(color) || isKing) {
						
				//MOVE LEFT
				for(int i=pawn.maxNumberBoxMoveLeft(actualState);i>0;i--){
					Node child = movePawnLeft(pawn,actualNode,alphaBetaDepth,captured,i);
					int stateHashCode = child.getState().hashCode();
					if(!hashCodeStateList.contains(stateHashCode)) {   
						hashCodeStateList.add(stateHashCode);
						BestNode childNode = alphaBeta(child,alphaBetaDepth+1,alfa,beta,!max);
						childVal = childNode.getVal();
						bestNodeMove = updateAlfaBetaValues(alfa, beta, val, childVal, bestNodeMove, childNode, max); //aggiorna alfa,beta,val e all'occasione bestNodeMove
						val = bestNodeMove.getAlfaBetaVal();
						alfa = bestNodeMove.getAlfa();
						beta = bestNodeMove.getBeta();
						if(isCutable(alfa,beta)) 
							return bestNodeMove;
					}				
				}
				
				//MOVE RIGHT
	
				for(int i=pawn.maxNumberBoxMoveRight(actualState);i>0;i--){
					Node child = movePawnRight(pawn,actualNode,alphaBetaDepth,captured,i);
					int stateHashCode = child.getState().hashCode();
					if(!hashCodeStateList.contains(stateHashCode)) {   
						hashCodeStateList.add(stateHashCode);
						BestNode childNode = alphaBeta(child,alphaBetaDepth+1,alfa,beta,!max);
						childVal = childNode.getVal();
						bestNodeMove = updateAlfaBetaValues(alfa, beta, val, childVal, bestNodeMove, childNode, max); //aggiorna alfa,beta,val e all'occasione bestNodeMove
						val = bestNodeMove.getAlfaBetaVal();
						alfa = bestNodeMove.getAlfa();
						beta = bestNodeMove.getBeta();
						if(isCutable(alfa,beta)) 
							return bestNodeMove;
					}				
				}
				//MOVE UP
				
				for(int i=pawn.maxNumberBoxMoveUp(actualState);i>0;i--){
					Node child = movePawnUp(pawn,actualNode,alphaBetaDepth,captured,i);
					int stateHashCode = child.getState().hashCode();
					if(!hashCodeStateList.contains(stateHashCode)) {   
						hashCodeStateList.add(stateHashCode);
						BestNode childNode = alphaBeta(child,alphaBetaDepth+1,alfa,beta,!max);
						childVal = childNode.getVal();
						bestNodeMove = updateAlfaBetaValues(alfa, beta, val, childVal, bestNodeMove, childNode, max); //aggiorna alfa,beta,val e all'occasione bestNodeMove
						val = bestNodeMove.getAlfaBetaVal();
						alfa = bestNodeMove.getAlfa();
						beta = bestNodeMove.getBeta();
						if(isCutable(alfa,beta)) 
							return bestNodeMove;
					}				
				}
				//MOVE DOWN
				
				for(int i=pawn.maxNumberBoxMoveDown(actualState);i>0;i--){
					Node child = movePawnDown(pawn,actualNode,alphaBetaDepth,captured,i);
					int stateHashCode = child.getState().hashCode();
					if(!hashCodeStateList.contains(stateHashCode)) {   
						hashCodeStateList.add(stateHashCode);
						BestNode childNode = alphaBeta(child,alphaBetaDepth+1,alfa,beta,!max);
						childVal = childNode.getVal();
						bestNodeMove = updateAlfaBetaValues(alfa, beta, val, childVal, bestNodeMove, childNode, max); //aggiorna alfa,beta,val e all'occasione bestNodeMove
						val = bestNodeMove.getAlfaBetaVal();
						alfa = bestNodeMove.getAlfa();
						beta = bestNodeMove.getBeta();
						if(isCutable(alfa,beta)) 
							return bestNodeMove;
					}				
				}
			}
			isKing = false;
		}
		
		return bestNodeMove;
	}
	
public BestNode alphaBeta(Node node,int depth,double alfa,double beta, boolean max) {
	actualTime = System.currentTimeMillis();
	elapsedTime = (actualTime-startTime)/1000;
	BestNode bestNodeMove = new BestNode(node, 0);
	
	while(elapsedTime < MAXTIME) {
		
		double val;
		int childCounter = 0;
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
			val = -3000;
			bestNodeMove.setVal(val);
			if(depth == MAXDEPTH || whiteWin(node) || blackWin(node)) { //NODI FOGLIA!
				//con la funzione euristica si assegna il valore al nodo
				val = Heuristic.getInstance().evaluateNode(node,maxColor);
				bestNodeMove = new BestNode(node,val); 
				return bestNodeMove;
			}
			else { 
				
				BestNode childNode = expandNodeAlphaBeta(node, maxColor, depth, val, alfa, beta,max); //c'era true e depth -val
				double childVal = childNode.getVal();
				if(childVal > bestNodeMove.getVal())
					bestNodeMove = childNode;
			} 
			
			return bestNodeMove;
		}
		else {
			val = +3000;
			bestNodeMove.setVal(val);
			if(depth == MAXDEPTH || whiteWin(node) || blackWin(node)) { //NODI FOGLIA!
				//con la funzione euristica si assegna il valore al nodo
				val = Heuristic.getInstance().evaluateNode(node,maxColor);  
				return new BestNode(node,val);
			}
			else { 
				//se nella lista dei nodi da espandere non ci sono figli di questo nodo, espandilo!
				BestNode childNode = expandNodeAlphaBeta(node, minColor, depth, val, alfa, beta,max); //c'era true -
				double childVal = childNode.getVal();
				if(childVal < bestNodeMove.getVal())
					bestNodeMove = childNode;
			}
			return bestNodeMove;
		}
	}
	System.out.println("Depth: "+depth+"Time: "+elapsedTime);
	return bestNodeMove;
}
	
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
	
	public boolean whiteWin(Node node) {
		Map<Position, PawnClass> currentState = node.getState();
		Position kingPosition = PawnMap.getInstance().findKingPosition(currentState);
		if(!(kingPosition == null)) {
			if(Heuristic.getInstance().getEscapePoints().contains(kingPosition))
				return true;
		}
		return false;
	}
	
	public boolean blackWin(Node node) {
		Map<Position, PawnClass> currentState = node.getState();
		Position kingPosition = PawnMap.getInstance().findKingPosition(currentState);
		if(kingPosition == null)
			return true;
		else
			return false;
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
	
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	

}