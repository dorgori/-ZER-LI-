package server;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ServerRunningGui implements Initializable{

    @FXML
    private Button btnDisc;

    
    @FXML
    private void disconnectPressed(ActionEvent event)throws Exception{
    		System.exit(0);
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
