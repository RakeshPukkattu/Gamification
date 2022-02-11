package com.zisbv.gamification.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.zisbv.gamification.config.AsynchronousMailSender;
import com.zisbv.gamification.config.Utility;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.exceptions.UserNotFoundException;
import com.zisbv.gamification.models.GamificationMailContents;
import com.zisbv.gamification.service.UserManagementService;

import net.bytebuddy.utility.RandomString;

@Controller
public class PasswordResetController {

	@Autowired
	private AsynchronousMailSender asynchronousWorker;

	@Autowired
	private UserManagementService userManagementService;

	@GetMapping("/forgot_password")
	public String showForgotPasswordForm(Model model) {
		model.addAttribute("pageTitle", "Forgot password");
		return "forgot_password_form";
	}

	@PostMapping("/forgot_password")
	public String processForgotPassword(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		String token = RandomString.make(30);

		try {
			userManagementService.updateResetPasswordToken(token, email);
			String resetPasswordLink = Utility.getSiteUrl(request) + "/reset_password?token=" + token;

			String subject = "Here's the link to reset your password";
			String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
					+ "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + resetPasswordLink
					+ "\">Change my password</a></p>" + "<br>"
					+ "<p>Ignore this email if you do remember your password, "
					+ "or you have not made the request.</p>";
			GamificationMailContents mail = new GamificationMailContents(email, subject, content);

			asynchronousWorker.sendMail(mail);

			model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

		} catch (UserNotFoundException ex) {
			model.addAttribute("error", ex.getMessage());
		} catch (UnsupportedEncodingException | MessagingException | InterruptedException e) {
			model.addAttribute("error", "Error while sending email");
		}

		return "forgot_password_form";
	}

	@GetMapping("/reset_password")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
		try {
			UserData user = userManagementService.getByResetPasswordToken(token);
			model.addAttribute("token", token);

			if (user == null) {
				model.addAttribute("message", "Invalid Token");
				return "home";
			}
		} catch (Exception e) {
			return "home";
		}

		return "set_password_form";
	}

	@PostMapping("/reset_password")
	public ResponseEntity<Void> processResetPassword(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");

		UserData customer = userManagementService.getByResetPasswordToken(token);
		model.addAttribute("title", "Reset your password");

		if (customer == null) {
			model.addAttribute("message", "Invalid Token");
			return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/")).build();
		} else {
			userManagementService.updatePassword(customer, password);

			model.addAttribute("message", "You have successfully changed your password.");
		}

		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/")).build();
	}

}
