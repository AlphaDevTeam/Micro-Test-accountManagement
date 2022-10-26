package com.alphadevs.wikunum.services.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.alphadevs.wikunum.services.IntegrationTest;
import com.alphadevs.wikunum.services.domain.Customer;
import com.alphadevs.wikunum.services.domain.CustomerAccount;
import com.alphadevs.wikunum.services.repository.CustomerAccountRepository;
import com.alphadevs.wikunum.services.service.CustomerAccountService;
import com.alphadevs.wikunum.services.service.criteria.CustomerAccountCriteria;
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
 * Integration tests for the {@link CustomerAccountResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustomerAccountResourceIT {

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

    private static final String ENTITY_API_URL = "/api/customer-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @Mock
    private CustomerAccountRepository customerAccountRepositoryMock;

    @Mock
    private CustomerAccountService customerAccountServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerAccountMockMvc;

    private CustomerAccount customerAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerAccount createEntity(EntityManager em) {
        CustomerAccount customerAccount = new CustomerAccount()
            .description(DEFAULT_DESCRIPTION)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .creditAmount(DEFAULT_CREDIT_AMOUNT)
            .debitAmount(DEFAULT_DEBIT_AMOUNT)
            .balanceAmount(DEFAULT_BALANCE_AMOUNT)
            .transactionID(DEFAULT_TRANSACTION_ID)
            .locationCode(DEFAULT_LOCATION_CODE)
            .tenantCode(DEFAULT_TENANT_CODE);
        return customerAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerAccount createUpdatedEntity(EntityManager em) {
        CustomerAccount customerAccount = new CustomerAccount()
            .description(UPDATED_DESCRIPTION)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .balanceAmount(UPDATED_BALANCE_AMOUNT)
            .transactionID(UPDATED_TRANSACTION_ID)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);
        return customerAccount;
    }

    @BeforeEach
    public void initTest() {
        customerAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerAccount() throws Exception {
        int databaseSizeBeforeCreate = customerAccountRepository.findAll().size();
        // Create the CustomerAccount
        restCustomerAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerAccount))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerAccount testCustomerAccount = customerAccountList.get(customerAccountList.size() - 1);
        assertThat(testCustomerAccount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCustomerAccount.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testCustomerAccount.getCreditAmount()).isEqualTo(DEFAULT_CREDIT_AMOUNT);
        assertThat(testCustomerAccount.getDebitAmount()).isEqualTo(DEFAULT_DEBIT_AMOUNT);
        assertThat(testCustomerAccount.getBalanceAmount()).isEqualTo(DEFAULT_BALANCE_AMOUNT);
        assertThat(testCustomerAccount.getTransactionID()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testCustomerAccount.getLocationCode()).isEqualTo(DEFAULT_LOCATION_CODE);
        assertThat(testCustomerAccount.getTenantCode()).isEqualTo(DEFAULT_TENANT_CODE);
    }

    @Test
    @Transactional
    void createCustomerAccountWithExistingId() throws Exception {
        // Create the CustomerAccount with an existing ID
        customerAccount.setId(1L);

        int databaseSizeBeforeCreate = customerAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTransactionIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAccountRepository.findAll().size();
        // set the field null
        customerAccount.setTransactionID(null);

        // Create the CustomerAccount, which fails.

        restCustomerAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerAccount))
            )
            .andExpect(status().isBadRequest());

        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAccountRepository.findAll().size();
        // set the field null
        customerAccount.setLocationCode(null);

        // Create the CustomerAccount, which fails.

        restCustomerAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerAccount))
            )
            .andExpect(status().isBadRequest());

        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTenantCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAccountRepository.findAll().size();
        // set the field null
        customerAccount.setTenantCode(null);

        // Create the CustomerAccount, which fails.

        restCustomerAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerAccount))
            )
            .andExpect(status().isBadRequest());

        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerAccounts() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList
        restCustomerAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAccount.getId().intValue())))
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
    void getAllCustomerAccountsWithEagerRelationshipsIsEnabled() throws Exception {
        when(customerAccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerAccountMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(customerAccountServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomerAccountsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(customerAccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerAccountMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(customerAccountRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustomerAccount() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get the customerAccount
        restCustomerAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, customerAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerAccount.getId().intValue()))
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
    void getCustomerAccountsByIdFiltering() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        Long id = customerAccount.getId();

        defaultCustomerAccountShouldBeFound("id.equals=" + id);
        defaultCustomerAccountShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerAccountShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where description equals to DEFAULT_DESCRIPTION
        defaultCustomerAccountShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the customerAccountList where description equals to UPDATED_DESCRIPTION
        defaultCustomerAccountShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCustomerAccountShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the customerAccountList where description equals to UPDATED_DESCRIPTION
        defaultCustomerAccountShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where description is not null
        defaultCustomerAccountShouldBeFound("description.specified=true");

        // Get all the customerAccountList where description is null
        defaultCustomerAccountShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where description contains DEFAULT_DESCRIPTION
        defaultCustomerAccountShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the customerAccountList where description contains UPDATED_DESCRIPTION
        defaultCustomerAccountShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where description does not contain DEFAULT_DESCRIPTION
        defaultCustomerAccountShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the customerAccountList where description does not contain UPDATED_DESCRIPTION
        defaultCustomerAccountShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultCustomerAccountShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the customerAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCustomerAccountShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultCustomerAccountShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the customerAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCustomerAccountShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDate is not null
        defaultCustomerAccountShouldBeFound("transactionDate.specified=true");

        // Get all the customerAccountList where transactionDate is null
        defaultCustomerAccountShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByCreditAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where creditAmount equals to DEFAULT_CREDIT_AMOUNT
        defaultCustomerAccountShouldBeFound("creditAmount.equals=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the customerAccountList where creditAmount equals to UPDATED_CREDIT_AMOUNT
        defaultCustomerAccountShouldNotBeFound("creditAmount.equals=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByCreditAmountIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where creditAmount in DEFAULT_CREDIT_AMOUNT or UPDATED_CREDIT_AMOUNT
        defaultCustomerAccountShouldBeFound("creditAmount.in=" + DEFAULT_CREDIT_AMOUNT + "," + UPDATED_CREDIT_AMOUNT);

        // Get all the customerAccountList where creditAmount equals to UPDATED_CREDIT_AMOUNT
        defaultCustomerAccountShouldNotBeFound("creditAmount.in=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByCreditAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where creditAmount is not null
        defaultCustomerAccountShouldBeFound("creditAmount.specified=true");

        // Get all the customerAccountList where creditAmount is null
        defaultCustomerAccountShouldNotBeFound("creditAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByCreditAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where creditAmount is greater than or equal to DEFAULT_CREDIT_AMOUNT
        defaultCustomerAccountShouldBeFound("creditAmount.greaterThanOrEqual=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the customerAccountList where creditAmount is greater than or equal to UPDATED_CREDIT_AMOUNT
        defaultCustomerAccountShouldNotBeFound("creditAmount.greaterThanOrEqual=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByCreditAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where creditAmount is less than or equal to DEFAULT_CREDIT_AMOUNT
        defaultCustomerAccountShouldBeFound("creditAmount.lessThanOrEqual=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the customerAccountList where creditAmount is less than or equal to SMALLER_CREDIT_AMOUNT
        defaultCustomerAccountShouldNotBeFound("creditAmount.lessThanOrEqual=" + SMALLER_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByCreditAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where creditAmount is less than DEFAULT_CREDIT_AMOUNT
        defaultCustomerAccountShouldNotBeFound("creditAmount.lessThan=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the customerAccountList where creditAmount is less than UPDATED_CREDIT_AMOUNT
        defaultCustomerAccountShouldBeFound("creditAmount.lessThan=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByCreditAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where creditAmount is greater than DEFAULT_CREDIT_AMOUNT
        defaultCustomerAccountShouldNotBeFound("creditAmount.greaterThan=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the customerAccountList where creditAmount is greater than SMALLER_CREDIT_AMOUNT
        defaultCustomerAccountShouldBeFound("creditAmount.greaterThan=" + SMALLER_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByDebitAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where debitAmount equals to DEFAULT_DEBIT_AMOUNT
        defaultCustomerAccountShouldBeFound("debitAmount.equals=" + DEFAULT_DEBIT_AMOUNT);

        // Get all the customerAccountList where debitAmount equals to UPDATED_DEBIT_AMOUNT
        defaultCustomerAccountShouldNotBeFound("debitAmount.equals=" + UPDATED_DEBIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByDebitAmountIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where debitAmount in DEFAULT_DEBIT_AMOUNT or UPDATED_DEBIT_AMOUNT
        defaultCustomerAccountShouldBeFound("debitAmount.in=" + DEFAULT_DEBIT_AMOUNT + "," + UPDATED_DEBIT_AMOUNT);

        // Get all the customerAccountList where debitAmount equals to UPDATED_DEBIT_AMOUNT
        defaultCustomerAccountShouldNotBeFound("debitAmount.in=" + UPDATED_DEBIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByDebitAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where debitAmount is not null
        defaultCustomerAccountShouldBeFound("debitAmount.specified=true");

        // Get all the customerAccountList where debitAmount is null
        defaultCustomerAccountShouldNotBeFound("debitAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByDebitAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where debitAmount is greater than or equal to DEFAULT_DEBIT_AMOUNT
        defaultCustomerAccountShouldBeFound("debitAmount.greaterThanOrEqual=" + DEFAULT_DEBIT_AMOUNT);

        // Get all the customerAccountList where debitAmount is greater than or equal to UPDATED_DEBIT_AMOUNT
        defaultCustomerAccountShouldNotBeFound("debitAmount.greaterThanOrEqual=" + UPDATED_DEBIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByDebitAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where debitAmount is less than or equal to DEFAULT_DEBIT_AMOUNT
        defaultCustomerAccountShouldBeFound("debitAmount.lessThanOrEqual=" + DEFAULT_DEBIT_AMOUNT);

        // Get all the customerAccountList where debitAmount is less than or equal to SMALLER_DEBIT_AMOUNT
        defaultCustomerAccountShouldNotBeFound("debitAmount.lessThanOrEqual=" + SMALLER_DEBIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByDebitAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where debitAmount is less than DEFAULT_DEBIT_AMOUNT
        defaultCustomerAccountShouldNotBeFound("debitAmount.lessThan=" + DEFAULT_DEBIT_AMOUNT);

        // Get all the customerAccountList where debitAmount is less than UPDATED_DEBIT_AMOUNT
        defaultCustomerAccountShouldBeFound("debitAmount.lessThan=" + UPDATED_DEBIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByDebitAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where debitAmount is greater than DEFAULT_DEBIT_AMOUNT
        defaultCustomerAccountShouldNotBeFound("debitAmount.greaterThan=" + DEFAULT_DEBIT_AMOUNT);

        // Get all the customerAccountList where debitAmount is greater than SMALLER_DEBIT_AMOUNT
        defaultCustomerAccountShouldBeFound("debitAmount.greaterThan=" + SMALLER_DEBIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByBalanceAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where balanceAmount equals to DEFAULT_BALANCE_AMOUNT
        defaultCustomerAccountShouldBeFound("balanceAmount.equals=" + DEFAULT_BALANCE_AMOUNT);

        // Get all the customerAccountList where balanceAmount equals to UPDATED_BALANCE_AMOUNT
        defaultCustomerAccountShouldNotBeFound("balanceAmount.equals=" + UPDATED_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByBalanceAmountIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where balanceAmount in DEFAULT_BALANCE_AMOUNT or UPDATED_BALANCE_AMOUNT
        defaultCustomerAccountShouldBeFound("balanceAmount.in=" + DEFAULT_BALANCE_AMOUNT + "," + UPDATED_BALANCE_AMOUNT);

        // Get all the customerAccountList where balanceAmount equals to UPDATED_BALANCE_AMOUNT
        defaultCustomerAccountShouldNotBeFound("balanceAmount.in=" + UPDATED_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByBalanceAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where balanceAmount is not null
        defaultCustomerAccountShouldBeFound("balanceAmount.specified=true");

        // Get all the customerAccountList where balanceAmount is null
        defaultCustomerAccountShouldNotBeFound("balanceAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByBalanceAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where balanceAmount is greater than or equal to DEFAULT_BALANCE_AMOUNT
        defaultCustomerAccountShouldBeFound("balanceAmount.greaterThanOrEqual=" + DEFAULT_BALANCE_AMOUNT);

        // Get all the customerAccountList where balanceAmount is greater than or equal to UPDATED_BALANCE_AMOUNT
        defaultCustomerAccountShouldNotBeFound("balanceAmount.greaterThanOrEqual=" + UPDATED_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByBalanceAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where balanceAmount is less than or equal to DEFAULT_BALANCE_AMOUNT
        defaultCustomerAccountShouldBeFound("balanceAmount.lessThanOrEqual=" + DEFAULT_BALANCE_AMOUNT);

        // Get all the customerAccountList where balanceAmount is less than or equal to SMALLER_BALANCE_AMOUNT
        defaultCustomerAccountShouldNotBeFound("balanceAmount.lessThanOrEqual=" + SMALLER_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByBalanceAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where balanceAmount is less than DEFAULT_BALANCE_AMOUNT
        defaultCustomerAccountShouldNotBeFound("balanceAmount.lessThan=" + DEFAULT_BALANCE_AMOUNT);

        // Get all the customerAccountList where balanceAmount is less than UPDATED_BALANCE_AMOUNT
        defaultCustomerAccountShouldBeFound("balanceAmount.lessThan=" + UPDATED_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByBalanceAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where balanceAmount is greater than DEFAULT_BALANCE_AMOUNT
        defaultCustomerAccountShouldNotBeFound("balanceAmount.greaterThan=" + DEFAULT_BALANCE_AMOUNT);

        // Get all the customerAccountList where balanceAmount is greater than SMALLER_BALANCE_AMOUNT
        defaultCustomerAccountShouldBeFound("balanceAmount.greaterThan=" + SMALLER_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByTransactionIDIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionID equals to DEFAULT_TRANSACTION_ID
        defaultCustomerAccountShouldBeFound("transactionID.equals=" + DEFAULT_TRANSACTION_ID);

        // Get all the customerAccountList where transactionID equals to UPDATED_TRANSACTION_ID
        defaultCustomerAccountShouldNotBeFound("transactionID.equals=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByTransactionIDIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionID in DEFAULT_TRANSACTION_ID or UPDATED_TRANSACTION_ID
        defaultCustomerAccountShouldBeFound("transactionID.in=" + DEFAULT_TRANSACTION_ID + "," + UPDATED_TRANSACTION_ID);

        // Get all the customerAccountList where transactionID equals to UPDATED_TRANSACTION_ID
        defaultCustomerAccountShouldNotBeFound("transactionID.in=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByTransactionIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionID is not null
        defaultCustomerAccountShouldBeFound("transactionID.specified=true");

        // Get all the customerAccountList where transactionID is null
        defaultCustomerAccountShouldNotBeFound("transactionID.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByTransactionIDContainsSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionID contains DEFAULT_TRANSACTION_ID
        defaultCustomerAccountShouldBeFound("transactionID.contains=" + DEFAULT_TRANSACTION_ID);

        // Get all the customerAccountList where transactionID contains UPDATED_TRANSACTION_ID
        defaultCustomerAccountShouldNotBeFound("transactionID.contains=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByTransactionIDNotContainsSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionID does not contain DEFAULT_TRANSACTION_ID
        defaultCustomerAccountShouldNotBeFound("transactionID.doesNotContain=" + DEFAULT_TRANSACTION_ID);

        // Get all the customerAccountList where transactionID does not contain UPDATED_TRANSACTION_ID
        defaultCustomerAccountShouldBeFound("transactionID.doesNotContain=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByLocationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where locationCode equals to DEFAULT_LOCATION_CODE
        defaultCustomerAccountShouldBeFound("locationCode.equals=" + DEFAULT_LOCATION_CODE);

        // Get all the customerAccountList where locationCode equals to UPDATED_LOCATION_CODE
        defaultCustomerAccountShouldNotBeFound("locationCode.equals=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByLocationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where locationCode in DEFAULT_LOCATION_CODE or UPDATED_LOCATION_CODE
        defaultCustomerAccountShouldBeFound("locationCode.in=" + DEFAULT_LOCATION_CODE + "," + UPDATED_LOCATION_CODE);

        // Get all the customerAccountList where locationCode equals to UPDATED_LOCATION_CODE
        defaultCustomerAccountShouldNotBeFound("locationCode.in=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByLocationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where locationCode is not null
        defaultCustomerAccountShouldBeFound("locationCode.specified=true");

        // Get all the customerAccountList where locationCode is null
        defaultCustomerAccountShouldNotBeFound("locationCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByLocationCodeContainsSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where locationCode contains DEFAULT_LOCATION_CODE
        defaultCustomerAccountShouldBeFound("locationCode.contains=" + DEFAULT_LOCATION_CODE);

        // Get all the customerAccountList where locationCode contains UPDATED_LOCATION_CODE
        defaultCustomerAccountShouldNotBeFound("locationCode.contains=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByLocationCodeNotContainsSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where locationCode does not contain DEFAULT_LOCATION_CODE
        defaultCustomerAccountShouldNotBeFound("locationCode.doesNotContain=" + DEFAULT_LOCATION_CODE);

        // Get all the customerAccountList where locationCode does not contain UPDATED_LOCATION_CODE
        defaultCustomerAccountShouldBeFound("locationCode.doesNotContain=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByTenantCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where tenantCode equals to DEFAULT_TENANT_CODE
        defaultCustomerAccountShouldBeFound("tenantCode.equals=" + DEFAULT_TENANT_CODE);

        // Get all the customerAccountList where tenantCode equals to UPDATED_TENANT_CODE
        defaultCustomerAccountShouldNotBeFound("tenantCode.equals=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByTenantCodeIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where tenantCode in DEFAULT_TENANT_CODE or UPDATED_TENANT_CODE
        defaultCustomerAccountShouldBeFound("tenantCode.in=" + DEFAULT_TENANT_CODE + "," + UPDATED_TENANT_CODE);

        // Get all the customerAccountList where tenantCode equals to UPDATED_TENANT_CODE
        defaultCustomerAccountShouldNotBeFound("tenantCode.in=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByTenantCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where tenantCode is not null
        defaultCustomerAccountShouldBeFound("tenantCode.specified=true");

        // Get all the customerAccountList where tenantCode is null
        defaultCustomerAccountShouldNotBeFound("tenantCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByTenantCodeContainsSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where tenantCode contains DEFAULT_TENANT_CODE
        defaultCustomerAccountShouldBeFound("tenantCode.contains=" + DEFAULT_TENANT_CODE);

        // Get all the customerAccountList where tenantCode contains UPDATED_TENANT_CODE
        defaultCustomerAccountShouldNotBeFound("tenantCode.contains=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByTenantCodeNotContainsSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where tenantCode does not contain DEFAULT_TENANT_CODE
        defaultCustomerAccountShouldNotBeFound("tenantCode.doesNotContain=" + DEFAULT_TENANT_CODE);

        // Get all the customerAccountList where tenantCode does not contain UPDATED_TENANT_CODE
        defaultCustomerAccountShouldBeFound("tenantCode.doesNotContain=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customerAccountRepository.saveAndFlush(customerAccount);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        customerAccount.setCustomer(customer);
        customerAccountRepository.saveAndFlush(customerAccount);
        Long customerId = customer.getId();

        // Get all the customerAccountList where customer equals to customerId
        defaultCustomerAccountShouldBeFound("customerId.equals=" + customerId);

        // Get all the customerAccountList where customer equals to (customerId + 1)
        defaultCustomerAccountShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerAccountShouldBeFound(String filter) throws Exception {
        restCustomerAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(DEFAULT_CREDIT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].debitAmount").value(hasItem(DEFAULT_DEBIT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].balanceAmount").value(hasItem(DEFAULT_BALANCE_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionID").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].tenantCode").value(hasItem(DEFAULT_TENANT_CODE)));

        // Check, that the count call also returns 1
        restCustomerAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerAccountShouldNotBeFound(String filter) throws Exception {
        restCustomerAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerAccount() throws Exception {
        // Get the customerAccount
        restCustomerAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerAccount() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        int databaseSizeBeforeUpdate = customerAccountRepository.findAll().size();

        // Update the customerAccount
        CustomerAccount updatedCustomerAccount = customerAccountRepository.findById(customerAccount.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerAccount are not directly saved in db
        em.detach(updatedCustomerAccount);
        updatedCustomerAccount
            .description(UPDATED_DESCRIPTION)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .balanceAmount(UPDATED_BALANCE_AMOUNT)
            .transactionID(UPDATED_TRANSACTION_ID)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);

        restCustomerAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomerAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCustomerAccount))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeUpdate);
        CustomerAccount testCustomerAccount = customerAccountList.get(customerAccountList.size() - 1);
        assertThat(testCustomerAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCustomerAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testCustomerAccount.getCreditAmount()).isEqualTo(UPDATED_CREDIT_AMOUNT);
        assertThat(testCustomerAccount.getDebitAmount()).isEqualTo(UPDATED_DEBIT_AMOUNT);
        assertThat(testCustomerAccount.getBalanceAmount()).isEqualTo(UPDATED_BALANCE_AMOUNT);
        assertThat(testCustomerAccount.getTransactionID()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testCustomerAccount.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testCustomerAccount.getTenantCode()).isEqualTo(UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void putNonExistingCustomerAccount() throws Exception {
        int databaseSizeBeforeUpdate = customerAccountRepository.findAll().size();
        customerAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerAccount() throws Exception {
        int databaseSizeBeforeUpdate = customerAccountRepository.findAll().size();
        customerAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerAccount() throws Exception {
        int databaseSizeBeforeUpdate = customerAccountRepository.findAll().size();
        customerAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAccountMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerAccount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerAccountWithPatch() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        int databaseSizeBeforeUpdate = customerAccountRepository.findAll().size();

        // Update the customerAccount using partial update
        CustomerAccount partialUpdatedCustomerAccount = new CustomerAccount();
        partialUpdatedCustomerAccount.setId(customerAccount.getId());

        partialUpdatedCustomerAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .transactionID(UPDATED_TRANSACTION_ID)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);

        restCustomerAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerAccount))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeUpdate);
        CustomerAccount testCustomerAccount = customerAccountList.get(customerAccountList.size() - 1);
        assertThat(testCustomerAccount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCustomerAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testCustomerAccount.getCreditAmount()).isEqualTo(UPDATED_CREDIT_AMOUNT);
        assertThat(testCustomerAccount.getDebitAmount()).isEqualTo(DEFAULT_DEBIT_AMOUNT);
        assertThat(testCustomerAccount.getBalanceAmount()).isEqualTo(DEFAULT_BALANCE_AMOUNT);
        assertThat(testCustomerAccount.getTransactionID()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testCustomerAccount.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testCustomerAccount.getTenantCode()).isEqualTo(UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateCustomerAccountWithPatch() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        int databaseSizeBeforeUpdate = customerAccountRepository.findAll().size();

        // Update the customerAccount using partial update
        CustomerAccount partialUpdatedCustomerAccount = new CustomerAccount();
        partialUpdatedCustomerAccount.setId(customerAccount.getId());

        partialUpdatedCustomerAccount
            .description(UPDATED_DESCRIPTION)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .balanceAmount(UPDATED_BALANCE_AMOUNT)
            .transactionID(UPDATED_TRANSACTION_ID)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);

        restCustomerAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerAccount))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeUpdate);
        CustomerAccount testCustomerAccount = customerAccountList.get(customerAccountList.size() - 1);
        assertThat(testCustomerAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCustomerAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testCustomerAccount.getCreditAmount()).isEqualTo(UPDATED_CREDIT_AMOUNT);
        assertThat(testCustomerAccount.getDebitAmount()).isEqualTo(UPDATED_DEBIT_AMOUNT);
        assertThat(testCustomerAccount.getBalanceAmount()).isEqualTo(UPDATED_BALANCE_AMOUNT);
        assertThat(testCustomerAccount.getTransactionID()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testCustomerAccount.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testCustomerAccount.getTenantCode()).isEqualTo(UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerAccount() throws Exception {
        int databaseSizeBeforeUpdate = customerAccountRepository.findAll().size();
        customerAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerAccount() throws Exception {
        int databaseSizeBeforeUpdate = customerAccountRepository.findAll().size();
        customerAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerAccount() throws Exception {
        int databaseSizeBeforeUpdate = customerAccountRepository.findAll().size();
        customerAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAccountMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerAccount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerAccount() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        int databaseSizeBeforeDelete = customerAccountRepository.findAll().size();

        // Delete the customerAccount
        restCustomerAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
