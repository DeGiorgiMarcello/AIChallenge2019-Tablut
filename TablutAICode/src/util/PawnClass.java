package util;

import java.util.Map;

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
	public int maxNumberBoxMoveUp(){
		/*restituisce il massimo numero di caselle
		 * in cui si può muovere la pedina*/
		int cont = 0;
		int max = 0;
		/*non posso salire neanche di una casella perchè c'è una pedina sopra a quella considerata*/

		if(PawnMap.getInstance().getMap().containsKey(new Position(this.row - 1 , this.column))) {
			return cont;
		}else {
			/*determino il massimo valore sul quale iterare per cercare pedine sulla colonna in cui 
			 * voglio muovere la pedina che sto considerando. */
			if(column == 4 && row > 4) {
				/*mi voglio spostare verso l'alto e mi trovo sulla colonna del castello*/
				max = 5;
			}else {
				switch(column) {
				case 4:
					max = 2;
					break;
				case 3:
					max = 1;
					break;
				case 5:
					max = 1;
					break;
				default:
					max = 0;
				}
			}
			/*assengo a cont il valore massimo dello spostamento, se ci sono pedine sul cammino, verrà ridotto*/
			cont = row-max;
			for(int i = row-1; i >= max; i--) {
				/*calcolo l'indice della riga della prima pedina sulla stessa colonna della pedina 
				 * che devo muovere.*/
				if(PawnMap.getInstance().getMap().containsKey(new Position(this.row, i))) {
					cont = this.row - i - 1;
					break;
				}/*else {
					cont = row-max;
				}*/
			}
		}
		return cont;
	}
	
	public int maxNumberBoxMoveDown(){
		/*restituisce il massimo numero di caselle
		 * in cui si può muovere la pedina*/
		int cont = 0;
		int max = 0;
		/*non posso salire neanche di una casella perchè c'è una pedina sopra a quella considerata*/
		if(PawnMap.getInstance().getMap().containsKey(new Position(this.row + 1 , this.column))) {
			return cont;
		}else {
			/*determino il massimo valore sul quale iterare per cercare pedine sulla colonna in cui 
			 * voglio muovere la pedina che sto considerando. */
			if(column == 4 && row < 4) {
				max = 3;
			}else {
				switch(column) {
				case 4:
					max = 6;
					break;
				case 3:
					max = 7;
					break;
				case 5:
					max = 7;
					break;
				default:
					max = 8;
				}
		}
		cont = max - row;
		for(int i = row+1; i <= max; i++) {
			/*calcolo l'indice della riga della prima pedina sulla stessa colonna della pedina 
			 * che devo muovere.*/
			if(PawnMap.getInstance().getMap().containsKey(new Position(row, i))) {
				cont = i - row - 1;
				break;
			}/*else {
				cont = max - row;
			}*/
		}
		return cont;
		}
	}
	
	public int maxNumberBoxMoveRight() {
		int cont = 0;
		int max = 0;
		if(PawnMap.getInstance().getMap().containsKey(new Position(row, column + 1))) {
			return cont;
		}else {
			if(row == 4 && column < 4) {
				max = 3;
			}else {
				switch(row) {
				case 4:
					max = 6;
					break;
				case 3:
					max = 7;
					break;
				case 5:
					max = 7;
				default:
					max = 8;
				}
			}
			cont = max - column;
			for(int i = column+1; i <= max; i++) {
				if(PawnMap.getInstance().getMap().containsKey(new Position(row, i))) {
					cont = i - column - 1;
			}
		}
		return cont;
		}
	}
	
	public int maxNumberBoxMoveLeft() {
		int cont = 0;
		int max = 0;
		if(PawnMap.getInstance().getMap().containsKey(new Position(row, column - 1))) {
			return cont;
		}else {
			if(row == 4 && column > 4) {
				max = 5;
			}else {
				switch(row) {
				case 4:
					max = 2;
					break;
				case 3:
					max = 1;
					break;
				case 5:
					max = 1;
				default:
					max = 0;
				}
			}
			cont = column - max;
			for(int i = column - 1; i >= max; i--) {
				if(PawnMap.getInstance().getMap().containsKey(new Position(row, i))) {
					cont = column - i - 1;
				}
			}
		return cont;
		}
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
	
	

	
}
