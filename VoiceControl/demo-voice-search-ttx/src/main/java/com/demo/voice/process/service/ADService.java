package com.demo.voice.process.service;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.demo.voice.process.model.User;

public class ADService {
	private RestTemplate restTemplate;
	private String url;

	public String checkUserStatus(String userId) {
		ResponseEntity<String> responseAD = restTemplate.getForEntity(url + "check-user-status/" + userId,
				String.class);
		return responseAD.getBody();
	}

	public String checkUserExisted(String userId) {
		if (userId.equalsIgnoreCase("linda.lee") || userId.equalsIgnoreCase("michael.smith"))
			return "FOUND";
		return "NOT_FOUND";
	}

	public String checkRegisterdServie(String userId) {
		if (userId.equalsIgnoreCase("linda.lee"))
			return "REGISTERED";
		return "NOT_REGISTERED";
	}

	public String generateCode(String userId) {
		ResponseEntity<String> responseAD = restTemplate.getForEntity(url + "generate-code/" + userId, String.class);
		return responseAD.getBody();
	}

	public boolean veriryCode(String code, String userId) {
		// : http://localhost:8080/verify-code/{userId}
		String requestBody = "{\"body\":\"" + code + "\"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
		ResponseEntity<Boolean> response = restTemplate.exchange(url + "verify-code/{userId}", HttpMethod.PUT, entity,
				Boolean.class, userId);

		// check the response, e.g. Location header, Status, and body
		response.getHeaders().getLocation();
		response.getStatusCode();
		return response.getBody();
	}

	public boolean unlockUser(String userId) {
		// : http://localhost:8080/verify-code/{userId}
		String requestBody = "{\"body\":\"" + userId + "\"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
		ResponseEntity<Boolean> response = restTemplate.exchange(url + "unlock-user", HttpMethod.POST, entity,
				Boolean.class);

		// check the response, e.g. Location header, Status, and body
		response.getHeaders().getLocation();
		response.getStatusCode();
		return response.getBody();
	}

	public boolean resetPassword(String userId) {
		// : http://localhost:8080/verify-code/{userId}
		String requestBody = "{\"body\":\"" + userId + "\"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
		ResponseEntity<Boolean> response = restTemplate.exchange(url + "reset-password-user", HttpMethod.POST, entity,
				Boolean.class);

		// check the response, e.g. Location header, Status, and body
		response.getHeaders().getLocation();
		response.getStatusCode();
		return response.getBody();
	}

	public List<String> getAllUserList() {
		return null;
	}

	public User getUser(String userId) {
		ResponseEntity<User> responseAD = restTemplate.getForEntity(url + "users/" + userId, User.class);
		return responseAD.getBody();
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
