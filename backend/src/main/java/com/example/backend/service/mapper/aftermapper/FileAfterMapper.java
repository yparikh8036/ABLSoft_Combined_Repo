package com.example.backend.service.mapper.aftermapper;

import com.example.backend.domain.Invoice;
import com.example.backend.service.dto.InvoiceDTO;
import com.example.backend.service.util.DateUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public class FileAfterMapper {

    @AfterMapping
    public void toDtoFromEntity(@MappingTarget InvoiceDTO invoiceDTO, Invoice invoice) {
        invoiceDTO.setInvoiceAge(DateUtil.calculateAge(invoice.getDate()));
    }
}
