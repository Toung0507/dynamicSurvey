package com.example.dynamicSurvey.res;

//單純改名字，是為了配合 service 層 Update方法返回的類型
//創建的class而已,不然那返回類型是CreateRes很奇怪，不符合見名知意。也可以直接用
//CreateRes，但有人會介意
public class UpdateRes extends CreateRes{

	public UpdateRes() {
		super();
	}

	public UpdateRes(int code, String message, int questionId) {
		super(code, message, questionId);
	}

	public UpdateRes(int code, String message) {
		super(code, message);
	}

}
