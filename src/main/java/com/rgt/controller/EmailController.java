package com.rgt.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rgt.response.ResponseObject;
import com.rgt.service.MailService;
import com.rgt.utils.URLConstant;

@RestController
public class EmailController {
	
	@Autowired
	private MailService mailService;
	
	
	
	private static Logger logger = LogManager.getLogger(EmailController.class);
	
	@GetMapping(value = URLConstant.SendDataViaMail)
	public ResponseObject sendBaseReport(@RequestParam String senders) {
		ResponseObject response = mailService.sendDataByEmailWithBody(senders);    //mailService.sendDataByEmail(senders);
		if (response == null || response.equals(""))
			response.setErrorMessage("Error In Reporting");  
		logger.info("sendBaseReport() - " + response);
		return response;
	}

}
