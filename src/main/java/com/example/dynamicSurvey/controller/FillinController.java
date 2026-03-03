package com.example.dynamicSurvey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dynamicSurvey.req.FeedbackReq;
import com.example.dynamicSurvey.req.FillinReq;
import com.example.dynamicSurvey.res.BasicRes;
import com.example.dynamicSurvey.res.FeedbackRes;
import com.example.dynamicSurvey.res.GetFeedbackUserRes;
import com.example.dynamicSurvey.service.FillinService;

@RestController
@CrossOrigin(origins = "*")
// 最後把這個檔案刪掉了，全部放到 QuizController 去，但我想分開放
public class FillinController {

	@Autowired
	private FillinService fillinService;
	
	@PostMapping("quiz/fillin")
	public BasicRes fillin(@RequestBody FillinReq req) {
		return fillinService.fillin(req);
	}
	
	@GetMapping("quiz_get_all_fillin_users")
	public GetFeedbackUserRes getAllFillinUsers(@RequestParam("quizId") int quizId) {
		return fillinService.getAllFillinUsers(quizId);
	}
	
	@PostMapping("quiz/feedback")
	public FeedbackRes feedback(@RequestBody FeedbackReq req) {
		return fillinService.feedback(req);
	}
}
