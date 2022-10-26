package com.alphadevs.wikunum.services.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CustomerAccount.
 */
@Entity
@Table(name = "customer_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerAccount implements Serializable {

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
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustomerAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public CustomerAccount description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getTransactionDate() {
        return this.transactionDate;
    }

    public CustomerAccount transactionDate(Instant transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getCreditAmount() {
        return this.creditAmount;
    }

    public CustomerAccount creditAmount(Double creditAmount) {
        this.setCreditAmount(creditAmount);
        return this;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Double getDebitAmount() {
        return this.debitAmount;
    }

    public CustomerAccount debitAmount(Double debitAmount) {
        this.setDebitAmount(debitAmount);
        return this;
    }

    public void setDebitAmount(Double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public Double getBalanceAmount() {
        return this.balanceAmount;
    }

    public CustomerAccount balanceAmount(Double balanceAmount) {
        this.setBalanceAmount(balanceAmount);
        return this;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getTransactionID() {
        return this.transactionID;
    }

    public CustomerAccount transactionID(String transactionID) {
        this.setTransactionID(transactionID);
        return this;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getLocationCode() {
        return this.locationCode;
    }

    public CustomerAccount locationCode(String locationCode) {
        this.setLocationCode(locationCode);
        return this;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getTenantCode() {
        return this.tenantCode;
    }

    public CustomerAccount tenantCode(String tenantCode) {
        this.setTenantCode(tenantCode);
        return this;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerAccount customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerAccount)) {
            return false;
        }
        return id != null && id.equals(((CustomerAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerAccount{" +
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
