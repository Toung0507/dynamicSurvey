package com.example.dynamicSurvey.req;

import com.example.dynamicSurvey.entity.Question;

// 一個 AnswerVo 代表一題問題的所有資訊以及答案
public class AnswerVo {

	private Question question;

	// 答案或選項
	private String answer;

	public Question getQuestion() {
		return question;
	}

	public AnswerVo() {
		super();
	}

	public AnswerVo(Question question) {
		super();
		this.question = question;
	}

	public AnswerVo(Question question, String answer) {
		super();
		this.question = question;
		this.answer = answer;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
