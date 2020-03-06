package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.document.ViolationType;
import com.drofff.palindrome.dto.PaymentViolationDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.PaymentViolationDtoMapper;
import com.drofff.palindrome.service.MappingsResolver;
import com.drofff.palindrome.service.PaymentService;
import com.drofff.palindrome.service.TicketService;
import com.drofff.palindrome.service.ViolationService;
import com.drofff.palindrome.service.ViolationTypeService;
import com.drofff.palindrome.type.Payment;
import com.drofff.palindrome.type.PaymentHistory;

@Controller
@RequestMapping("/pay")
@PreAuthorize("hasAuthority('DRIVER')")
public class PaymentController {

	private static final String PAY_FOR_VIOLATION_VIEW = "payForViolationPage";
	private static final String PAYMENT_SUCCESS_VIEW = "paymentSuccessPage";

	private static final String VIOLATION_PARAM = "violation";
	private static final String PUBLIC_KEY_PARAM = "public_key";

	private final ViolationService violationService;
	private final PaymentService paymentService;
	private final ViolationTypeService violationTypeService;
	private final TicketService ticketService;
	private final PaymentViolationDtoMapper paymentViolationDtoMapper;
	private final MappingsResolver mappingsResolver;

	@Autowired
	public PaymentController(ViolationService violationService, PaymentService paymentService,
	                         ViolationTypeService violationTypeService, TicketService ticketService,
	                         PaymentViolationDtoMapper paymentViolationDtoMapper, MappingsResolver mappingsResolver) {
		this.violationService = violationService;
		this.paymentService = paymentService;
		this.violationTypeService = violationTypeService;
		this.ticketService = ticketService;
		this.paymentViolationDtoMapper = paymentViolationDtoMapper;
		this.mappingsResolver = mappingsResolver;
	}

	@GetMapping("/{id}")
	public String getPayForViolationPage(@PathVariable String id, Model model) {
		Violation violation = getCurrentUserViolationWithId(id);
		if(isNotPaid(violation)) {
			model.addAttribute(PUBLIC_KEY_PARAM, paymentService.getPublicKey());
			model.addAttribute(VIOLATION_PARAM, toPaymentViolationDto(violation));
			return PAY_FOR_VIOLATION_VIEW;
		}
		return redirectToWithMessage(HOME_ENDPOINT, "Violation has been already paid! Thank you!");
	}

	private boolean isNotPaid(Violation violation) {
		return !violation.isPaid();
	}

	@PostMapping("/{id}")
	public String payForViolationWithId(@PathVariable String id, String stripeToken, Model model) {
		Violation violation = getCurrentUserViolationWithId(id);
		ViolationType violationType = violationTypeService.getViolationTypeById(violation.getViolationTypeId());
		Payment payment = paymentForViolationTypeWithToken(violationType, stripeToken);
		try {
			PaymentHistory paymentHistory = paymentService.executePayment(payment);
			paymentHistory.setViolationId(violation.getId());
			violationService.markAsPaid(violation);
			ticketService.saveTicketForPayment(paymentHistory);
			return PAYMENT_SUCCESS_VIEW;
		} catch(ValidationException e) {
			model.addAttribute(VIOLATION_PARAM, toPaymentViolationDto(violation));
			model.addAttribute(PUBLIC_KEY_PARAM, paymentService.getPublicKey());
			putValidationExceptionIntoModel(e, model);
			return PAY_FOR_VIOLATION_VIEW;
		}
	}

	private Violation getCurrentUserViolationWithId(String id) {
		User currentUser = getCurrentUser();
		return violationService.getViolationOfUserById(currentUser, id);
	}

	private Payment paymentForViolationTypeWithToken(ViolationType violationType, String token) {
		return new Payment.Builder()
				.forViolationType(violationType)
				.withPaymentToken(token)
				.build();
	}

	private PaymentViolationDto toPaymentViolationDto(Violation violation) {
		PaymentViolationDto violationDto = paymentViolationDtoMapper.toDto(violation);
		return mappingsResolver.resolveMappings(violation, violationDto);
	}

}
