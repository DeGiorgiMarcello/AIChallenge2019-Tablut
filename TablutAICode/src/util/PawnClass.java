package util;

import domain.State.Pawn;

public class PawnClass {
	private int row, column;
	private Pawn type;
	/*
	 * color: b = black w = white
	 */

	public PawnClass(int row, int column, Pawn type) {
		this.row = row;
		this.column = column;
		this.type = type;
	}

	/* METODI PER MUOVERE LA PEDINA */

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
	
	

	
}
