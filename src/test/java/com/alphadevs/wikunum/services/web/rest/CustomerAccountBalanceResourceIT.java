package com.alphadevs.wikunum.services.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.alphadevs.wikunum.services.IntegrationTest;
import com.alphadevs.wikunum.services.domain.Customer;
import com.alphadevs.wikunum.services.domain.CustomerAccountBalance;
import com.alphadevs.wikunum.services.repository.CustomerAccountBalanceRepository;
import com.alphadevs.wikunum.services.service.CustomerAccountBalanceService;
import com.alphadevs.wikunum.services.service.criteria.CustomerAccountBalanceCriteria;
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
 * Integration tests for the {@link CustomerAccountBalanceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustomerAccountBalanceResourceIT {

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;
    private static final Double SMALLER_BALANCE = 1D - 1D;

    private static final String DEFAULT_LOCATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TENANT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-account-balances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerAccountBalanceRepository customerAccountBalanceRepository;

    @Mock
    private CustomerAccountBalanceRepository customerAccountBalanceRepositoryMock;

    @Mock
    private CustomerAccountBalanceService customerAccountBalanceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerAccountBalanceMockMvc;

    private CustomerAccountBalance customerAccountBalance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerAccountBalance createEntity(EntityManager em) {
        CustomerAccountBalance customerAccountBalance = new CustomerAccountBalance()
            .balance(DEFAULT_BALANCE)
            .locationCode(DEFAULT_LOCATION_CODE)
            .tenantCode(DEFAULT_TENANT_CODE);
        return customerAccountBalance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerAccountBalance createUpdatedEntity(EntityManager em) {
        CustomerAccountBalance customerAccountBalance = new CustomerAccountBalance()
            .balance(UPDATED_BALANCE)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);
        return customerAccountBalance;
    }

    @BeforeEach
    public void initTest() {
        customerAccountBalance = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerAccountBalance() throws Exception {
        int databaseSizeBeforeCreate = customerAccountBalanceRepository.findAll().size();
        // Create the CustomerAccountBalance
        restCustomerAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAccountBalance))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerAccountBalance testCustomerAccountBalance = customerAccountBalanceList.get(customerAccountBalanceList.size() - 1);
        assertThat(testCustomerAccountBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testCustomerAccountBalance.getLocationCode()).isEqualTo(DEFAULT_LOCATION_CODE);
        assertThat(testCustomerAccountBalance.getTenantCode()).isEqualTo(DEFAULT_TENANT_CODE);
    }

    @Test
    @Transactional
    void createCustomerAccountBalanceWithExistingId() throws Exception {
        // Create the CustomerAccountBalance with an existing ID
        customerAccountBalance.setId(1L);

        int databaseSizeBeforeCreate = customerAccountBalanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLocationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAccountBalanceRepository.findAll().size();
        // set the field null
        customerAccountBalance.setLocationCode(null);

        // Create the CustomerAccountBalance, which fails.

        restCustomerAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAccountBalance))
            )
            .andExpect(status().isBadRequest());

        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTenantCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAccountBalanceRepository.findAll().size();
        // set the field null
        customerAccountBalance.setTenantCode(null);

        // Create the CustomerAccountBalance, which fails.

        restCustomerAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAccountBalance))
            )
            .andExpect(status().isBadRequest());

        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalances() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList
        restCustomerAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].tenantCode").value(hasItem(DEFAULT_TENANT_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomerAccountBalancesWithEagerRelationshipsIsEnabled() throws Exception {
        when(customerAccountBalanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerAccountBalanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(customerAccountBalanceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomerAccountBalancesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(customerAccountBalanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerAccountBalanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(customerAccountBalanceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustomerAccountBalance() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get the customerAccountBalance
        restCustomerAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL_ID, customerAccountBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerAccountBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.locationCode").value(DEFAULT_LOCATION_CODE))
            .andExpect(jsonPath("$.tenantCode").value(DEFAULT_TENANT_CODE));
    }

    @Test
    @Transactional
    void getCustomerAccountBalancesByIdFiltering() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        Long id = customerAccountBalance.getId();

        defaultCustomerAccountBalanceShouldBeFound("id.equals=" + id);
        defaultCustomerAccountBalanceShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerAccountBalanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerAccountBalanceShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerAccountBalanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerAccountBalanceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance equals to DEFAULT_BALANCE
        defaultCustomerAccountBalanceShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the customerAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultCustomerAccountBalanceShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultCustomerAccountBalanceShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the customerAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultCustomerAccountBalanceShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance is not null
        defaultCustomerAccountBalanceShouldBeFound("balance.specified=true");

        // Get all the customerAccountBalanceList where balance is null
        defaultCustomerAccountBalanceShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance is greater than or equal to DEFAULT_BALANCE
        defaultCustomerAccountBalanceShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the customerAccountBalanceList where balance is greater than or equal to UPDATED_BALANCE
        defaultCustomerAccountBalanceShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance is less than or equal to DEFAULT_BALANCE
        defaultCustomerAccountBalanceShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the customerAccountBalanceList where balance is less than or equal to SMALLER_BALANCE
        defaultCustomerAccountBalanceShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance is less than DEFAULT_BALANCE
        defaultCustomerAccountBalanceShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the customerAccountBalanceList where balance is less than UPDATED_BALANCE
        defaultCustomerAccountBalanceShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance is greater than DEFAULT_BALANCE
        defaultCustomerAccountBalanceShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the customerAccountBalanceList where balance is greater than SMALLER_BALANCE
        defaultCustomerAccountBalanceShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByLocationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where locationCode equals to DEFAULT_LOCATION_CODE
        defaultCustomerAccountBalanceShouldBeFound("locationCode.equals=" + DEFAULT_LOCATION_CODE);

        // Get all the customerAccountBalanceList where locationCode equals to UPDATED_LOCATION_CODE
        defaultCustomerAccountBalanceShouldNotBeFound("locationCode.equals=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByLocationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where locationCode in DEFAULT_LOCATION_CODE or UPDATED_LOCATION_CODE
        defaultCustomerAccountBalanceShouldBeFound("locationCode.in=" + DEFAULT_LOCATION_CODE + "," + UPDATED_LOCATION_CODE);

        // Get all the customerAccountBalanceList where locationCode equals to UPDATED_LOCATION_CODE
        defaultCustomerAccountBalanceShouldNotBeFound("locationCode.in=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByLocationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where locationCode is not null
        defaultCustomerAccountBalanceShouldBeFound("locationCode.specified=true");

        // Get all the customerAccountBalanceList where locationCode is null
        defaultCustomerAccountBalanceShouldNotBeFound("locationCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByLocationCodeContainsSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where locationCode contains DEFAULT_LOCATION_CODE
        defaultCustomerAccountBalanceShouldBeFound("locationCode.contains=" + DEFAULT_LOCATION_CODE);

        // Get all the customerAccountBalanceList where locationCode contains UPDATED_LOCATION_CODE
        defaultCustomerAccountBalanceShouldNotBeFound("locationCode.contains=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByLocationCodeNotContainsSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where locationCode does not contain DEFAULT_LOCATION_CODE
        defaultCustomerAccountBalanceShouldNotBeFound("locationCode.doesNotContain=" + DEFAULT_LOCATION_CODE);

        // Get all the customerAccountBalanceList where locationCode does not contain UPDATED_LOCATION_CODE
        defaultCustomerAccountBalanceShouldBeFound("locationCode.doesNotContain=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByTenantCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where tenantCode equals to DEFAULT_TENANT_CODE
        defaultCustomerAccountBalanceShouldBeFound("tenantCode.equals=" + DEFAULT_TENANT_CODE);

        // Get all the customerAccountBalanceList where tenantCode equals to UPDATED_TENANT_CODE
        defaultCustomerAccountBalanceShouldNotBeFound("tenantCode.equals=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByTenantCodeIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where tenantCode in DEFAULT_TENANT_CODE or UPDATED_TENANT_CODE
        defaultCustomerAccountBalanceShouldBeFound("tenantCode.in=" + DEFAULT_TENANT_CODE + "," + UPDATED_TENANT_CODE);

        // Get all the customerAccountBalanceList where tenantCode equals to UPDATED_TENANT_CODE
        defaultCustomerAccountBalanceShouldNotBeFound("tenantCode.in=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByTenantCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where tenantCode is not null
        defaultCustomerAccountBalanceShouldBeFound("tenantCode.specified=true");

        // Get all the customerAccountBalanceList where tenantCode is null
        defaultCustomerAccountBalanceShouldNotBeFound("tenantCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByTenantCodeContainsSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where tenantCode contains DEFAULT_TENANT_CODE
        defaultCustomerAccountBalanceShouldBeFound("tenantCode.contains=" + DEFAULT_TENANT_CODE);

        // Get all the customerAccountBalanceList where tenantCode contains UPDATED_TENANT_CODE
        defaultCustomerAccountBalanceShouldNotBeFound("tenantCode.contains=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByTenantCodeNotContainsSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where tenantCode does not contain DEFAULT_TENANT_CODE
        defaultCustomerAccountBalanceShouldNotBeFound("tenantCode.doesNotContain=" + DEFAULT_TENANT_CODE);

        // Get all the customerAccountBalanceList where tenantCode does not contain UPDATED_TENANT_CODE
        defaultCustomerAccountBalanceShouldBeFound("tenantCode.doesNotContain=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerAccountBalancesByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        customerAccountBalance.setCustomer(customer);
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);
        Long customerId = customer.getId();

        // Get all the customerAccountBalanceList where customer equals to customerId
        defaultCustomerAccountBalanceShouldBeFound("customerId.equals=" + customerId);

        // Get all the customerAccountBalanceList where customer equals to (customerId + 1)
        defaultCustomerAccountBalanceShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerAccountBalanceShouldBeFound(String filter) throws Exception {
        restCustomerAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].tenantCode").value(hasItem(DEFAULT_TENANT_CODE)));

        // Check, that the count call also returns 1
        restCustomerAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerAccountBalanceShouldNotBeFound(String filter) throws Exception {
        restCustomerAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerAccountBalance() throws Exception {
        // Get the customerAccountBalance
        restCustomerAccountBalanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerAccountBalance() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        int databaseSizeBeforeUpdate = customerAccountBalanceRepository.findAll().size();

        // Update the customerAccountBalance
        CustomerAccountBalance updatedCustomerAccountBalance = customerAccountBalanceRepository
            .findById(customerAccountBalance.getId())
            .get();
        // Disconnect from session so that the updates on updatedCustomerAccountBalance are not directly saved in db
        em.detach(updatedCustomerAccountBalance);
        updatedCustomerAccountBalance.balance(UPDATED_BALANCE).locationCode(UPDATED_LOCATION_CODE).tenantCode(UPDATED_TENANT_CODE);

        restCustomerAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomerAccountBalance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCustomerAccountBalance))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        CustomerAccountBalance testCustomerAccountBalance = customerAccountBalanceList.get(customerAccountBalanceList.size() - 1);
        assertThat(testCustomerAccountBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testCustomerAccountBalance.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testCustomerAccountBalance.getTenantCode()).isEqualTo(UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void putNonExistingCustomerAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = customerAccountBalanceRepository.findAll().size();
        customerAccountBalance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerAccountBalance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = customerAccountBalanceRepository.findAll().size();
        customerAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = customerAccountBalanceRepository.findAll().size();
        customerAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAccountBalance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerAccountBalanceWithPatch() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        int databaseSizeBeforeUpdate = customerAccountBalanceRepository.findAll().size();

        // Update the customerAccountBalance using partial update
        CustomerAccountBalance partialUpdatedCustomerAccountBalance = new CustomerAccountBalance();
        partialUpdatedCustomerAccountBalance.setId(customerAccountBalance.getId());

        partialUpdatedCustomerAccountBalance.balance(UPDATED_BALANCE).tenantCode(UPDATED_TENANT_CODE);

        restCustomerAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerAccountBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerAccountBalance))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        CustomerAccountBalance testCustomerAccountBalance = customerAccountBalanceList.get(customerAccountBalanceList.size() - 1);
        assertThat(testCustomerAccountBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testCustomerAccountBalance.getLocationCode()).isEqualTo(DEFAULT_LOCATION_CODE);
        assertThat(testCustomerAccountBalance.getTenantCode()).isEqualTo(UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateCustomerAccountBalanceWithPatch() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        int databaseSizeBeforeUpdate = customerAccountBalanceRepository.findAll().size();

        // Update the customerAccountBalance using partial update
        CustomerAccountBalance partialUpdatedCustomerAccountBalance = new CustomerAccountBalance();
        partialUpdatedCustomerAccountBalance.setId(customerAccountBalance.getId());

        partialUpdatedCustomerAccountBalance.balance(UPDATED_BALANCE).locationCode(UPDATED_LOCATION_CODE).tenantCode(UPDATED_TENANT_CODE);

        restCustomerAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerAccountBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerAccountBalance))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        CustomerAccountBalance testCustomerAccountBalance = customerAccountBalanceList.get(customerAccountBalanceList.size() - 1);
        assertThat(testCustomerAccountBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testCustomerAccountBalance.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testCustomerAccountBalance.getTenantCode()).isEqualTo(UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = customerAccountBalanceRepository.findAll().size();
        customerAccountBalance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerAccountBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = customerAccountBalanceRepository.findAll().size();
        customerAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = customerAccountBalanceRepository.findAll().size();
        customerAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerAccountBalance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerAccountBalance() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        int databaseSizeBeforeDelete = customerAccountBalanceRepository.findAll().size();

        // Delete the customerAccountBalance
        restCustomerAccountBalanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerAccountBalance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
