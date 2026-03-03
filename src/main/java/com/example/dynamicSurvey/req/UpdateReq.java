package com.example.dynamicSurvey.req;

public class UpdateReq extends CreateReq {
	private int quizId;

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}
}
