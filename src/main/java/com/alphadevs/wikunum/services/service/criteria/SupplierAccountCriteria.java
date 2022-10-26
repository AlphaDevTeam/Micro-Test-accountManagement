package com.alphadevs.wikunum.services.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.alphadevs.wikunum.services.domain.SupplierAccount} entity. This class is used
 * in {@link com.alphadevs.wikunum.services.web.rest.SupplierAccountResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /supplier-accounts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierAccountCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private InstantFilter transactionDate;

    private DoubleFilter creditAmount;

    private DoubleFilter debitAmount;

    private DoubleFilter balanceAmount;

    private StringFilter transactionID;

    private StringFilter locationCode;

    private StringFilter tenantCode;

    private LongFilter supplierId;

    private Boolean distinct;

    public SupplierAccountCriteria() {}

    public SupplierAccountCriteria(SupplierAccountCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.transactionDate = other.transactionDate == null ? null : other.transactionDate.copy();
        this.creditAmount = other.creditAmount == null ? null : other.creditAmount.copy();
        this.debitAmount = other.debitAmount == null ? null : other.debitAmount.copy();
        this.balanceAmount = other.balanceAmount == null ? null : other.balanceAmount.copy();
        this.transactionID = other.transactionID == null ? null : other.transactionID.copy();
        this.locationCode = other.locationCode == null ? null : other.locationCode.copy();
        this.tenantCode = other.tenantCode == null ? null : other.tenantCode.copy();
        this.supplierId = other.supplierId == null ? null : other.supplierId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SupplierAccountCriteria copy() {
        return new SupplierAccountCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public InstantFilter getTransactionDate() {
        return transactionDate;
    }

    public InstantFilter transactionDate() {
        if (transactionDate == null) {
            transactionDate = new InstantFilter();
        }
        return transactionDate;
    }

    public void setTransactionDate(InstantFilter transactionDate) {
        this.transactionDate = transactionDate;
    }

    public DoubleFilter getCreditAmount() {
        return creditAmount;
    }

    public DoubleFilter creditAmount() {
        if (creditAmount == null) {
            creditAmount = new DoubleFilter();
        }
        return creditAmount;
    }

    public void setCreditAmount(DoubleFilter creditAmount) {
        this.creditAmount = creditAmount;
    }

    public DoubleFilter getDebitAmount() {
        return debitAmount;
    }

    public DoubleFilter debitAmount() {
        if (debitAmount == null) {
            debitAmount = new DoubleFilter();
        }
        return debitAmount;
    }

    public void setDebitAmount(DoubleFilter debitAmount) {
        this.debitAmount = debitAmount;
    }

    public DoubleFilter getBalanceAmount() {
        return balanceAmount;
    }

    public DoubleFilter balanceAmount() {
        if (balanceAmount == null) {
            balanceAmount = new DoubleFilter();
        }
        return balanceAmount;
    }

    public void setBalanceAmount(DoubleFilter balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public StringFilter getTransactionID() {
        return transactionID;
    }

    public StringFilter transactionID() {
        if (transactionID == null) {
            transactionID = new StringFilter();
        }
        return transactionID;
    }

    public void setTransactionID(StringFilter transactionID) {
        this.transactionID = transactionID;
    }

    public StringFilter getLocationCode() {
        return locationCode;
    }

    public StringFilter locationCode() {
        if (locationCode == null) {
            locationCode = new StringFilter();
        }
        return locationCode;
    }

    public void setLocationCode(StringFilter locationCode) {
        this.locationCode = locationCode;
    }

    public StringFilter getTenantCode() {
        return tenantCode;
    }

    public StringFilter tenantCode() {
        if (tenantCode == null) {
            tenantCode = new StringFilter();
        }
        return tenantCode;
    }

    public void setTenantCode(StringFilter tenantCode) {
        this.tenantCode = tenantCode;
    }

    public LongFilter getSupplierId() {
        return supplierId;
    }

    public LongFilter supplierId() {
        if (supplierId == null) {
            supplierId = new LongFilter();
        }
        return supplierId;
    }

    public void setSupplierId(LongFilter supplierId) {
        this.supplierId = supplierId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SupplierAccountCriteria that = (SupplierAccountCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(creditAmount, that.creditAmount) &&
            Objects.equals(debitAmount, that.debitAmount) &&
            Objects.equals(balanceAmount, that.balanceAmount) &&
            Objects.equals(transactionID, that.transactionID) &&
            Objects.equals(locationCode, that.locationCode) &&
            Objects.equals(tenantCode, that.tenantCode) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            transactionDate,
            creditAmount,
            debitAmount,
            balanceAmount,
            transactionID,
            locationCode,
            tenantCode,
            supplierId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierAccountCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
            (creditAmount != null ? "creditAmount=" + creditAmount + ", " : "") +
            (debitAmount != null ? "debitAmount=" + debitAmount + ", " : "") +
            (balanceAmount != null ? "balanceAmount=" + balanceAmount + ", " : "") +
            (transactionID != null ? "transactionID=" + transactionID + ", " : "") +
            (locationCode != null ? "locationCode=" + locationCode + ", " : "") +
            (tenantCode != null ? "tenantCode=" + tenantCode + ", " : "") +
            (supplierId != null ? "supplierId=" + supplierId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
