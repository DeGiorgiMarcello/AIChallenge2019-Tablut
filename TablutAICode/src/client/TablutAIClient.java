package client;

import java.io.IOException;
import java.net.UnknownHostException;
import domain.*;
import domain.State.Turn;
import strategy.Strategy;
import util.PawnMap;

public class TablutAIClient extends TablutClient {
	
	String player;
	static Strategy strategy;

	public TablutAIClient(String player, String name) throws UnknownHostException, IOException {
		super(player, name);
		// TODO Auto-generated constructor stub
		player = player.toLowerCase();
		this.player = player;
	}
	
	//per lanciarlo inserire giocatore e nome come argomenti
	public static void main(String args[]) throws UnknownHostException, IOException{
		int maxTime = 59;
		int maxDepth = 3;
		String name = "aBitBetterThanRandom";
		String player = "";
		
		String usage = "Usage: java TablutAIClient [-n <name>] [-p <player>] [-t <time>] [-d <maxDepth>]\n"
				+ "\tname can be everything; default \"aBitBetterThanRandom\""
				+ "\tplayer must white or black\n"
				+ "time must be an integer (number of seconds); default: 60"
				+ "\tmaxDepth must be an integer >= 0; default: 3\n";
		
		for(int i=0;i<args.length-1;i++) {
			if(args[i].equals("-t")) {
				i++;
				try {
					 maxTime = Integer.parseInt(args[i]);
					 if (maxTime < 1) {
							System.out.println("Time format not allowed!");
							System.out.println(args[i]);
							System.out.println(usage);
							System.exit(1);
						}
				}catch(Exception e) {
					System.out.println("The time format is not correct!");
					System.out.println(args[i]);
					System.out.println(usage);
					System.exit(1);
				}
			}
			if(args[i].equals("-d")) {
				i++;
				try {
					maxDepth = Integer.parseInt(args[i]);
					if(maxDepth < 1) {
						System.out.println("Depth is too low. It must be at least 1!");
						System.out.println(args[i]);
						System.out.println(usage);
						System.exit(1);
						
					}
				} catch(Exception e) {
					System.out.println("Number format is not correct!");
					System.out.println(args[i]);
					System.out.println(usage);
					System.exit(1);
				}
				
			}
			if(args[i].equals("-n")) {
				i++;
				name = args[i];
			}
			if(args[i].equals("-p")) {
				i++;
				player = args[i];
				player = player.toLowerCase();
				if(!player.equals("white") && !player.equals("black")) {
					System.out.println("Color not valid!");
					System.out.println(args[i]);
					System.out.println(usage);
					System.exit(1);	
				}
			}
		}
		if(player.isEmpty()) {
			System.out.println("A player must be specified!");
			System.out.println(usage);
			System.exit(1);	
		}
		
		strategy = Strategy.getInstance();
		strategy.setMAXDEPTH(maxDepth);
		strategy.setMAXTIME(maxTime - 1);
		TablutClient client = new TablutAIClient(player,name);
		client.run();
	}

	@Override
	public void run() {
		
		System.out.println("You are player " + this.getPlayer().toString() + "!");
		String actionStringFrom = "";
		String actionStringTo = "";
		String[] move = new String[2];
		Action action;
		try {
			this.declareName();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		if(this.getPlayer() == Turn.WHITE) {
			while(true) {
				try {
					
					//prende lo stato dal server
					this.read();
					
					if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITE)) {
						//System.out.println("Player "+this.getPlayer().toString()+ " is moving.");
						PawnMap.getInstance().createMap(this.getCurrentState());  
						move = strategy.getMove(this.player);  
						actionStringFrom = move[0];
						actionStringTo = move[1];
						action = new Action(actionStringFrom, actionStringTo, State.Turn.WHITE);
						System.out.println(action.toString());
						this.write(action);  // la mossa viene mandata al server
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
						//System.out.println("Waiting for your opponent move... ");
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITEWIN)) {						
						System.out.println("YOU WIN!");
						System.exit(0);
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACKWIN)) {						
						System.out.println("YOU LOSE!");
						System.exit(0);
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.DRAW)) {
						System.out.println("DRAW!");
						System.exit(0);
					}
								
					
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
					System.exit(1);
				} 
			} 
			
		} else {
			while(true) {
				try {
					//prende lo stato dal server
					this.read();
					
					//System.out.println("Current state:");
					//System.out.println(this.getCurrentState().toString());
					
					if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
						//System.out.println("Player "+this.getPlayer().toString()+ " is moving.");
						PawnMap.getInstance().createMap(this.getCurrentState());  
						move = strategy.getMove(this.player); 
						actionStringFrom = move[0];
						actionStringTo = move[1];
						action = new Action(actionStringFrom, actionStringTo, State.Turn.BLACK);
						System.out.println(action.toString());
						this.write(action);  // la mossa viene mandata al server 
						
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITE)) {
						//System.out.println("Waiting for your opponent move... ");
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACKWIN)) {
						System.out.println("YOU WIN!");
						System.exit(0);
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITEWIN)) {
						System.out.println("YOU LOSE!");
						System.exit(0);
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.DRAW)) {
						System.out.println("DRAW!");
						System.exit(0);
					}
	
					
					
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
					System.exit(1);
				} 
			} 
		}
		
	} 

}
