package poker.app;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import netgame.common.Client;
import poker.app.model.PokerHub;
import poker.app.view.ClientServerStartController;
import poker.app.view.PokerTableController;
import poker.app.view.RootLayoutController;
import pokerBase.Action;
import pokerBase.GamePlay;
import pokerBase.Player;
import pokerBase.Table;
import pokerEnums.eAction;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	private PokerHub pHub = null;
	private PokerClient pClient = null;

	private PokerTableController pokerController = null;
	private RootLayoutController rootController = null;

	private boolean isServer = false;

	private Player appPlayer;

	public int GetPlayerID()
	{
		return pClient.getID();
	}
	public Player getPlayer() {
		return appPlayer;
	}

	public void setPlayer(Player player) {
		this.appPlayer = player;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		// INIT is executed by the Application framework FIRST
		//connection.startConnection();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// START is executed by the Application framework after INIT
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 500, 500);

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Poker");

		// Set the application icon.
		//this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/26.png")));

		this.primaryStage.setScene(scene);
		this.primaryStage.show();

		showClientServer();
	}

	public void showPoker(boolean bStartHub, String strComputerName, int iPort, String strPlayerName) {

		if (bStartHub) {
			try {
				pHub = new PokerHub(iPort);
			} catch (Exception e) {
				System.out.println("Error: Can't listen on port " + iPort);
			}
		}
		try {
			pClient = new PokerClient(strComputerName, iPort);
		} catch (IOException e) {
			e.printStackTrace();
		}

		setPlayer(new Player(strPlayerName, pClient.getID()));
		
		initRootLayout();

		showPokerTable();		
	}

	public void showClientServer() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			
			loader =  new FXMLLoader(getClass().getResource("view/ClientServerStart.fxml"));
			 
			BorderPane ClientServerOverview = (BorderPane) loader.load();

			Scene scene = new Scene(ClientServerOverview);

			primaryStage.setScene(scene);

			// Give the controller access to the main app.
			ClientServerStartController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initRootLayout() {
		try {
			
			Screen screen = Screen.getPrimary();
			Rectangle2D bounds = screen.getVisualBounds();

			primaryStage.setX(bounds.getMinX());
			primaryStage.setY(bounds.getMinY());
			primaryStage.setWidth(bounds.getWidth());
			primaryStage.setHeight(bounds.getHeight());
			
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			// Give the controller access to the main app.
			// RootLayoutController controller = loader.getController();
			rootController = loader.getController();

			rootController.setMainApp(this);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showPokerTable() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PokerTable.fxml"));
			BorderPane pokerOverview = (BorderPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(pokerOverview);

			// Give the controller access to the main app.
			//PokerTableController controller = loader.getController();
			pokerController = loader.getController();
			pokerController.setMainApp(this);
			
			getPlayer().setiPlayerPosition(0);
			Action act = new Action(eAction.TableState, getPlayer());
			messageSend(act);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void EndPoker() {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});
	}

	@Override
	public void stop() throws Exception {
		//connection.closeConnection();
	}

	public void messageSend(final Object message)
	{
		System.out.println("Sending message from MainApp");
		pClient.messageSend(message);	
	}
	
	public String getRuleName()
	{
		return rootController.getRuleName();
	}
	
	private class PokerClient extends Client {

		public PokerClient(String hubHostName, int hubPort) throws IOException {
			super(hubHostName, hubPort);
		}

		/*
		 * messageSend - One single place to send messages
		 */
		protected void messageSend(Object message)
		{
			System.out.println("Sending message from MainApp.Client");
			resetOutput();
			super.send(message);
		}
		
		/*
		 * messageReceived will get an Object message... it's up to you to determine
		 * what should happen to that the message.
		 * 
		 * If it's a Table, handle Table - level action
		 * If it's a GamePlay, handle GamePlay - level action
		 */
		@Override
		protected void messageReceived(final Object message) {
			Platform.runLater(() -> {		
				System.out.println("Message Received.  The message: " + message);
				
				if (message instanceof String)
				{				
					System.out.println("Message Received " + message);
				}
				else if (message instanceof Table)
				{
					//TODO: If the message is a Table, run the 
					//		method Handle_TableState in the 
					//		pokerController.
				}
				pokerController.MessageFromMainApp((String)message);
			});
		}
		
		
		@Override
		/*
		 * serverShutdown - Call the hard exit.
		 */
	    protected void serverShutdown(String message) {
	    	
			Platform.runLater(() -> {		
				Platform.exit();
		        System.exit(0);
			});
	    }
	

	}
}
