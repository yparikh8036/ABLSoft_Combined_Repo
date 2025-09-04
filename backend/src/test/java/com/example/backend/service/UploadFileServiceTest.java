package com.example.backend.service;

import com.example.backend.domain.Invoice;
import com.example.backend.repository.InvoiceRepo;
import com.example.backend.service.dto.InvoiceDTO;
import com.example.backend.service.mapper.InvoiceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
        dto.setDate("2025-09-01");
        dto.setDescription("Website development");
        dto.setAmount(1500f);
    }

    @Test
    @DisplayName("save(UploadFileDTO) saves when not duplicate and returns DTO")
    void saveDto_success() throws Exception {
        InvoiceDTO create = new InvoiceDTO();
        create.setCustomerId("102");
        create.setInvoiceNum("6001");
        create.setDate("2025-09-02");
        create.setDescription("Design");
        create.setAmount(2500f);

        Invoice entityToSave = new Invoice();
        entityToSave.setCustomerId("102");
        entityToSave.setInvoiceNum("6001");
        entityToSave.setDate("2025-09-02");
        entityToSave.setDescription("Design");
        entityToSave.setAmount(2500f);

        Invoice savedEntity = new Invoice();
        savedEntity.setId(10L);
        savedEntity.setCustomerId("102");
        savedEntity.setInvoiceNum("6001");
        savedEntity.setDate("2025-09-02");
        savedEntity.setDescription("Design");
        savedEntity.setAmount(2500f);

        InvoiceDTO expected = new InvoiceDTO();
        expected.setId(10L);
        expected.setCustomerId("102");
        expected.setInvoiceNum("6001");
        expected.setDate("2025-09-02");
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
    @DisplayName("uploadAndSave throws on malformed CSV (non-numeric amount)")
    void uploadAndSave_malformedCsv_throws() {
        String badCsv = "customerId,invoiceNum,date,description,amount\n" +
                "101,5001,2025-09-01,Website development,abc\n";
        MockMultipartFile file = new MockMultipartFile(
                "file", "invoices.csv", "text/csv", badCsv.getBytes(StandardCharsets.UTF_8));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> invoiceService.uploadAndSave(file));
        assertTrue(ex.getMessage().startsWith("Error while processing CSV file:"));
        verify(invoiceRepo, never()).saveAll(anyList());
    }
}
