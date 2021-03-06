package org.codenergic.theskeleton.registration.changepass;

import org.codenergic.theskeleton.registration.*;
import org.codenergic.theskeleton.tokenstore.TokenStoreEntity;
import org.codenergic.theskeleton.tokenstore.TokenStoreService;
import org.codenergic.theskeleton.tokenstore.TokenStoreType;
import org.codenergic.theskeleton.user.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping("changepass")
public class ChangePasswordController {

	private static final String CHANGEPASS = "changepass";
	private static final String CHANGEPASS_CONFIRMATION = "changepass_confirmation";
	private static final String CHANGEPASS_UPDATE = "changepass_update";

	private RegistrationService registrationService;
	private TokenStoreService tokenStoreService;

	public ChangePasswordController(RegistrationService registrationService, TokenStoreService tokenStoreService) {
		this.registrationService = registrationService;
		this.tokenStoreService = tokenStoreService;
	}

	@GetMapping
	public String changepassView(ChangePasswordForm changePasswordForm) {
		return CHANGEPASS;
	}

	@PostMapping
	public String changepass(Model model, @Valid ChangePasswordForm changePasswordForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return changepassView(changePasswordForm);

		UserEntity user = registrationService.findUserByEmail(changePasswordForm.getEmail());
		if (user == null) {
			bindingResult.rejectValue("email", "error.changePasswordForm", "Can't find that email, sorry.");
			return changepassView(changePasswordForm);
		} else {
			tokenStoreService.sendTokenNotification(TokenStoreType.CHANGE_PASSWORD, user);
		}
		model.addAttribute("message", "changepass");
		return CHANGEPASS_CONFIRMATION;
	}

	@GetMapping(path = "/update")
	public String updateView(@RequestParam(name = "rt") String resetToken, UpdatePasswordForm updatePasswordForm) {
		TokenStoreEntity token = tokenStoreService.findByTokenAndType(resetToken, TokenStoreType.CHANGE_PASSWORD);
		if (token == null) {
			return "redirect:/changepass";
		}
		updatePasswordForm.setToken(token.getToken());
		return CHANGEPASS_UPDATE;
	}

	@PostMapping(path = "/update")
	public String update(Model model, @Valid UpdatePasswordForm updatePasswordForm, BindingResult bindingResult,
						 @RequestParam(name = "rt") String resetToken) {
		if (bindingResult.hasErrors())
			return updateView(resetToken, updatePasswordForm);
		try {
			registrationService.changePassword(resetToken, updatePasswordForm.getPassword());
		} catch (RegistrationException e) {
			model.addAttribute("message", "error");
			model.addAttribute("error", e.getMessage());
			return CHANGEPASS_CONFIRMATION;
		}
		model.addAttribute("message", "update");
		return CHANGEPASS_CONFIRMATION;
	}
}
