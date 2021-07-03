package com.weblearnex.app.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface PrintLabelService {
    ResponseEntity<Resource> generateOrderPrintLabel(String awbNumber);
    ResponseEntity<Resource> generateInvoicePrintLabel(String awbNumber);
    ResponseEntity<Resource> generateManifestPrintLabel(String manifestNumber);
}
