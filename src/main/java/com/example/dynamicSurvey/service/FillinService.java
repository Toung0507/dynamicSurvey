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
	
	// AI 給的解釋
//	public GetFeedbackUserRes getAllFillinUsers(int quizId) {
//	    // 基礎檢查：如果 ID 不合法就直接回傳錯誤
//	    if (quizId <= 0) {
//	        return new GetFeedbackUserRes(ReplyMessage.QUIZ_ID_ERROR.getCode(), 
//	                                      ReplyMessage.QUIZ_ID_ERROR.getMessage());
//	    }
//
//	    // 第一步：從資料庫抓出這份問卷「所有的填答紀錄」 (這是扁平的清單，裡面人跟題目都擠在一起)
//	    List<Fillin> list = fillinDao.getByQuizId(quizId);
//	    
//	    // 預先準備好所有的題目，等等比對用 (這行放外面可以省下大量資料庫查詢次數)
//	    List<Question> questionList = questionDao.getByQuizId(quizId);
//
//	    // 第二步：準備暫存區 (Map)
//	    // emailUserVoMap: 用來記錄「這個人建立過了嗎？」，防止重複建立個資
//	    Map<String, FeedbackUserVo> emailUserVoMap = new HashMap<>();
//	    // answerVomap: 用來記錄「某個使用者的專屬籃子」，方便我們隨時把答案丟進去
//	    Map<String, List<AnswerVo>> answerVomap = new HashMap<>();
//	    
//	    // 最終要回傳的「填答者清單」
//	    List<FeedbackUserVo> userVoList = new ArrayList<>();
//
//	    // 第三步：開始跑迴圈，一筆一筆處理填答紀錄
//	    for (Fillin fillin : list) {
//	        String email = fillin.getUserEmail();
//
//	        // 【處理人的部分】：如果這個 Email 沒出現過，代表是新的一位填答者
//	        if (!emailUserVoMap.containsKey(email)) {
//	            
//	            // 1. 去資料庫抓這個人的基本個資
//	            User user = userDao.getByEmail(email);
//	            
//	            // 2. 為這個人準備一個全新的「答案籃子」 (空的 List)
//	            List<AnswerVo> newAnswerList = new ArrayList<>();
//	            
//	            // 3. 建立這個人的 Vo 物件，並把「籃子」交給他
//	            FeedbackUserVo userVo = new FeedbackUserVo(
//	                user.getName(), user.getTel(), user.getEmail(), 
//	                user.getAge(), fillin.getFillinDate(), newAnswerList
//	            );
//
//	            // 4. 把這個人加入最終結果清單
//	            userVoList.add(userVo);
//	            
//	            // 5. 重要的紀錄：在 Map 記下這個人已經處理過，並記下他的籃子位置
//	            emailUserVoMap.put(email, userVo);
//	            answerVomap.put(email, newAnswerList);
//	        }
//
//	        // 【處理答案比對的部分】：
//	        // 不論是不是新的人，現在這筆 fillin 的答案都要對應到正確的題目
//	        for (Question question : questionList) {
//	            
//	            // 如果這筆填答的題號 == 題庫裡的題號
//	            if (fillin.getQuestionId() == question.getQuestionId()) {
//	                
//	                // 1. 把題目跟答案打包成 AnswerVo
//	                AnswerVo answerVo = new AnswerVo(question, fillin.getAnswer());
//	                
//	                // 2. 【核心動作】：從 Map 裡找出「這個人」的籃子 (這是老師強調的指向問題)
//	                List<AnswerVo> currentUsersBasket = answerVomap.get(email);
//	                
//	                // 3. 把打包好的答案丟進籃子裡
//	                currentUsersBasket.add(answerVo);
//	                
//	                // (註：因為是引用，籃子內容更新了，userVoList 裡的資料也會跟著變)
//	                break; // 找到對應題目後就跳出內層迴圈，節省時間
//	            }
//	        }
//	    }
//
//	    // 最後回傳組裝完成的結果
//	    return new GetFeedbackUserRes(ReplyMessage.SUCCESS.getCode(), 
//	                                  ReplyMessage.SUCCESS.getMessage(), 
//	                                  userVoList);
//	}

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
