package com.example.ablsoft.rest;

import com.example.ablsoft.service.InvoiceService;
import com.example.ablsoft.service.dto.InvoiceDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller providing endpoints to manage upload file records and CSV imports.
 *
 * Endpoints:
 * - POST /api/upload-file: Create a single UploadFile from JSON (no ID allowed).
 * - PUT  /api/upload-file: Update an existing UploadFile from JSON (ID required).
 * - GET  /api/files:       List all UploadFiles.
 * - GET  /api/files/{id}:  Retrieve a single UploadFile by id.
 * - POST /api/upload:      Upload a CSV file (multipart/form-data) and persist non-duplicates.
 */
@RestController
@RequestMapping("/api")
public class InvoiceResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);

    public InvoiceResource(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    private final InvoiceService invoiceService;

    /**
     * Creates a single UploadFile record from JSON.
     *
     * Constraints:
     * - The incoming DTO must NOT contain an id; otherwise a 500 will be thrown by the generic Exception.
     *   Consider refining to a typed exception and handler if needed.
     *
     * @param invoiceDTO the payload describing the upload file to persist
     * @return 200 OK with the persisted DTO
     * @throws Exception when the DTO contains an id (creation not allowed with id)
     */
    @PostMapping("/upload-file")
    public ResponseEntity<InvoiceDTO> upload(@RequestBody @Valid InvoiceDTO invoiceDTO) throws Exception {
        log.debug("REST request to save upload file : {}", invoiceDTO);
        if (invoiceDTO.getId() != null) {
            throw new Exception("A new upload file cannot already have an ID");
        }
        InvoiceDTO result = invoiceService.save(invoiceDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * Updates an existing UploadFile record.
     *
     * Requirements:
     * - The DTO must contain a non-null id.
     *
     * @param invoiceDTO the payload containing updated fields and an id
     * @return 200 OK with the persisted DTO after update
     * @throws Exception when id is missing
     */
    @PutMapping("/upload-file")
    public ResponseEntity<InvoiceDTO> update(@RequestBody @Valid InvoiceDTO invoiceDTO) throws Exception {
        log.debug("REST request to update uploaded file : {}", invoiceDTO);

        if (invoiceDTO.getId() == null) {
            throw new Exception("A update uploaded file should have an ID");
        }
        InvoiceDTO result = invoiceService.save(invoiceDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * Lists all UploadFile records.
     *
     * @return array of UploadFileDTO
     */
    @GetMapping("/files")
    public List<InvoiceDTO> getAllFiles() {
        log.debug("REST request to get all files");
        return invoiceService.findAll();
    }

    /**
     * Retrieves a single UploadFile by its id.
     *
     * @param fileId primary key of the record
     * @return 200 OK with the DTO
     */
    @GetMapping("/files/{fileId}")
    public ResponseEntity<InvoiceDTO> getFileById(@PathVariable Long fileId) {
        log.debug("Request to get file By Id : {}", fileId);
        return ResponseEntity.ok(invoiceService.findById(fileId));
    }

    /**
     * Accepts a CSV file via multipart/form-data and persists non-duplicate rows.
     *
     * Expected columns in the CSV header: customerId, invoiceNum, date, description, amount.
     * Empty files return 400 Bad Request.
     *
     * @param file CSV file to be processed
     * @return 200 OK with the list of persisted DTOs
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<List<InvoiceDTO>> uploadFile(@RequestParam("file") MultipartFile file) {
        log.debug("Request to get file : {}", file.getName());
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<InvoiceDTO> uploadFileDTOS = invoiceService.uploadAndSave(file);
        return ResponseEntity.ok(uploadFileDTOS);
    }
}
