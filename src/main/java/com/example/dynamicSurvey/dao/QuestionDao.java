package com.example.dynamicSurvey.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dynamicSurvey.entity.Question;
import com.example.dynamicSurvey.entity.QuestionID;

import jakarta.transaction.Transactional;

@Repository
public interface QuestionDao extends JpaRepository<Question, QuestionID> {
	// 新增問卷題目
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO question (quiz_id,question_id,question,type,is_required,options) "//
			+ "	VAlUES (?1,?2,?3,?4,?5,?6)", nativeQuery = true)
	public void insert(int quizID, int questionId, String question, String type, boolean required, String options);

	// 取的相對應的題目
	@Query(value = "SELECT * FROM question WHERE quiz_id = ?1", nativeQuery = true)
	public List<Question> getByQuizId(int quizId);

	// 刪除問卷相對應的全部題目
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM question WHERE quiz_id = ?1", nativeQuery = true)
	public void delete(int quizId);
	
	// 刪除多張問卷的相對應的全部題目
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM question WHERE quiz_id IN (?1)", nativeQuery = true)
	public void delete(List<Integer> idList);
}
