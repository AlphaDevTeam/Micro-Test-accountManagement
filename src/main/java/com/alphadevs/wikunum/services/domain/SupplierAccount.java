package com.alphadevs.wikunum.services.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SupplierAccount.
 */
@Entity
@Table(name = "supplier_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "transaction_date")
    private Instant transactionDate;

    @Column(name = "credit_amount")
    private Double creditAmount;

    @Column(name = "debit_amount")
    private Double debitAmount;

    @Column(name = "balance_amount")
    private Double balanceAmount;

    @NotNull
    @Column(name = "transaction_id", nullable = false)
    private String transactionID;

    @NotNull
    @Column(name = "location_code", nullable = false)
    private String locationCode;

    @NotNull
    @Column(name = "tenant_code", nullable = false)
    private String tenantCode;

    @ManyToOne
    private Supplier supplier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SupplierAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public SupplierAccount description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getTransactionDate() {
        return this.transactionDate;
    }

    public SupplierAccount transactionDate(Instant transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getCreditAmount() {
        return this.creditAmount;
    }

    public SupplierAccount creditAmount(Double creditAmount) {
        this.setCreditAmount(creditAmount);
        return this;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Double getDebitAmount() {
        return this.debitAmount;
    }

    public SupplierAccount debitAmount(Double debitAmount) {
        this.setDebitAmount(debitAmount);
        return this;
    }

    public void setDebitAmount(Double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public Double getBalanceAmount() {
        return this.balanceAmount;
    }

    public SupplierAccount balanceAmount(Double balanceAmount) {
        this.setBalanceAmount(balanceAmount);
        return this;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getTransactionID() {
        return this.transactionID;
    }

    public SupplierAccount transactionID(String transactionID) {
        this.setTransactionID(transactionID);
        return this;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getLocationCode() {
        return this.locationCode;
    }

    public SupplierAccount locationCode(String locationCode) {
        this.setLocationCode(locationCode);
        return this;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getTenantCode() {
        return this.tenantCode;
    }

    public SupplierAccount tenantCode(String tenantCode) {
        this.setTenantCode(tenantCode);
        return this;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public Supplier getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public SupplierAccount supplier(Supplier supplier) {
        this.setSupplier(supplier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupplierAccount)) {
            return false;
        }
        return id != null && id.equals(((SupplierAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierAccount{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", creditAmount=" + getCreditAmount() +
            ", debitAmount=" + getDebitAmount() +
            ", balanceAmount=" + getBalanceAmount() +
            ", transactionID='" + getTransactionID() + "'" +
            ", locationCode='" + getLocationCode() + "'" +
            ", tenantCode='" + getTenantCode() + "'" +
            "}";
    }
}
