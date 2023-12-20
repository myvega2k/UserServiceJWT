package com.example.userservice.vo;

import java.util.List;

import lombok.Data;

@Data
public class ResponseUser {
	private String email;
	private String name;
	private String userId;
	
	private List<ResponseOrder> orders;
}
