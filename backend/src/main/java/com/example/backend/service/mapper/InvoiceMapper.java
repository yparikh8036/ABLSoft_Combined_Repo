package com.example.backend.service.mapper;

import com.example.backend.domain.Invoice;
import com.example.backend.service.dto.InvoiceDTO;
import com.example.backend.service.mapper.aftermapper.FileAfterMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {FileAfterMapper.class})
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {

    Invoice toEntity(InvoiceDTO dto);

    InvoiceDTO toDto(Invoice entity);

    List<InvoiceDTO> toDtos(List<Invoice> invoices);
}
