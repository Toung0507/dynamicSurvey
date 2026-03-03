package com.example.dynamicSurvey.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name="qustion")
@IdClass(value = QuestionID.class)
public class Question {

	@Id
	@Column(name = "quiz_id")
	private int quizID;
	
	@Id
	@Column(name = "question_id")
	private int questionId;

	@Column(name = "question")
	private String question;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "is_required")
	private boolean required;

	@Column(name = "options")
	private String options;
	
	public Question() {
		super();
	}
	
	public Question(int quizID, int questionId, String question, String type, boolean required, String options) {
		super();
		this.quizID = quizID;
		this.questionId = questionId;
		this.question = question;
		this.type = type;
		this.required = required;
		this.options = options;
	}

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

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

}
