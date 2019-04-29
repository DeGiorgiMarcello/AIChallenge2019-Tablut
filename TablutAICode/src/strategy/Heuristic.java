package strategy;

import java.util.ArrayList;
import java.util.Map;

import domain.State;
import domain.State.Pawn;
import domain.StateTablut;
import util.Node;
import util.PawnClass;
import util.PawnMap;
import util.Position;

public class Heuristic {
	private static Heuristic instance;
	private ArrayList<Position> escapePoints = new ArrayList();
	private Position escape = null; //salvo la posizione del punto di fuga più vicino
	private Position castle = new Position(4,4);
	/*posizioni adiacenti al castello*/
	private ArrayList<Position> adjacentPointsCastle = new ArrayList();
	
	public static Heuristic getInstance() {
		if(instance == null) {
			instance = new Heuristic();
		}
		return instance;
	}
	
	private Heuristic() {
		/*inizializzo la lista solo la prima volta che chiamo la classse Heuristic*/
		initEscapePoint();
		initAdjacentPointsCaste();
	}
	
	public int evaluateNode(Node node) {
		Position position = convertLetterToInt(node.getPawnMoveTo());
		PawnClass pawn = (PawnClass) node.getState().get(position);
		if(pawn.getType().equalsPawn(Pawn.WHITE.toString()) || pawn.getType().equalsPawn(Pawn.KING.toString()) ) {
			return whiteHeuristic(node);
		}else {
			return BlackHeuristic(node);
		}
	}
	
	public int whiteHeuristic(Node node) {
		int sum = 0;
		Position king = PawnMap.getInstance().findKingPosition(node.getState());
		final int capturedBlack = 1;
		final int capturedWhite = -1;
		final int protectedKingOneSide = 3;
		final int protectedKingTwoSide = 6;
		final int protectedKingThreeSide = 4;
		final int protectedKingFourSide = 2;
		final int distanceEscapePoint = 1;
		final int rowColumnFree = 40;
		final int kingCaptured = -100;
		final int win = 100;
		
		if(king == null) { //se kingPosition è null il king non c'è, ho perso.
			return -200;
		}else {
			//AVENUTA CATTURA
			int valCapturedBlack = numberOfPawnCaptured(node, Pawn.BLACK); //verifica se ho mangiato pedine avversarie
			int valCapturedWhite = numberOfPawnCaptured(node, Pawn.WHITE); //verifica se sono state mangiate delle mie pedine
			//RE PROTETTO aggiungo 1 unità al risultato per ogni lato su cui il re è protetto
			int[] valProtectedKing = kingProtected(node, king, Pawn.WHITE);
			int valProtectedKingOneSide = valProtectedKing[0];
			int valProtectedKingTwoSide = valProtectedKing[1];
			int valProtectedKingThreeSide = valProtectedKing[2];
			int valProtectedKingFourSide = valProtectedKing[3];
			//DISTANZA DEL RE DAL PUNTO DI FUGA PIù VICINO
			int valDistanceEscapePoint = distanceBetweenKingEscape(king);
			
			//RE HA RIGA/COLONNA LIBERA VERSO UN PUNTO DI FUGA
			int valRowColumnFree = freeEscapeRoute(node, king);
			
			//PEDINA VIENE CATTURATA
			
			//RE VIENE CATTURATO
			int valKingCaptured = kingCaptured(node);
			//RE IN UN PUNTO DI FUGA => VITTORIA
			int valwin = kingInEscapePoint(king);
			/*CALCOLARE SOMMA PESATA*/
			return capturedBlack*valCapturedBlack+capturedWhite*valCapturedWhite+protectedKingOneSide*valProtectedKingOneSide+
					protectedKingTwoSide*valProtectedKingTwoSide+protectedKingThreeSide*valProtectedKingThreeSide+
					protectedKingFourSide*valProtectedKingFourSide+distanceEscapePoint*valDistanceEscapePoint+
					rowColumnFree*valRowColumnFree+kingCaptured*valKingCaptured+win*valwin+node.getDepth();
		}
		
	}
	
