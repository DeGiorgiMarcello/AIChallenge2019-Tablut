package strategy;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import domain.State;
import domain.State.Pawn;
import domain.StateTablut;
import util.Node;
import util.PawnClass;
import util.PawnMap;
import util.Position;

public class Heuristic {
	private static Heuristic instance;
	private final int length = PawnMap.getInstance().getLength();
	private final int width = PawnMap.getInstance().getWidth();
	private ArrayList<Position> escapePoints = new ArrayList();
	private ArrayList<Position> protectPosition = new ArrayList<Position>();
	private Position escape = null;
	private Position castle = new Position(4,4);
	private final int maxMove = length-1;
	private ArrayList<Position> adjacentPointsCastle = new ArrayList();
	
	public static Heuristic getInstance() {
		if(instance == null) {
			instance = new Heuristic();
		}
		return instance;
	}
	
	private Heuristic() {
		initEscapePoint();
		initAdjacentPointsCaste();
		initProtectPosition();
	}
	
	public int evaluateNode(Node node,Pawn color) {
		if(isWhite(color)){
			return whiteHeuristic(node);
		}else {
			return BlackHeuristic(node);
		}
	}
	
	/**
	 * This method calculates the value of the node for white player
	 * @param node
	 * @return
	 */
	public int whiteHeuristic(Node node) {
		int sum = 0;
		Position king = PawnMap.getInstance().findKingPosition(node.getState());
		final int capturedBlack = 1;
		final int capturedWhite = -1;
		final int protectedKingOneSide = 8;
		final int protectedKingTwoSide = 6;
		final int protectedKingThreeSide = -10; 
		final int protectedKingFourSide = -15;
		final int distanceEscapePoint = 5; 
		final int rowColumnFree = 800;
		final int kingCaptured = -2500;
		final int win = 2000;
		final int kingInCastle = -200;
		if(king == null) {
			return kingCaptured;
		}else {			
			int valCapturedBlack = numberOfPawnCaptured(node, Pawn.BLACK);
			int valCapturedWhite = numberOfPawnCaptured(node, Pawn.WHITE);
			int[] valProtectedKing = kingProtected(node, king, Pawn.WHITE);
			int valProtectedKingOneSide = valProtectedKing[0];
			int valProtectedKingTwoSide = valProtectedKing[1];
			int valProtectedKingThreeSide = valProtectedKing[2];
			int valProtectedKingFourSide = valProtectedKing[3];
			int valDistanceEscapePoint = distanceBetweenKingEscape(king);
			int valRowColumnFree = freeEscapeRoute(node, king);
			int valKingCaptured = kingCaptured(node);
			int valwin = kingInEscapePoint(king);
			int valKingInCastle=0;
			if(kingInCaste(king)) {
				valKingInCastle=1;
			}else
				valKingInCastle=-1;
			return kingInCastle*valKingInCastle+capturedBlack*valCapturedBlack+capturedWhite*valCapturedWhite+protectedKingOneSide*valProtectedKingOneSide+
					protectedKingTwoSide*valProtectedKingTwoSide+protectedKingThreeSide*valProtectedKingThreeSide+
					protectedKingFourSide*valProtectedKingFourSide+distanceEscapePoint*(maxMove - valDistanceEscapePoint)+
					rowColumnFree*valRowColumnFree+kingCaptured*valKingCaptured+win*valwin;
		}
		
	}
	
