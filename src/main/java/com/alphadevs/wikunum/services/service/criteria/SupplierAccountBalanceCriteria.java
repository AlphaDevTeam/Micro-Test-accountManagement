package com.alphadevs.wikunum.services.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.alphadevs.wikunum.services.domain.SupplierAccountBalance} entity. This class is used
 * in {@link com.alphadevs.wikunum.services.web.rest.SupplierAccountBalanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /supplier-account-balances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierAccountBalanceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter balance;

    private StringFilter locationCode;

    private StringFilter tenantCode;

    private LongFilter supplierId;

    private Boolean distinct;

    public SupplierAccountBalanceCriteria() {}

    public SupplierAccountBalanceCriteria(SupplierAccountBalanceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.balance = other.balance == null ? null : other.balance.copy();
        this.locationCode = other.locationCode == null ? null : other.locationCode.copy();
        this.tenantCode = other.tenantCode == null ? null : other.tenantCode.copy();
        this.supplierId = other.supplierId == null ? null : other.supplierId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SupplierAccountBalanceCriteria copy() {
        return new SupplierAccountBalanceCriteria(this);
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

    public DoubleFilter getBalance() {
        return balance;
    }

    public DoubleFilter balance() {
        if (balance == null) {
            balance = new DoubleFilter();
        }
        return balance;
    }

    public void setBalance(DoubleFilter balance) {
        this.balance = balance;
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
        final SupplierAccountBalanceCriteria that = (SupplierAccountBalanceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(balance, that.balance) &&
            Objects.equals(locationCode, that.locationCode) &&
            Objects.equals(tenantCode, that.tenantCode) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, locationCode, tenantCode, supplierId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierAccountBalanceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (balance != null ? "balance=" + balance + ", " : "") +
            (locationCode != null ? "locationCode=" + locationCode + ", " : "") +
            (tenantCode != null ? "tenantCode=" + tenantCode + ", " : "") +
            (supplierId != null ? "supplierId=" + supplierId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