	public int BlackHeuristic(Node node) {
		Position king = PawnMap.getInstance().findKingPosition(node.getState());
		int sum = 0;
		final int capturedWhite = 1;
		final int capturedBlack = -1;
		final int kingTrappedOneSide = 2;
		final int kingTrappedTwoSide = 3;
		final int kingTrappedThreeSide = 4;
		final int kingTrappedFourSide = 6;
		final int escapePointBlocked = 40;
		final int kingcaptured = 100;
		final int kingWin = -100;
		
		if(king == null) {
			return 200;
		}else {
			//AVVENUTA CATTURA
			int valCapturedWhite = numberOfPawnCaptured(node, Pawn.WHITE); //verifica se ho catturato pedine avversarie
			int valCapturedBlack = numberOfPawnCaptured(node, Pawn.BLACK); //verifica se sono state catturate le mie pedine
			
			//re chiuso sui lati
			int[] valKingTrapped = kingProtected(node, king, Pawn.BLACK);
			int valKingTrappedOneSide = valKingTrapped[0];
			int valKingTrappedTwoSide = valKingTrapped[1];
			int valKingTrappedThreeSide = valKingTrapped[2];
			int valKingTrappedFourSide = valKingTrapped[3];
			//numeor di vie di fuga bloccate al re
			int valEscapePointBlocked = blockEscapeRoute(node, king);
			
			//vedere se ho catturato il re
			int valKingcaptured = kingCaptured(node);
			//vedere se il re è in un punto di fuga
			int valKingWin = kingInEscapePoint(king);
			return capturedWhite*valCapturedWhite+capturedBlack*valCapturedBlack+kingTrappedOneSide*valKingTrappedOneSide+
					kingTrappedTwoSide*valKingTrappedTwoSide+kingTrappedThreeSide*valKingTrappedThreeSide+kingTrappedFourSide*valKingTrappedFourSide
					+escapePointBlocked*valEscapePointBlocked+kingcaptured*valKingcaptured+kingWin*valKingWin+node.getDepth();
		}
		
	}
	
	/*public int possibleCaptureKingInOneMove(Node node, Position king, int valKingTrappeThreeSide, int valKingTrappeOneSide, int valKingTrappeTwoSide) {
		int result = 0;
		Map<Position, PawnClass> map = node.getState();
		if(king.equals(castle)) {
			//se il re è nel castello lo devo circondare su 4 lati per vincere
			
			if(valKingTrappeThreeSide == 1) {
				//il re ha tre pedine nere su tre lati, verifico se il quarto lato è libero
				for(Position position: adjacentPointsCastle) {
					if(!(map.containsKey(position) && map.get(position).getType().equalsPawn(Pawn.BLACK.toString()))) {
						//o la casella è vuota o c'è una pedina bianca
						if(!map.containsKey(position)) {
							//la casella è libera
							result = 1;
						}
					}
				}
			}
		}else {
			if(adjacentPointsCastle.contains(king)) {
				//re si trova adiacente al castello
				if(valKingTrappeTwoSide == 1) {
					if(map.containsKey() && map.get(position).getType().equalsPawn(Pawn.BLACK.toString())) {
						
					}
				}
			}else {
				//re in un'altra posizione 
			}
		}
	}*/
	
	public int numberOfPawnCaptured(Node node, Pawn pawn) {
		int result = 0;
		int pawnParent = 0;
		int pawnChild = 0;
		Map<Position, PawnClass> rootState = PawnMap.getInstance().getMap();
		//System.out.println("\nfrom Heuristic "+rootState.size());
		//Map<Position, PawnClass> parentState = node.getParent().getState();
		pawnParent = numberOfPawn(rootState, pawn);
		pawnChild = numberOfPawn(node.getState(), pawn);
		result = pawnParent - pawnChild;
		return result;
	}
	
	public int numberOfPawn(Map<Position, PawnClass> map, Pawn pawn) {
		int cont = 0;
		boolean white = false;
		if(pawn.equalsPawn(Pawn.WHITE.toString()))
			white = true;
		for(Map.Entry<Position, PawnClass> entry: map.entrySet()) {
			PawnClass currentPawn = entry.getValue();
			if(white) {
				if(currentPawn.getType().equalsPawn(Pawn.WHITE.toString()) || currentPawn.getType().equalsPawn(Pawn.KING.toString()))
					cont++;
			}else {
				if(currentPawn.getType().equalsPawn(Pawn.BLACK.toString())) 
					cont ++;
			}
		}
		return cont;
	}
	
	public int kingCaptured(Node node) {
		/*verifica se nella mappa c'è il re, se c'è (=> NON è STATO CATTURATO) ritorna 0
		 * altrimenti ritorna 1*/
		int result = 0;
		for(Map.Entry<Position, PawnClass> entry : node.getState().entrySet()) {
			PawnClass currentPawn = entry.getValue();
			if(currentPawn.getType().equalsPawn(Pawn.KING.toString())) {
				return result;
			}
		}
		result = 1;
		return result;
		
	}
	
	public int kingInEscapePoint(Position king) {
		for(int i = 0; i < escapePoints.size(); i++) {
			if(king.equals(escapePoints.get(i)))
				return 1;
		}
		return 0;
	}
	