	/**
	 * This method calculates the value of the node for black player
	 * @param node
	 * @return
	 */
	public int BlackHeuristic(Node node) {
		Position king = PawnMap.getInstance().findKingPosition(node.getState());
		int sum = 0;
		final int capturedWhite = 20;
		final int capturedBlack = -1;
		final int kingTrappedOneSide = 20; 
		final int kingTrappedTwoSide = 25; 
		final int kingTrappedThreeSide = 35;
		final int kingTrappedFourSide = 45;
		final int escapePointBlocked = -900; 
		final int kingcaptured = 2000;
		final int kingWin = -2000;
		final int blackProtectEscape = 5;
		if(king == null) {
			return kingcaptured;
		}else {
			int valCapturedWhite = numberOfPawnCaptured(node, Pawn.WHITE); 
			int valCapturedBlack = numberOfPawnCaptured(node, Pawn.BLACK); 
			int[] valKingTrapped = kingProtected(node, king, Pawn.BLACK);
			int valKingTrappedOneSide = valKingTrapped[0];
			int valKingTrappedTwoSide = valKingTrapped[1];
			int valKingTrappedThreeSide = valKingTrapped[2];
			int valKingTrappedFourSide = valKingTrapped[3];
			int valEscapePointBlocked = freeEscapeRoute(node, king);
			int valKingcaptured = kingCaptured(node);
			int valKingWin = kingInEscapePoint(king);
			int valBlackProtect = blackProtectEscape(node);
			return capturedWhite*valCapturedWhite+capturedBlack*valCapturedBlack+kingTrappedOneSide*valKingTrappedOneSide+
					kingTrappedTwoSide*valKingTrappedTwoSide+kingTrappedThreeSide*valKingTrappedThreeSide+kingTrappedFourSide*valKingTrappedFourSide
					+escapePointBlocked*valEscapePointBlocked+kingcaptured*valKingcaptured+kingWin*valKingWin+valBlackProtect*blackProtectEscape;
		}
		
	}
	/**
	 * This method calculates the difference between the number of pawn of root 
	 * and the number of pawn of current node.
	 * @param node
	 * @param pawn
	 * @return
	 */
	public int numberOfPawnCaptured(Node node, Pawn pawn) {
		int result = 0;
		int pawnParent = 0;
		int pawnChild = 0;
		Map<Position, PawnClass> rootState = PawnMap.getInstance().getMap();
		pawnParent = numberOfPawn(rootState, pawn);
		pawnChild = numberOfPawn(node.getState(), pawn);
		result = pawnParent - pawnChild;
		return result;
	}
	
	public int numberOfPawn(Map<Position, PawnClass> map, Pawn pawn) {
		int cont = 0;
		boolean white = false;
		if(isWhite(pawn))
			white = true;
		for(Map.Entry<Position, PawnClass> entry: map.entrySet()) {
			PawnClass currentPawn = entry.getValue();
			if(white) {
				if(pawnIsWhite(currentPawn) || pawnIsKing(currentPawn))
					cont++;
			}else {
				if(pawnIsBlack(currentPawn)) 
					cont ++;
			}
		}
		return cont;
	}
	
