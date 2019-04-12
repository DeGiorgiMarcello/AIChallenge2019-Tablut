package util;

public class Position {
	private int row, column;
	
	public Position(int row, int column) {
		this.row = row;
		this.column = column;
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

	@Override
	public String toString() {
		return "Riga = "+row+"  Colonna = "+column;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Position position = (Position)obj;
		if(row == position.getRow() && column == position.getColumn())
			return true;
		else
			return false;
	}
	
	

}
