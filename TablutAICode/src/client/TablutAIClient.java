package client;

import java.io.IOException;
import java.net.UnknownHostException;
import domain.*;
import domain.State.Turn;

public class TablutAIClient extends TablutClient {
	

	public TablutAIClient(String player, String name) throws UnknownHostException, IOException {
		super(player, name);
		// TODO Auto-generated constructor stub
	}
	
	//per lanciarlo inserire giocatore e nome come argomenti
	public static void main(String args[]) throws UnknownHostException, IOException{
		if(args.length < 2 ) {
			System.out.println("Errore. Giocatore e/o nome non inseriti.");
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
		String[] mossa = new String[2];
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
						System.out.println("Mossa di "+this.getPlayer().toString()+ ".");
						//Map.getInstance().createMap(this.getCurrentState());  -> inserire nel metodo un controllo se l'hashmap è già stato creato.
						//Strategia strategia = Stragegia.getInstance();
						//mossa = strategia.calcolaMossa(this.player); -> array di due stringhe
						//actionStringFrom = mossa[0];
						//actionStringTo = mossa[1];
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
					System.out.println("Mossa di "+this.getPlayer().toString()+ ".");
					//Map.getInstance().createMap(this.getCurrentState());
					//Strategia strategia = Stragegia.getInstance();
					//mossa = strategia.calcolaMossa(this.player); -> array di due stringhe
					//actionStringFrom = mossa[0];
					//actionStringTo = mossa[1];
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
				
				
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				System.exit(1);
			} 
			
		} 
		
	} 

}
