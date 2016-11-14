package poker.app.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import exceptions.DeckException;
import netgame.common.Hub;
import poker.app.MainApp;
import pokerBase.Action;
import pokerBase.Card;
import pokerBase.CardDraw;
import pokerBase.Deck;
import pokerBase.GamePlay;
import pokerBase.GamePlayPlayerHand;
import pokerBase.Player;
import pokerBase.Rule;
import pokerBase.Table;
import pokerEnums.eAction;
import pokerEnums.eCardDestination;
import pokerEnums.eDrawCount;
import pokerEnums.eGame;
import pokerEnums.eGameState;


public class PokerHub extends Hub {

	private Table HubPokerTable = new Table();
	private GamePlay HubGamePlay;
	private int iDealNbr = 0;
	private eGameState eGameState;
	private MainApp mainApp;

	public PokerHub(int port) throws IOException {
		super(port);
	}

	protected void playerConnected(int playerID) {

		if (playerID == 2) {
			shutdownServerSocket();
		}
	}

	protected void playerDisconnected(int playerID) {
		shutDownHub();
	}

	public void setMainApp(MainApp mainApp){
		this.mainApp=mainApp;
	}
	
	protected void messageReceived(int ClientID, Object message) {

		if (message instanceof Action) {
			
			
			if (message == eAction.StartGame){
				GamePlay newGame = new GamePlay(mainApp.getGameRule(),HubPokerTable.PickRandomPlayerAtTable().getPlayerID());
			}
			
			else if (message == eAction.Sit){
				HubPokerTable.AddPlayerToTable(mainApp.getPlayer());
				sendToAll(HubPokerTable);
			}
			else if (message == eAction.Leave){
				HubPokerTable.RemovePlayerFromTable(mainApp.getPlayer());
				sendToAll(HubPokerTable);
			}
			else if (message == eAction.GameState){
				sendToAll(HubGamePlay);
			}
		}

		System.out.println("Message Received by Hub");
		
		sendToAll("Sending Message Back to Client");
	}

}
