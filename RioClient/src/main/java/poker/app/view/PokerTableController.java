package poker.app.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import poker.app.MainApp;
import pokerBase.Action;
import pokerBase.Table;
import pokerEnums.eAction;
import pokerEnums.ePlayerPosition;

public class PokerTableController implements Initializable {

	// Reference to the main application.
	private MainApp mainApp;

	public PokerTableController() {
	}

	@FXML
	private ImageView imgViewDealerButtonPos1;
	@FXML
	private ImageView imgViewDealerButtonPos2;
	@FXML
	private ImageView imgViewDealerButtonPos3;
	@FXML
	private ImageView imgViewDealerButtonPos4;

	@FXML
	private BorderPane OuterBorderPane;

	@FXML
	private Label lblNumberOfPlayers;
	@FXML
	private TextArea txtPlayerArea;

	@FXML
	private Button btnStartGame;
	@FXML
	private ToggleButton btnPos1SitLeave;
	@FXML
	private ToggleButton btnPos2SitLeave;
	@FXML
	private ToggleButton btnPos3SitLeave;
	@FXML
	private ToggleButton btnPos4SitLeave;

	@FXML
	private Label lblPos1Name;
	@FXML
	private Label lblPos2Name;
	@FXML
	private Label lblPos3Name;
	@FXML
	private Label lblPos4Name;

	@FXML
	private HBox hBoxDeck;

	@FXML
	private HBox hboxP1Cards;
	@FXML
	private HBox hboxP2Cards;
	@FXML
	private HBox hboxP3Cards;
	@FXML
	private HBox hboxP4Cards;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		imgViewDealerButtonPos3.setVisible(true);
		imgViewDealerButtonPos4.setVisible(true);		
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void handlePlay() {
	}

	@FXML
	public void GetGameState() {
		Action act = new Action(eAction.GameState, mainApp.getPlayer());
		mainApp.messageSend(act);
	}

	public void btnSitLeave_Click(ActionEvent event) {
		ToggleButton btnSitLeave = (ToggleButton) event.getSource();
		int iPlayerPosition = 0;
		if (btnSitLeave.isSelected()) {
			switch (btnSitLeave.getId().toString()) {
			case "btnPos1SitLeave":
				iPlayerPosition = ePlayerPosition.ONE.getiPlayerPosition();
				break;
			case "btnPos2SitLeave":
				iPlayerPosition = ePlayerPosition.TWO.getiPlayerPosition();
				break;
			case "btnPos3SitLeave":
				iPlayerPosition = ePlayerPosition.THREE.getiPlayerPosition();
				break;
			case "btnPos4SitLeave":
				iPlayerPosition = ePlayerPosition.FOUR.getiPlayerPosition();
				break;
			}
		} else {
			iPlayerPosition = 0;
		}

		//	Set the PlayerPosition in the Player
		mainApp.getPlayer().setiPlayerPosition(iPlayerPosition);

		//	Build an Action message
		Action act = new Action(btnSitLeave.isSelected() ? eAction.Sit : eAction.Leave, mainApp.getPlayer());

		// Send the Action to the Hub
		mainApp.messageSend(act);
	}

	public void MessageFromMainApp(String strMessage) {
		System.out.println("Message received by PokerTableController: " + strMessage);
	}

	public void Handle_TableState(Table HubPokerTable) {

		//TODO: If this message is called, that means there
		//		was a change to the state of the Table (player
		//		probably ran 'sit' or 'leave'
		//		The Table was updated, you just have to refresh the
		//		UI controls to show the current state of the 
		//		Table object
		
		//TODO: run the 'getHashPlayers' method, iterate 
		//		for all players and update the player label
		//		and state of the sit/leave button.

		//		Example: Joe sits at Position 1
		//		Joe should see the 'Sit' button in position 1 in the
		//		'pressed in' state, and with 
	}

	@FXML
	void btnStart_Click(ActionEvent event) {
		// Start the Game
		//TODO: Create an instance of Action, Action = StartGame
		//		Send the message to the hub
	}

	@FXML
	void btnDeal_Click(ActionEvent event) {
		// Example - how to set card images in the HBOX
		hboxP1Cards.getChildren().clear();

		ImageView i1 = new ImageView(new Image(getClass().getResourceAsStream("/img/26.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i1);

		ImageView i2 = new ImageView(new Image(getClass().getResourceAsStream("/img/27.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i2);

		ImageView i3 = new ImageView(new Image(getClass().getResourceAsStream("/img/28.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i3);

		ImageView i4 = new ImageView(new Image(getClass().getResourceAsStream("/img/29.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i4);

		ImageView i5 = new ImageView(new Image(getClass().getResourceAsStream("/img/30.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i5);

	}

	@FXML
	public void btnFold_Click(ActionEvent event) {
		Button btnFold = (Button) event.getSource();
		switch (btnFold.getId().toString()) {
		case "btnPlayer1Fold":
			// Fold for Player 1
			break;
		case "btnPlayer2Fold":
			// Fold for Player 2
			break;
		case "btnPlayer3Fold":
			// Fold for Player 3
			break;
		case "btnPlayer4Fold":
			// Fold for Player 4
			break;

		}
	}

	@FXML
	public void btnCheck_Click(ActionEvent event) {
		Button btnCheck = (Button) event.getSource();
		switch (btnCheck.getId().toString()) {
		case "btnPlayer1Check":
			// Check for Player 1
			break;
		case "btnPlayer2Check":
			// Check for Player 2
			break;
		case "btnPlayer3Check":
			// Check for Player 3
			break;
		case "btnPlayer4Check":
			// Check for Player 4
			break;
		}
	}

	private void FadeButton(Button btn) {
		FadeTransition ft = new FadeTransition(Duration.millis(3000), btn);
		ft.setFromValue(1.0);
		ft.setToValue(0.3);
		ft.setCycleCount(4);
		ft.setAutoReverse(true);

		ft.play();
	}


}