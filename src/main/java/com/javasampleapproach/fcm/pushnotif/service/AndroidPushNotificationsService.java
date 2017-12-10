package com.javasampleapproach.fcm.pushnotif.service;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.javasampleapproach.fcm.pushnotif.model.FirebaseResponse;
import com.javasampleapproach.fcm.pushnotif.utils.FirebaseUtil;

@Service
public class AndroidPushNotificationsService {

	
	@Async
//	public CompletableFuture<String> send(HttpEntity<String> entity) {
	public CompletableFuture<FirebaseResponse> send(HttpEntity<String> entity) {

		RestTemplate restTemplate = new RestTemplate();

		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FirebaseUtil.FIREBASE_SERVER_KEY));
		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
		restTemplate.setInterceptors(interceptors);

//		String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);
		FirebaseResponse firebaseResponse = restTemplate.postForObject(FirebaseUtil.FIREBASE_API_URL, entity, FirebaseResponse.class);

		return CompletableFuture.completedFuture(firebaseResponse);
	}
}
