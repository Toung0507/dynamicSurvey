package com.example.dynamicSurvey.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.dynamicSurvey.constants.ReplyMessage;
import com.example.dynamicSurvey.constants.Type;
import com.example.dynamicSurvey.dao.FillinDao;
import com.example.dynamicSurvey.dao.QuestionDao;
import com.example.dynamicSurvey.dao.UserDao;
import com.example.dynamicSurvey.entity.Fillin;
import com.example.dynamicSurvey.entity.Question;
import com.example.dynamicSurvey.entity.User;
import com.example.dynamicSurvey.req.AnswerVo;
import com.example.dynamicSurvey.req.FeedbackReq;
import com.example.dynamicSurvey.req.FillinReq;
import com.example.dynamicSurvey.res.BasicRes;
import com.example.dynamicSurvey.res.FeedbackRes;
import com.example.dynamicSurvey.res.FeedbackUserVo;
import com.example.dynamicSurvey.res.GetFeedbackUserRes;

import jakarta.transaction.Transactional;

@Service
public class FillinService {

	@Autowired
	private FillinDao fillinDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private QuestionDao questionDao;

	@Transactional(rollbackOn = Exception.class)
	public BasicRes fillin(FillinReq req) {

		// 參數檢查
		BasicRes res = checkParams(req);
		if (res != null) {
			return res;
		}

		// 新增資料
		try {
			for (AnswerVo vo : req.getAnswerVoList()) {
				fillinDao.insert(req.getQuizID(), vo.getQuestion().getQuestionId(), //
						req.getEmail(), vo.getAnswer());
			}

		} catch (Exception e) {
			throw e;
		}

		return new BasicRes(ReplyMessage.SUCCESS.getCode(), //
				ReplyMessage.SUCCESS.getMessage());

	}

	private BasicRes checkParams(FillinReq req) {
		if (req.getQuizID() <= 0) {
			return new BasicRes(ReplyMessage.QUIZ_ID_ERROR.getCode(), //
					ReplyMessage.QUIZ_ID_ERROR.getMessage());
		}

		if (!StringUtils.hasText(req.getEmail())) {
			return new BasicRes(ReplyMessage.USER_EMAIL_ERROR.getCode(), //
					ReplyMessage.USER_EMAIL_ERROR.getMessage());
		}

		if (!StringUtils.hasText(req.getName())) {
			return new BasicRes(ReplyMessage.USER_NAME_ERROR.getCode(), //
					ReplyMessage.USER_NAME_ERROR.getMessage());
		}

		if (req.getAge() <= 18) {
			return new BasicRes(ReplyMessage.USER_AGE_ERROR.getCode(), //
					ReplyMessage.USER_AGE_ERROR.getMessage());
		}

		// 檢查是否必填 & 是否有相對應的答案
		for (AnswerVo vo : req.getAnswerVoList()) {
			if (vo.getQuestion().isRequired()) {
				if (!StringUtils.hasText(vo.getAnswer())) {
					return new BasicRes(ReplyMessage.ANSWER_REQUIRED.getCode(), //
							ReplyMessage.ANSWER_REQUIRED.getMessage());
				}
			}
			// 檢查選項與答案是否匹配(前提是答案要有值)
			// 單複選有答案的話，要去檢查是否與 Question 中的選項匹配
			/**
			 * if (Type.isChoiceType(vo.getQuestion().getType())) {<br>
			 * if (StringUtils.hasText(vo.getAnswer())) {<br>
			 * if(vo.getQuestion().getP)<br>
			 * }<br>
			 * }<br>
			 */

		}

		return null;
	}

