package com.example.dynamicSurvey.res;

import java.time.LocalDate;
import java.util.List;

import com.example.dynamicSurvey.req.AnswerVo;

public class FeedbackUserVo {
	private String userName;

	private String tel;

	private String email;

	private int age;

	private LocalDate fillinDate;

	List<AnswerVo> answerVoList;

	public FeedbackUserVo() {
		super();
	}

	public FeedbackUserVo(String userName, String tel, String email, int age, LocalDate fillinDate,
			List<AnswerVo> answerVoList) {
		super();
		this.userName = userName;
		this.tel = tel;
		this.email = email;
		this.age = age;
		this.fillinDate = fillinDate;
		this.answerVoList = answerVoList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public LocalDate getFillinDate() {
		return fillinDate;
	}

	public void setFillinDate(LocalDate fillinDate) {
		this.fillinDate = fillinDate;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<AnswerVo> getAnswerVoList() {
		return answerVoList;
	}

	public void setAnswerVoList(List<AnswerVo> answerVoList) {
		this.answerVoList = answerVoList;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
