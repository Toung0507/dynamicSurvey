package com.example.dynamicSurvey.req;

import java.time.LocalDate;
import java.util.List;

import com.example.dynamicSurvey.constants.VaildationMsg;
import com.example.dynamicSurvey.entity.Question;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CreateReq {

	/*
	 * @NotBlank:限制屬性值不能是 1. 空字串 2. 全空白字串 3. null message
	 * 是指當屬性值違反限制時得到的訊息，等號後面的值必須是常數(final)
	 */

	@NotBlank(message = VaildationMsg.TITLE_ERROR_MSG)
	private String title;

	@NotBlank(message = VaildationMsg.DESCRIPTION_ERROR_MSG)
	private String description;

	@NotNull(message = VaildationMsg.START_DATE_ERROR_MSG)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	@NotNull(message = VaildationMsg.END_DATE_ERROR_MSG)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	private boolean published;
	
	/** 嵌套驗證: 驗證自定義物件(class)中的屬性
	 * @Valid: 為了讓嵌套驗證中的屬性限制生效，就是 Question 中的屬性限制
	 */
	@Valid
	@NotEmpty(message = VaildationMsg.QUESTION_LIST_IS_EMPTY)
	private List<Question> questionList;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}

}