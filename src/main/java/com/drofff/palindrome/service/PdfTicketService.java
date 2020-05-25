package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Ticket;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.TicketRepository;
import com.drofff.palindrome.type.PaymentHistory;
import com.drofff.palindrome.type.TicketFile;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.drofff.palindrome.constants.FileConstants.PDF_EXTENSION;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.FileUtils.*;
import static com.drofff.palindrome.utils.FormattingUtils.appendUrlPathSegment;
import static com.drofff.palindrome.utils.FormattingUtils.concatPathSegments;
import static com.drofff.palindrome.utils.StringUtils.joinWithSpace;
import static com.drofff.palindrome.utils.TicketUtils.*;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNullEntityHasId;
import static com.itextpdf.layout.property.HorizontalAlignment.CENTER;
import static java.nio.file.Paths.get;
import static org.springframework.http.MediaType.APPLICATION_PDF;

@Service
public class PdfTicketService implements TicketService {

	private final TicketRepository ticketRepository;
	private final PdfService pdfService;
	private final ViolationService violationService;
	private final DriverService driverService;
	private final FileService fileService;

	@Value("${application.tmp.storage}")
	private String tmpStoragePath;

	@Value("${application.logo}")
	private String logoPath;

	@Autowired
	public PdfTicketService(TicketRepository ticketRepository, PdfService pdfService,
							ViolationService violationService, DriverService driverService,
							FileService fileService) {
		this.ticketRepository = ticketRepository;
		this.pdfService = pdfService;
		this.violationService = violationService;
		this.driverService = driverService;
		this.fileService = fileService;
	}

	@Override
	public void saveTicketForPayment(PaymentHistory paymentHistory) {
		Violation violation = violationService.getViolationById(paymentHistory.getViolationId());
		validateViolationHasNoTicket(violation);
		Driver payer = driverService.getDriverByUserId(violation.getViolatorId());
		String ticketPath = generatePdfTicket(paymentHistory, payer);
		Ticket ticket = new Ticket(violation.getId(), ticketPath);
		ticketRepository.save(ticket);
	}

	private void validateViolationHasNoTicket(Violation violation) {
		if(existsTicketForViolation(violation)) {
			throw new ValidationException("Ticket has been already generated for this violation");
		}
	}

	private boolean existsTicketForViolation(Violation violation) {
		return ticketRepository.findByViolationId(violation.getId()).isPresent();
	}

	private String generatePdfTicket(PaymentHistory paymentHistory, Driver payer) {
		createTmpStorageDirIfNotExists();
		String filename = generateTicketFilename(paymentHistory);
		String tmpFilePath = toTmpFilePath(filename);
		Document document = pdfService.newPdfDocument(tmpFilePath);
		addLogoToDocument(document);
		addPaymentTableToDocument(paymentHistory, payer, document);
		document.close();
		moveFileFromTmpStorage(filename);
		return filename;
	}

	private void createTmpStorageDirIfNotExists() {
		File tmpStorage = new File(tmpStoragePath);
		if(notExists(tmpStorage)) {
			Path tmpStorageDirPath = get(tmpStoragePath);
			createDirectoryAtPath(tmpStorageDirPath);
		}
	}

	private boolean notExists(File file) {
		return !file.exists();
	}

	private String generateTicketFilename(PaymentHistory paymentHistory) {
		return paymentHistory.getChargeId() + PDF_EXTENSION;
	}

	private void addLogoToDocument(Document document) {
		Image logo = getPalindromeLogo();
		document.add(logo);
	}

	private Image getPalindromeLogo() {
		try {
			String absoluteLogoPath = relativeToAbsolutePath(logoPath);
			ImageData imageData = ImageDataFactory.create(absoluteLogoPath);
			return new Image(imageData);
		} catch(MalformedURLException e) {
			throw new PalindromeException(e.getMessage());
		}
	}

	private void addPaymentTableToDocument(PaymentHistory paymentHistory, Driver payer, Document document) {
		Table table = tableOfPaymentAndPayer(paymentHistory, payer);
		table.setMarginTop(60f);
		table.setHorizontalAlignment(CENTER);
		document.add(table);
	}

	private Table tableOfPaymentAndPayer(PaymentHistory paymentHistory, Driver payer) {
		Map<String, String> tableValues = new HashMap<>();
		tableValues.put(getChargeIdTitle(), paymentHistory.getChargeId());
		tableValues.put(getViolationIdTitle(), paymentHistory.getViolationId());
		tableValues.put(getSumTitle(), getPaymentSumStr(paymentHistory));
		tableValues.put(getPayerTitle(), getPayerFullName(payer));
		return pdfService.tableFromMap(tableValues);
	}

	private String getPaymentSumStr(PaymentHistory paymentHistory) {
		long amount = toHumanReadableAmount(paymentHistory.getAmount());
		return joinWithSpace(amount, paymentHistory.getCurrency());
	}

	private long toHumanReadableAmount(long amount) {
		return amount / 100;
	}

	private String getPayerFullName(Driver payer) {
		return joinWithSpace(payer.getFirstName(), payer.getLastName());
	}

	private void moveFileFromTmpStorage(String filename) {
		String tmpFilePathStr = toTmpFilePath(filename);
		Path tmpFilePath = get(tmpFilePathStr);
		byte[] content = readAndDeleteFileAtPath(tmpFilePath);
		saveAtPermanentStorage(filename, content);
	}

	private String toTmpFilePath(String filename) {
		String tmpStorageAbsolutePath = relativeToAbsolutePath(tmpStoragePath);
		return appendUrlPathSegment(tmpStorageAbsolutePath, filename);
	}

	private void saveAtPermanentStorage(String filename, byte[] content) {
		String permanentFilename = toPermanentFilename(filename);
		fileService.saveFile(permanentFilename, content);
	}

	@Override
	public TicketFile getPayedViolationTicket(Violation violation) {
		validateNotNullEntityHasId(violation);
		return getTicketFileByViolationId(violation.getId());
	}

	private TicketFile getTicketFileByViolationId(String id) {
		Ticket ticket = ticketRepository.findByViolationId(id)
				.orElseThrow(() -> new ValidationException("Ticket for the violation is not present"));
		String filename = ticket.getPath();
		byte[] ticketContent = getFromPermanentStorage(filename);
		return toTicketFile(filename, ticketContent);
	}

	private byte[] getFromPermanentStorage(String filename) {
		String permanentFilename = toPermanentFilename(filename);
		return fileService.getFileByName(permanentFilename);
	}

	private String toPermanentFilename(String filename) {
		String username = getCurrentUser().getUsername();
		return concatPathSegments(username, filename);
	}

	private TicketFile toTicketFile(String filename, byte[] content) {
		return new TicketFile.Builder()
				.name(filename)
				.contentType(APPLICATION_PDF.toString())
				.content(content)
				.build();
	}

}