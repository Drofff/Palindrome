package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.document.ViolationType;
import com.drofff.palindrome.dto.PaymentViolationDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.PaymentViolationDtoMapper;
import com.drofff.palindrome.service.*;
import com.drofff.palindrome.type.Payment;
import com.drofff.palindrome.type.PaymentHistory;
import com.drofff.palindrome.type.TicketFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.drofff.palindrome.constants.EndpointConstants.ERROR_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.ERROR_MESSAGE_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.VIOLATION_PARAM;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.FormattingUtils.uriWithQueryParams;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Controller
@RequestMapping("/pay")
@PreAuthorize("hasAuthority('DRIVER')")
public class PaymentController {

	private static final String PAY_FOR_VIOLATION_VIEW = "payForViolationPage";
	private static final String PAYMENT_SUCCESS_VIEW = "paymentSuccessPage";

	private static final String PUBLIC_KEY_PARAM = "public_key";

	private static final String ATTACHMENT_CONTENT_DISPOSITION_PREFIX = "attachment; filename=";

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
		ViolationType violationType = violationTypeService.getById(violation.getViolationTypeId());
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

	@GetMapping("/ticket/{id}")
	@ResponseBody
	public ResponseEntity<byte[]> getPaymentTicket(@PathVariable String id, HttpServletResponse response) throws IOException {
		try {
			Violation violation = violationService.getViolationById(id);
			TicketFile ticket = ticketService.getPayedViolationTicket(violation);
			return ticketAsResponse(ticket);
		} catch(ValidationException e) {
			redirectToErrorPageWithMessage(response, e.getMessage());
			return ResponseEntity.badRequest().body(new byte[] {});
		}
	}

	private ResponseEntity<byte[]> ticketAsResponse(TicketFile ticketFile) {
		HttpHeaders httpHeaders = headersForTicketFile(ticketFile);
		return responseEntityForFileWithHeaders(ticketFile.getContent(), httpHeaders);
	}

	private HttpHeaders headersForTicketFile(TicketFile ticketFile) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(CONTENT_TYPE, ticketFile.getContentType());
		String contentDisposition = ATTACHMENT_CONTENT_DISPOSITION_PREFIX + ticketFile.getName();
		httpHeaders.add(CONTENT_DISPOSITION, contentDisposition);
		return httpHeaders;
	}

	private ResponseEntity<byte[]> responseEntityForFileWithHeaders(byte[] fileContent, HttpHeaders httpHeaders) {
		return ResponseEntity.ok()
				.headers(httpHeaders)
				.contentLength(fileContent.length)
				.body(fileContent);
	}

	private void redirectToErrorPageWithMessage(HttpServletResponse response, String message) throws IOException {
		Pair<String, String> errorMessageParam = Pair.of(ERROR_MESSAGE_PARAM, message);
		String errorPageUri = uriWithQueryParams(ERROR_ENDPOINT, Collections.singletonList(errorMessageParam));
		response.sendRedirect(errorPageUri);
	}

}
