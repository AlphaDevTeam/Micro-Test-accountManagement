package com.alphadevs.wikunum.services.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.alphadevs.wikunum.services.IntegrationTest;
import com.alphadevs.wikunum.services.domain.Supplier;
import com.alphadevs.wikunum.services.domain.SupplierAccountBalance;
import com.alphadevs.wikunum.services.repository.SupplierAccountBalanceRepository;
import com.alphadevs.wikunum.services.service.SupplierAccountBalanceService;
import com.alphadevs.wikunum.services.service.criteria.SupplierAccountBalanceCriteria;
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
 * Integration tests for the {@link SupplierAccountBalanceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierAccountBalanceResourceIT {

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;
    private static final Double SMALLER_BALANCE = 1D - 1D;

    private static final String DEFAULT_LOCATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TENANT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/supplier-account-balances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SupplierAccountBalanceRepository supplierAccountBalanceRepository;

    @Mock
    private SupplierAccountBalanceRepository supplierAccountBalanceRepositoryMock;

    @Mock
    private SupplierAccountBalanceService supplierAccountBalanceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierAccountBalanceMockMvc;

    private SupplierAccountBalance supplierAccountBalance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierAccountBalance createEntity(EntityManager em) {
        SupplierAccountBalance supplierAccountBalance = new SupplierAccountBalance()
            .balance(DEFAULT_BALANCE)
            .locationCode(DEFAULT_LOCATION_CODE)
            .tenantCode(DEFAULT_TENANT_CODE);
        return supplierAccountBalance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierAccountBalance createUpdatedEntity(EntityManager em) {
        SupplierAccountBalance supplierAccountBalance = new SupplierAccountBalance()
            .balance(UPDATED_BALANCE)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);
        return supplierAccountBalance;
    }

    @BeforeEach
    public void initTest() {
        supplierAccountBalance = createEntity(em);
    }

    @Test
    @Transactional
    void createSupplierAccountBalance() throws Exception {
        int databaseSizeBeforeCreate = supplierAccountBalanceRepository.findAll().size();
        // Create the SupplierAccountBalance
        restSupplierAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccountBalance))
            )
            .andExpect(status().isCreated());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        SupplierAccountBalance testSupplierAccountBalance = supplierAccountBalanceList.get(supplierAccountBalanceList.size() - 1);
        assertThat(testSupplierAccountBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testSupplierAccountBalance.getLocationCode()).isEqualTo(DEFAULT_LOCATION_CODE);
        assertThat(testSupplierAccountBalance.getTenantCode()).isEqualTo(DEFAULT_TENANT_CODE);
    }

    @Test
    @Transactional
    void createSupplierAccountBalanceWithExistingId() throws Exception {
        // Create the SupplierAccountBalance with an existing ID
        supplierAccountBalance.setId(1L);

        int databaseSizeBeforeCreate = supplierAccountBalanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLocationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierAccountBalanceRepository.findAll().size();
        // set the field null
        supplierAccountBalance.setLocationCode(null);

        // Create the SupplierAccountBalance, which fails.

        restSupplierAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccountBalance))
            )
            .andExpect(status().isBadRequest());

        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTenantCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierAccountBalanceRepository.findAll().size();
        // set the field null
        supplierAccountBalance.setTenantCode(null);

        // Create the SupplierAccountBalance, which fails.

        restSupplierAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccountBalance))
            )
            .andExpect(status().isBadRequest());

        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalances() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList
        restSupplierAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].tenantCode").value(hasItem(DEFAULT_TENANT_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierAccountBalancesWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierAccountBalanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierAccountBalanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierAccountBalanceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierAccountBalancesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierAccountBalanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierAccountBalanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierAccountBalanceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplierAccountBalance() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get the supplierAccountBalance
        restSupplierAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL_ID, supplierAccountBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplierAccountBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.locationCode").value(DEFAULT_LOCATION_CODE))
            .andExpect(jsonPath("$.tenantCode").value(DEFAULT_TENANT_CODE));
    }

    @Test
    @Transactional
    void getSupplierAccountBalancesByIdFiltering() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        Long id = supplierAccountBalance.getId();

        defaultSupplierAccountBalanceShouldBeFound("id.equals=" + id);
        defaultSupplierAccountBalanceShouldNotBeFound("id.notEquals=" + id);

        defaultSupplierAccountBalanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSupplierAccountBalanceShouldNotBeFound("id.greaterThan=" + id);

        defaultSupplierAccountBalanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSupplierAccountBalanceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance equals to DEFAULT_BALANCE
        defaultSupplierAccountBalanceShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the supplierAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultSupplierAccountBalanceShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultSupplierAccountBalanceShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the supplierAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultSupplierAccountBalanceShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance is not null
        defaultSupplierAccountBalanceShouldBeFound("balance.specified=true");

        // Get all the supplierAccountBalanceList where balance is null
        defaultSupplierAccountBalanceShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance is greater than or equal to DEFAULT_BALANCE
        defaultSupplierAccountBalanceShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the supplierAccountBalanceList where balance is greater than or equal to UPDATED_BALANCE
        defaultSupplierAccountBalanceShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance is less than or equal to DEFAULT_BALANCE
        defaultSupplierAccountBalanceShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the supplierAccountBalanceList where balance is less than or equal to SMALLER_BALANCE
        defaultSupplierAccountBalanceShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance is less than DEFAULT_BALANCE
        defaultSupplierAccountBalanceShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the supplierAccountBalanceList where balance is less than UPDATED_BALANCE
        defaultSupplierAccountBalanceShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance is greater than DEFAULT_BALANCE
        defaultSupplierAccountBalanceShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the supplierAccountBalanceList where balance is greater than SMALLER_BALANCE
        defaultSupplierAccountBalanceShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByLocationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where locationCode equals to DEFAULT_LOCATION_CODE
        defaultSupplierAccountBalanceShouldBeFound("locationCode.equals=" + DEFAULT_LOCATION_CODE);

        // Get all the supplierAccountBalanceList where locationCode equals to UPDATED_LOCATION_CODE
        defaultSupplierAccountBalanceShouldNotBeFound("locationCode.equals=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByLocationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where locationCode in DEFAULT_LOCATION_CODE or UPDATED_LOCATION_CODE
        defaultSupplierAccountBalanceShouldBeFound("locationCode.in=" + DEFAULT_LOCATION_CODE + "," + UPDATED_LOCATION_CODE);

        // Get all the supplierAccountBalanceList where locationCode equals to UPDATED_LOCATION_CODE
        defaultSupplierAccountBalanceShouldNotBeFound("locationCode.in=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByLocationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where locationCode is not null
        defaultSupplierAccountBalanceShouldBeFound("locationCode.specified=true");

        // Get all the supplierAccountBalanceList where locationCode is null
        defaultSupplierAccountBalanceShouldNotBeFound("locationCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByLocationCodeContainsSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where locationCode contains DEFAULT_LOCATION_CODE
        defaultSupplierAccountBalanceShouldBeFound("locationCode.contains=" + DEFAULT_LOCATION_CODE);

        // Get all the supplierAccountBalanceList where locationCode contains UPDATED_LOCATION_CODE
        defaultSupplierAccountBalanceShouldNotBeFound("locationCode.contains=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByLocationCodeNotContainsSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where locationCode does not contain DEFAULT_LOCATION_CODE
        defaultSupplierAccountBalanceShouldNotBeFound("locationCode.doesNotContain=" + DEFAULT_LOCATION_CODE);

        // Get all the supplierAccountBalanceList where locationCode does not contain UPDATED_LOCATION_CODE
        defaultSupplierAccountBalanceShouldBeFound("locationCode.doesNotContain=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByTenantCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where tenantCode equals to DEFAULT_TENANT_CODE
        defaultSupplierAccountBalanceShouldBeFound("tenantCode.equals=" + DEFAULT_TENANT_CODE);

        // Get all the supplierAccountBalanceList where tenantCode equals to UPDATED_TENANT_CODE
        defaultSupplierAccountBalanceShouldNotBeFound("tenantCode.equals=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByTenantCodeIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where tenantCode in DEFAULT_TENANT_CODE or UPDATED_TENANT_CODE
        defaultSupplierAccountBalanceShouldBeFound("tenantCode.in=" + DEFAULT_TENANT_CODE + "," + UPDATED_TENANT_CODE);

        // Get all the supplierAccountBalanceList where tenantCode equals to UPDATED_TENANT_CODE
        defaultSupplierAccountBalanceShouldNotBeFound("tenantCode.in=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByTenantCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where tenantCode is not null
        defaultSupplierAccountBalanceShouldBeFound("tenantCode.specified=true");

        // Get all the supplierAccountBalanceList where tenantCode is null
        defaultSupplierAccountBalanceShouldNotBeFound("tenantCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByTenantCodeContainsSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where tenantCode contains DEFAULT_TENANT_CODE
        defaultSupplierAccountBalanceShouldBeFound("tenantCode.contains=" + DEFAULT_TENANT_CODE);

        // Get all the supplierAccountBalanceList where tenantCode contains UPDATED_TENANT_CODE
        defaultSupplierAccountBalanceShouldNotBeFound("tenantCode.contains=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesByTenantCodeNotContainsSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where tenantCode does not contain DEFAULT_TENANT_CODE
        defaultSupplierAccountBalanceShouldNotBeFound("tenantCode.doesNotContain=" + DEFAULT_TENANT_CODE);

        // Get all the supplierAccountBalanceList where tenantCode does not contain UPDATED_TENANT_CODE
        defaultSupplierAccountBalanceShouldBeFound("tenantCode.doesNotContain=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllSupplierAccountBalancesBySupplierIsEqualToSomething() throws Exception {
        Supplier supplier;
        if (TestUtil.findAll(em, Supplier.class).isEmpty()) {
            supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);
            supplier = SupplierResourceIT.createEntity(em);
        } else {
            supplier = TestUtil.findAll(em, Supplier.class).get(0);
        }
        em.persist(supplier);
        em.flush();
        supplierAccountBalance.setSupplier(supplier);
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);
        Long supplierId = supplier.getId();

        // Get all the supplierAccountBalanceList where supplier equals to supplierId
        defaultSupplierAccountBalanceShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the supplierAccountBalanceList where supplier equals to (supplierId + 1)
        defaultSupplierAccountBalanceShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierAccountBalanceShouldBeFound(String filter) throws Exception {
        restSupplierAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].tenantCode").value(hasItem(DEFAULT_TENANT_CODE)));

        // Check, that the count call also returns 1
        restSupplierAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierAccountBalanceShouldNotBeFound(String filter) throws Exception {
        restSupplierAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplierAccountBalance() throws Exception {
        // Get the supplierAccountBalance
        restSupplierAccountBalanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplierAccountBalance() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        int databaseSizeBeforeUpdate = supplierAccountBalanceRepository.findAll().size();

        // Update the supplierAccountBalance
        SupplierAccountBalance updatedSupplierAccountBalance = supplierAccountBalanceRepository
            .findById(supplierAccountBalance.getId())
            .get();
        // Disconnect from session so that the updates on updatedSupplierAccountBalance are not directly saved in db
        em.detach(updatedSupplierAccountBalance);
        updatedSupplierAccountBalance.balance(UPDATED_BALANCE).locationCode(UPDATED_LOCATION_CODE).tenantCode(UPDATED_TENANT_CODE);

        restSupplierAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSupplierAccountBalance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSupplierAccountBalance))
            )
            .andExpect(status().isOk());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        SupplierAccountBalance testSupplierAccountBalance = supplierAccountBalanceList.get(supplierAccountBalanceList.size() - 1);
        assertThat(testSupplierAccountBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testSupplierAccountBalance.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testSupplierAccountBalance.getTenantCode()).isEqualTo(UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void putNonExistingSupplierAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = supplierAccountBalanceRepository.findAll().size();
        supplierAccountBalance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierAccountBalance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplierAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = supplierAccountBalanceRepository.findAll().size();
        supplierAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplierAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = supplierAccountBalanceRepository.findAll().size();
        supplierAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccountBalance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierAccountBalanceWithPatch() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        int databaseSizeBeforeUpdate = supplierAccountBalanceRepository.findAll().size();

        // Update the supplierAccountBalance using partial update
        SupplierAccountBalance partialUpdatedSupplierAccountBalance = new SupplierAccountBalance();
        partialUpdatedSupplierAccountBalance.setId(supplierAccountBalance.getId());

        partialUpdatedSupplierAccountBalance.balance(UPDATED_BALANCE).locationCode(UPDATED_LOCATION_CODE);

        restSupplierAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierAccountBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSupplierAccountBalance))
            )
            .andExpect(status().isOk());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        SupplierAccountBalance testSupplierAccountBalance = supplierAccountBalanceList.get(supplierAccountBalanceList.size() - 1);
        assertThat(testSupplierAccountBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testSupplierAccountBalance.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testSupplierAccountBalance.getTenantCode()).isEqualTo(DEFAULT_TENANT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateSupplierAccountBalanceWithPatch() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        int databaseSizeBeforeUpdate = supplierAccountBalanceRepository.findAll().size();

        // Update the supplierAccountBalance using partial update
        SupplierAccountBalance partialUpdatedSupplierAccountBalance = new SupplierAccountBalance();
        partialUpdatedSupplierAccountBalance.setId(supplierAccountBalance.getId());

        partialUpdatedSupplierAccountBalance.balance(UPDATED_BALANCE).locationCode(UPDATED_LOCATION_CODE).tenantCode(UPDATED_TENANT_CODE);

        restSupplierAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierAccountBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSupplierAccountBalance))
            )
            .andExpect(status().isOk());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        SupplierAccountBalance testSupplierAccountBalance = supplierAccountBalanceList.get(supplierAccountBalanceList.size() - 1);
        assertThat(testSupplierAccountBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testSupplierAccountBalance.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testSupplierAccountBalance.getTenantCode()).isEqualTo(UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingSupplierAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = supplierAccountBalanceRepository.findAll().size();
        supplierAccountBalance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierAccountBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplierAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = supplierAccountBalanceRepository.findAll().size();
        supplierAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccountBalance))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplierAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = supplierAccountBalanceRepository.findAll().size();
        supplierAccountBalance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplierAccountBalance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplierAccountBalance() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        int databaseSizeBeforeDelete = supplierAccountBalanceRepository.findAll().size();

        // Delete the supplierAccountBalance
        restSupplierAccountBalanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplierAccountBalance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