	public int blockEscapeRoute(Node node, Position king) {
		/*RITORNA IL NUMERO DI VIE BLOCCATE*/
		/*devo vedere se sulla riga o colonna del re c'è una via di fuga
		 * RIGHE VIE DI FUGA 1 2 6 7
		 * COLONNE VIE DI FUGA 1 2 6 7*/
		int cont = 0;
		boolean block = false;
		if(king.getRow() == 1 || king.getRow() == 2 || king.getRow() == 6 || king.getRow() == 7) {
			/*il re è su una riga in corrispondenza di un punto di fuga*/
			//verifico sinistra libera
			for(int i = king.getColumn()-1; i >= 0; i--) {
				Position position = new Position(king.getRow(), i);
				if(node.getState().containsKey(position) && (node.getState().get(position).getType().equalsPawn(Pawn.BLACK.toString()) ||
						node.getState().get(position).getType().equalsPawn(Pawn.WHITE.toString()) ||
						position.equals(new Position(1,4)) || position.equals(new Position(7,4)))) {
					//pedina nera sulla via di fuga del re
					block = true;
					break;
				}	
			}
			if(block)
				cont++;
			block = false;
			//verifico destra libera
			for(int i = king.getColumn()+1; i<=8; i++) {
				Position position = new Position(king.getRow(), i);
				if(node.getState().containsKey(position) && (node.getState().get(position).getType().equalsPawn(Pawn.BLACK.toString()) ||
						node.getState().get(position).getType().equalsPawn(Pawn.WHITE.toString()) ||
						position.equals(new Position(1,4)) || position.equals(new Position(7,4)))) {
					//riga occupata
					block = true;
					break;
				}
			}
			if(block)
				cont++;
			block = false;
		}
		
		if(king.getColumn() == 1 || king.getColumn() == 2 || king.getColumn() == 6 || king.getColumn() == 7) {
			/*il re è su una colonna in corrispondenza di un punto di fuga*/
			//verifico sopra libera
			for(int i = king.getRow()-1; i >= 0; i--) {
				Position position = new Position(i, king.getColumn());
				if(node.getState().containsKey(position) && (node.getState().get(position).getType().equalsPawn(Pawn.BLACK.toString()) ||
						node.getState().get(position).getType().equalsPawn(Pawn.WHITE.toString()) ||
						position.equals(new Position(4,1)) || position.equals(new Position(4,7)))) {
					//colonna occupata
					block = true;
					break;
				}	
			}
			if(block)
				cont++;
			block = false;
			//verifico sotto libera
			for(int i = king.getRow()+1; i<=8; i++) {
				Position position = new Position(i, king.getColumn());
				if(node.getState().containsKey(position) && 
						(node.getState().get(position).getType().equalsPawn(Pawn.BLACK.toString()) ||
								node.getState().get(position).getType().equalsPawn(Pawn.WHITE.toString()) ||
								position.equals(new Position(4,1)) || position.equals(new Position(4,7)))) {
					//riga occupata
					block = true;
				}
			}
			if(block)
				cont++;
			block = false;
		}
		
		return cont;
	}
	
	public int freeEscapeRoute(Node node, Position king) {
		/*devo vedere se sulla riga o colonna del re c'è una via di fuga
		 * RIGHE VIE DI FUGA 1 2 6 7
		 * COLONNE VIE DI FUGA 1 2 6 7*/
		int cont = 0;
		boolean block = false;
		if(king.getRow() == 1 || king.getRow() == 2 || king.getRow() == 6 || king.getRow() == 7) {
			/*il re è su una riga in corrispondenza di un punto di fuga*/
			//verifico sinistra libera
			for(int i = king.getColumn()-1; i >= 0; i--) {
				Position position = new Position(king.getRow(), i);
				if(node.getState().containsKey(position) || position.equals(new Position(1,4)) || position.equals(new Position(7,4))) {
					//riga occupata
					block = true;
					break;
				}
			}
			if(!block) {
				++cont;
			}
			block = false;
			//verifico destra libera
			for(int i = king.getColumn()+1; i<=8; i++) {
				Position position = new Position(king.getRow(), i);
				if(node.getState().containsKey(position) || position.equals(new Position(1,4)) || position.equals(new Position(7,4))) {
					//riga occupata
					block = true;
					break;
				}
			}
			if(!block) {
				++cont;
			}
			block = false;
		}
		
		if(king.getColumn() == 1 || king.getColumn() == 2 || king.getColumn() == 6 || king.getColumn() == 7) {
			/*il re è su una colonna in corrispondenza di un punto di fuga*/
			//verifico sopra libera
			for(int i = king.getRow()-1; i >= 0; i--) {
				Position position = new Position(i, king.getColumn());
				if(node.getState().containsKey(position) || position.equals(new Position(4,1)) || position.equals(new Position(4,7))) {
					//colonna occupata
					block = true;
					break;
				}
			}
			if(!block) {
				++cont;
			}
			block = false;
			//verifico sotto libero
			for(int i = king.getRow()+1; i<=8; i++) {
				Position position = new Position(i, king.getColumn());
				if(node.getState().containsKey(position) || position.equals(new Position(4,1)) || position.equals(new Position(4,7))) {
					//colonna occupata
					block = true;
					break;
				}
			}
			if(!block) {
				++cont;
			}
			block = false;
		}
		return cont;
		/*if(cont > 0)
			return 1;
		else
			return 0;*/
		
	}
	
