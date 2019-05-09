package util;

import java.util.ArrayList;
import java.util.Map;

import domain.State.Pawn;
import strategy.Strategy;

public class PawnClass {
	private int row, column;
	private Pawn type;
	private final int length = PawnMap.getInstance().getLength();
	private final int width = PawnMap.getInstance().getWidth();
	private Position castle = new Position(4,4);
	//private ArrayList<Position> citadels = new ArrayList<Position>();
	/*
	 * color: b = black w = white
	 */

	public PawnClass(int row, int column, Pawn type) {
		this.row = row;
		this.column = column;
		this.type = type;
	}

	/* METODI PER MUOVERE LA PEDINA */
	public int maxNumberBoxMoveUp(Map<Position,PawnClass> pawnMap){
		/*restituisce il massimo numero di caselle
		 * in cui si può muovere la pedina*/
		int cont = 0;
		boolean notInCitadels = true;
		Position current = new Position();
		current.setColumn(column);
		if(Strategy.getInstance().getCitadels().contains(new Position(row,column))) {
			notInCitadels = false;
		}
		for(int i = row-1; i >= 0; i--) {
			current.setRow(i);
			if(cont > 0)
				notInCitadels = true;
			if(pawnMap.containsKey(current) || (notInCitadels && Strategy.getInstance().getCitadels().contains(current)) || current.equals(castle)) {
				break;
			}else {
				cont++;
			}
		}
		return cont;
	}
	
	public int maxNumberBoxMoveDown(Map<Position,PawnClass> pawnMap){
		/*restituisce il massimo numero di caselle
		 * in cui si può muovere la pedina*/
		int cont = 0;
		boolean notInCitadels = true;
		Position current = new Position();
		current.setColumn(column);
		if(Strategy.getInstance().getCitadels().contains(new Position(row,column))) {
			notInCitadels = false;
		}
		for(int i = row+1; i <= 8; i++) {
			/*calcolo l'indice della riga della prima pedina sulla stessa colonna della pedina 
			 * che devo muovere.*/
			current.setRow(i);
			if(cont > 0)
				notInCitadels = true;
			if(pawnMap.containsKey(current) || (notInCitadels && Strategy.getInstance().getCitadels().contains(current)) || current.equals(castle)) {
				break;
			}else {
				cont++;
			}
		}
		return cont;
	}
	
	public int maxNumberBoxMoveRight(Map<Position,PawnClass> pawnMap) {
		int cont = 0;
		boolean notInCitadels = true;
		Position current = new Position();
		current.setRow(row);
		if(Strategy.getInstance().getCitadels().contains(new Position(row,column))) {
			notInCitadels = false;
		}
		for(int i = column+1; i <= 8; i++) {
			current.setColumn(i);
			if(cont > 0)
				notInCitadels = true;
			if(pawnMap.containsKey(current) || (notInCitadels && Strategy.getInstance().getCitadels().contains(current)) || current.equals(castle)) {
				break;
			}else
				cont++;
		}
		return cont;
	}
	
	public int maxNumberBoxMoveLeft(Map<Position,PawnClass> pawnMap) {
		int cont = 0;
		boolean notInCitadels = true;
		Position current = new Position();
		current.setRow(row);
		if(Strategy.getInstance().getCitadels().contains(new Position(row,column))) {
			notInCitadels = false;
		}
		for(int i = column - 1; i >= 0; i--) {
			current.setColumn(i);
			if(cont > 0)
				notInCitadels = true;
			if(pawnMap.containsKey(current) || (notInCitadels && Strategy.getInstance().getCitadels().contains(current)) || current.equals(castle)) {
				break;
			}else
				cont++;
		}
		return cont;
		
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public Pawn getType() {
		return type;
	}

	public void setType(Pawn type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Pawn "+type.toString();
	}

	@Override
	public boolean equals(Object obj) {
		PawnClass pawnClass = (PawnClass) obj;
		if(pawnClass.getRow() == this.row && pawnClass.getColumn() == this.column && pawnClass.getType().equalsPawn(this.getType().toString()))
			return true;
		else
			return false;
	}
	
}
