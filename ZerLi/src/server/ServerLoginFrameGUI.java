package server;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * This class responsible for the functionality of the server gui
 * 
 * @author dorgori
 *
 */
public class ServerLoginFrameGUI implements Initializable {

	Stage s;
	@FXML
	private Button btnLogin;

	@FXML
	private Button btnExit;

	@FXML
	private TextField txtPath;

	@FXML
	private TextField txtUsername;

	@FXML
	private TextField txtPassword;

	/**
	 * This method responsible for showing the gui of the server until disconnecting
	 * @param event
	 * @throws IOException
	 */
	public void loginListener(ActionEvent event) throws IOException {
		Server.setDBAtributers(txtPath.getText(), txtUsername.getText(), txtPassword.getText());
		callserver();
		((Node) event.getSource()).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("ServerRunning.fxml").openStream());
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void callserver() {
		Server.tryConnectingToServer();
	}

	/**
	 * This method occur when exit
	 * 
	 * @param e
	 *            the event
	 * @throws Exception
	 */
	public void exitBtnListenner(ActionEvent e) throws Exception {
		System.exit(0);
	}

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("ServerLoginFrame.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		s = primaryStage;
		primaryStage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

}
