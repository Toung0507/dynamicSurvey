package com.example.dynamicSurvey.constants;

import org.springframework.util.StringUtils;

public enum Type {
	SINGLE("single-choice"), // 單選
	MULTI("multiple-choice"), // 複選
	TEXT("text"); // 文字

	private String type;

	private Type(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static boolean check(String input) {
		if (!StringUtils.hasText(input)) {
			return false;
		}
		// 三個分開的
		/**
		 * if (input.equalsIgnoreCase(SINGLE.getType())) { return true; } <br>
		 * if (input.equalsIgnoreCase(MULTI.getType())) { return true; } <br>
		 * if (input.equalsIgnoreCase(TEXT.getType())) { return true; }
		 */

		// 三個合併
		/*
		 * if (input.equalsIgnoreCase(SINGLE.getType()) ||
		 * input.equalsIgnoreCase(MULTI.getType()) ||
		 * input.equalsIgnoreCase(TEXT.getType())) { return true; }
		 */

		// 透過 enum 的自有方法去判斷 ==>
		// values 是一個陣列代表此 enum 中的所有列舉(就是指SINGLE、MULTI、TEXT)
		for (Type type : values()) {
			if (input.contentEquals(type.getType())) {
				return true;
			}
		}

		return false;
	}

	public static boolean isChoiceType(String input) {
		// 三個合併
		/*
		 * if (input.equalsIgnoreCase(SINGLE.getType()) ||
		 * input.equalsIgnoreCase(MULTI.getType()) 
		 * ) { return true; }
		 */

		// 有產生就會是 true 了
		return input.equalsIgnoreCase(SINGLE.getType()) ||
				 input.equalsIgnoreCase(MULTI.getType());
	}
}
