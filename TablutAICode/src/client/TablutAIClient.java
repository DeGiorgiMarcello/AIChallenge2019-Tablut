package client;

import java.io.IOException;
import java.net.UnknownHostException;
import domain.*;
import domain.State.Turn;
import strategy.Strategy;
import util.PawnMap;

public class TablutAIClient extends TablutClient {
	
	String player;

	public TablutAIClient(String player, String name) throws UnknownHostException, IOException {
		super(player, name);
		// TODO Auto-generated constructor stub
		player = player.toLowerCase();
		this.player = player;
	}
	
	//per lanciarlo inserire giocatore e nome come argomenti
	public static void main(String args[]) throws UnknownHostException, IOException{
		if(args.length < 2 ) {
			System.out.println("Error. Player/name not inserted.");
			System.exit(-1);
		}
		
		TablutClient client = new TablutAIClient(args[0],args[1]);
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
					/*
					System.out.println("Current state:");
					System.out.println(this.getCurrentState().toString());
					*/
					if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITE)) {
						System.out.println("Player "+this.getPlayer().toString()+ " is moving.");
						PawnMap.getInstance().createMap(this.getCurrentState());  
						Strategy strategy = Strategy.getInstance();
						move = strategy.getMove(this.player); 
						actionStringFrom = move[0];
						actionStringTo = move[1];
						action = new Action(actionStringFrom, actionStringTo, this.getPlayer());
						this.write(action);  // la mossa viene mandata al server
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
						System.out.println("Waiting for your opponent move... ");
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

				//	this.read();  //read per leggere lo stato modificato dal bianco
					
					
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
					System.exit(1);
				} 
			} 
			
		} else {
			try {
				//prende lo stato dal server
				this.read();
				
				//System.out.println("Current state:");
				//System.out.println(this.getCurrentState().toString());
				
				if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
					System.out.println("Player "+this.getPlayer().toString()+ " is moving.");
					PawnMap.getInstance().createMap(this.getCurrentState());  
					Strategy strategy = Strategy.getInstance();
					move = strategy.getMove(this.player); 
					actionStringFrom = move[0];
					actionStringTo = move[1];
					System.out.println(actionStringFrom+actionStringTo);
					action = new Action(actionStringFrom, actionStringTo, this.getPlayer());
					this.write(action);  // la mossa viene mandata al server
				} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
					System.out.println("Waiting for your opponent move... ");
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

			//	this.read();  //read per leggere l'aggiornamento del nero
				
				
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				System.exit(1);
			} 
			
		} 
		
	} 

}
