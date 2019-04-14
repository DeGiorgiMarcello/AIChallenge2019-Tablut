package strategy;

import java.util.ArrayList;

import domain.State.Pawn;
import util.Node;
import util.PawnClass;
import util.PawnMap;
import util.Position;

public class Heuristic {
	
	private static Heuristic instance;
	private ArrayList<Position> escapePoints = new ArrayList();
	private Position escape = null; //salvo la posizione del punto di fuga più vicino
	
	public static Heuristic getInstance() {
		if(instance == null) {
			instance = new Heuristic();
		}
		return instance;
	}
	
	private Heuristic() {
		/*inizializzo la lista solo la prima volta che chiamo la classse Heuristic*/
		initEscapePoint();
	}
	
	public void evaluateNode(Node node) {
		Position position = convertLetterToInt(node.getPawnMoveTo());
		PawnClass pawn = (PawnClass) node.getState().get(position);
		if(pawn.getType().equalsPawn(Pawn.WHITE.toString()) || pawn.getType().equalsPawn(Pawn.KING.toString()) ) {
			//funzione euristica per giocatore bianco
		}else {
			//funzione euristica per giocatore nero
		}
	}
	
	public int whiteHeuristic(Node node) {
		Position king = PawnMap.getInstance().findKingPosition(node.getState());
		int val = 0;
		//AVENUTA CATTURA
		int valCaptured = capturedPawn(node);
		
		//RE PROTETTO aggiungo 1 unità al risultato per ogni lato su cui il re è protetto
		int valProtectedKing = kingProtected(node, king, Pawn.WHITE);
		
		//DISTANZA DEL RE DAL PUNTO DI FUGA PIù VICINO
		int valDistanceEscapePoint = distanceBetweenKingEscape(king);
		
		//RE HA RIGA/COLONNA LIBERA VERSO UN PUNTO DI FUGA
		int valRowColumnFree = freeEscapeRoute(node, king);
		
		//PEDINA VIENE CATTURATA
		
		//RE VIENE CATTURATO
		
		//RE IN UN PUNTO DI FUGA => VITTORIA
		int win = kingInEscapePoint(king);
		/*CALCOLARE SOMMA PESATA*/
		return val;
	}
	
	public int BlackHeuristic(Node node) {
		Position king = PawnMap.getInstance().findKingPosition(node.getState());
		int val = 0;
		//AVVENUTA CATTURA
		int valCaptured = capturedPawn(node);
		
		//re chiuso sui lati
		int valKingTrapped = kingProtected(node, king, Pawn.BLACK);
		
		//numeor di vie di fuga bloccate al re
		int valEscapePointBlocked = blockEscapeRoute(node, king);
		
		//vedere se ho catturato il re
		//?????
		return val;
	}
	
	public int kingInEscapePoint(Position king) {
		for(int i = 0; i < escapePoints.size(); i++) {
			if(king.equals(escapePoints.get(i)))
				return 1;
		}
		return 0;
	}
	
	public int blockEscapeRoute(Node node, Position king) {
		/*devo vedere se sulla riga o colonna del re c'è una via di fuga
		 * RIGHE VIE DI FUGA 1 2 6 7
		 * COLONNE VIE DI FUGA 1 2 6 7*/
		int cont = 0;
		if(king.getRow() == 1 || king.getRow() == 2 || king.getRow() == 6 || king.getRow() == 7) {
			/*il re è su una riga in corrispondenza di un punto di fuga*/
			//verifico sinistra libera
			for(int i = king.getColumn()-1; i >= 0; i--) {
				Position position = new Position(king.getRow(), i);
				if(node.getState().containsKey(position) && node.getState().get(position).getType().equalsPawn(Pawn.BLACK.toString())) {
					//pedina nera sulla via di fuga del re
					cont++;
				}	
			}
			//verifico destra libera
			for(int i = king.getColumn()+1; i<=8; i++) {
				Position position = new Position(king.getRow(), i);
				if(node.getState().containsKey(position) && node.getState().get(position).getType().equalsPawn(Pawn.BLACK.toString())) {
					//riga occupata
					cont++;
				}
			}
		}
		
		if(king.getColumn() == 1 || king.getColumn() == 2 || king.getColumn() == 6 || king.getColumn() == 7) {
			/*il re è su una colonna in corrispondenza di un punto di fuga*/
			//verifico sopra libera
			for(int i = king.getRow()-1; i >= 0; i--) {
				Position position = new Position(i, king.getColumn());
				if(node.getState().containsKey(position) && node.getState().get(position).getType().equalsPawn(Pawn.BLACK.toString())) {
					//colonna occupata
					cont++;
				}	
			}
			//verifico destra libera
			for(int i = king.getRow()+1; i<=8; i++) {
				Position position = new Position(i, king.getColumn());
				if(node.getState().containsKey(position) && node.getState().get(position).getType().equalsPawn(Pawn.BLACK.toString())) {
					//riga occupata
					cont++;
				}
			}
		}
		
		return cont;
	}
	
