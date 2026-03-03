package com.example.dynamicSurvey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dynamicSurvey.req.CreateReq;
import com.example.dynamicSurvey.req.DeleteReq;
import com.example.dynamicSurvey.req.UpdateReq;
import com.example.dynamicSurvey.res.BasicRes;
import com.example.dynamicSurvey.res.CreateRes;
import com.example.dynamicSurvey.res.GetQuestionRes;
import com.example.dynamicSurvey.res.GetQuizRes;
import com.example.dynamicSurvey.res.UpdateRes;
import com.example.dynamicSurvey.service.QuizService;
import com.mysql.cj.x.protobuf.MysqlxCrud.Delete;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class QuizController {

	@Autowired
	private QuizService quizService;

	// @Valid: 讓 CreateReq 中的屬性限制生效 
	@PostMapping("quiz/create")
	public CreateRes create(@Valid @RequestBody CreateReq req) {
		return quizService.create(req);
	}

	@GetMapping("quiz/getAll")
	public GetQuizRes getQuizList() {
		return quizService.getQuizList();
	}

	// API 路徑 ： http://localhost:8080/quiz/get_question_list?quizId=1(quizID)
	@GetMapping("quiz/get_question_list")
	public GetQuestionRes getQuestionList(@RequestParam("quizId") int quizID) {
		return quizService.getQuestionList(quizID);
	}

	@PostMapping("quiz/update")
	public UpdateRes update(@RequestBody UpdateReq req) {
		return quizService.udpate(req);
	}

	@GetMapping("quiz/delete")
	public BasicRes delete(@RequestParam("quizId") int quizId) {
		return quizService.delete(quizId);
	}
}
