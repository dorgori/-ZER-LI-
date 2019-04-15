package server;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class represent the first gui that open
 * 
 * @author dorgori
 *
 */
public class GuiOpener extends Application {
	public static String[] parameters = null;
	public ServerLoginFrameGUI Frame;

	public GuiOpener() {
	};

	/**
	 * This method start the chain eventually the function start will occur
	 */
	public static void init_launch() {
		Application.launch(parameters);
	}

	/**
	 * This function upload the gui for the server
	 */
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("server window");
		Frame = new ServerLoginFrameGUI(); // create ProductFrame
		Frame.start(primaryStage);
	}

}