	public int freeEscapeRoute(Node node, Position king) {
		/*devo vedere se sulla riga o colonna del re c'è una via di fuga
		 * RIGHE VIE DI FUGA 1 2 6 7
		 * COLONNE VIE DI FUGA 1 2 6 7*/
		int cont = 0;
		if(king.getRow() == 1 || king.getRow() == 2 || king.getRow() == 6 || king.getRow() == 7) {
			/*il re è su una riga in corrispondenza di un punto di fuga*/
			//verifico sinistra libera
			for(int i = king.getColumn()-1; i >= 0; i--) {
				Position position = new Position(king.getRow(), i);
				if(node.getState().containsKey(position) || i == 4) {
					//riga occupata
					cont--;
				}	
			}
			//verifico destra libera
			for(int i = king.getColumn()+1; i<=8; i++) {
				Position position = new Position(king.getRow(), i);
				if(node.getState().containsKey(position) || i == 4) {
					//riga occupata
					cont--;
				}
			}
		}
		
		if(king.getColumn() == 1 || king.getColumn() == 2 || king.getColumn() == 6 || king.getColumn() == 7) {
			/*il re è su una colonna in corrispondenza di un punto di fuga*/
			//verifico sopra libera
			for(int i = king.getRow()-1; i >= 0; i--) {
				Position position = new Position(i, king.getColumn());
				if(node.getState().containsKey(position) || i == 4) {
					//colonna occupata
					cont--;
				}	
			}
			//verifico destra libera
			for(int i = king.getRow()+1; i<=8; i++) {
				Position position = new Position(i, king.getColumn());
				if(node.getState().containsKey(position) || i == 4) {
					//riga occupata
					cont--;
				}
			}
		}
		
		return cont;
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
	
	public int kingProtected(Node node,Position king, Pawn pawn) {
		int cont = 0;
		Position adjacentKingUp = new Position(king.getRow()-1, king.getColumn());
		Position adjacentKingDown = new Position(king.getRow()+1, king.getColumn());
		Position adjacentKingRight = new Position(king.getRow(), king.getColumn()+1);
		Position adjacentKingLeft = new Position(king.getRow(), king.getColumn()-1);
		if(node.getState().containsKey(adjacentKingUp)) {
			PawnClass p = (PawnClass) node.getState().get(adjacentKingUp);
			if(p.getType().equalsPawn(pawn.toString()))
				cont++;
		}
		if(node.getState().containsKey(adjacentKingDown)) {
			PawnClass p = (PawnClass) node.getState().get(adjacentKingDown);
			if(p.getType().equalsPawn(pawn.toString()))
				cont++;
		}
		if(node.getState().containsKey(adjacentKingRight)) {
			PawnClass p = (PawnClass) node.getState().get(adjacentKingRight);
			if(p.getType().equalsPawn(pawn.toString()))
				cont++;
		}
		if(node.getState().containsKey(adjacentKingLeft)) {
			PawnClass p = (PawnClass) node.getState().get(adjacentKingLeft);
			if(p.getType().equalsPawn(pawn.toString()))
				cont++;
		}
		return cont;
	}
	
	public int capturedPawn(Node node) {
		if(node.getCaptured() != 0)
			return 1;
		else
			return 0;
	}
	
	public Position convertLetterToInt(String position) {
		char row = position.charAt(0);
		int column = Character.getNumericValue(position.charAt(1));
		Position result = null;
		switch(row) {
		case 'a': result = new Position(0,column-1);
			break;
		case 'b': result = new Position(1, column-1);
			break;
		case 'c': result = new Position(2, column -1);
			break;
		case 'd': result = new Position(3, column -1);
			break;
		case 'e': result = new Position(4, column-1);
			break;
		case 'f': result = new Position(5, column-1);
			break;
		case 'g': result = new Position(6, column-1);
			break;
		case 'h': result = new Position(7, column-1);
			break;
		case 'i': result = new Position(8, column-1);
			break;
		}
		return result;
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
}
