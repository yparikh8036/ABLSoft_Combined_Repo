package com.example.ablsoft.service.mapper;

import com.example.ablsoft.domain.Invoice;
import com.example.ablsoft.service.dto.InvoiceDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {

    Invoice toEntity(InvoiceDTO dto);

    InvoiceDTO toDto(Invoice entity);

    List<InvoiceDTO> toDtos(List<Invoice> invoices);
}
