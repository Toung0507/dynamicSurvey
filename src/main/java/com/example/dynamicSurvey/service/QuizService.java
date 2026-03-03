package com.example.dynamicSurvey.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.dynamicSurvey.constants.ReplyMessage;
import com.example.dynamicSurvey.constants.Type;
import com.example.dynamicSurvey.dao.QuizDao;
import com.example.dynamicSurvey.dao.QuestionDao;
import com.example.dynamicSurvey.entity.Question;
import com.example.dynamicSurvey.entity.Quiz;
import com.example.dynamicSurvey.req.CreateReq;
import com.example.dynamicSurvey.req.DeleteReq;
import com.example.dynamicSurvey.req.UpdateReq;
import com.example.dynamicSurvey.res.BasicRes;
import com.example.dynamicSurvey.res.CreateRes;
import com.example.dynamicSurvey.res.GetQuestionRes;
import com.example.dynamicSurvey.res.GetQuizRes;
import com.example.dynamicSurvey.res.UpdateRes;

import jakarta.transaction.Transactional;

@Service
public class QuizService {

	@Autowired
	private QuizDao quizDao;

	@Autowired	
	private QuestionDao questionDao;

	/**
	 * 一個方法中若有使用到多個 Dao 或是同一個 Dao <br>
	 * 有呼叫多次去對資料作變更(新增、修改、刪除)<br>
	 * 必須要用@Transactional<br>
	 * 因為這些 Dao 的操作，都屬於同一次的操作<br>
	 * 因此資料的變更要嘛全部成功<br>
	 * 不然就全部失敗，回溯到尚未變更之前 這行也可放在 Class 上面
	 */
	@Transactional(rollbackOn = Exception.class)
	public CreateRes create(CreateReq req) {
		// 先作參數檢查
		CreateRes res = checkParams(req);
		if (res != null) {
			return res;
		}

		try {
			// 新增問卷 -> 取問卷ID --> 新增問題
			quizDao.insert(req.getTitle(), req.getDescription(), req.getStartDate(), req.getEndDate(),
					req.isPublished());

			// 取的最新的 問卷 ID
			int quizId = quizDao.getQuizMaxId();

			// 新增問題進去
			for (Question question : req.getQuestionList()) {
				questionDao.insert(quizId, question.getQuestionId(), question.getQuestion(), question.getType(),
						question.isRequired(), question.getOptions());
			}
		} catch (Exception e) {
			throw e;
		}

		return new CreateRes(ReplyMessage.SUCCESS.getCode(), //
				ReplyMessage.SUCCESS.getMessage());
	}

