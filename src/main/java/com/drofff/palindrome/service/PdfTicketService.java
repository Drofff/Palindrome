package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.FormattingUtils.appendUrlPathSegment;
import static com.drofff.palindrome.utils.FormattingUtils.concatPathSegments;
import static com.drofff.palindrome.utils.ResourceUtils.getUrlOfClasspathResource;
import static com.drofff.palindrome.utils.ResourceUtils.loadClasspathResource;
import static com.drofff.palindrome.utils.StringUtils.joinWithSpace;
import static com.drofff.palindrome.utils.TicketUtils.getChargeIdTitle;
import static com.drofff.palindrome.utils.TicketUtils.getPayerTitle;
import static com.drofff.palindrome.utils.TicketUtils.getSumTitle;
import static com.drofff.palindrome.utils.TicketUtils.getViolationIdTitle;
import static com.drofff.palindrome.utils.ValidationUtils.validateEntityHasId;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;
import static com.itextpdf.kernel.font.PdfFontFactory.createFont;
import static com.itextpdf.kernel.geom.PageSize.A4;
import static com.itextpdf.layout.property.HorizontalAlignment.CENTER;
import static org.springframework.http.MediaType.APPLICATION_PDF;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

@Service
public class PdfTicketService implements TicketService {

	private static final String PDF_EXTENSION = ".pdf";

	private static final PageSize TICKET_PAGE_SIZE = A4;

	private static final int PAYMENT_TABLE_COLUMNS_COUNT = 2;
	private static final float PAYMENT_TABLE_TOP_MARGIN = 60f;
	private static final String PAYMENT_TABLE_FONT_ENCODING = "Identity-H";
	private static final boolean PAYMENT_TABLE_FONT_EMBEDDED = true;

	private final TicketRepository ticketRepository;
	private final ViolationService violationService;
	private final DriverService driverService;
	private final FileService fileService;

	@Value("${classpath.resource.tmp.storage}")
	private String tmpStorageDir;

	@Value("${classpath.resource.logo}")
	private String logoUri;

	@Value("${classpath.resource.font}")
	private String fontUri;

	@Autowired
	public PdfTicketService(TicketRepository ticketRepository, ViolationService violationService,
	                        DriverService driverService, FileService fileService) {
		this.ticketRepository = ticketRepository;
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
		String filename = generateTicketFilename(paymentHistory);
		String tmpFilePath = toTmpFilePath(filename);
		Document document = createPdfDocumentWithName(tmpFilePath);
		addLogoToDocument(document);
		addPaymentTableToDocument(paymentHistory, payer, document);
		document.close();
		moveFileFromTmpStorage(filename);
		return filename;
	}

	private String generateTicketFilename(PaymentHistory paymentHistory) {
		return paymentHistory.getChargeId() + PDF_EXTENSION;
	}

	private Document createPdfDocumentWithName(String name) {
		try {
			return createPdfDocument(name);
		} catch(FileNotFoundException e) {
			throw new PalindromeException(e.getMessage());
		}
	}

	private Document createPdfDocument(String name) throws FileNotFoundException {
		PdfWriter pdfWriter = new PdfWriter(name);
		PdfDocument pdfDocument = new PdfDocument(pdfWriter);
		return new Document(pdfDocument, TICKET_PAGE_SIZE);
	}

	private void addLogoToDocument(Document document) {
		Image logo = getPalindromeLogo();
		document.add(logo);
	}

	private Image getPalindromeLogo() {
		byte[] logo = loadClasspathResource(logoUri);
		ImageData imageData = ImageDataFactory.create(logo);
		return new Image(imageData);
	}

	private void addPaymentTableToDocument(PaymentHistory paymentHistory, Driver payer, Document document) {
		Table table = tableOfPaymentAndPayer(paymentHistory, payer);
		table.setMarginTop(PAYMENT_TABLE_TOP_MARGIN);
		table.setHorizontalAlignment(CENTER);
		document.add(table);
	}

	private Table tableOfPaymentAndPayer(PaymentHistory paymentHistory, Driver payer) {
		Table table = new Table(PAYMENT_TABLE_COLUMNS_COUNT);
		addRowToTable(getChargeIdTitle(), paymentHistory.getChargeId(), table);
		addRowToTable(getViolationIdTitle(), paymentHistory.getViolationId(), table);
		addRowToTable(getSumTitle(), getPaymentSumStr(paymentHistory), table);
		addRowToTable(getPayerTitle(), getPayerFullName(payer), table);
		return table;
	}

	private void addRowToTable(String title, String value, Table table) {
		addCellToTable(title, table);
		addCellToTable(value, table);
	}

	private void addCellToTable(String cellValue, Table table) {
		Paragraph paragraph = paragraphOfText(cellValue);
		table.addCell(paragraph);
	}

	private Paragraph paragraphOfText(String text) {
		Paragraph paragraph = new Paragraph(text);
		PdfFont tableFont = getPaymentTableFont();
		paragraph.setFont(tableFont);
		return paragraph;
	}

	private PdfFont getPaymentTableFont() {
		try {
			byte[] font = loadClasspathResource(fontUri);
			return createFont(font, PAYMENT_TABLE_FONT_ENCODING, PAYMENT_TABLE_FONT_EMBEDDED);
		} catch(IOException e) {
			throw new PalindromeException("Error while loading font");
		}
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
		Path tmpFilePath = Paths.get(tmpFilePathStr);
		byte[] content = readAndDeleteFileByPath(tmpFilePath);
		saveAtPermanentStorage(filename, content);
	}

	private byte[] readAndDeleteFileByPath(Path path) {
		try {
			byte[] content = Files.readAllBytes(path);
			Files.delete(path);
			return content;
		} catch(IOException e) {
			throw new PalindromeException("Error while managing file " + path.toString());
		}
	}

	private String toTmpFilePath(String filename) {
		String tmpStorageUrl = getUrlOfClasspathResource(tmpStorageDir);
		return appendUrlPathSegment(tmpStorageUrl, filename);
	}

	private void saveAtPermanentStorage(String filename, byte[] content) {
		String permanentFilename = toPermanentFilename(filename);
		fileService.saveFile(permanentFilename, content);
	}

	@Override
	public TicketFile getPayedViolationTicket(Violation violation) {
		validateNotNull(violation);
		validateEntityHasId(violation);
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
