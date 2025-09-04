package com.example.ablsoft.service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class InvoiceDTO {

    private Long id;

    @NotNull
    private String customerId;

    @NotNull
    private String invoiceNum;

    @NotNull
    private String date;

    private String description;

    @NotNull
    private Float amount;

    private Long invoiceAge;

    public InvoiceDTO() {
    }

    public InvoiceDTO(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvoiceAge() {
        return invoiceAge;
    }

    public void setInvoiceAge(Long invoiceAge) {
        this.invoiceAge = invoiceAge;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof InvoiceDTO that)) return false;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", invoiceNum=" + invoiceNum +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}