	private CreateRes checkParams(CreateReq req) {

		// 檢查問卷標題
		if (!StringUtils.hasText(req.getTitle())) {
			return new CreateRes(ReplyMessage.TITLE_ERROR.getCode(), //
					ReplyMessage.TITLE_ERROR.getMessage());
		}

		// 檢查問卷描述
		if (!StringUtils.hasText(req.getDescription())) {
			return new CreateRes(ReplyMessage.DESCRIPTION_ERROR.getCode(), //
					ReplyMessage.DESCRIPTION_ERROR.getMessage());
		}


		// 1.開始時間不能比今天早 2.開始時間不能比結束時間晚
		if (req.getStartDate() == null || req.getStartDate().isBefore(LocalDate.now())
				|| req.getStartDate().isAfter(req.getEndDate())) {
			return new CreateRes(ReplyMessage.START_DATE_ERROR.getCode(), //
					ReplyMessage.START_DATE_ERROR.getMessage());
		}

		// 結束時間不能比今天早
		if (req.getEndDate() == null || req.getEndDate().isBefore(LocalDate.now())) {
			return new CreateRes(ReplyMessage.END_DATE_ERROR.getCode(), //
					ReplyMessage.END_DATE_ERROR.getMessage());
		}

		// 檢查問題列表
		for (Question item : req.getQuestionList()) {
			// 檢查問題的 ID
			if (item.getQuestionId() <= 0) {
				return new CreateRes(ReplyMessage.QUESTION_ID_ERROR.getCode(), //
						ReplyMessage.QUESTION_ID_ERROR.getMessage(), item.getQuestionId());
			}

			// 檢查問題敘述
			if (!StringUtils.hasText(item.getQuestion())) {
				return new CreateRes(ReplyMessage.QUESTION_ERROR.getCode(), //
						ReplyMessage.QUESTION_ERROR.getMessage(), item.getQuestionId());
			}

			// 檢查類型
			// 第一種方式
			/*
			 * if (!StringUtils.hasText(item.getType()) // &&
			 * (!item.getType().equalsIgnoreCase(Type.SINGLE.getType()) // ||
			 * !item.getType().equalsIgnoreCase(Type.MULTI.getType()) // ||
			 * !item.getType().equalsIgnoreCase(Type.TEXT.getType())) // )
			 */
			// 第二種方式
			if (!Type.check(item.getType())) {
				return new CreateRes(ReplyMessage.TYPE_ERROR.getCode(), //
						ReplyMessage.TYPE_ERROR.getMessage(), item.getQuestionId());
			}

			// 檢查選項
			// 判斷是否為單/複選的寫法
			/**
			 * if (item.getType().equalsIgnoreCase(Type.SINGLE.getType()) <br>
			 * || item.getType().equalsIgnoreCase(Type.MULTI.getType())) {<br>
			 * if (!StringUtils.hasText(item.getOptions())) {<br>
			 * return new CreateRes(ReplyMessage.OPTIONS_ERROR.getCode(), // <br>
			 * ReplyMessage.OPTIONS_ERROR.getMessage(),item.getIdquestionId());<br>
			 * }<br>
			 * }<br>
			 */
			// 但因為其實類型固定成三種 ( 單複選 簡答 )，所以可以改成只檢查是否不是 簡答就好
			if (!item.getType().equalsIgnoreCase(Type.TEXT.getType())) {
				if (!StringUtils.hasText(item.getOptions())) {
					return new CreateRes(ReplyMessage.OPTIONS_ERROR.getCode(), //
							ReplyMessage.OPTIONS_ERROR.getMessage(), item.getQuestionId());
				}
			}
		}

		return null;
	}

	// 取得全部的問卷資料
	public GetQuizRes getQuizList() {
		// List<Quiz> quizList = quizDao.getQuizAll();
		return new GetQuizRes(ReplyMessage.SUCCESS.getCode(), //
				ReplyMessage.SUCCESS.getMessage(), quizDao.getQuizAll());
	}

	// 取得全部的問卷相對應題目資料
	public GetQuestionRes getQuestionList(int quizID) {
		// List<Quiz> quizList = quizDao.getQuizAll();
		return new GetQuestionRes(ReplyMessage.SUCCESS.getCode(), //
				ReplyMessage.SUCCESS.getMessage(), questionDao.getByQuizId(quizID));
	}