	public int kingCaptured(Node node) {
		int result = 0;
		for(Map.Entry<Position, PawnClass> entry : node.getState().entrySet()) {
			PawnClass currentPawn = entry.getValue();
			if(pawnIsKing(currentPawn)) {
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
	 
	  public int freeEscapeRoute(Node node, Position king) {
	    int cont = 0;
	    boolean block = false;
	    if(kingInEscapeRow(king)) {
	      //veryfing if the left is free
	      for(int i = king.getColumn()-1; i >= 0; i--) {
	        Position position = new Position(king.getRow(), i);
	        if(stateContainsPosition(node, position) || citadelInEscapeRow(position)) {
	          // row occupied
	          block = true;
	          break;
	        }
	      }
	      if(!block) {
	        ++cont;
	      }
	      block = false;
	      //veryfing if the right is free
	      for(int i = king.getColumn()+1; i<=8; i++) {
	        Position position = new Position(king.getRow(), i);
	        if(stateContainsPosition(node, position) || citadelInEscapeRow(position)) {
	          //row occupied
	          block = true;
	          break;
	        }
	      }
	      if(!block) {
	        ++cont;
	      }
	      block = false;
	    }
	    
	    if(kingInEscapeColumn(king)) {
	      //veryfing if the upside is free
	      for(int i = king.getRow()-1; i >= 0; i--) {
	        Position position = new Position(i, king.getColumn());
	        if(stateContainsPosition(node, position) || citadelInEscapeColumn(position)) {
	          //colonna occupata
	          block = true;
	          break;
	        }
	      }
	      if(!block) {
	        ++cont;
	      }
	      block = false;
	      //veryfing if the down side is free
	      for(int i = king.getRow()+1; i<=8; i++) {
	        Position position = new Position(i, king.getColumn());
	        if(stateContainsPosition(node, position) || citadelInEscapeColumn(position)) {
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
	  }
	/**
	 * This method calculates the Manhattan distance between king position
	 * and escape point.
	 * @param king
	 * @return
	 */
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
		if(stateContainsPosition(node, adjacentKingUp)) {
			PawnClass p = (PawnClass) node.getState().get(adjacentKingUp);
			if(pawnHasColor(p, pawn))
				var++;
		}
		if(stateContainsPosition(node, adjacentKingDown)) {
			PawnClass p = (PawnClass) node.getState().get(adjacentKingDown);
			if(pawnHasColor(p, pawn))
				var++;
		}
		if(stateContainsPosition(node, adjacentKingRight)) {
			PawnClass p = (PawnClass) node.getState().get(adjacentKingRight);
			if(pawnHasColor(p, pawn))
				var++;
		}
		if(stateContainsPosition(node, adjacentKingLeft)) {
			PawnClass p = (PawnClass) node.getState().get(adjacentKingLeft);
			if(pawnHasColor(p, pawn))
				var++;
		}
		if(var == 0)
			return result;
		else {
			result[var -1] = 1;
			return result;
		}
	}
	
	public int blackProtectEscape(Node node) {
		int cont = 0;
		for(Entry<Position, PawnClass> entry : node.getState().entrySet()) {
			if(protectPosition.contains(entry.getKey())){
				if(entry.getValue().getType().equalsPawn(Pawn.BLACK.toString())) {
					cont++;
				}
			}
		}
		return cont;
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
	
	public boolean stateContainsPosition(Node node, Position position) {
		if(node.getState().containsKey(position))
			return true;
		return false;
	}
	
	public boolean pawnHasColor(PawnClass currentPawn, Pawn color) {
		if(currentPawn.getType().equalsPawn(color.toString()))
			return true;
		return false;
	}
	public boolean isWhite(Pawn color) {
		if(color.equalsPawn(Pawn.WHITE.toString()))
			return true;
		return false;
	}
	
	public boolean isBlack(Pawn color) {
		if(color.equalsPawn(Pawn.BLACK.toString()))
			return true;
		return false;
	}
	
	public boolean kingInCaste(Position kingPosition) {
		Position caste = new Position(4,4);
		if(kingPosition.equals(caste))
			return true;
		else
			return false;
	}
	
	public boolean pawnIsWhite(PawnClass pawn) {
		Pawn type = pawn.getType();
		if(type.equalsPawn(Pawn.WHITE.toString()))
			return true;
		return false;
	}
	
	public boolean pawnIsBlack(PawnClass pawn) {
		Pawn type = pawn.getType();
		if(type.equalsPawn(Pawn.BLACK.toString()))
				return true;
		return false;
	}
	
	public boolean pawnIsKing(PawnClass pawn) {
		if(pawn.getType().equalsPawn(Pawn.KING.toString())) {
			return true;
		}
		return false;	
	}
	
	public boolean kingInEscapeRow(Position king) {
	    if(king.getRow() == 1 || king.getRow() == 2 || king.getRow() == 6 || king.getRow() == 7) 
	      return true;
	    return false;
	  }
	  public boolean kingInEscapeColumn(Position king) {
	    if(king.getColumn() == 1 || king.getColumn() == 2 || king.getColumn() == 6 || king.getColumn() == 7) 
	      return true;
	    return false;
	  }
	  
	  public boolean citadelInEscapeRow(Position position) {
	    if(position.equals(new Position(1,4)) || position.equals(new Position(7,4)))
	      return true;
	    return false;
	  }
	  
	  public boolean citadelInEscapeColumn(Position position) {
	    if(position.equals(new Position(4,1)) || position.equals(new Position(4,7)))
	      return true;
	    return false;
	  }
	public void initProtectPosition() {
		protectPosition.add(new Position(1,2));
		protectPosition.add(new Position(2,1));
		protectPosition.add(new Position(1,6));
		protectPosition.add(new Position(6,1));
		protectPosition.add(new Position(7,2));
		protectPosition.add(new Position(2,7));
		protectPosition.add(new Position(6,7));
		protectPosition.add(new Position(7,6));
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
