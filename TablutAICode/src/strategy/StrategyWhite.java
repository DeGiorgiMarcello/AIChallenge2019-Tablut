package strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.swing.text.Position;

import util.PawnClass;

public class StrategyWhite {
	//calcola tutte le possibili mosse a partire da uno stato
	private static StrategyWhite instance;
	private ArrayList children = new ArrayList<Map<Position, PawnClass>>();
		
	private StrategyWhite() {
		
	}
	
	public static StrategyWhite getInstance() {
		if(instance == null)
			instance = new StrategyWhite();
		return instance;
	}
	
	/*A partire dalla mappa, per ogni pedina bianca calcola tutte le possibili mosse*/
	
}
