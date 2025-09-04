package com.example.ablsoft.service.mapper;

import com.example.ablsoft.domain.Invoice;
import com.example.ablsoft.service.dto.InvoiceDTO;
import com.example.ablsoft.service.mapper.aftermapper.FileAfterMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {FileAfterMapper.class})
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {

    Invoice toEntity(InvoiceDTO dto);

    InvoiceDTO toDto(Invoice entity);

    List<InvoiceDTO> toDtos(List<Invoice> invoices);
}
