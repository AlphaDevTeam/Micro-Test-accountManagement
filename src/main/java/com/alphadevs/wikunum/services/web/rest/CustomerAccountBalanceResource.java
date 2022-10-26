package com.alphadevs.wikunum.services.web.rest;

import com.alphadevs.wikunum.services.domain.CustomerAccountBalance;
import com.alphadevs.wikunum.services.repository.CustomerAccountBalanceRepository;
import com.alphadevs.wikunum.services.service.CustomerAccountBalanceQueryService;
import com.alphadevs.wikunum.services.service.CustomerAccountBalanceService;
import com.alphadevs.wikunum.services.service.criteria.CustomerAccountBalanceCriteria;
import com.alphadevs.wikunum.services.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.alphadevs.wikunum.services.domain.CustomerAccountBalance}.
 */
@RestController
@RequestMapping("/api")
public class CustomerAccountBalanceResource {

    private final Logger log = LoggerFactory.getLogger(CustomerAccountBalanceResource.class);

    private static final String ENTITY_NAME = "accountManagementCustomerAccountBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerAccountBalanceService customerAccountBalanceService;

    private final CustomerAccountBalanceRepository customerAccountBalanceRepository;

    private final CustomerAccountBalanceQueryService customerAccountBalanceQueryService;

    public CustomerAccountBalanceResource(
        CustomerAccountBalanceService customerAccountBalanceService,
        CustomerAccountBalanceRepository customerAccountBalanceRepository,
        CustomerAccountBalanceQueryService customerAccountBalanceQueryService
    ) {
        this.customerAccountBalanceService = customerAccountBalanceService;
        this.customerAccountBalanceRepository = customerAccountBalanceRepository;
        this.customerAccountBalanceQueryService = customerAccountBalanceQueryService;
    }

    /**
     * {@code POST  /customer-account-balances} : Create a new customerAccountBalance.
     *
     * @param customerAccountBalance the customerAccountBalance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerAccountBalance, or with status {@code 400 (Bad Request)} if the customerAccountBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-account-balances")
    public ResponseEntity<CustomerAccountBalance> createCustomerAccountBalance(
        @Valid @RequestBody CustomerAccountBalance customerAccountBalance
    ) throws URISyntaxException {
        log.debug("REST request to save CustomerAccountBalance : {}", customerAccountBalance);
        if (customerAccountBalance.getId() != null) {
            throw new BadRequestAlertException("A new customerAccountBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerAccountBalance result = customerAccountBalanceService.save(customerAccountBalance);
        return ResponseEntity
            .created(new URI("/api/customer-account-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-account-balances/:id} : Updates an existing customerAccountBalance.
     *
     * @param id the id of the customerAccountBalance to save.
     * @param customerAccountBalance the customerAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerAccountBalance,
     * or with status {@code 400 (Bad Request)} if the customerAccountBalance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-account-balances/{id}")
    public ResponseEntity<CustomerAccountBalance> updateCustomerAccountBalance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerAccountBalance customerAccountBalance
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerAccountBalance : {}, {}", id, customerAccountBalance);
        if (customerAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerAccountBalance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerAccountBalanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerAccountBalance result = customerAccountBalanceService.update(customerAccountBalance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerAccountBalance.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customer-account-balances/:id} : Partial updates given fields of an existing customerAccountBalance, field will ignore if it is null
     *
     * @param id the id of the customerAccountBalance to save.
     * @param customerAccountBalance the customerAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerAccountBalance,
     * or with status {@code 400 (Bad Request)} if the customerAccountBalance is not valid,
     * or with status {@code 404 (Not Found)} if the customerAccountBalance is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customer-account-balances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerAccountBalance> partialUpdateCustomerAccountBalance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerAccountBalance customerAccountBalance
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerAccountBalance partially : {}, {}", id, customerAccountBalance);
        if (customerAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerAccountBalance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerAccountBalanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerAccountBalance> result = customerAccountBalanceService.partialUpdate(customerAccountBalance);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerAccountBalance.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-account-balances} : get all the customerAccountBalances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerAccountBalances in body.
     */
    @GetMapping("/customer-account-balances")
    public ResponseEntity<List<CustomerAccountBalance>> getAllCustomerAccountBalances(
        CustomerAccountBalanceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CustomerAccountBalances by criteria: {}", criteria);
        Page<CustomerAccountBalance> page = customerAccountBalanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-account-balances/count} : count all the customerAccountBalances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/customer-account-balances/count")
    public ResponseEntity<Long> countCustomerAccountBalances(CustomerAccountBalanceCriteria criteria) {
        log.debug("REST request to count CustomerAccountBalances by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerAccountBalanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-account-balances/:id} : get the "id" customerAccountBalance.
     *
     * @param id the id of the customerAccountBalance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerAccountBalance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-account-balances/{id}")
    public ResponseEntity<CustomerAccountBalance> getCustomerAccountBalance(@PathVariable Long id) {
        log.debug("REST request to get CustomerAccountBalance : {}", id);
        Optional<CustomerAccountBalance> customerAccountBalance = customerAccountBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerAccountBalance);
    }

    /**
     * {@code DELETE  /customer-account-balances/:id} : delete the "id" customerAccountBalance.
     *
     * @param id the id of the customerAccountBalance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-account-balances/{id}")
    public ResponseEntity<Void> deleteCustomerAccountBalance(@PathVariable Long id) {
        log.debug("REST request to delete CustomerAccountBalance : {}", id);
        customerAccountBalanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
