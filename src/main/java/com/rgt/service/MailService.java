package com.rgt.service;

import com.rgt.response.ResponseObject;

public interface MailService {

	ResponseObject sendDataByEmail(String senders);

	ResponseObject sendDataByEmailWithBody(String senders);

	void sendEmail(String toMail, String subject, String messageBody);

}
