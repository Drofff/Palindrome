package com.drofff.palindrome.service;

import com.drofff.palindrome.document.*;
import com.drofff.palindrome.type.ExportedViolation;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static com.drofff.palindrome.constants.FileConstants.PDF_EXTENSION;
import static com.drofff.palindrome.utils.DateUtils.dateTimeToStr;
import static com.drofff.palindrome.utils.DateUtils.dateToStr;
import static com.drofff.palindrome.utils.FileUtils.*;
import static com.drofff.palindrome.utils.FormattingUtils.appendUrlPathSegment;
import static com.drofff.palindrome.utils.FormattingUtils.putParamsIntoText;
import static com.drofff.palindrome.utils.StringUtils.joinWithSpace;
import static com.drofff.palindrome.utils.StringUtils.randomString;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;
import static com.drofff.palindrome.utils.ViolationUtils.getViolationExportBody;
import static com.drofff.palindrome.utils.ViolationUtils.getViolationExportTitle;
import static com.itextpdf.layout.property.HorizontalAlignment.CENTER;
import static com.itextpdf.layout.property.HorizontalAlignment.LEFT;
import static java.time.LocalDate.now;
import static org.springframework.http.MediaType.APPLICATION_PDF;

@Service
public class PdfViolationExportService implements ViolationExportService {

    private final ViolationTypeService violationTypeService;
    private final PoliceService policeService;
    private final DriverService driverService;
    private final CarService carService;
    private final PdfService pdfService;

    @Value("${application.tmp.storage}")
    private String tmpDirPath;

    @Autowired
    public PdfViolationExportService(ViolationTypeService violationTypeService, PoliceService policeService,
                                     DriverService driverService, CarService carService, PdfService pdfService) {
        this.violationTypeService = violationTypeService;
        this.policeService = policeService;
        this.driverService = driverService;
        this.carService = carService;
        this.pdfService = pdfService;
    }

    @Override
    public ExportedViolation exportViolation(Violation violation) {
        validateNotNull(violation, "Could not export an empty violation");
        String documentTitle = getDocumentTitleForViolation(violation);
        String documentBody = getDocumentBodyForViolation(violation);
        return composeViolationDocumentPdf(documentTitle, documentBody);
    }

    private String getDocumentTitleForViolation(Violation violation) {
        String title = getViolationExportTitle();
        return putParamsIntoText(title, "violationId", violation.getId());
    }

    private String getDocumentBodyForViolation(Violation violation) {
        String body = getViolationExportBody();
        String[] params = {
            "violationType", getViolationTypeName(violation),
            "location", violation.getLocation(),
            "dateTime", getViolationDateTimeStr(violation),
            "policeName", getOfficerFullName(violation),
            "violatorName", getViolatorFullName(violation),
            "carNumber", getCarNumber(violation)
        };
        return putParamsIntoText(body, params);
    }

    private String getViolationTypeName(Violation violation) {
        ViolationType violationType = violationTypeService.getById(violation.getViolationTypeId());
        return violationType.getName();
    }

    private String getViolationDateTimeStr(Violation violation) {
        LocalDateTime dateTime = violation.getDateTime();
        return dateTimeToStr(dateTime);
    }

    private String getOfficerFullName(Violation violation) {
        Police officer = policeService.getPoliceById(violation.getOfficerId());
        return joinWithSpace(officer.getFirstName(), officer.getLastName());
    }

    private String getViolatorFullName(Violation violation) {
        Driver violator = driverService.getDriverByUserId(violation.getViolatorId());
        return joinWithSpace(violator.getFirstName(), violator.getLastName());
    }

    private String getCarNumber(Violation violation) {
        Car car = carService.getCarById(violation.getCarId());
        return car.getNumber();
    }

    private ExportedViolation composeViolationDocumentPdf(String title, String body) {
        String exportedViolationFilename = generateExportedViolationFilename();
        createTmpDirIfNotExists();
        Path exportedViolationTmpPath = toTmpFilePath(exportedViolationFilename);
        createPdfDocumentAtPath(title, body, exportedViolationTmpPath.toString());
        byte[] content = readAndDeleteFileAtPath(exportedViolationTmpPath);
        return toExportedPdfViolation(exportedViolationFilename, content);
    }

    private String generateExportedViolationFilename() {
        return randomString() + PDF_EXTENSION;
    }

    private void createTmpDirIfNotExists() {
        File tmpDir = new File(tmpDirPath);
        if(notExists(tmpDir)) {
            createDirectoryAtPath(tmpDir.toPath());
        }
    }

    private boolean notExists(File file) {
        return !file.exists();
    }

    private Path toTmpFilePath(String filename) {
        String absoluteTmpDirPath = relativeToAbsolutePath(tmpDirPath);
        String filePath = appendUrlPathSegment(absoluteTmpDirPath, filename);
        return Paths.get(filePath);
    }

    private void createPdfDocumentAtPath(String title, String body, String path) {
        try(Document document = pdfService.newPdfDocument(path)) {
            appendTitleToDocument(title, document);
            appendBodyToDocument(body, document);
            appendDateToDocument(document);
        }
    }

    private void appendTitleToDocument(String title, Document document) {
        Paragraph titleParagraph = pdfService.paragraphOfText(title);
        titleParagraph.setHorizontalAlignment(CENTER);
        titleParagraph.setTextAlignment(TextAlignment.CENTER);
        titleParagraph.setMarginTop(150f);
        document.add(titleParagraph);
    }

    private void appendBodyToDocument(String body, Document document) {
        Paragraph bodyParagraph = pdfService.paragraphOfText(body);
        bodyParagraph.setHorizontalAlignment(CENTER);
        bodyParagraph.setMarginTop(40f);
        bodyParagraph.setMaxWidth(440f);
        document.add(bodyParagraph);
    }

    private void appendDateToDocument(Document document) {
        String dateStr = dateToStr(now());
        Paragraph dateParagraph = pdfService.paragraphOfText(dateStr);
        dateParagraph.setHorizontalAlignment(LEFT);
        dateParagraph.setMarginTop(80f);
        dateParagraph.setMarginLeft(50f);
        document.add(dateParagraph);
    }

    private ExportedViolation toExportedPdfViolation(String filename, byte[] content) {
        return new ExportedViolation(filename, APPLICATION_PDF, content);
    }

}
