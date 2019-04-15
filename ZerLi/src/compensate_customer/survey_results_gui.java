package compensate_customer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import client.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import survey.Survey;

/**
 * This class provide the gui for survey options
 * 
 * @author dorgori
 *
 */
public class survey_results_gui implements Initializable {

	public static Client client;
	private ArrayList<Survey> Survey_arr = new ArrayList<Survey>();
	ObservableList<String> surveyList;
	private static int selectedListIndex;
	ArrayList<String> Pro_comment_arr = new ArrayList<>();
	@FXML
	private ListView<String> list_view = new ListView<String>();

	@FXML
	private TextArea add_comment;

	@FXML
	private Button submit;

	@FXML
	private Button cancel;

	/**
	 * This method initialize the list of survey
	 * 
	 * @throws IOException
	 */
	private void initSurveyList() throws IOException {
		int[] ans = new int[6];
		ArrayList<String> survey_answers = new ArrayList<String>();
		Survey_arr = CompensateController.ask_for_survey(); // array of surveys

		if (Survey_arr != null) {
			System.out.println(Survey_arr.size()); // sholud be equal to numofsurvey
			for (int i1 = 0; i1 < Survey_arr.size(); i1++) {
				for (int k = 0; k < 6; k++)
					ans[k] = Survey_arr.get(i1).getAnswer()[k]; // copy the 6 answers

				survey_answers.add("" + ans[0] + "        " + ans[1] + "         " + ans[2] + "         " + ans[3]
						+ "         " + ans[4] + "       " + ans[5]);
				surveyList = FXCollections.observableArrayList(survey_answers);
				list_view.setItems(surveyList);
				list_view.refresh();
			}
		} else {
			System.out.println("no array answers");
		}
	}

	/**
	 * Change window function- go back to customer service window
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void cancel(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader
				.load(getClass().getResource("/compensate_customer/customerServiceEmployeeGui.fxml").openStream());
		LoginController.setStageTitle("CustomerServiceEmployeeMenu");
		CompensateController.changeWindow(root);
	}

	/**
	 * This function occur when submit is press submit the survey
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void submitPressed(ActionEvent event) throws IOException {
		selectedListIndex = list_view.getSelectionModel().getSelectedIndex();
		if (selectedListIndex >= 0) {
			if (add_comment.getText() != null) {
				CompensateController.add_pro_comment(selectedListIndex, add_comment.getText());
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				FXMLLoader loader = new FXMLLoader();
				Pane root = loader.load(
						getClass().getResource("/compensate_customer/customerServiceEmployeeGui.fxml").openStream());
				LoginController.setStageTitle("CustomerServiceEmployeeMenu");
				CompensateController.changeWindow(root);
			}
		}
	}

	/**
	 * This method occur when show pro is pressed
	 * 
	 * @param event
	 */
	@FXML
	void show_pro_comment(MouseEvent event) {

		Pro_comment_arr = CompensateController.ask_for_pro_comment_arr(); // array of surveys
		selectedListIndex = list_view.getSelectionModel().getSelectedIndex();
		if (selectedListIndex >= 0) {
			add_comment.setText(Pro_comment_arr.get(selectedListIndex));
		}
	}

	/**
	 * This method occur when the gui is open
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			initSurveyList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
