package com.example.ablsoft.service;

import com.example.ablsoft.domain.Invoice;
import com.example.ablsoft.repository.InvoiceRepo;
import com.example.ablsoft.service.dto.InvoiceDTO;
import com.example.ablsoft.service.mapper.InvoiceMapper;
import com.example.ablsoft.service.errors.ErrorConstants;
import com.example.ablsoft.service.errors.GlobalException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for managing Invoice entities and CSV import operations.
 * <p>
 * Responsibilities include:
 * - Persisting single records (via DTO or entity) while preventing duplicates.
 * - Retrieving records (all or by id).
 * - Parsing and persisting records from an uploaded CSV file.
 * </p>
 */
@Service
public class InvoiceService {
    private final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    private final InvoiceRepo invoiceRepo;

    private final InvoiceMapper invoiceMapper;

    /**
     * Constructs the service with the required repository and mapper dependencies.
     *
     * @param invoiceRepo   repository for persistence operations
     * @param invoiceMapper mapper to convert between entity and DTO
     */
    public InvoiceService(InvoiceRepo invoiceRepo, InvoiceMapper invoiceMapper) {
        this.invoiceRepo = invoiceRepo;
        this.invoiceMapper = invoiceMapper;
    }

    /**
     * Saves an Invoice represented as a DTO.
     * <p>
     * The DTO is converted to an entity and delegated to {@link #save(Invoice)}
     * which enforces duplicate checks and persistence.
     * </p>
     *
     * @param invoiceDTO the data transfer object containing Invoice fields
     * @return the persisted Invoice converted back to a DTO
     * @throws GlobalException if a duplicate (customerId + invoiceNum) is detected
     */
    public InvoiceDTO save(InvoiceDTO invoiceDTO) {
        log.debug("Request to save upload Invoice DTO : {}", invoiceDTO);

        return save(invoiceMapper.toEntity(invoiceDTO));
    }

    /**
     * Saves an Invoice entity after validating it is not a duplicate.
     *
     * @param invoice the entity to be persisted
     * @return the persisted entity mapped to a DTO
     * @throws GlobalException if an Invoice with the same customerId and invoiceNum already exists
     */
    public InvoiceDTO save(Invoice invoice) {
        log.debug("Request to save Invoice  : {}", invoice);

        if (checkDuplicate(invoice.getCustomerId(), invoice.getInvoiceNum())) {
            throw new GlobalException(ErrorConstants.DUPLICATE_INVOICE_EXCEPTION_MESSAGE, ErrorConstants.DUPLICATE_INVOICE_EXCEPTION_CODE, HttpStatus.BAD_REQUEST);
        }

        invoice = invoiceRepo.save(invoice);
        return invoiceMapper.toDto(invoice);
    }

    /**
     * Checks if an UploadFile already exists for the given composite key.
     *
     * @param customerId the customer identifier
     * @param invoiceNum the invoice number
     * @return true if a record exists with the same customerId and invoiceNum; false otherwise
     */
    private boolean checkDuplicate(String customerId, String invoiceNum) {
        return invoiceRepo.existsByCustomerIdAndInvoiceNum(customerId, invoiceNum);
    }

    /**
     * Retrieves all Invoice records.
     *
     * @return a list of InvoiceDTOs for all persisted records
     */
    public List<InvoiceDTO> findAll() {
        log.debug("Request to get all Invoicex");
        return invoiceRepo.findAll()
                .stream()
                .map(invoiceMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a single Invoice by its id.
     *
     * @param fileId the primary key of the Invoice
     * @return the corresponding InvoiceDTO
     * @throws RuntimeException if no record exists for the provided id
     */
    public InvoiceDTO findById(Long fileId) {
        log.debug("Request to get Invoice By Id : {}", fileId);
        return invoiceRepo.findById(fileId)
                .map(invoiceMapper::toDto)
                .orElseThrow(() -> new RuntimeException(String.format("Post Not by id = %s", fileId)));
    }

    /**
     * Parses an uploaded CSV file and persists non-duplicate records.
     * <p>
     * The CSV is expected to contain a header with the following columns:
     * customerId, invoiceNum, date, description, amount.
     * Duplicate rows (by customerId + invoiceNum) are filtered out before saving.
     * </p>
     *
     * @param file multipart CSV file to parse
     * @return list of persisted UploadFileDTOs corresponding to non-duplicate rows
     * @throws RuntimeException if CSV parsing fails or input cannot be read
     */
    public List<InvoiceDTO> uploadAndSave(MultipartFile file) {
        log.debug("Request to upload Invoice : {}", file.getName());

        List<Invoice> invoices = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));

             CSVParser csvParser = new CSVParser(reader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            for (CSVRecord csvRecord : csvParser) {

                Invoice invoice = new Invoice();
                invoice.setCustomerId(csvRecord.get("customerId"));
                invoice.setInvoiceNum(csvRecord.get("invoiceNum"));
                invoice.setDate(csvRecord.get("date"));
                invoice.setDescription(csvRecord.get("description"));
                invoice.setAmount(Float.parseFloat(csvRecord.get("amount")));
                invoices.add(invoice);
            }

        } catch (Exception e) {
            throw new GlobalException(ErrorConstants.CSV_PROCESSING_EXCEPTION_MESSAGE, ErrorConstants.CSV_PROCESSING_EXCEPTION_CODE, HttpStatus.BAD_REQUEST);
        }

        return saveUploadedFiles(invoices);
    }

    /**
     * Persists the provided list of UploadFile entities after filtering out duplicates.
     *
     * @param invoices list of Invoice entities parsed from input
     * @return list of DTOs for the records that were actually saved
     */
    private List<InvoiceDTO> saveUploadedFiles(List<Invoice> invoices) {
        List<Invoice> filteredList = invoices
                .stream()
                .filter(uploadFile -> !checkDuplicate(uploadFile.getCustomerId(), uploadFile.getInvoiceNum()))
                .toList();

        return invoiceMapper.toDtos(invoiceRepo.saveAll(filteredList));
    }

}
