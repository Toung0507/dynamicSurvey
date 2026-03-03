package com.example.dynamicSurvey.res;

import java.util.List;

import com.example.dynamicSurvey.req.AnswerVo;

public class FeedbackRes extends BasicRes {
	private int quizID;

	private String email;

	private String name;

	private String tel;

	private int age;

	private List<AnswerVo> answerVoList;

	public FeedbackRes() {
		super();
	}

	public FeedbackRes(int quizID, String email, String name, String tel, int age) {
		super();
		this.quizID = quizID;
		this.email = email;
		this.name = name;
		this.tel = tel;
		this.age = age;
	}

	public FeedbackRes(int code, String message) {
		super(code, message);
	}

	public FeedbackRes(int code, String message, int quizID, String email, String name, String tel, int age,
			List<AnswerVo> answerVoList) {
		super(code, message);
		this.quizID = quizID;
		this.email = email;
		this.name = name;
		this.tel = tel;
		this.age = age;
		this.answerVoList = answerVoList;
	}

	public int getQuizID() {
		return quizID;
	}

	public void setQuizID(int quizID) {
		this.quizID = quizID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<AnswerVo> getAnswerVoList() {
		return answerVoList;
	}

	public void setAnswerVoList(List<AnswerVo> answerVoList) {
		this.answerVoList = answerVoList;
	}

}
