package com.example.dynamicSurvey.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.dynamicSurvey.entity.User;


public interface UserDao extends JpaRepository<User, String> {

	// 新增 User
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO user (email,name,password,tel,age " + //
			"VALUES(?1,?2,?3,?4,?5)", nativeQuery = true)
	public void insert(String email, String name, String password, String tel, int age);

	// 檢查 email 是否已被註冊在資料庫中
	@Query(value = "SELECT COUNT(email) FROM user WHERE email = ?1",nativeQuery = true)
	public int getEmailCount(String userEmail);
	
	// 透過 email 撈出使用者的資訊
	@Query(value = "SELECT * FROM user WHERE email = ?1",nativeQuery = true)
	public User getByEmail(String userEmail);
}
