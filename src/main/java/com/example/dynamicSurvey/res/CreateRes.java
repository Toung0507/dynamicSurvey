package com.example.dynamicSurvey.res;

public class CreateRes extends BasicRes {

	private int qusetionID;

	public CreateRes() {
		super();
	}

	public CreateRes(int code, String message) {
		super(code, message);
	}

	public CreateRes(int code, String message, int qusetionID) {
		super(code, message);
		this.qusetionID = qusetionID;
	}

	public CreateRes(int qusetionID) {
		super();
		this.qusetionID = qusetionID;
	}


	public int getQusetionID() {
		return qusetionID;
	}

	public void setQusetionID(int qusetionID) {
		this.qusetionID = qusetionID;
	}

}
