package com.javasampleapproach.fcm.pushnotif.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javasampleapproach.fcm.pushnotif.model.FirebaseResponse;
import com.javasampleapproach.fcm.pushnotif.service.AndroidPushNotificationsService;
import com.javasampleapproach.fcm.pushnotif.utils.FirebaseUtil;

@RestController
public class WebController {
	private static final Logger logger = LoggerFactory.getLogger(WebController.class);
	
	@Autowired
	AndroidPushNotificationsService androidPushNotificationsService;

	@RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> send() throws JSONException {

		JSONObject body = new JSONObject();
//		body.put("to", "/topics/" + TOPIC);
		body.put("to", FirebaseUtil.FIREBASE_SERVER_KEY);
		body.put("priority", "high");

		JSONObject notification = new JSONObject();
		notification.put("title", "타이틀!");
		notification.put("body", "내용!");
		
		JSONObject data = new JSONObject();
		data.put("Key-1", "JSA Data 1");
		data.put("Key-2", "JSA Data 2");

		body.put("notification", notification);
		body.put("data", data);

		HttpEntity<String> request = new HttpEntity<>(body.toString());

		CompletableFuture<FirebaseResponse> pushNotification = androidPushNotificationsService.send(request);
		CompletableFuture.allOf(pushNotification).join();

		try {
			FirebaseResponse firebaseResponse = pushNotification.get();
			if(firebaseResponse.getSuccess() == 1)
				logger.info("Push Notification Sent OK!");
			else
				logger.error("Error Sending Push Notifications : " + firebaseResponse.toString());
			
			return new ResponseEntity<>(firebaseResponse.toString(), HttpStatus.OK);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
	}
}
