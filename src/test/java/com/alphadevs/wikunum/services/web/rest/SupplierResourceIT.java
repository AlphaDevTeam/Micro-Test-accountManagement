package com.alphadevs.wikunum.services.web.rest;

import static com.alphadevs.wikunum.services.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.alphadevs.wikunum.services.IntegrationTest;
import com.alphadevs.wikunum.services.domain.Supplier;
import com.alphadevs.wikunum.services.repository.SupplierRepository;
import com.alphadevs.wikunum.services.service.criteria.SupplierCriteria;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SupplierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SupplierResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CREDIT_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT_LIMIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_CREDIT_LIMIT = new BigDecimal(1 - 1);

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;
    private static final Double SMALLER_RATING = 1D - 1D;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TENANT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/suppliers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierMockMvc;

    private Supplier supplier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplier createEntity(EntityManager em) {
        Supplier supplier = new Supplier()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .creditLimit(DEFAULT_CREDIT_LIMIT)
            .rating(DEFAULT_RATING)
            .isActive(DEFAULT_IS_ACTIVE)
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .email(DEFAULT_EMAIL)
            .transactionID(DEFAULT_TRANSACTION_ID)
            .locationCode(DEFAULT_LOCATION_CODE)
            .tenantCode(DEFAULT_TENANT_CODE);
        return supplier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplier createUpdatedEntity(EntityManager em) {
        Supplier supplier = new Supplier()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .rating(UPDATED_RATING)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .email(UPDATED_EMAIL)
            .transactionID(UPDATED_TRANSACTION_ID)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);
        return supplier;
    }

    @BeforeEach
    public void initTest() {
        supplier = createEntity(em);
    }

    @Test
    @Transactional
    void createSupplier() throws Exception {
        int databaseSizeBeforeCreate = supplierRepository.findAll().size();
        // Create the Supplier
        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isCreated());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeCreate + 1);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSupplier.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSupplier.getCreditLimit()).isEqualByComparingTo(DEFAULT_CREDIT_LIMIT);
        assertThat(testSupplier.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testSupplier.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testSupplier.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSupplier.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testSupplier.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testSupplier.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testSupplier.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testSupplier.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSupplier.getTransactionID()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testSupplier.getLocationCode()).isEqualTo(DEFAULT_LOCATION_CODE);
        assertThat(testSupplier.getTenantCode()).isEqualTo(DEFAULT_TENANT_CODE);
    }

    @Test
    @Transactional
    void createSupplierWithExistingId() throws Exception {
        // Create the Supplier with an existing ID
        supplier.setId(1L);

        int databaseSizeBeforeCreate = supplierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setName(null);

        // Create the Supplier, which fails.

        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isBadRequest());

        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setPhone(null);

        // Create the Supplier, which fails.

        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isBadRequest());

        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setCity(null);

        // Create the Supplier, which fails.

        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isBadRequest());

        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setTransactionID(null);

        // Create the Supplier, which fails.

        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isBadRequest());

        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setLocationCode(null);

        // Create the Supplier, which fails.

        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isBadRequest());

        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTenantCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setTenantCode(null);

        // Create the Supplier, which fails.

        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isBadRequest());

        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSuppliers() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].creditLimit").value(hasItem(sameNumber(DEFAULT_CREDIT_LIMIT))))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].transactionID").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].tenantCode").value(hasItem(DEFAULT_TENANT_CODE)));
    }

    @Test
    @Transactional
    void getSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get the supplier
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL_ID, supplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplier.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.creditLimit").value(sameNumber(DEFAULT_CREDIT_LIMIT)))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.transactionID").value(DEFAULT_TRANSACTION_ID))
            .andExpect(jsonPath("$.locationCode").value(DEFAULT_LOCATION_CODE))
            .andExpect(jsonPath("$.tenantCode").value(DEFAULT_TENANT_CODE));
    }

    @Test
    @Transactional
    void getSuppliersByIdFiltering() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        Long id = supplier.getId();

        defaultSupplierShouldBeFound("id.equals=" + id);
        defaultSupplierShouldNotBeFound("id.notEquals=" + id);

        defaultSupplierShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSupplierShouldNotBeFound("id.greaterThan=" + id);

        defaultSupplierShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSupplierShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSuppliersByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where code equals to DEFAULT_CODE
        defaultSupplierShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the supplierList where code equals to UPDATED_CODE
        defaultSupplierShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSuppliersByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where code in DEFAULT_CODE or UPDATED_CODE
        defaultSupplierShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the supplierList where code equals to UPDATED_CODE
        defaultSupplierShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSuppliersByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where code is not null
        defaultSupplierShouldBeFound("code.specified=true");

        // Get all the supplierList where code is null
        defaultSupplierShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByCodeContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where code contains DEFAULT_CODE
        defaultSupplierShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the supplierList where code contains UPDATED_CODE
        defaultSupplierShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSuppliersByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where code does not contain DEFAULT_CODE
        defaultSupplierShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the supplierList where code does not contain UPDATED_CODE
        defaultSupplierShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSuppliersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name equals to DEFAULT_NAME
        defaultSupplierShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the supplierList where name equals to UPDATED_NAME
        defaultSupplierShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSuppliersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSupplierShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the supplierList where name equals to UPDATED_NAME
        defaultSupplierShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSuppliersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name is not null
        defaultSupplierShouldBeFound("name.specified=true");

        // Get all the supplierList where name is null
        defaultSupplierShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByNameContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name contains DEFAULT_NAME
        defaultSupplierShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the supplierList where name contains UPDATED_NAME
        defaultSupplierShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSuppliersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name does not contain DEFAULT_NAME
        defaultSupplierShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the supplierList where name does not contain UPDATED_NAME
        defaultSupplierShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSuppliersByCreditLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where creditLimit equals to DEFAULT_CREDIT_LIMIT
        defaultSupplierShouldBeFound("creditLimit.equals=" + DEFAULT_CREDIT_LIMIT);

        // Get all the supplierList where creditLimit equals to UPDATED_CREDIT_LIMIT
        defaultSupplierShouldNotBeFound("creditLimit.equals=" + UPDATED_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllSuppliersByCreditLimitIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where creditLimit in DEFAULT_CREDIT_LIMIT or UPDATED_CREDIT_LIMIT
        defaultSupplierShouldBeFound("creditLimit.in=" + DEFAULT_CREDIT_LIMIT + "," + UPDATED_CREDIT_LIMIT);

        // Get all the supplierList where creditLimit equals to UPDATED_CREDIT_LIMIT
        defaultSupplierShouldNotBeFound("creditLimit.in=" + UPDATED_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllSuppliersByCreditLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where creditLimit is not null
        defaultSupplierShouldBeFound("creditLimit.specified=true");

        // Get all the supplierList where creditLimit is null
        defaultSupplierShouldNotBeFound("creditLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByCreditLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where creditLimit is greater than or equal to DEFAULT_CREDIT_LIMIT
        defaultSupplierShouldBeFound("creditLimit.greaterThanOrEqual=" + DEFAULT_CREDIT_LIMIT);

        // Get all the supplierList where creditLimit is greater than or equal to UPDATED_CREDIT_LIMIT
        defaultSupplierShouldNotBeFound("creditLimit.greaterThanOrEqual=" + UPDATED_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllSuppliersByCreditLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where creditLimit is less than or equal to DEFAULT_CREDIT_LIMIT
        defaultSupplierShouldBeFound("creditLimit.lessThanOrEqual=" + DEFAULT_CREDIT_LIMIT);

        // Get all the supplierList where creditLimit is less than or equal to SMALLER_CREDIT_LIMIT
        defaultSupplierShouldNotBeFound("creditLimit.lessThanOrEqual=" + SMALLER_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllSuppliersByCreditLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where creditLimit is less than DEFAULT_CREDIT_LIMIT
        defaultSupplierShouldNotBeFound("creditLimit.lessThan=" + DEFAULT_CREDIT_LIMIT);

        // Get all the supplierList where creditLimit is less than UPDATED_CREDIT_LIMIT
        defaultSupplierShouldBeFound("creditLimit.lessThan=" + UPDATED_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllSuppliersByCreditLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where creditLimit is greater than DEFAULT_CREDIT_LIMIT
        defaultSupplierShouldNotBeFound("creditLimit.greaterThan=" + DEFAULT_CREDIT_LIMIT);

        // Get all the supplierList where creditLimit is greater than SMALLER_CREDIT_LIMIT
        defaultSupplierShouldBeFound("creditLimit.greaterThan=" + SMALLER_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    void getAllSuppliersByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where rating equals to DEFAULT_RATING
        defaultSupplierShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the supplierList where rating equals to UPDATED_RATING
        defaultSupplierShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllSuppliersByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultSupplierShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the supplierList where rating equals to UPDATED_RATING
        defaultSupplierShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllSuppliersByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where rating is not null
        defaultSupplierShouldBeFound("rating.specified=true");

        // Get all the supplierList where rating is null
        defaultSupplierShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where rating is greater than or equal to DEFAULT_RATING
        defaultSupplierShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the supplierList where rating is greater than or equal to UPDATED_RATING
        defaultSupplierShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllSuppliersByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where rating is less than or equal to DEFAULT_RATING
        defaultSupplierShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the supplierList where rating is less than or equal to SMALLER_RATING
        defaultSupplierShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllSuppliersByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where rating is less than DEFAULT_RATING
        defaultSupplierShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the supplierList where rating is less than UPDATED_RATING
        defaultSupplierShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllSuppliersByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where rating is greater than DEFAULT_RATING
        defaultSupplierShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the supplierList where rating is greater than SMALLER_RATING
        defaultSupplierShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllSuppliersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where isActive equals to DEFAULT_IS_ACTIVE
        defaultSupplierShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the supplierList where isActive equals to UPDATED_IS_ACTIVE
        defaultSupplierShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSuppliersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultSupplierShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the supplierList where isActive equals to UPDATED_IS_ACTIVE
        defaultSupplierShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSuppliersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where isActive is not null
        defaultSupplierShouldBeFound("isActive.specified=true");

        // Get all the supplierList where isActive is null
        defaultSupplierShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phone equals to DEFAULT_PHONE
        defaultSupplierShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the supplierList where phone equals to UPDATED_PHONE
        defaultSupplierShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSuppliersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultSupplierShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the supplierList where phone equals to UPDATED_PHONE
        defaultSupplierShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSuppliersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phone is not null
        defaultSupplierShouldBeFound("phone.specified=true");

        // Get all the supplierList where phone is null
        defaultSupplierShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phone contains DEFAULT_PHONE
        defaultSupplierShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the supplierList where phone contains UPDATED_PHONE
        defaultSupplierShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSuppliersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phone does not contain DEFAULT_PHONE
        defaultSupplierShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the supplierList where phone does not contain UPDATED_PHONE
        defaultSupplierShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultSupplierShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the supplierList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultSupplierShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultSupplierShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the supplierList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultSupplierShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where addressLine1 is not null
        defaultSupplierShouldBeFound("addressLine1.specified=true");

        // Get all the supplierList where addressLine1 is null
        defaultSupplierShouldNotBeFound("addressLine1.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where addressLine1 contains DEFAULT_ADDRESS_LINE_1
        defaultSupplierShouldBeFound("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the supplierList where addressLine1 contains UPDATED_ADDRESS_LINE_1
        defaultSupplierShouldNotBeFound("addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where addressLine1 does not contain DEFAULT_ADDRESS_LINE_1
        defaultSupplierShouldNotBeFound("addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the supplierList where addressLine1 does not contain UPDATED_ADDRESS_LINE_1
        defaultSupplierShouldBeFound("addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultSupplierShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the supplierList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultSupplierShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultSupplierShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the supplierList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultSupplierShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where addressLine2 is not null
        defaultSupplierShouldBeFound("addressLine2.specified=true");

        // Get all the supplierList where addressLine2 is null
        defaultSupplierShouldNotBeFound("addressLine2.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where addressLine2 contains DEFAULT_ADDRESS_LINE_2
        defaultSupplierShouldBeFound("addressLine2.contains=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the supplierList where addressLine2 contains UPDATED_ADDRESS_LINE_2
        defaultSupplierShouldNotBeFound("addressLine2.contains=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where addressLine2 does not contain DEFAULT_ADDRESS_LINE_2
        defaultSupplierShouldNotBeFound("addressLine2.doesNotContain=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the supplierList where addressLine2 does not contain UPDATED_ADDRESS_LINE_2
        defaultSupplierShouldBeFound("addressLine2.doesNotContain=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllSuppliersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where city equals to DEFAULT_CITY
        defaultSupplierShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the supplierList where city equals to UPDATED_CITY
        defaultSupplierShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllSuppliersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where city in DEFAULT_CITY or UPDATED_CITY
        defaultSupplierShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the supplierList where city equals to UPDATED_CITY
        defaultSupplierShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllSuppliersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where city is not null
        defaultSupplierShouldBeFound("city.specified=true");

        // Get all the supplierList where city is null
        defaultSupplierShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByCityContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where city contains DEFAULT_CITY
        defaultSupplierShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the supplierList where city contains UPDATED_CITY
        defaultSupplierShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllSuppliersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where city does not contain DEFAULT_CITY
        defaultSupplierShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the supplierList where city does not contain UPDATED_CITY
        defaultSupplierShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllSuppliersByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where country equals to DEFAULT_COUNTRY
        defaultSupplierShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the supplierList where country equals to UPDATED_COUNTRY
        defaultSupplierShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSuppliersByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultSupplierShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the supplierList where country equals to UPDATED_COUNTRY
        defaultSupplierShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSuppliersByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where country is not null
        defaultSupplierShouldBeFound("country.specified=true");

        // Get all the supplierList where country is null
        defaultSupplierShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByCountryContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where country contains DEFAULT_COUNTRY
        defaultSupplierShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the supplierList where country contains UPDATED_COUNTRY
        defaultSupplierShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSuppliersByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where country does not contain DEFAULT_COUNTRY
        defaultSupplierShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the supplierList where country does not contain UPDATED_COUNTRY
        defaultSupplierShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSuppliersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email equals to DEFAULT_EMAIL
        defaultSupplierShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the supplierList where email equals to UPDATED_EMAIL
        defaultSupplierShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSuppliersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultSupplierShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the supplierList where email equals to UPDATED_EMAIL
        defaultSupplierShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSuppliersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email is not null
        defaultSupplierShouldBeFound("email.specified=true");

        // Get all the supplierList where email is null
        defaultSupplierShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByEmailContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email contains DEFAULT_EMAIL
        defaultSupplierShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the supplierList where email contains UPDATED_EMAIL
        defaultSupplierShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSuppliersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email does not contain DEFAULT_EMAIL
        defaultSupplierShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the supplierList where email does not contain UPDATED_EMAIL
        defaultSupplierShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSuppliersByTransactionIDIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where transactionID equals to DEFAULT_TRANSACTION_ID
        defaultSupplierShouldBeFound("transactionID.equals=" + DEFAULT_TRANSACTION_ID);

        // Get all the supplierList where transactionID equals to UPDATED_TRANSACTION_ID
        defaultSupplierShouldNotBeFound("transactionID.equals=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllSuppliersByTransactionIDIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where transactionID in DEFAULT_TRANSACTION_ID or UPDATED_TRANSACTION_ID
        defaultSupplierShouldBeFound("transactionID.in=" + DEFAULT_TRANSACTION_ID + "," + UPDATED_TRANSACTION_ID);

        // Get all the supplierList where transactionID equals to UPDATED_TRANSACTION_ID
        defaultSupplierShouldNotBeFound("transactionID.in=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllSuppliersByTransactionIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where transactionID is not null
        defaultSupplierShouldBeFound("transactionID.specified=true");

        // Get all the supplierList where transactionID is null
        defaultSupplierShouldNotBeFound("transactionID.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByTransactionIDContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where transactionID contains DEFAULT_TRANSACTION_ID
        defaultSupplierShouldBeFound("transactionID.contains=" + DEFAULT_TRANSACTION_ID);

        // Get all the supplierList where transactionID contains UPDATED_TRANSACTION_ID
        defaultSupplierShouldNotBeFound("transactionID.contains=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllSuppliersByTransactionIDNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where transactionID does not contain DEFAULT_TRANSACTION_ID
        defaultSupplierShouldNotBeFound("transactionID.doesNotContain=" + DEFAULT_TRANSACTION_ID);

        // Get all the supplierList where transactionID does not contain UPDATED_TRANSACTION_ID
        defaultSupplierShouldBeFound("transactionID.doesNotContain=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllSuppliersByLocationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where locationCode equals to DEFAULT_LOCATION_CODE
        defaultSupplierShouldBeFound("locationCode.equals=" + DEFAULT_LOCATION_CODE);

        // Get all the supplierList where locationCode equals to UPDATED_LOCATION_CODE
        defaultSupplierShouldNotBeFound("locationCode.equals=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllSuppliersByLocationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where locationCode in DEFAULT_LOCATION_CODE or UPDATED_LOCATION_CODE
        defaultSupplierShouldBeFound("locationCode.in=" + DEFAULT_LOCATION_CODE + "," + UPDATED_LOCATION_CODE);

        // Get all the supplierList where locationCode equals to UPDATED_LOCATION_CODE
        defaultSupplierShouldNotBeFound("locationCode.in=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllSuppliersByLocationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where locationCode is not null
        defaultSupplierShouldBeFound("locationCode.specified=true");

        // Get all the supplierList where locationCode is null
        defaultSupplierShouldNotBeFound("locationCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByLocationCodeContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where locationCode contains DEFAULT_LOCATION_CODE
        defaultSupplierShouldBeFound("locationCode.contains=" + DEFAULT_LOCATION_CODE);

        // Get all the supplierList where locationCode contains UPDATED_LOCATION_CODE
        defaultSupplierShouldNotBeFound("locationCode.contains=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllSuppliersByLocationCodeNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where locationCode does not contain DEFAULT_LOCATION_CODE
        defaultSupplierShouldNotBeFound("locationCode.doesNotContain=" + DEFAULT_LOCATION_CODE);

        // Get all the supplierList where locationCode does not contain UPDATED_LOCATION_CODE
        defaultSupplierShouldBeFound("locationCode.doesNotContain=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllSuppliersByTenantCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where tenantCode equals to DEFAULT_TENANT_CODE
        defaultSupplierShouldBeFound("tenantCode.equals=" + DEFAULT_TENANT_CODE);

        // Get all the supplierList where tenantCode equals to UPDATED_TENANT_CODE
        defaultSupplierShouldNotBeFound("tenantCode.equals=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllSuppliersByTenantCodeIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where tenantCode in DEFAULT_TENANT_CODE or UPDATED_TENANT_CODE
        defaultSupplierShouldBeFound("tenantCode.in=" + DEFAULT_TENANT_CODE + "," + UPDATED_TENANT_CODE);

        // Get all the supplierList where tenantCode equals to UPDATED_TENANT_CODE
        defaultSupplierShouldNotBeFound("tenantCode.in=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllSuppliersByTenantCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where tenantCode is not null
        defaultSupplierShouldBeFound("tenantCode.specified=true");

        // Get all the supplierList where tenantCode is null
        defaultSupplierShouldNotBeFound("tenantCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByTenantCodeContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where tenantCode contains DEFAULT_TENANT_CODE
        defaultSupplierShouldBeFound("tenantCode.contains=" + DEFAULT_TENANT_CODE);

        // Get all the supplierList where tenantCode contains UPDATED_TENANT_CODE
        defaultSupplierShouldNotBeFound("tenantCode.contains=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllSuppliersByTenantCodeNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where tenantCode does not contain DEFAULT_TENANT_CODE
        defaultSupplierShouldNotBeFound("tenantCode.doesNotContain=" + DEFAULT_TENANT_CODE);

        // Get all the supplierList where tenantCode does not contain UPDATED_TENANT_CODE
        defaultSupplierShouldBeFound("tenantCode.doesNotContain=" + UPDATED_TENANT_CODE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierShouldBeFound(String filter) throws Exception {
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].creditLimit").value(hasItem(sameNumber(DEFAULT_CREDIT_LIMIT))))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].transactionID").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].tenantCode").value(hasItem(DEFAULT_TENANT_CODE)));

        // Check, that the count call also returns 1
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierShouldNotBeFound(String filter) throws Exception {
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplier() throws Exception {
        // Get the supplier
        restSupplierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Update the supplier
        Supplier updatedSupplier = supplierRepository.findById(supplier.getId()).get();
        // Disconnect from session so that the updates on updatedSupplier are not directly saved in db
        em.detach(updatedSupplier);
        updatedSupplier
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .rating(UPDATED_RATING)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .email(UPDATED_EMAIL)
            .transactionID(UPDATED_TRANSACTION_ID)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);

        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSupplier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSupplier))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSupplier.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupplier.getCreditLimit()).isEqualByComparingTo(UPDATED_CREDIT_LIMIT);
        assertThat(testSupplier.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testSupplier.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testSupplier.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSupplier.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testSupplier.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testSupplier.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testSupplier.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testSupplier.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSupplier.getTransactionID()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testSupplier.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testSupplier.getTenantCode()).isEqualTo(UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void putNonExistingSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierWithPatch() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Update the supplier using partial update
        Supplier partialUpdatedSupplier = new Supplier();
        partialUpdatedSupplier.setId(supplier.getId());

        partialUpdatedSupplier
            .name(UPDATED_NAME)
            .rating(UPDATED_RATING)
            .isActive(UPDATED_IS_ACTIVE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .email(UPDATED_EMAIL);

        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSupplier))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSupplier.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupplier.getCreditLimit()).isEqualByComparingTo(DEFAULT_CREDIT_LIMIT);
        assertThat(testSupplier.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testSupplier.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testSupplier.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSupplier.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testSupplier.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testSupplier.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testSupplier.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testSupplier.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSupplier.getTransactionID()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testSupplier.getLocationCode()).isEqualTo(DEFAULT_LOCATION_CODE);
        assertThat(testSupplier.getTenantCode()).isEqualTo(DEFAULT_TENANT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateSupplierWithPatch() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Update the supplier using partial update
        Supplier partialUpdatedSupplier = new Supplier();
        partialUpdatedSupplier.setId(supplier.getId());

        partialUpdatedSupplier
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .rating(UPDATED_RATING)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .email(UPDATED_EMAIL)
            .transactionID(UPDATED_TRANSACTION_ID)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);

        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSupplier))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSupplier.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupplier.getCreditLimit()).isEqualByComparingTo(UPDATED_CREDIT_LIMIT);
        assertThat(testSupplier.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testSupplier.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testSupplier.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSupplier.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testSupplier.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testSupplier.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testSupplier.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testSupplier.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSupplier.getTransactionID()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testSupplier.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testSupplier.getTenantCode()).isEqualTo(UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeDelete = supplierRepository.findAll().size();

        // Delete the supplier
        restSupplierMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
