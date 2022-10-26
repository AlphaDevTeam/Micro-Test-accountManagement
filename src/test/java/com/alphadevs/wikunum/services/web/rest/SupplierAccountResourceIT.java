package com.alphadevs.wikunum.services.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.alphadevs.wikunum.services.IntegrationTest;
import com.alphadevs.wikunum.services.domain.Supplier;
import com.alphadevs.wikunum.services.domain.SupplierAccount;
import com.alphadevs.wikunum.services.repository.SupplierAccountRepository;
import com.alphadevs.wikunum.services.service.SupplierAccountService;
import com.alphadevs.wikunum.services.service.criteria.SupplierAccountCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SupplierAccountResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierAccountResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_TRANSACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRANSACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_CREDIT_AMOUNT = 1D;
    private static final Double UPDATED_CREDIT_AMOUNT = 2D;
    private static final Double SMALLER_CREDIT_AMOUNT = 1D - 1D;

    private static final Double DEFAULT_DEBIT_AMOUNT = 1D;
    private static final Double UPDATED_DEBIT_AMOUNT = 2D;
    private static final Double SMALLER_DEBIT_AMOUNT = 1D - 1D;

    private static final Double DEFAULT_BALANCE_AMOUNT = 1D;
    private static final Double UPDATED_BALANCE_AMOUNT = 2D;
    private static final Double SMALLER_BALANCE_AMOUNT = 1D - 1D;

    private static final String DEFAULT_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TENANT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/supplier-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SupplierAccountRepository supplierAccountRepository;

    @Mock
    private SupplierAccountRepository supplierAccountRepositoryMock;

    @Mock
    private SupplierAccountService supplierAccountServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierAccountMockMvc;

    private SupplierAccount supplierAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierAccount createEntity(EntityManager em) {
        SupplierAccount supplierAccount = new SupplierAccount()
            .description(DEFAULT_DESCRIPTION)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .creditAmount(DEFAULT_CREDIT_AMOUNT)
            .debitAmount(DEFAULT_DEBIT_AMOUNT)
            .balanceAmount(DEFAULT_BALANCE_AMOUNT)
            .transactionID(DEFAULT_TRANSACTION_ID)
            .locationCode(DEFAULT_LOCATION_CODE)
            .tenantCode(DEFAULT_TENANT_CODE);
        return supplierAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierAccount createUpdatedEntity(EntityManager em) {
        SupplierAccount supplierAccount = new SupplierAccount()
            .description(UPDATED_DESCRIPTION)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .balanceAmount(UPDATED_BALANCE_AMOUNT)
            .transactionID(UPDATED_TRANSACTION_ID)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);
        return supplierAccount;
    }

    @BeforeEach
    public void initTest() {
        supplierAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createSupplierAccount() throws Exception {
        int databaseSizeBeforeCreate = supplierAccountRepository.findAll().size();
        // Create the SupplierAccount
        restSupplierAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplierAccount))
            )
            .andExpect(status().isCreated());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeCreate + 1);
        SupplierAccount testSupplierAccount = supplierAccountList.get(supplierAccountList.size() - 1);
        assertThat(testSupplierAccount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSupplierAccount.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testSupplierAccount.getCreditAmount()).isEqualTo(DEFAULT_CREDIT_AMOUNT);
        assertThat(testSupplierAccount.getDebitAmount()).isEqualTo(DEFAULT_DEBIT_AMOUNT);
        assertThat(testSupplierAccount.getBalanceAmount()).isEqualTo(DEFAULT_BALANCE_AMOUNT);
        assertThat(testSupplierAccount.getTransactionID()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testSupplierAccount.getLocationCode()).isEqualTo(DEFAULT_LOCATION_CODE);
        assertThat(testSupplierAccount.getTenantCode()).isEqualTo(DEFAULT_TENANT_CODE);
    }

    @Test
    @Transactional
    void createSupplierAccountWithExistingId() throws Exception {
        // Create the SupplierAccount with an existing ID
        supplierAccount.setId(1L);

        int databaseSizeBeforeCreate = supplierAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplierAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTransactionIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierAccountRepository.findAll().size();
        // set the field null
        supplierAccount.setTransactionID(null);

        // Create the SupplierAccount, which fails.

        restSupplierAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplierAccount))
            )
            .andExpect(status().isBadRequest());

        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierAccountRepository.findAll().size();
        // set the field null
        supplierAccount.setLocationCode(null);

        // Create the SupplierAccount, which fails.

        restSupplierAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplierAccount))
            )
            .andExpect(status().isBadRequest());

        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTenantCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierAccountRepository.findAll().size();
        // set the field null
        supplierAccount.setTenantCode(null);

        // Create the SupplierAccount, which fails.

        restSupplierAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplierAccount))
            )
            .andExpect(status().isBadRequest());

        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSupplierAccounts() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList
        restSupplierAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(DEFAULT_CREDIT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].debitAmount").value(hasItem(DEFAULT_DEBIT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].balanceAmount").value(hasItem(DEFAULT_BALANCE_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionID").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].tenantCode").value(hasItem(DEFAULT_TENANT_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierAccountsWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierAccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierAccountMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierAccountServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierAccountsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierAccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierAccountMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierAccountRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplierAccount() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get the supplierAccount
        restSupplierAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, supplierAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplierAccount.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.creditAmount").value(DEFAULT_CREDIT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.debitAmount").value(DEFAULT_DEBIT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.balanceAmount").value(DEFAULT_BALANCE_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.transactionID").value(DEFAULT_TRANSACTION_ID))
            .andExpect(jsonPath("$.locationCode").value(DEFAULT_LOCATION_CODE))
            .andExpect(jsonPath("$.tenantCode").value(DEFAULT_TENANT_CODE));
    }

    @Test
    @Transactional
    void getSupplierAccountsByIdFiltering() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        Long id = supplierAccount.getId();

        defaultSupplierAccountShouldBeFound("id.equals=" + id);
        defaultSupplierAccountShouldNotBeFound("id.notEquals=" + id);

        defaultSupplierAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSupplierAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultSupplierAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSupplierAccountShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where description equals to DEFAULT_DESCRIPTION
        defaultSupplierAccountShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the supplierAccountList where description equals to UPDATED_DESCRIPTION
        defaultSupplierAccountShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSupplierAccountShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the supplierAccountList where description equals to UPDATED_DESCRIPTION
        defaultSupplierAccountShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where description is not null
        defaultSupplierAccountShouldBeFound("description.specified=true");

        // Get all the supplierAccountList where description is null
        defaultSupplierAccountShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where description contains DEFAULT_DESCRIPTION
        defaultSupplierAccountShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the supplierAccountList where description contains UPDATED_DESCRIPTION
        defaultSupplierAccountShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where description does not contain DEFAULT_DESCRIPTION
        defaultSupplierAccountShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the supplierAccountList where description does not contain UPDATED_DESCRIPTION
        defaultSupplierAccountShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultSupplierAccountShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the supplierAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultSupplierAccountShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultSupplierAccountShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the supplierAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultSupplierAccountShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDate is not null
        defaultSupplierAccountShouldBeFound("transactionDate.specified=true");

        // Get all the supplierAccountList where transactionDate is null
        defaultSupplierAccountShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByCreditAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where creditAmount equals to DEFAULT_CREDIT_AMOUNT
        defaultSupplierAccountShouldBeFound("creditAmount.equals=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the supplierAccountList where creditAmount equals to UPDATED_CREDIT_AMOUNT
        defaultSupplierAccountShouldNotBeFound("creditAmount.equals=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByCreditAmountIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where creditAmount in DEFAULT_CREDIT_AMOUNT or UPDATED_CREDIT_AMOUNT
        defaultSupplierAccountShouldBeFound("creditAmount.in=" + DEFAULT_CREDIT_AMOUNT + "," + UPDATED_CREDIT_AMOUNT);

        // Get all the supplierAccountList where creditAmount equals to UPDATED_CREDIT_AMOUNT
        defaultSupplierAccountShouldNotBeFound("creditAmount.in=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByCreditAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where creditAmount is not null
        defaultSupplierAccountShouldBeFound("creditAmount.specified=true");

        // Get all the supplierAccountList where creditAmount is null
        defaultSupplierAccountShouldNotBeFound("creditAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByCreditAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where creditAmount is greater than or equal to DEFAULT_CREDIT_AMOUNT
        defaultSupplierAccountShouldBeFound("creditAmount.greaterThanOrEqual=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the supplierAccountList where creditAmount is greater than or equal to UPDATED_CREDIT_AMOUNT
        defaultSupplierAccountShouldNotBeFound("creditAmount.greaterThanOrEqual=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByCreditAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where creditAmount is less than or equal to DEFAULT_CREDIT_AMOUNT
        defaultSupplierAccountShouldBeFound("creditAmount.lessThanOrEqual=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the supplierAccountList where creditAmount is less than or equal to SMALLER_CREDIT_AMOUNT
        defaultSupplierAccountShouldNotBeFound("creditAmount.lessThanOrEqual=" + SMALLER_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByCreditAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where creditAmount is less than DEFAULT_CREDIT_AMOUNT
        defaultSupplierAccountShouldNotBeFound("creditAmount.lessThan=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the supplierAccountList where creditAmount is less than UPDATED_CREDIT_AMOUNT
        defaultSupplierAccountShouldBeFound("creditAmount.lessThan=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByCreditAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where creditAmount is greater than DEFAULT_CREDIT_AMOUNT
        defaultSupplierAccountShouldNotBeFound("creditAmount.greaterThan=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the supplierAccountList where creditAmount is greater than SMALLER_CREDIT_AMOUNT
        defaultSupplierAccountShouldBeFound("creditAmount.greaterThan=" + SMALLER_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByDebitAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where debitAmount equals to DEFAULT_DEBIT_AMOUNT
        defaultSupplierAccountShouldBeFound("debitAmount.equals=" + DEFAULT_DEBIT_AMOUNT);

        // Get all the supplierAccountList where debitAmount equals to UPDATED_DEBIT_AMOUNT
        defaultSupplierAccountShouldNotBeFound("debitAmount.equals=" + UPDATED_DEBIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByDebitAmountIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where debitAmount in DEFAULT_DEBIT_AMOUNT or UPDATED_DEBIT_AMOUNT
        defaultSupplierAccountShouldBeFound("debitAmount.in=" + DEFAULT_DEBIT_AMOUNT + "," + UPDATED_DEBIT_AMOUNT);

        // Get all the supplierAccountList where debitAmount equals to UPDATED_DEBIT_AMOUNT
        defaultSupplierAccountShouldNotBeFound("debitAmount.in=" + UPDATED_DEBIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByDebitAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where debitAmount is not null
        defaultSupplierAccountShouldBeFound("debitAmount.specified=true");

        // Get all the supplierAccountList where debitAmount is null
        defaultSupplierAccountShouldNotBeFound("debitAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByDebitAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where debitAmount is greater than or equal to DEFAULT_DEBIT_AMOUNT
        defaultSupplierAccountShouldBeFound("debitAmount.greaterThanOrEqual=" + DEFAULT_DEBIT_AMOUNT);

        // Get all the supplierAccountList where debitAmount is greater than or equal to UPDATED_DEBIT_AMOUNT
        defaultSupplierAccountShouldNotBeFound("debitAmount.greaterThanOrEqual=" + UPDATED_DEBIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByDebitAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where debitAmount is less than or equal to DEFAULT_DEBIT_AMOUNT
        defaultSupplierAccountShouldBeFound("debitAmount.lessThanOrEqual=" + DEFAULT_DEBIT_AMOUNT);

        // Get all the supplierAccountList where debitAmount is less than or equal to SMALLER_DEBIT_AMOUNT
        defaultSupplierAccountShouldNotBeFound("debitAmount.lessThanOrEqual=" + SMALLER_DEBIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByDebitAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where debitAmount is less than DEFAULT_DEBIT_AMOUNT
        defaultSupplierAccountShouldNotBeFound("debitAmount.lessThan=" + DEFAULT_DEBIT_AMOUNT);

        // Get all the supplierAccountList where debitAmount is less than UPDATED_DEBIT_AMOUNT
        defaultSupplierAccountShouldBeFound("debitAmount.lessThan=" + UPDATED_DEBIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByDebitAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where debitAmount is greater than DEFAULT_DEBIT_AMOUNT
        defaultSupplierAccountShouldNotBeFound("debitAmount.greaterThan=" + DEFAULT_DEBIT_AMOUNT);

        // Get all the supplierAccountList where debitAmount is greater than SMALLER_DEBIT_AMOUNT
        defaultSupplierAccountShouldBeFound("debitAmount.greaterThan=" + SMALLER_DEBIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByBalanceAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where balanceAmount equals to DEFAULT_BALANCE_AMOUNT
        defaultSupplierAccountShouldBeFound("balanceAmount.equals=" + DEFAULT_BALANCE_AMOUNT);

        // Get all the supplierAccountList where balanceAmount equals to UPDATED_BALANCE_AMOUNT
        defaultSupplierAccountShouldNotBeFound("balanceAmount.equals=" + UPDATED_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByBalanceAmountIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where balanceAmount in DEFAULT_BALANCE_AMOUNT or UPDATED_BALANCE_AMOUNT
        defaultSupplierAccountShouldBeFound("balanceAmount.in=" + DEFAULT_BALANCE_AMOUNT + "," + UPDATED_BALANCE_AMOUNT);

        // Get all the supplierAccountList where balanceAmount equals to UPDATED_BALANCE_AMOUNT
        defaultSupplierAccountShouldNotBeFound("balanceAmount.in=" + UPDATED_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByBalanceAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where balanceAmount is not null
        defaultSupplierAccountShouldBeFound("balanceAmount.specified=true");

        // Get all the supplierAccountList where balanceAmount is null
        defaultSupplierAccountShouldNotBeFound("balanceAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByBalanceAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where balanceAmount is greater than or equal to DEFAULT_BALANCE_AMOUNT
        defaultSupplierAccountShouldBeFound("balanceAmount.greaterThanOrEqual=" + DEFAULT_BALANCE_AMOUNT);

        // Get all the supplierAccountList where balanceAmount is greater than or equal to UPDATED_BALANCE_AMOUNT
        defaultSupplierAccountShouldNotBeFound("balanceAmount.greaterThanOrEqual=" + UPDATED_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByBalanceAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where balanceAmount is less than or equal to DEFAULT_BALANCE_AMOUNT
        defaultSupplierAccountShouldBeFound("balanceAmount.lessThanOrEqual=" + DEFAULT_BALANCE_AMOUNT);

        // Get all the supplierAccountList where balanceAmount is less than or equal to SMALLER_BALANCE_AMOUNT
        defaultSupplierAccountShouldNotBeFound("balanceAmount.lessThanOrEqual=" + SMALLER_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByBalanceAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where balanceAmount is less than DEFAULT_BALANCE_AMOUNT
        defaultSupplierAccountShouldNotBeFound("balanceAmount.lessThan=" + DEFAULT_BALANCE_AMOUNT);

        // Get all the supplierAccountList where balanceAmount is less than UPDATED_BALANCE_AMOUNT
        defaultSupplierAccountShouldBeFound("balanceAmount.lessThan=" + UPDATED_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByBalanceAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where balanceAmount is greater than DEFAULT_BALANCE_AMOUNT
        defaultSupplierAccountShouldNotBeFound("balanceAmount.greaterThan=" + DEFAULT_BALANCE_AMOUNT);

        // Get all the supplierAccountList where balanceAmount is greater than SMALLER_BALANCE_AMOUNT
        defaultSupplierAccountShouldBeFound("balanceAmount.greaterThan=" + SMALLER_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByTransactionIDIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionID equals to DEFAULT_TRANSACTION_ID
        defaultSupplierAccountShouldBeFound("transactionID.equals=" + DEFAULT_TRANSACTION_ID);

        // Get all the supplierAccountList where transactionID equals to UPDATED_TRANSACTION_ID
        defaultSupplierAccountShouldNotBeFound("transactionID.equals=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByTransactionIDIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionID in DEFAULT_TRANSACTION_ID or UPDATED_TRANSACTION_ID
        defaultSupplierAccountShouldBeFound("transactionID.in=" + DEFAULT_TRANSACTION_ID + "," + UPDATED_TRANSACTION_ID);

        // Get all the supplierAccountList where transactionID equals to UPDATED_TRANSACTION_ID
        defaultSupplierAccountShouldNotBeFound("transactionID.in=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByTransactionIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionID is not null
        defaultSupplierAccountShouldBeFound("transactionID.specified=true");

        // Get all the supplierAccountList where transactionID is null
        defaultSupplierAccountShouldNotBeFound("transactionID.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByTransactionIDContainsSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionID contains DEFAULT_TRANSACTION_ID
        defaultSupplierAccountShouldBeFound("transactionID.contains=" + DEFAULT_TRANSACTION_ID);

        // Get all the supplierAccountList where transactionID contains UPDATED_TRANSACTION_ID
        defaultSupplierAccountShouldNotBeFound("transactionID.contains=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByTransactionIDNotContainsSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionID does not contain DEFAULT_TRANSACTION_ID
        defaultSupplierAccountShouldNotBeFound("transactionID.doesNotContain=" + DEFAULT_TRANSACTION_ID);

        // Get all the supplierAccountList where transactionID does not contain UPDATED_TRANSACTION_ID
        defaultSupplierAccountShouldBeFound("transactionID.doesNotContain=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByLocationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where locationCode equals to DEFAULT_LOCATION_CODE
        defaultSupplierAccountShouldBeFound("locationCode.equals=" + DEFAULT_LOCATION_CODE);

        // Get all the supplierAccountList where locationCode equals to UPDATED_LOCATION_CODE
        defaultSupplierAccountShouldNotBeFound("locationCode.equals=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByLocationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where locationCode in DEFAULT_LOCATION_CODE or UPDATED_LOCATION_CODE
        defaultSupplierAccountShouldBeFound("locationCode.in=" + DEFAULT_LOCATION_CODE + "," + UPDATED_LOCATION_CODE);

        // Get all the supplierAccountList where locationCode equals to UPDATED_LOCATION_CODE
        defaultSupplierAccountShouldNotBeFound("locationCode.in=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByLocationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where locationCode is not null
        defaultSupplierAccountShouldBeFound("locationCode.specified=true");

        // Get all the supplierAccountList where locationCode is null
        defaultSupplierAccountShouldNotBeFound("locationCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByLocationCodeContainsSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where locationCode contains DEFAULT_LOCATION_CODE
        defaultSupplierAccountShouldBeFound("locationCode.contains=" + DEFAULT_LOCATION_CODE);

        // Get all the supplierAccountList where locationCode contains UPDATED_LOCATION_CODE
        defaultSupplierAccountShouldNotBeFound("locationCode.contains=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByLocationCodeNotContainsSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where locationCode does not contain DEFAULT_LOCATION_CODE
        defaultSupplierAccountShouldNotBeFound("locationCode.doesNotContain=" + DEFAULT_LOCATION_CODE);

        // Get all the supplierAccountList where locationCode does not contain UPDATED_LOCATION_CODE
        defaultSupplierAccountShouldBeFound("locationCode.doesNotContain=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByTenantCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where tenantCode equals to DEFAULT_TENANT_CODE
        defaultSupplierAccountShouldBeFound("tenantCode.equals=" + DEFAULT_TENANT_CODE);

        // Get all the supplierAccountList where tenantCode equals to UPDATED_TENANT_CODE
        defaultSupplierAccountShouldNotBeFound("tenantCode.equals=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByTenantCodeIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where tenantCode in DEFAULT_TENANT_CODE or UPDATED_TENANT_CODE
        defaultSupplierAccountShouldBeFound("tenantCode.in=" + DEFAULT_TENANT_CODE + "," + UPDATED_TENANT_CODE);

        // Get all the supplierAccountList where tenantCode equals to UPDATED_TENANT_CODE
        defaultSupplierAccountShouldNotBeFound("tenantCode.in=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByTenantCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where tenantCode is not null
        defaultSupplierAccountShouldBeFound("tenantCode.specified=true");

        // Get all the supplierAccountList where tenantCode is null
        defaultSupplierAccountShouldNotBeFound("tenantCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByTenantCodeContainsSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where tenantCode contains DEFAULT_TENANT_CODE
        defaultSupplierAccountShouldBeFound("tenantCode.contains=" + DEFAULT_TENANT_CODE);

        // Get all the supplierAccountList where tenantCode contains UPDATED_TENANT_CODE
        defaultSupplierAccountShouldNotBeFound("tenantCode.contains=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsByTenantCodeNotContainsSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where tenantCode does not contain DEFAULT_TENANT_CODE
        defaultSupplierAccountShouldNotBeFound("tenantCode.doesNotContain=" + DEFAULT_TENANT_CODE);

        // Get all the supplierAccountList where tenantCode does not contain UPDATED_TENANT_CODE
        defaultSupplierAccountShouldBeFound("tenantCode.doesNotContain=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountsBySupplierIsEqualToSomething() throws Exception {
        Supplier supplier;
        if (TestUtil.findAll(em, Supplier.class).isEmpty()) {
            supplierAccountRepository.saveAndFlush(supplierAccount);
            supplier = SupplierResourceIT.createEntity(em);
        } else {
            supplier = TestUtil.findAll(em, Supplier.class).get(0);
        }
        em.persist(supplier);
        em.flush();
        supplierAccount.setSupplier(supplier);
        supplierAccountRepository.saveAndFlush(supplierAccount);
        Long supplierId = supplier.getId();

        // Get all the supplierAccountList where supplier equals to supplierId
        defaultSupplierAccountShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the supplierAccountList where supplier equals to (supplierId + 1)
        defaultSupplierAccountShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierAccountShouldBeFound(String filter) throws Exception {
        restSupplierAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(DEFAULT_CREDIT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].debitAmount").value(hasItem(DEFAULT_DEBIT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].balanceAmount").value(hasItem(DEFAULT_BALANCE_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionID").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].tenantCode").value(hasItem(DEFAULT_TENANT_CODE)));

        // Check, that the count call also returns 1
        restSupplierAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierAccountShouldNotBeFound(String filter) throws Exception {
        restSupplierAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplierAccount() throws Exception {
        // Get the supplierAccount
        restSupplierAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplierAccount() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        int databaseSizeBeforeUpdate = supplierAccountRepository.findAll().size();

        // Update the supplierAccount
        SupplierAccount updatedSupplierAccount = supplierAccountRepository.findById(supplierAccount.getId()).get();
        // Disconnect from session so that the updates on updatedSupplierAccount are not directly saved in db
        em.detach(updatedSupplierAccount);
        updatedSupplierAccount
            .description(UPDATED_DESCRIPTION)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .balanceAmount(UPDATED_BALANCE_AMOUNT)
            .transactionID(UPDATED_TRANSACTION_ID)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);

        restSupplierAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSupplierAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSupplierAccount))
            )
            .andExpect(status().isOk());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeUpdate);
        SupplierAccount testSupplierAccount = supplierAccountList.get(supplierAccountList.size() - 1);
        assertThat(testSupplierAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSupplierAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testSupplierAccount.getCreditAmount()).isEqualTo(UPDATED_CREDIT_AMOUNT);
        assertThat(testSupplierAccount.getDebitAmount()).isEqualTo(UPDATED_DEBIT_AMOUNT);
        assertThat(testSupplierAccount.getBalanceAmount()).isEqualTo(UPDATED_BALANCE_AMOUNT);
        assertThat(testSupplierAccount.getTransactionID()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testSupplierAccount.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testSupplierAccount.getTenantCode()).isEqualTo(UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void putNonExistingSupplierAccount() throws Exception {
        int databaseSizeBeforeUpdate = supplierAccountRepository.findAll().size();
        supplierAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplierAccount() throws Exception {
        int databaseSizeBeforeUpdate = supplierAccountRepository.findAll().size();
        supplierAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplierAccount() throws Exception {
        int databaseSizeBeforeUpdate = supplierAccountRepository.findAll().size();
        supplierAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierAccountMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplierAccount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierAccountWithPatch() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        int databaseSizeBeforeUpdate = supplierAccountRepository.findAll().size();

        // Update the supplierAccount using partial update
        SupplierAccount partialUpdatedSupplierAccount = new SupplierAccount();
        partialUpdatedSupplierAccount.setId(supplierAccount.getId());

        partialUpdatedSupplierAccount.description(UPDATED_DESCRIPTION).locationCode(UPDATED_LOCATION_CODE);

        restSupplierAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSupplierAccount))
            )
            .andExpect(status().isOk());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeUpdate);
        SupplierAccount testSupplierAccount = supplierAccountList.get(supplierAccountList.size() - 1);
        assertThat(testSupplierAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSupplierAccount.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testSupplierAccount.getCreditAmount()).isEqualTo(DEFAULT_CREDIT_AMOUNT);
        assertThat(testSupplierAccount.getDebitAmount()).isEqualTo(DEFAULT_DEBIT_AMOUNT);
        assertThat(testSupplierAccount.getBalanceAmount()).isEqualTo(DEFAULT_BALANCE_AMOUNT);
        assertThat(testSupplierAccount.getTransactionID()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testSupplierAccount.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testSupplierAccount.getTenantCode()).isEqualTo(DEFAULT_TENANT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateSupplierAccountWithPatch() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        int databaseSizeBeforeUpdate = supplierAccountRepository.findAll().size();

        // Update the supplierAccount using partial update
        SupplierAccount partialUpdatedSupplierAccount = new SupplierAccount();
        partialUpdatedSupplierAccount.setId(supplierAccount.getId());

        partialUpdatedSupplierAccount
            .description(UPDATED_DESCRIPTION)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .balanceAmount(UPDATED_BALANCE_AMOUNT)
            .transactionID(UPDATED_TRANSACTION_ID)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);

        restSupplierAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSupplierAccount))
            )
            .andExpect(status().isOk());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeUpdate);
        SupplierAccount testSupplierAccount = supplierAccountList.get(supplierAccountList.size() - 1);
        assertThat(testSupplierAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSupplierAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testSupplierAccount.getCreditAmount()).isEqualTo(UPDATED_CREDIT_AMOUNT);
        assertThat(testSupplierAccount.getDebitAmount()).isEqualTo(UPDATED_DEBIT_AMOUNT);
        assertThat(testSupplierAccount.getBalanceAmount()).isEqualTo(UPDATED_BALANCE_AMOUNT);
        assertThat(testSupplierAccount.getTransactionID()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testSupplierAccount.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testSupplierAccount.getTenantCode()).isEqualTo(UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingSupplierAccount() throws Exception {
        int databaseSizeBeforeUpdate = supplierAccountRepository.findAll().size();
        supplierAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplierAccount() throws Exception {
        int databaseSizeBeforeUpdate = supplierAccountRepository.findAll().size();
        supplierAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplierAccount() throws Exception {
        int databaseSizeBeforeUpdate = supplierAccountRepository.findAll().size();
        supplierAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierAccountMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplierAccount() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        int databaseSizeBeforeDelete = supplierAccountRepository.findAll().size();

        // Delete the supplierAccount
        restSupplierAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplierAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
