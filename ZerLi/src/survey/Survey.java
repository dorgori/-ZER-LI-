package survey;

import java.io.Serializable;

public class Survey implements Serializable{//
	private int surveyID;
	private String storeID;
	private String createdDate;
	private int []answer=new int [6];
	private int totalAvg;
	private String procomment;
	
	/**
	 * Contractor to add new survey to db 
	 * @param arr
	 * @param sid
	 */
	public Survey(int arr[], String sid) {
		int sum=0;
		for(int i=0; i<6;i++) {
			answer[i]=arr[i];
			sum+=arr[i];
		}
		totalAvg=(sum)/6;
		storeID=sid;
	}
	
/**
 * Contractor after with take survey from db 
 * @param surveyID
 * @param createdDate
 * @param arr
 * @param sid
 * @param Procomment
 */
	public Survey(int surveyID, String createdDate,int[] arr , String sid, String Procomment) {
		this.surveyID=surveyID;
		this.createdDate=createdDate;
		this.storeID=sid;
		int sum=0;
		for(int i=0; i<6;i++) {
			answer[i]=arr[i];
			sum+=arr[i];
		}
		totalAvg=(sum)/6;
	}
	/**
	 * getters and setters to the attributs.
	 * @return
	 */
	public int getSurveyID() {
		return surveyID;
	}
	public void setSurveyID(int surveyID) {
		this.surveyID = surveyID;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	public int[] getAnswer() {
		return answer;
	}

	public void setAnswer(int[] answer) {
		this.answer = answer;
	}

	public String getProcomment() {
		return procomment;
	}

	public void setProcomment(String procomment) {
		this.procomment = procomment;
	}
	
	public int getTotalAvg() {
		return totalAvg;
	}
	public void setTotalAvg(int totalAvg) {
		this.totalAvg = totalAvg;
	}

	public String getStoreID() {
		return storeID;
	}

	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}
	
	public void setAns(int ans,int place) {
		this.answer[place]=ans;
	}
	
	public int getAns(int place) {
		return answer[place];
	}
	
}
