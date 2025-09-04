package com.example.ablsoft.service;

import com.example.ablsoft.domain.Invoice;
import com.example.ablsoft.repository.InvoiceRepo;
import com.example.ablsoft.service.dto.InvoiceDTO;
import com.example.ablsoft.service.errors.ErrorConstants;
import com.example.ablsoft.service.errors.GlobalException;
import com.example.ablsoft.service.mapper.InvoiceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadFileServiceTest {

    @Mock
    private InvoiceRepo invoiceRepo;

    @Mock
    private InvoiceMapper invoiceMapper;

    @InjectMocks
    private InvoiceService invoiceService;

    private Invoice entity;
    private InvoiceDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Invoice();
        entity.setId(1L);
        entity.setCustomerId("101");
        entity.setInvoiceNum("5001");
        entity.setDate("2025-09-01T10:30:00Z");
        entity.setDescription("Website development");
        entity.setAmount(1500f);

        dto = new InvoiceDTO();
        dto.setId(1L);
        dto.setCustomerId("101");
        dto.setInvoiceNum("5001");
        dto.setDate("2025-09-01T10:30:00Z");
        dto.setDescription("Website development");
        dto.setAmount(1500f);
    }

    @Test
    @DisplayName("save(UploadFileDTO) saves when not duplicate and returns DTO")
    void saveDto_success() throws Exception {
        InvoiceDTO create = new InvoiceDTO();
        create.setCustomerId("102");
        create.setInvoiceNum("6001");
        create.setDate("2025-09-02T14:15:00Z");
        create.setDescription("Design");
        create.setAmount(2500f);

        Invoice entityToSave = new Invoice();
        entityToSave.setCustomerId("102");
        entityToSave.setInvoiceNum("6001");
        entityToSave.setDate("2025-09-02T14:15:00Z");
        entityToSave.setDescription("Design");
        entityToSave.setAmount(2500f);

        Invoice savedEntity = new Invoice();
        savedEntity.setId(10L);
        savedEntity.setCustomerId("102");
        savedEntity.setInvoiceNum("6001");
        savedEntity.setDate("2025-09-02T14:15:00Z");
        savedEntity.setDescription("Design");
        savedEntity.setAmount(2500f);

        InvoiceDTO expected = new InvoiceDTO();
        expected.setId(10L);
        expected.setCustomerId("102");
        expected.setInvoiceNum("6001");
        expected.setDate("2025-09-02T14:15:00Z");
        expected.setDescription("Design");
        expected.setAmount(2500f);

        when(invoiceRepo.existsByCustomerIdAndInvoiceNum("102", "6001")).thenReturn(false);
        when(invoiceMapper.toEntity(create)).thenReturn(entityToSave);
        when(invoiceRepo.save(entityToSave)).thenReturn(savedEntity);
        when(invoiceMapper.toDto(savedEntity)).thenReturn(expected);

        InvoiceDTO result = invoiceService.save(create);

        assertEquals(expected, result);
        verify(invoiceRepo).save(entityToSave);
    }

    @Test
    @DisplayName("findAll maps and returns list")
    void findAll_success() {
        when(invoiceRepo.findAll()).thenReturn(List.of(entity));
        when(invoiceMapper.toDto(entity)).thenReturn(dto);

        List<InvoiceDTO> result = invoiceService.findAll();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    @DisplayName("findById returns mapped DTO when found")
    void findById_found() {
        when(invoiceRepo.findById(1L)).thenReturn(Optional.of(entity));
        when(invoiceMapper.toDto(entity)).thenReturn(dto);

        InvoiceDTO result = invoiceService.findById(1L);

        assertEquals(dto, result);
    }

    @Test
    @DisplayName("findById throws when not found")
    void findById_notFound() {
        when(invoiceRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> invoiceService.findById(99L));
        assertTrue(ex.getMessage().contains("Post Not by id"));
    }

    @Test
    @DisplayName("uploadAndSave parses CSV, filters duplicates, saves non-duplicates and maps")
    void uploadAndSave_success_filtersDuplicates() {
        String csv = "customerId,invoiceNum,date,description,amount\n" +
                "101,5001,2025-09-01T10:30:00Z,Website development,1500\n" +
                "102,5002,2025-09-02T14:15:00Z,Mobile app design,2500\n";
        MockMultipartFile file = new MockMultipartFile(
                "file", "invoices.csv", "text/csv", csv.getBytes(StandardCharsets.UTF_8));

        // duplicate check: first is duplicate, second is new
        when(invoiceRepo.existsByCustomerIdAndInvoiceNum("101", "5001")).thenReturn(true);
        when(invoiceRepo.existsByCustomerIdAndInvoiceNum("102", "5002")).thenReturn(false);

        // capture list passed to saveAll
        ArgumentCaptor<List<Invoice>> listCaptor = ArgumentCaptor.forClass(List.class);

        // simulate saveAll returns same list with IDs assigned
        Invoice saved = new Invoice();
        saved.setId(200L);
        saved.setCustomerId("102");
        saved.setInvoiceNum("5002");
        saved.setDate("2025-09-02T14:15:00Z");
        saved.setDescription("Mobile app design");
        saved.setAmount(2500f);

        when(invoiceRepo.saveAll(anyList())).thenReturn(List.of(saved));
        when(invoiceMapper.toDtos(List.of(saved))).thenReturn(List.of(buildDtoFrom(saved)));

        List<InvoiceDTO> result = invoiceService.uploadAndSave(file);

        verify(invoiceRepo).saveAll(listCaptor.capture());
        List<Invoice> filtered = listCaptor.getValue();
        assertEquals(1, filtered.size());
        assertEquals("102", filtered.get(0).getCustomerId());
        assertEquals("5002", filtered.get(0).getInvoiceNum());

        assertEquals(1, result.size());
        assertEquals(200L, result.get(0).getId());
        assertEquals("102", result.get(0).getCustomerId());
    }

    @Test
    @DisplayName("uploadAndSave throws on malformed CSV (non-numeric amount)")
    void uploadAndSave_malformedCsv_throws() {
        String badCsv = "customerId,invoiceNum,date,description,amount\n" +
                "101,5001,2025-09-01T10:30:00Z,Website development,abc\n";
        MockMultipartFile file = new MockMultipartFile(
                "file", "invoices.csv", "text/csv", badCsv.getBytes(StandardCharsets.UTF_8));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> invoiceService.uploadAndSave(file));
        assertTrue(ex.getMessage().startsWith("Error while processing CSV file:"));
        verify(invoiceRepo, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("uploadAndSave with semicolon-delimited sample should fail with processing error")
    void uploadAndSave_semicolonSample_fails() {
        String sample = "customerId;invoiceNum;date;description;amount\n" +
                "101;5001;2025-09-01T10:30:00Z;Website development;1500\n";
        MockMultipartFile file = new MockMultipartFile(
                "file", "invoices.csv", "text/csv", sample.getBytes(StandardCharsets.UTF_8));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> invoiceService.uploadAndSave(file));
        assertTrue(ex.getMessage().startsWith("Error while processing CSV file:"));
    }

    private InvoiceDTO buildDtoFrom(Invoice e) {
        InvoiceDTO d = new InvoiceDTO();
        d.setId(e.getId());
        d.setCustomerId(e.getCustomerId());
        d.setInvoiceNum(e.getInvoiceNum());
        d.setDate(e.getDate());
        d.setDescription(e.getDescription());
        d.setAmount(e.getAmount());
        return d;
    }
}