	@Transactional(rollbackOn = Exception.class)
	public UpdateRes udpate(UpdateReq req) {
		// 更新要檢查 quizId 因為存在 DB 中的 quizId 一定是大於 0
		if (req.getQuizId() <= 0) {
			return new UpdateRes(ReplyMessage.QUIZ_ID_ERROR.getCode(), //
					ReplyMessage.QUIZ_ID_ERROR.getMessage());
		}

		// 檢查 req 中的 quizId 和每個 Qusetion 的 quizId 是否相同
		for (Question item : req.getQuestionList()) {
			if (req.getQuizId() != item.getQuizID()) {
				return new UpdateRes(ReplyMessage.QUIZ_ID_MISMATCH.getCode(), //
						ReplyMessage.QUIZ_ID_MISMATCH.getMessage());
			}
		}

		// 檢查更新的問卷是否存在：主要是取得問卷的發布狀態
		Quiz quiz = quizDao.getQuizById(req.getQuizId());
		if (quiz == null) {
			return new UpdateRes(ReplyMessage.QUIZ_NOT_FOUND.getCode(), //
					ReplyMessage.QUIZ_NOT_FOUND.getMessage());
		}

		// 檢查是否可以被修改的狀態
		// 1. 未發布
		// 2. 已發布請尚未開始
		// 不用正向的寫法，用排除的寫法
		/**
		 * if (!req.isPublished() || <br>
		 * (req.isPublished() && req.getStartDate().isAfter(LocalDate.now()))) {<br>
		 * }<br>
		 */
		// 需排除的：已發布且 進行中 或 已結束
		// 開始日期不是在今天之後 ==> 開始日期0701在今天0630或今天之前
		if (quiz.isPublished() && //
				!req.getStartDate().isAfter(LocalDate.now())) {
			return new UpdateRes(ReplyMessage.QUIZ_ID_MISMATCH.getCode(), //
					ReplyMessage.QUIZ_ID_MISMATCH.getMessage());
		}

		// 檢查其餘的參數
		CreateRes res = checkParams(req);
		if (res != null) {
			// 不能直接將 父類別 ( CreateRes ) 強制轉成子類別 ( UpdateRes )，會報錯
			// 所以不能直接寫成 return (UpdateRes) res;
			return new UpdateRes(res.getCode(), res.getMessage());
		}

		// 開始更新到資料庫
		try {
			// 1. 更新問卷
			quizDao.update(req.getQuizId(), req.getTitle(), req.getDescription(), req.getStartDate(), req.getEndDate(),
					req.isPublished());

			// 2.刪除同一個 quizId 下所有的 Qusetion
			questionDao.delete(quiz.getId());

			// 3. 新增更新後的問題
			for (Question item : req.getQuestionList()) {
				questionDao.insert(req.getQuizId(), item.getQuestionId(), item.getQuestion(), //
						item.getType(), item.isRequired(), item.getOptions());
			}

		} catch (Exception e) {
			throw e;
		}

		return null;
	}

	@Transactional(rollbackOn = Exception.class)
	public BasicRes delete(DeleteReq req) {
		// 檢查參數
		if (CollectionUtils.isEmpty(req.getQuizIdList())) {
			return new BasicRes(ReplyMessage.QUIZ_ID_ERROR.getCode(), //
					ReplyMessage.QUIZ_ID_ERROR.getMessage());
		}

		for (int id : req.getQuizIdList()) {
			if (id <= 0) {
				return new BasicRes(ReplyMessage.QUIZ_ID_ERROR.getCode(), //
						ReplyMessage.QUIZ_ID_ERROR.getMessage());
			}
		}

		try {
			quizDao.delete(req.getQuizIdList());
			questionDao.delete(req.getQuizIdList());
		} catch (Exception e) {
			throw e;
		}

		return new BasicRes(ReplyMessage.SUCCESS.getCode(), //
				ReplyMessage.SUCCESS.getMessage());
	}

	// 刪除單一筆問卷
	@Transactional(rollbackOn = Exception.class)
	public BasicRes delete(int quizId) {
		/**
		 * 兩種寫法 ； 一個分開，一個長
		 * 
		 * // List<Integer> list = List.of(quizId); // DeleteReq req = new
		 * DeleteReq(list); // BasicRes res = delete(req); // return res;
		 */

		return delete(new DeleteReq(List.of(quizId)));

		/*
		 * 分開寫 // 參數檢查 if (quizId <= 0) { return new
		 * BasicRes(ReplyMessage.QUIZ_ID_ERROR.getCode(), //
		 * ReplyMessage.QUIZ_ID_ERROR.getMessage()); }
		 * 
		 * try { quizDao.delete(quizId); questionDao.delete(quizId); } catch (Exception
		 * e) { throw e; }
		 * 
		 * return new BasicRes(ReplyMessage.SUCCESS.getCode(), //
		 * ReplyMessage.SUCCESS.getMessage());
		 */
	}

}
