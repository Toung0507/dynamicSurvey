package com.example.dynamicSurvey.res;

import java.util.List;

public class GetFeedbackUserRes extends BasicRes{
	
	private List<FeedbackUserVo> userVoList;

	public GetFeedbackUserRes() {
		super();
	}

	public GetFeedbackUserRes(int code, String message) {
		super(code, message);
	}

	public GetFeedbackUserRes(int code, String message, List<FeedbackUserVo> userVoList) {
		super(code, message);
		this.userVoList = userVoList;
	}

	public List<FeedbackUserVo> getUserVo() {
		return userVoList;
	}

	public void setUserVo(List<FeedbackUserVo> userVo) {
		this.userVoList = userVo;
	}
	
}
