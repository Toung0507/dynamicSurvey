package com.example.dynamicSurvey.res;

import java.util.List;

import com.example.dynamicSurvey.entity.Question;

public class GetQuestionRes extends BasicRes {
	private List<Question> questionList;

	public GetQuestionRes() {
		super();
	}

	public GetQuestionRes(int code, String message) {
		super(code, message);
	}

	public GetQuestionRes(List<Question> questionList) {
		super();
		this.questionList = questionList;
	}

	public GetQuestionRes(int code, String message, List<Question> questionList) {
		super(code, message);
		this.questionList = questionList;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}

}
