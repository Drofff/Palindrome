package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.type.ExportedViolation;

public interface ViolationExportService {

    ExportedViolation exportViolation(Violation violation);

}
