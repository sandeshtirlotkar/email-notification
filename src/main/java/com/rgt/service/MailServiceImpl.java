package com.rgt.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.rgt.entity.RegisterUserEntity;
import com.rgt.repository.RegisterUserRepository;
import com.rgt.response.ResponseObject;
import com.rgt.utils.CommonUtility;
import com.rgt.utils.Constant;
import com.rgt.utils.MailClientUtils;

@Service
public class MailServiceImpl implements MailService {

	private static Logger logger = LogManager.getLogger(MailServiceImpl.class);

	@Autowired
	private RegisterUserRepository registerUserRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public ResponseObject sendDataByEmail(String senders) {

		boolean sendInEmail = true;

		ResponseObject response = new ResponseObject();

		StringBuilder htmldata = new StringBuilder();
		htmldata.append(getData("SEND.TPL.FILE"));
		CommonUtility.replace(htmldata, "$currYear", CommonUtility.getCurrentYear() + "");
		List<RegisterUserEntity> registerUserEntityList = registerUserRepository.getRegisterData();

		StringBuilder bodyPart = new StringBuilder();
		for (RegisterUserEntity registerUserEntity : registerUserEntityList) {
			String rowpart = getData("SEND.TPL.BODY");
			rowpart = rowpart
					.replace("$username",
							registerUserEntity.getUsername() == null ? "" : registerUserEntity.getUsername())
					.replace("$address", registerUserEntity.getAddress() == null ? "" : registerUserEntity.getAddress())
					.replace("$city", registerUserEntity.getCity() == null ? "" : registerUserEntity.getCity())
					.replace("$pincode", registerUserEntity.getPincode()).replace("$dob", registerUserEntity.getDob());
			bodyPart.append(rowpart);

			logger.info("Data for row part" + rowpart);
		}

		if (sendInEmail) {

			boolean resposneStatus = sendMail(htmldata.toString().replace("$tableContent", bodyPart.toString()),
					senders); // ? "Email Sent Successfully." : "Email delivery failed";

			if (resposneStatus) {

				response.setStatus(true);
				response.setSuccessMessage("Email Sent Successfully.");

			} else {
				response.setStatus(false);
				response.setErrorMessage("Email delivery failed");

			}

		} else {
			htmldata.toString().replace("$tableContent", bodyPart.toString());
		}

		return response;
	}

	private String getData(String fileNameKey) {
		String path = CommonUtility.getValueFromPropeties("TPL.FILE.LOCATION", Constant.COMMON_FILE_NAME).trim();
		path = path.concat(CommonUtility.getValueFromPropeties(fileNameKey, Constant.COMMON_FILE_NAME).trim());
		String emailBody = CommonUtility.readTPLfile(path);
		return emailBody;
	}

	private boolean sendMail(String emailBody, String senders) {
		try {
			String subject = CommonUtility.getValueFromPropeties("SEND.SUBJECT", Constant.COMMON_FILE_NAME).trim();
			String sendTo = CommonUtility.getValueFromPropeties("SEND.USERS", Constant.COMMON_FILE_NAME).trim();
			if (subject == null || subject.equals("") || sendTo == null || sendTo.equals(""))
				return false;

			String[] sendersList = senders.split(",");
			String senderEmails = "";
			for (String sender : sendersList)
				// senderEmails += sender + "@gmail.com;";
				senderEmails += sender;
			MailClientUtils.sendMailWithDefaultConf(subject, emailBody, senderEmails);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// only message send
	@Override
	public ResponseObject sendDataByEmailWithBody(String senders) {
		boolean sendInEmail = true;

		ResponseObject response = new ResponseObject();
		StringBuilder htmldata = new StringBuilder();

		if (sendInEmail) {
			htmldata.append("This is Ratna Global Technologies Test Mail, Please Ignore");

			boolean resposneStatus = sendMail(htmldata.toString(), senders); // ? "Email Sent Successfully." : "Email

			if (resposneStatus) {

				response.setStatus(true);
				response.setSuccessMessage("Email Sent Successfully.");

			} else {
				response.setStatus(false);
				response.setErrorMessage("Email delivery failed");
			}

		} else {
			htmldata.toString();
		}
		return response;
	}

	// otpsendmail
	@Override
	@Async
	public void sendEmail(String toMail, String subject, String messageBody) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("1994sandesh@gmail.com");
		simpleMailMessage.setTo(toMail);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(messageBody);
		javaMailSender.send(simpleMailMessage);
	}

}
