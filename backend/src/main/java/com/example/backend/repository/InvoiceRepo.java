package com.example.backend.repository;

import com.example.backend.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepo extends
        JpaRepository<Invoice, Long>,
        PagingAndSortingRepository<Invoice, Long> {

    boolean existsByCustomerIdAndInvoiceNum(String customerId, String invoiceNum);
}