	public int distanceBetweenKingEscape(Position king) {
		int dist = 0;
		int distance[] = new int[escapePoints.size()];
		escapePoints.forEach((position) -> {
			distance[escapePoints.indexOf(position)] = (Math.abs(king.getRow()-position.getRow())) + (Math.abs(king.getColumn() - position.getColumn()));
		});
		dist = distance[0];
		escape = escapePoints.get(0);
		for(int i = 1; i < distance.length; i++) {
			if(distance[i] < dist) {
				dist = distance[i];
				escape = escapePoints.get(i);
			}
		}
		return dist;
	}
	
	public int[] kingProtected(Node node,Position king, Pawn pawn) {
		int[] result = {0,0,0,0};
		int var = 0;
		Position adjacentKingUp = new Position(king.getRow()-1, king.getColumn());
		Position adjacentKingDown = new Position(king.getRow()+1, king.getColumn());
		Position adjacentKingRight = new Position(king.getRow(), king.getColumn()+1);
		Position adjacentKingLeft = new Position(king.getRow(), king.getColumn()-1);
		if(node.getState().containsKey(adjacentKingUp)) {
			PawnClass p = (PawnClass) node.getState().get(adjacentKingUp);
			if(p.getType().equalsPawn(pawn.toString()))
				var++;
		}
		if(node.getState().containsKey(adjacentKingDown)) {
			PawnClass p = (PawnClass) node.getState().get(adjacentKingDown);
			if(p.getType().equalsPawn(pawn.toString()))
				var++;
		}
		if(node.getState().containsKey(adjacentKingRight)) {
			PawnClass p = (PawnClass) node.getState().get(adjacentKingRight);
			if(p.getType().equalsPawn(pawn.toString()))
				var++;
		}
		if(node.getState().containsKey(adjacentKingLeft)) {
			PawnClass p = (PawnClass) node.getState().get(adjacentKingLeft);
			if(p.getType().equalsPawn(pawn.toString()))
				var++;
		}
		if(var == 0)
			return result;
		else {
			result[var -1] = 1;
			return result;
		}
	}
	
	public int capturedPawn(Node node) {
		if(node.getCaptured() != 0)
			return 1;
		else
			return 0;
	}
	
	public Position convertLetterToInt(String position) {
		char column = position.charAt(0);
		int row = Character.getNumericValue(position.charAt(1));
		row = row -1;
		Position result = null;
		switch(column) {
		case 'a': result = new Position(row,0);
			break;
		case 'b': result = new Position(row,1);
			break;
		case 'c': result = new Position(row,2);
			break;
		case 'd': result = new Position(row,3);
			break;
		case 'e': result = new Position(row,4);
			break;
		case 'f': result = new Position(row,5);
			break;
		case 'g': result = new Position(row,6);
			break;
		case 'h': result = new Position(row,7);
			break;
		case 'i': result = new Position(row,8);
			break;
		}
		return result;
	}
	
	public void initAdjacentPointsCaste() {
		adjacentPointsCastle.add(new Position(3,4));
		adjacentPointsCastle.add(new Position(4,3));
		adjacentPointsCastle.add(new Position(4,5));
		adjacentPointsCastle.add(new Position(5,4));
	}
	
	public void initEscapePoint() {
		escapePoints.add(new Position(0,1));
		escapePoints.add(new Position(0,2));
		escapePoints.add(new Position(0,6));
		escapePoints.add(new Position(0,7));
		escapePoints.add(new Position(1,0));
		escapePoints.add(new Position(1,8));
		escapePoints.add(new Position(2,0));
		escapePoints.add(new Position(2,8));
		escapePoints.add(new Position(6,0));
		escapePoints.add(new Position(6,8));
		escapePoints.add(new Position(7,0));
		escapePoints.add(new Position(7,8));
		escapePoints.add(new Position(8,1));
		escapePoints.add(new Position(8,2));
		escapePoints.add(new Position(8,6));
		escapePoints.add(new Position(8,7));
	}

	public ArrayList<Position> getEscapePoints() {
		return escapePoints;
	}

	public void setEscapePoints(ArrayList<Position> escapePoints) {
		this.escapePoints = escapePoints;
	}
	
	
}