	// 取得所有填答的使用者
	public GetFeedbackUserRes getAllFillinUsers(int quizId) {
		if (quizId <= 0) {
			return new GetFeedbackUserRes(ReplyMessage.QUIZ_ID_ERROR.getCode(), //
					ReplyMessage.QUIZ_ID_ERROR.getMessage());
		}

		List<Fillin> list = fillinDao.getByQuizId(quizId);

		// 接著先新增一個空的 answerList
		List<AnswerVo> answerVoList = new ArrayList<>();
		// 將 user 放到一個 map
		Map<String, FeedbackUserVo> emailUserVoMap = new HashMap<>();
		//
		List<FeedbackUserVo> userVoList = new ArrayList<>();
		//
		Map<String, List<AnswerVo>> answerVoMap = new HashMap<>();
		for (Fillin fillin : list) {

			if (!emailUserVoMap.containsKey(fillin.getUserEmail())) {

				/*
				 * 使用 map 可以防止同一位使用者有多題的作答時，會產生多個 FeedbackUserVo， 因為是同一位使用者對同一張問卷作答多題
				 */
				/* 新的使用者必須要重置 List<AnswerVo> answerVoList */

				// 取出相對應的 user 資料
				User user = userDao.getByEmail(fillin.getUserEmail());
				answerVoList = new ArrayList<>();
				// 將取出的 user 資料放到 feedbackUserVo 內
				FeedbackUserVo userVo = new FeedbackUserVo(user.getName(), user.getTel(), //
						user.getEmail(), user.getAge(), fillin.getFillinDate(), answerVoList);

				userVoList.add(userVo);
				emailUserVoMap.put(fillin.getUserEmail(), userVo);
				answerVoMap.put(fillin.getUserEmail(), answerVoList);
			}

			// 透過 quizId 去取出全部相對應的題目回傳
			List<Question> questionList = questionDao.getByQuizId(quizId);

			// 接著將取出的題目 跟 填答的問題 ID 去做比對
			// 有比對到，就新增一個 answervo
			for (Question question : questionList) {
				if (fillin.getQuestionId() == question.getQuestionId()) {
					AnswerVo answerVo = new AnswerVo(question, fillin.getAnswer());
					// 取出特定使用者舊的 answerVoList
					answerVoList = answerVoMap.get(fillin.getUserEmail());
					// 增加新的 answerVo
					answerVoList.add(answerVo);
					// 把新的 answerVoList 放回到 answerVomap 中
					answerVoMap.put(fillin.getUserEmail(), answerVoList);
				}
			}
		}
		return new GetFeedbackUserRes(ReplyMessage.SUCCESS.getCode(), //
				ReplyMessage.SUCCESS.getMessage(), userVoList);

	}
	// 老師寫的版本
//	public GetFeedbackUserRes getAllFillinUsers(int quizId) {
//		if(quizId <= 0) {
//			return new GetFeedbackUserRes(ReplyMessage.QUIZ_ID_ERROR.getCode(), //
//					ReplyMessage.QUIZ_ID_ERROR.getMessage());
//		}
//		List<Fillin> list = fillinDao.getByQuizId(quizId);
//		List<AnswerVo> answerVoList = new ArrayList<>();
//		Map<String, FeedbackUserVo> map = new HashMap<>();
//		List<FeedbackUserVo> userVoList = new ArrayList<>();
//		Map<String, List<AnswerVo>> answerVomap = new HashMap<>();
//		for(Fillin fillin : list) {
//			if(!map.containsKey(fillin.getUserEmail())) {
//				/* 使用 map 可以防止同一位使用者有多題的作答時，會產生多個 FeedbackUserVo，
//				 * 因為是同一位使用者對同一張問卷作答多題*/
//				/* 新的使用者必須要重置 List<AnswerVo> answerVoList */
//				User user = userDao.getByEmail(fillin.getUserEmail());
//				answerVoList = new ArrayList<>();
//				FeedbackUserVo userVo = new FeedbackUserVo(user.getName(), user.getPhone(), //
//						user.getEmail(), user.getAge(), fillin.getFillinDate(), answerVoList);
//				userVoList.add(userVo);
//				map.put(fillin.getUserEmail(), userVo);
//				answerVomap.put(fillin.getUserEmail(), answerVoList);
//			}			
//			List<Question> questionList = questionDao.getByQuizId(quizId);
//			for(Question question : questionList) {
//				/* 比對 question_id: 一樣的就把答案放到 AnswerVo*/
//				if(fillin.getQuestionId() == question.getQuestionId()) {
//					AnswerVo answerVo = new AnswerVo(question, fillin.getAnswer());
//					/* 取出特定使用者舊的 answerVoList */
//					answerVoList = answerVomap.get(fillin.getUserEmail());
//					/* 增加新的 answerVo*/
//					answerVoList.add(answerVo);
//					/* 把新的 answerVoList 放回到 answerVomap 中 */
//					answerVomap.put(fillin.getUserEmail(), answerVoList);
//				}
//			}
//		}
//		return new GetFeedbackUserRes(ReplyMessage.SUCCESS.getCode(), //
//				ReplyMessage.SUCCESS.getMessage(), userVoList);
//	}
//	

	public FeedbackRes feedback(FeedbackReq req) {
		// 參數檢查
		// 透過 quizId 取得所有問題
		List<Question> questionList = questionDao.getByQuizId(req.getQuizId());
		// 取出某一位在某個表單中所填答得全部資料
		List<Fillin> fillinList = fillinDao.getByQuizIdAndEmail(req.getQuizId(), req.getEmail());
		// 取出 user 的資料
		User user = userDao.getByEmail(req.getEmail());
		if (user == null) {
			return new FeedbackRes(ReplyMessage.USER_NOT_FOUND.getCode(), //
					ReplyMessage.USER_NOT_FOUND.getMessage());
		}



		// 把答案匹配到對應的問題上
		List<AnswerVo> answerVoList = new ArrayList<>();
		for (Question qu : questionList) {
			// vo 中一定會有問題 Question ，但因為有可能不是必填，所以有機率沒有 Fillin
			AnswerVo vo = new AnswerVo(qu);
			for (Fillin fillin : fillinList) {
				if (qu.getQuestionId() == fillin.getQuestionId()) {
					// 把 answer 放到 vo 中
					vo.setAnswer(fillin.getAnswer());
					answerVoList.add(vo);
					// 有匹配到相同的題號，就可以跳過剩下fillin的比對
					break;
				}
			}
		}
		
		FeedbackRes res = new FeedbackRes(ReplyMessage.SUCCESS.getCode(), //
				ReplyMessage.SUCCESS.getMessage(),req.getQuizId(), req.getEmail(), //
				user.getName(), user.getTel(), user.getAge(),answerVoList);

		return res;
	}

}
