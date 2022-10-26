package com.alphadevs.wikunum.services.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.alphadevs.wikunum.services.domain.Supplier} entity. This class is used
 * in {@link com.alphadevs.wikunum.services.web.rest.SupplierResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /suppliers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private BigDecimalFilter creditLimit;

    private DoubleFilter rating;

    private BooleanFilter isActive;

    private StringFilter phone;

    private StringFilter addressLine1;

    private StringFilter addressLine2;

    private StringFilter city;

    private StringFilter country;

    private StringFilter email;

    private StringFilter transactionID;

    private StringFilter locationCode;

    private StringFilter tenantCode;

    private Boolean distinct;

    public SupplierCriteria() {}

    public SupplierCriteria(SupplierCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.creditLimit = other.creditLimit == null ? null : other.creditLimit.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.addressLine1 = other.addressLine1 == null ? null : other.addressLine1.copy();
        this.addressLine2 = other.addressLine2 == null ? null : other.addressLine2.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.transactionID = other.transactionID == null ? null : other.transactionID.copy();
        this.locationCode = other.locationCode == null ? null : other.locationCode.copy();
        this.tenantCode = other.tenantCode == null ? null : other.tenantCode.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SupplierCriteria copy() {
        return new SupplierCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BigDecimalFilter getCreditLimit() {
        return creditLimit;
    }

    public BigDecimalFilter creditLimit() {
        if (creditLimit == null) {
            creditLimit = new BigDecimalFilter();
        }
        return creditLimit;
    }

    public void setCreditLimit(BigDecimalFilter creditLimit) {
        this.creditLimit = creditLimit;
    }

    public DoubleFilter getRating() {
        return rating;
    }

    public DoubleFilter rating() {
        if (rating == null) {
            rating = new DoubleFilter();
        }
        return rating;
    }

    public void setRating(DoubleFilter rating) {
        this.rating = rating;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getAddressLine1() {
        return addressLine1;
    }

    public StringFilter addressLine1() {
        if (addressLine1 == null) {
            addressLine1 = new StringFilter();
        }
        return addressLine1;
    }

    public void setAddressLine1(StringFilter addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public StringFilter getAddressLine2() {
        return addressLine2;
    }

    public StringFilter addressLine2() {
        if (addressLine2 == null) {
            addressLine2 = new StringFilter();
        }
        return addressLine2;
    }

    public void setAddressLine2(StringFilter addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
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
        final SupplierCriteria that = (SupplierCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(creditLimit, that.creditLimit) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(addressLine1, that.addressLine1) &&
            Objects.equals(addressLine2, that.addressLine2) &&
            Objects.equals(city, that.city) &&
            Objects.equals(country, that.country) &&
            Objects.equals(email, that.email) &&
            Objects.equals(transactionID, that.transactionID) &&
            Objects.equals(locationCode, that.locationCode) &&
            Objects.equals(tenantCode, that.tenantCode) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            name,
            creditLimit,
            rating,
            isActive,
            phone,
            addressLine1,
            addressLine2,
            city,
            country,
            email,
            transactionID,
            locationCode,
            tenantCode,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (creditLimit != null ? "creditLimit=" + creditLimit + ", " : "") +
            (rating != null ? "rating=" + rating + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (addressLine1 != null ? "addressLine1=" + addressLine1 + ", " : "") +
            (addressLine2 != null ? "addressLine2=" + addressLine2 + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (transactionID != null ? "transactionID=" + transactionID + ", " : "") +
            (locationCode != null ? "locationCode=" + locationCode + ", " : "") +
            (tenantCode != null ? "tenantCode=" + tenantCode + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
