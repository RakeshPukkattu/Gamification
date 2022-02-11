package com.zisbv.gamification.config;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zisbv.gamification.models.GamificationMailContents;

@Component
@Scope("singleton")
public class AsynchronousMailSender {

	private static final Logger logger = LoggerFactory.getLogger(AsynchronousMailSender.class);

	@Autowired
	private JavaMailSender mailSender;

	@Async("asynchThreadPoolTaskExecutor")
	public void sendMail(GamificationMailContents mail)
			throws InterruptedException, MessagingException, UnsupportedEncodingException {

		logger.info("async job started: " + new Date() + " threadId:" + Thread.currentThread().getId());

		String recipientEmail = mail.getRecipientEmail();
		String subject = mail.getSubject();
		String content = mail.getContent();

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("support@zispl.com", "Gamification Support");
		helper.setTo(recipientEmail);
		helper.setSubject(subject);
		helper.setText(content, true);

		mailSender.send(message);

		TimeUnit.SECONDS.sleep(3);
		logger.info("async job finished: " + new Date() + " threadId:" + Thread.currentThread().getId());
	}

}
