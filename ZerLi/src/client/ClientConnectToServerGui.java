package client;

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
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Describes the first gui the launches when launching the Client main class.
 * the IP of the server host to connect to need to be typed here or you can
 * leave it empty if it's the server is locally
 *
 */
public class ClientConnectToServerGui implements Initializable {

	@FXML
	private Button btnLogin;

	@FXML
	private Text lblIP;

	@FXML
	private TextField txtIP;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	/**
	 * enables first gui
	 * 
	 * @param primaryStage
	 *            the first scene to be shown to the client
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("ClientConnectToServer.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("login server");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * connect to server and proceed to next window if connection was successful
	 * 
	 * @param event
	 *            clicked on "Connect" button
	 * @throws Exception
	 */
	@FXML
	private void btnListener(ActionEvent event) throws Exception {
		Client.connectToServer(txtIP.getText());
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("login window");
		Pane root = loader.load(getClass().getResource("loginGui.fxml").openStream());
		LoginController.changeWindow(root);
	}
}
