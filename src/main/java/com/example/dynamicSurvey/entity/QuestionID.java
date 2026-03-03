package com.example.dynamicSurvey.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class QuestionID implements Serializable{
	private int quizID;
	
	private int questionId;

	public int getQuizID() {
		return quizID;
	}

	public void setQuizID(int quizID) {
		this.quizID = quizID;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
}
