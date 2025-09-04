package com.example.ablsoft.service.mapper.aftermapper;

import com.example.ablsoft.domain.Invoice;
import com.example.ablsoft.service.dto.InvoiceDTO;
import com.example.ablsoft.service.util.DateUtil;
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
