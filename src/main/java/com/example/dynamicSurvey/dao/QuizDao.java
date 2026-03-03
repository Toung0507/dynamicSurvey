package com.example.dynamicSurvey.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dynamicSurvey.entity.Quiz;

import jakarta.transaction.Transactional;

@Repository
public interface QuizDao extends JpaRepository<Quiz, Integer> {

	// 新增問卷
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO quiz (title,description,start_date,end_date,is_published) "//
			+ "	VAlUES (?1,?2,?3,?4,?5)", nativeQuery = true)
	public void insert(String title, String description, LocalDate startDate, LocalDate endDate, boolean published);

	// 取得最大的問卷 ID
	@Query(value = "SELECT MAX(id) FROM quiz", nativeQuery = true)
	public int getQuizMaxId();

	// 取得全部的問卷
	@Query(value = "SELECT * FROM quiz", nativeQuery = true)
	public List<Quiz> getQuizAll();

	// 更新問卷
	@Modifying
	@Transactional
	@Query(value = "UPDATE quiz SET title = ?2 ,description = ?3 ,start_date = ?4 ,end_date = ?5, is_published = ?6"
			+ " WHERE id = ?1", nativeQuery = true)
	public void update(int id, String title, String description, LocalDate startDate, LocalDate endDate,
			boolean published);

	// 取得某筆問卷的資料
	// 取得全部的問卷
	@Query(value = "SELECT * FROM quiz WHERE id = ?1", nativeQuery = true)
	public Quiz getQuizById(int id);
	

	// 刪除問券 IN () jpa 這邊沒有 () 也可以，但標準 SQL 或者是 myBtais 就需要
	/**
	 * 如果 idList = [1,2,3]　<br>
	 * SQL 語法會是：DELETE FROM quiz WHERE id IN (1,2,3)
	 */
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM quiz WHERE id IN (?1)", nativeQuery = true)
	public void delete(List<Integer> idList);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM quiz WHERE id = ?1", nativeQuery = true)
	public void delete(int id);
}
