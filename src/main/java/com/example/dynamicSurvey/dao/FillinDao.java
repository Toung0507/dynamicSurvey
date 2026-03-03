package com.example.dynamicSurvey.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.dynamicSurvey.entity.Fillin;
import com.example.dynamicSurvey.entity.FillinID;

public interface FillinDao extends JpaRepository<Fillin, FillinID> {

	// 新增 填答資訊
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO fillin (quiz_id,question_id,user_email,answer " + //
			"VALUES(?1,?2,?3,?4)", nativeQuery = true)
	public void insert(int quizId, int questionId, String email, String answer);

	// 取出某一種表單，所有填答人的資訊
	@Query(value = "SELECT * FROM fillin WHERE quiz_id = ?1", nativeQuery = true)
	public List<Fillin> getByQuizId(int quiz);
	
	// 取出某一位在某個表單中所填答得全部資料
	@Query(value = "SELECT * FROM fillin WHERE quiz_id = ?1 AND email = ?2", nativeQuery = true)
	public List<Fillin> getByQuizIdAndEmail(int quiz,String email);

}
