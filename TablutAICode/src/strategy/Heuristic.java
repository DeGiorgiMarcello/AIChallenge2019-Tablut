package strategy;

public class Heuristic {
	
	private static Heuristic instance;

	public static Heuristic getInstance() {
		if(instance == null) {
			instance = new Heuristic();
		}
		return instance;
	}
	
	public String evaluate(String player) {
		String result = "";
		
		
		return result;
	}
}
