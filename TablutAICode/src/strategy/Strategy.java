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
	private int oldSizeMap = 0;
	

	private int MAXDEPTH = 4;
	private int MAXTIME = 59;
	
	private Strategy() {
		initCitadels();
		heuristicInstance.getInstance();
	}
	
	public static Strategy getInstance() {
		if(instance == null)
			instance = new Strategy();
		return instance;
	}
	/**
	 * this method verifies if a pawn has been ate. In that case it clears the old hashcode state list.
	 * @param root
	 * the node from where the method gets the actual map
	 */
	public void updateHashCodeStateList(Node root) {
		int sizeMap = root.getState().size();
		if(sizeMap != oldSizeMap) {
			hashCodeStateList.clear();
			oldSizeMap = sizeMap;
		}
		int stateHashCode = root.getState().hashCode(); 
		hashCodeStateList.add(stateHashCode);
	}
	
	/**
	 * Gets the first node walking through the parents.
	 * @param bestNode
	 * @return
	 */
	
	public Node getFirstNode(Node bestNode) {
		while(bestNode.getDepth() != 1) {
			bestNode = bestNode.getParent();
		}
		return bestNode;
	}
	
	public String[] getMove(String player) {
		
		startTime = System.currentTimeMillis();
		String[] move = new String[2];
		int depth = 0;
		this.player = player;
		int alfa = -4000;  //-infinito
		int beta = 4000;   //+infinito
		Node root = new Node();
		updateHashCodeStateList(root);
		BestNode alphaBetaBestNode = alphaBeta(root,depth,alfa,beta,true); 
		Node bestNode = alphaBetaBestNode.getNode();
		bestNode = getFirstNode(bestNode);
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
	/**
	 * Gets the actual pawn and moves it to the left, updating the state and verifying if a capture occurred. Then it creates and returns a new child of the actual node.
	 * @param pawn 
	 * @param actualNode
	 * @param alphaBetaDepth
	 * @param captured
	 * @param i
	 * @return
	 */
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
	/**
	 * Gets the actual pawn and moves it to the right, updating the state and verifying if a capture occurred. Then it creates and returns a new child of the actual node.
	 * @param pawn 
	 * @param actualNode
	 * @param alphaBetaDepth
	 * @param captured
	 * @param i
	 * @return
	 */
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
	/**
	 * Gets the actual pawn and moves it down, updating the state and verifying if a capture occurred. Then it creates and returns a new child of the actual node.
	 * @param pawn 
	 * @param actualNode
	 * @param alphaBetaDepth
	 * @param captured
	 * @param i
	 * @return
	 */
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
	/**
	 * Gets the actual pawn and moves it up, updating the state and verifying if a capture occurred. Then it creates and returns a new child of the actual node.
	 * @param pawn 
	 * @param actualNode
	 * @param alphaBetaDepth
	 * @param captured
	 * @param i
	 * @return
	 */
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
	/**
	 * updates alfa-beta values and verifies if the most recently calculated node is better than the actual best node. 
	 * @param alfa
	 * @param beta
	 * @param val
	 * @param childVal
	 * @param bestNodeMove
	 * @param childNode
	 * @param max
	 * @return
	 */
	public BestNode updateAlphaBetaValues(double alfa,double beta, double val,double childVal,BestNode bestNodeMove,BestNode childNode, boolean max) {
			
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
	
	
	public boolean pawnIsKing(PawnClass pawn) {
		if(pawn.getType().equalsPawn(Pawn.KING.toString())) {
			return true;
		}
		return false;	
	}
	
	public boolean playerIsWhite(Pawn color) {
		if(color.equalsPawn(Pawn.WHITE.toString()))
			return true;
		return false;
	}
	/**
	 * For each entry in the PawnMap the method verifies if it has the same pawn color of the player at this depth then,
	 *  moving the pawn in any direction it generates a new child at an higher depth and calls the alphaBeta method. Then
	 *  it updates all the alpha-beta values. If an alpha-beta cut occur the method returns.
	 * @param actualNode
	 * @param color
	 * @param alphaBetaDepth
	 * @param val
	 * @param alfa
	 * @param beta
	 * @param max
	 * @return
	 */
	public BestNode expandNodeAlphaBeta(Node actualNode, Pawn color,int alphaBetaDepth,double val,double alfa, double beta,boolean max) {
			
		boolean isKing = false;
		BestNode bestNodeMove = new BestNode(actualNode,val); 
		Map<Position,PawnClass> actualState = PawnMap.getInstance().cloneState(actualNode.getState());
		double childVal;
		
		for(Map.Entry<Position, PawnClass> entry : actualState.entrySet()) {	
		
			PawnClass pawn = entry.getValue();
			int captured = actualNode.getCaptured();
			if(pawnIsKing(pawn) && playerIsWhite(color)) 
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
						bestNodeMove = updateAlphaBetaValues(alfa, beta, val, childVal, bestNodeMove, childNode, max); //aggiorna alfa,beta,val e all'occasione bestNodeMove
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
						bestNodeMove = updateAlphaBetaValues(alfa, beta, val, childVal, bestNodeMove, childNode, max); //aggiorna alfa,beta,val e all'occasione bestNodeMove
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
						bestNodeMove = updateAlphaBetaValues(alfa, beta, val, childVal, bestNodeMove, childNode, max); //aggiorna alfa,beta,val e all'occasione bestNodeMove
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
						bestNodeMove = updateAlphaBetaValues(alfa, beta, val, childVal, bestNodeMove, childNode, max); //aggiorna alfa,beta,val e all'occasione bestNodeMove
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
	
	public Pawn setMaxColor(String player) {
		if(player.equals("white"))
			return Pawn.WHITE;
		else
			return Pawn.BLACK;
	}
	public Pawn setMinColor(String player) {
		if(player.equals("white"))
			return Pawn.BLACK;
		else
			return Pawn.WHITE;
	}
	
	public boolean isLeaf(int depth,Node node) {
		if(depth == MAXDEPTH || whiteWin(node) || blackWin(node))
			return true;
		return false;
	}
	
	public BestNode alphaBeta(Node node,int depth,double alfa,double beta, boolean max) {
		actualTime = System.currentTimeMillis();
		elapsedTime = (actualTime-startTime)/1000;
		BestNode bestNodeMove = new BestNode(node, 0);  
		
		while(elapsedTime < MAXTIME) {
			
			double val;
			int childCounter = 0;
			Pawn maxColor = setMaxColor(player);
			Pawn minColor = setMinColor(player);
			
			if(max) {
				val = -3000;
				bestNodeMove.setVal(val);
				if(isLeaf(depth,node)) { 
					val = Heuristic.getInstance().evaluateNode(node,maxColor);
					bestNodeMove = new BestNode(node,val); 
					return bestNodeMove;
				}
				else { 
					BestNode childNode = expandNodeAlphaBeta(node, maxColor, depth, val, alfa, beta,max); 
					double childVal = childNode.getVal();
					if(childVal > bestNodeMove.getVal())
						bestNodeMove = childNode;
				} 
				
				return bestNodeMove;
			}
			else {
				val = +3000;
				bestNodeMove.setVal(val);
				if(isLeaf(depth,node)) { 
					val = Heuristic.getInstance().evaluateNode(node,maxColor);  
					return new BestNode(node,val);
				}
				else { 
					BestNode childNode = expandNodeAlphaBeta(node, minColor, depth, val, alfa, beta,max); 
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
	
	/**
	 * Converts the coordinates from integer to the algebric notation
	 * @param pos
	 * @return
	 */
	public String convertCoordinates(Position pos) {
		String result = "";
		int row = pos.getRow()+1;  //rows start from 1, not from 0
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
	/**
	 * Takes the old coordinates of the pawn and update the state to the new configuration.
	 * @param state
	 * @param pawn
	 * @param newRow
	 * @param newColumn
	 * @return
	 */
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

	public boolean pawnIsWhite(PawnClass pawn) {
		Pawn type = pawn.getType();
		if(type.equalsPawn(Pawn.WHITE.toString()) || pawnIsKing(pawn))
			return true;
		return false;
	}
	public boolean pawnIsBlack(PawnClass pawn) {
		Pawn type = pawn.getType();
		if(type.equalsPawn(Pawn.BLACK.toString()))
				return true;
		return false;
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
		
		if(pawnIsWhite(pawn)) {
			return captureVerificationWhite(map, newPawnPosition);
		}
		else {
			return captureVerificationBlack(map, newPawnPosition);
		}
	}
	
	public boolean mapContains(Map<Position,PawnClass> map,Position position1,Position position2) {
		if(map.containsKey(position1) && map.containsKey(position2)) {
			return true;
		}
		return false;
	}
	
	public boolean isCitadelOrCastle(Position position) {
		if(citadels.contains(position) || castle.equals(position)) 
			return true;
		return false;
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
		Position abovePosition1 = new Position(newPawnPosition.getRow()-1, newPawnPosition.getColumn());
		Position abovePosition2 = new Position(newPawnPosition.getRow()-2, newPawnPosition.getColumn());
		
		if(mapContains(map,abovePosition1,abovePosition2)){
			PawnClass pawnAbove1 = map.get(abovePosition1);
			PawnClass pawnAbove2 = map.get(abovePosition2);
			if(pawnIsBlack(pawnAbove1) && pawnIsWhite(pawnAbove2)) {	
				map.remove(abovePosition1);
				taken = true;
			}
		}
		
		//CATTURO PEDINA SUBITO SOTTO
		Position belowPosition1 = new Position(newPawnPosition.getRow()+1, newPawnPosition.getColumn());
		Position belowPosition2 = new Position(newPawnPosition.getRow()+2, newPawnPosition.getColumn());
		
		if(mapContains(map,belowPosition1,belowPosition2)){
			PawnClass pawnBelow1 = map.get(belowPosition1);
			PawnClass pawnBelow2 = map.get(belowPosition2);
			if(pawnIsBlack(pawnBelow1) && pawnIsWhite(pawnBelow2)) {			
				map.remove(belowPosition1);
				taken = true;
			}
		}
		
		//CATTURO PEDINA A DESTRA
		Position rightPosition1 = new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+1);
		Position rightPosition2 = new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()+2);
		
		if(mapContains(map,rightPosition1,rightPosition2)) {
			PawnClass pawnRight1 = map.get(rightPosition1);
			PawnClass pawnRight2 = map.get(rightPosition2);
			if(pawnIsBlack(pawnRight1) && pawnIsWhite(pawnRight2)) {		
					map.remove(rightPosition1);
					taken = true;
				}
		}
		
		//CATTURO A SINISTRA
		Position leftPosition1 = new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-1);
		Position leftPosition2 = new Position(newPawnPosition.getRow(), newPawnPosition.getColumn()-2);
		
		if(mapContains(map,leftPosition1,leftPosition2)){
			PawnClass pawnLeft1 = map.get(leftPosition1);
			PawnClass pawnLeft2 = map.get(leftPosition2);
				
			if(pawnIsBlack(pawnLeft1) && pawnIsWhite(pawnLeft2)) {		
				map.remove(leftPosition1);
				taken = true;
			}
		}
		
		
		
		//bianco-nero-citadels && bianco-nero-castle
		/*per verificare la cattura verifico se citadels un elemnto che si trova due posizioni sopra la pedina
		 * considerata*/
		//CATTURO LA PEDINA SUBITO SOPRA
		
		if(map.containsKey(abovePosition1) && isCitadelOrCastle(abovePosition2)){
			PawnClass pawnAbove1 = map.get(abovePosition1);
							
			if(pawnIsBlack(pawnAbove1) && !isCitadelOrCastle(abovePosition1)) {
				map.remove(abovePosition1);
				taken = true;
			}
		}	
		
		//CATTURO PEDINA SUBITO SOTTO
		if(map.containsKey(belowPosition1) && isCitadelOrCastle(belowPosition2)){
			PawnClass pawnBelow1 = map.get(belowPosition1);
			
			if(pawnIsBlack(pawnBelow1) && !isCitadelOrCastle(belowPosition1)) {
				map.remove(belowPosition1);
				taken = true;
			}
		}
		
		//CATTURO PEDINA A DESTRA
		if(map.containsKey(rightPosition1) && isCitadelOrCastle(rightPosition2)){
			PawnClass pawnRight1 = map.get(rightPosition1);
			if(pawnIsBlack(pawnRight1) && !isCitadelOrCastle(rightPosition1)) {
				map.remove(rightPosition1);
				taken = true;
			}
		}
		
		//CATTURO A SINISTRA
		if(map.containsKey(leftPosition1) && isCitadelOrCastle(leftPosition2)){
			PawnClass pawnLeft1 = map.get(leftPosition1);
			if(pawnIsBlack(pawnLeft1) && !isCitadelOrCastle(leftPosition1)) {
				map.remove(leftPosition1);
			taken = true;
			}
		}
		return map;
	}
	
	public boolean isKingInOrAdjacentCastle(Position kingPosition) {
		if(kingPosition.equals(castle) || !kingNotAdjacent(kingPosition))
			return true;
		return false;
	}
	
	public boolean isCastleSieged(Map<Position,PawnClass> map) {
		if(map.containsKey(a1) && map.containsKey(a2) && map.containsKey(a3) && map.containsKey(a4)
				&& pawnIsBlack(map.get(a1))
				&& pawnIsBlack(map.get(a2))
				&& pawnIsBlack(map.get(a3))
				&& pawnIsBlack(map.get(a4)))
			return true;
		return false;
	}
	
	public boolean isKing3SidesBlockedUp(Position kingPosition,Map<Position,PawnClass> map) {
		Position p1 = new Position(3,3);
		Position p2 = new Position(3,5);
		Position p3 = new Position(2,3);
		if(kingPosition.equals(a1) && map.containsKey(p1) && map.containsKey(p2) && map.containsKey(p3)
			&& pawnIsBlack(map.get(p1)) && pawnIsBlack(map.get(p2)) && pawnIsBlack(map.get(p3)))
			return true;
		
		return false;
	}
	public boolean isKing3SidesBlockedDown(Position kingPosition,Map<Position,PawnClass> map) {
		Position p1 = new Position(5,3);
		Position p2 = new Position(5,5);
		Position p3 = new Position(6,4);
		if(kingPosition.equals(a4) && map.containsKey(p1) && map.containsKey(p2) && map.containsKey(p3)
			&& pawnIsBlack(map.get(p1)) && pawnIsBlack(map.get(p2)) && pawnIsBlack(map.get(p3)))
			return true;
		
			return false;
		}
	public boolean isKing3SidesBlockedLeft(Position kingPosition,Map<Position,PawnClass> map) {
		Position p1 = new Position(3,3);
		Position p2 = new Position(5,3);
		Position p3 = new Position(6,4);
		if(kingPosition.equals(a2) && map.containsKey(p1) && map.containsKey(p2) && map.containsKey(p3)
			&& pawnIsBlack(map.get(p1)) && pawnIsBlack(map.get(p2)) && pawnIsBlack(map.get(p3)))
			return true;
		
		return false;
	}
	public boolean isKing3SidesBlockedRight(Position kingPosition,Map<Position,PawnClass> map) {
		Position p1 = new Position(5,5);
		Position p2 = new Position(3,5);
		Position p3 = new Position(4,6);
		if(kingPosition.equals(a3) && map.containsKey(p1) && map.containsKey(p2) && map.containsKey(p3)
			&& pawnIsBlack(map.get(p1)) && pawnIsBlack(map.get(p2)) && pawnIsBlack(map.get(p3)))
			return true;
		
		return false;
	}
	public Map<Position, PawnClass> captureVerificationBlack(Map<Position,PawnClass> map, Position newPawnPosition) {
		Position kingPosition = PawnMap.getInstance().findKingPosition(map);
		if(kingPosition != null) {
			//black-white/king-black
			Position abovePosition1 = new Position(newPawnPosition.getRow()-1, newPawnPosition.getColumn());
			Position abovePosition2 = new Position(newPawnPosition.getRow()-2, newPawnPosition.getColumn());
			
			//CATTURO PEDINA SOPRA
			if(map.containsKey(abovePosition1) && map.containsKey(abovePosition2)) {
					
				PawnClass pawnAbove1 = map.get(abovePosition1);
				PawnClass pawnAbove2 = map.get(abovePosition2);
				
				if(pawnIsKing(pawnAbove1) && isKingInOrAdjacentCastle(kingPosition)) {	
					//if the king is inside or adjacent the castle it can't be taken 
				}
				else if (pawnIsWhite(pawnAbove1) && pawnIsBlack(pawnAbove2)) {
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
				
				if(pawnIsKing(pawnBelow1) && isKingInOrAdjacentCastle(kingPosition)) {	
					//if the king is inside or adjacent the castle it can't be taken 
				}
				else if (pawnIsWhite(pawnBelow1) && pawnIsBlack(pawnBelow2)) {
							
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
				
				if(pawnIsKing(pawnRight1) && isKingInOrAdjacentCastle(kingPosition)) {	
					//if the king is inside or adjacent the castle it can't be taken 
				}
				else if (pawnIsWhite(pawnRight1) && pawnIsBlack(pawnRight2)) {
							
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
				
				if(pawnIsKing(pawnLeft1) && isKingInOrAdjacentCastle(kingPosition)) {	
					//if the king is inside or adjacent the castle it can't be taken 
				}
				else if (pawnIsWhite(pawnLeft1) && pawnIsBlack(pawnLeft2)) {
					map.remove(leftPosition1);
					taken = true;
				}
			}
		
			
			//black-white-citadels/castle
			//CATTURO PEDINA SOPRA
			
			if(map.containsKey(abovePosition1) && isCitadelOrCastle(abovePosition2) && !isKingInOrAdjacentCastle(kingPosition)){
				PawnClass pawnAbove1 = map.get(abovePosition1);
							
				if(pawnIsWhite(pawnAbove1)) {
					map.remove(abovePosition1);
					taken = true;
				}
			}	
						
			//CATTURO PEDINA SOTTO
			if(map.containsKey(belowPosition1) && isCitadelOrCastle(belowPosition2) && !isKingInOrAdjacentCastle(kingPosition)){
				PawnClass pawnBelow1 = map.get(belowPosition1);
				
				if(pawnIsWhite(pawnBelow1)){
					map.remove(belowPosition1);
					taken = true;
				}
			}

	//CATTURO PEDINA A DESTRA
			
			if(map.containsKey(rightPosition1) && isCitadelOrCastle(rightPosition2) && !isKingInOrAdjacentCastle(kingPosition)){
				PawnClass pawnRight1 = map.get(rightPosition1);
				if(pawnIsWhite(pawnRight1)) {
					map.remove(rightPosition1);
					taken = true;
				}
			}
			
			//CATTURO A SINISTRA
			if(map.containsKey(leftPosition1) && isCitadelOrCastle(leftPosition2) && !isKingInOrAdjacentCastle(kingPosition)){
				PawnClass pawnLeft1 = map.get(leftPosition1);
				if(pawnIsWhite(pawnLeft1)) {
					map.remove(leftPosition1);
					taken = true;
				}
			}
						
			//KING IN CASTLE
			/*devo circondare il re sui 4 lati*/
			if(kingPosition.equals(castle) && isCastleSieged(map)) {
				map.remove(kingPosition);
				taken = true;
				/*PARTITA VINTA*/
			}
			
			//KING ADJACENT THE CASTLE
			if(isKing3SidesBlockedUp(kingPosition, map)) {
				map.remove(kingPosition);
				taken = true;
				/*partita vinta*/
			}
			if(isKing3SidesBlockedLeft(kingPosition, map)) {
				map.remove(kingPosition);
				taken = true;
				/*partita vinta*/
			}
			if(isKing3SidesBlockedRight(kingPosition, map)) {
				map.remove(kingPosition);
				taken = true;
				/*partita vinta*/
			}
			if(isKing3SidesBlockedDown(kingPosition, map)) {
				map.remove(kingPosition);
				taken = true;
				/*partita vinta*/
			}
			return map;
		}
		else return map; 
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

	public int getMAXTIME() {
		return MAXTIME;
	}

	public void setMAXTIME(int mAXTIME) {
		MAXTIME = mAXTIME;
	}

	public void setMAXDEPTH(int mAXDEPTH) {
		MAXDEPTH = mAXDEPTH;
	}
}