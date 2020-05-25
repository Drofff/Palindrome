package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.service.ViolationExportService;
import com.drofff.palindrome.service.ViolationService;
import com.drofff.palindrome.type.ExportedViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.drofff.palindrome.constants.HttpHeaderConstants.ATTACHMENT_CONTENT_DISPOSITION_PREFIX;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Controller
@RequestMapping("/export/violation")
@PreAuthorize("hasAuthority('POLICE')")
public class ViolationExportController {

    private final ViolationService violationService;
    private final ViolationExportService violationExportService;

    @Autowired
    public ViolationExportController(ViolationService violationService, ViolationExportService violationExportService) {
        this.violationService = violationService;
        this.violationExportService = violationExportService;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> exportViolationWithId(@PathVariable String id) {
        Violation violation = violationService.getViolationById(id);
        ExportedViolation exportedViolation =  violationExportService.exportViolation(violation);
        return asResponseEntity(exportedViolation);
    }

    private ResponseEntity<byte[]> asResponseEntity(ExportedViolation exportedViolation) {
        byte[] content = exportedViolation.getContent();
        return ResponseEntity.ok()
                .headers(httpHeadersOf(exportedViolation))
                .contentLength(content.length)
                .body(content);
    }

    private HttpHeaders httpHeadersOf(ExportedViolation exportedViolation) {
        HttpHeaders httpHeaders = new HttpHeaders();
        String contentDisposition = ATTACHMENT_CONTENT_DISPOSITION_PREFIX + exportedViolation.getFilename();
        httpHeaders.add(CONTENT_DISPOSITION, contentDisposition);
        String contentType = exportedViolation.getMediaType().toString();
        httpHeaders.add(CONTENT_TYPE, contentType);
        return httpHeaders;
    }

}
