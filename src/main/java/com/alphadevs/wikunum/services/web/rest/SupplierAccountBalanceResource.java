package com.alphadevs.wikunum.services.web.rest;

import com.alphadevs.wikunum.services.domain.SupplierAccountBalance;
import com.alphadevs.wikunum.services.repository.SupplierAccountBalanceRepository;
import com.alphadevs.wikunum.services.service.SupplierAccountBalanceQueryService;
import com.alphadevs.wikunum.services.service.SupplierAccountBalanceService;
import com.alphadevs.wikunum.services.service.criteria.SupplierAccountBalanceCriteria;
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
 * REST controller for managing {@link com.alphadevs.wikunum.services.domain.SupplierAccountBalance}.
 */
@RestController
@RequestMapping("/api")
public class SupplierAccountBalanceResource {

    private final Logger log = LoggerFactory.getLogger(SupplierAccountBalanceResource.class);

    private static final String ENTITY_NAME = "accountManagementSupplierAccountBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierAccountBalanceService supplierAccountBalanceService;

    private final SupplierAccountBalanceRepository supplierAccountBalanceRepository;

    private final SupplierAccountBalanceQueryService supplierAccountBalanceQueryService;

    public SupplierAccountBalanceResource(
        SupplierAccountBalanceService supplierAccountBalanceService,
        SupplierAccountBalanceRepository supplierAccountBalanceRepository,
        SupplierAccountBalanceQueryService supplierAccountBalanceQueryService
    ) {
        this.supplierAccountBalanceService = supplierAccountBalanceService;
        this.supplierAccountBalanceRepository = supplierAccountBalanceRepository;
        this.supplierAccountBalanceQueryService = supplierAccountBalanceQueryService;
    }

    /**
     * {@code POST  /supplier-account-balances} : Create a new supplierAccountBalance.
     *
     * @param supplierAccountBalance the supplierAccountBalance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierAccountBalance, or with status {@code 400 (Bad Request)} if the supplierAccountBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/supplier-account-balances")
    public ResponseEntity<SupplierAccountBalance> createSupplierAccountBalance(
        @Valid @RequestBody SupplierAccountBalance supplierAccountBalance
    ) throws URISyntaxException {
        log.debug("REST request to save SupplierAccountBalance : {}", supplierAccountBalance);
        if (supplierAccountBalance.getId() != null) {
            throw new BadRequestAlertException("A new supplierAccountBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SupplierAccountBalance result = supplierAccountBalanceService.save(supplierAccountBalance);
        return ResponseEntity
            .created(new URI("/api/supplier-account-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /supplier-account-balances/:id} : Updates an existing supplierAccountBalance.
     *
     * @param id the id of the supplierAccountBalance to save.
     * @param supplierAccountBalance the supplierAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierAccountBalance,
     * or with status {@code 400 (Bad Request)} if the supplierAccountBalance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/supplier-account-balances/{id}")
    public ResponseEntity<SupplierAccountBalance> updateSupplierAccountBalance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierAccountBalance supplierAccountBalance
    ) throws URISyntaxException {
        log.debug("REST request to update SupplierAccountBalance : {}, {}", id, supplierAccountBalance);
        if (supplierAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierAccountBalance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierAccountBalanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SupplierAccountBalance result = supplierAccountBalanceService.update(supplierAccountBalance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierAccountBalance.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /supplier-account-balances/:id} : Partial updates given fields of an existing supplierAccountBalance, field will ignore if it is null
     *
     * @param id the id of the supplierAccountBalance to save.
     * @param supplierAccountBalance the supplierAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierAccountBalance,
     * or with status {@code 400 (Bad Request)} if the supplierAccountBalance is not valid,
     * or with status {@code 404 (Not Found)} if the supplierAccountBalance is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/supplier-account-balances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierAccountBalance> partialUpdateSupplierAccountBalance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierAccountBalance supplierAccountBalance
    ) throws URISyntaxException {
        log.debug("REST request to partial update SupplierAccountBalance partially : {}, {}", id, supplierAccountBalance);
        if (supplierAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierAccountBalance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierAccountBalanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierAccountBalance> result = supplierAccountBalanceService.partialUpdate(supplierAccountBalance);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierAccountBalance.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-account-balances} : get all the supplierAccountBalances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierAccountBalances in body.
     */
    @GetMapping("/supplier-account-balances")
    public ResponseEntity<List<SupplierAccountBalance>> getAllSupplierAccountBalances(
        SupplierAccountBalanceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SupplierAccountBalances by criteria: {}", criteria);
        Page<SupplierAccountBalance> page = supplierAccountBalanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /supplier-account-balances/count} : count all the supplierAccountBalances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/supplier-account-balances/count")
    public ResponseEntity<Long> countSupplierAccountBalances(SupplierAccountBalanceCriteria criteria) {
        log.debug("REST request to count SupplierAccountBalances by criteria: {}", criteria);
        return ResponseEntity.ok().body(supplierAccountBalanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /supplier-account-balances/:id} : get the "id" supplierAccountBalance.
     *
     * @param id the id of the supplierAccountBalance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierAccountBalance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/supplier-account-balances/{id}")
    public ResponseEntity<SupplierAccountBalance> getSupplierAccountBalance(@PathVariable Long id) {
        log.debug("REST request to get SupplierAccountBalance : {}", id);
        Optional<SupplierAccountBalance> supplierAccountBalance = supplierAccountBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierAccountBalance);
    }

    /**
     * {@code DELETE  /supplier-account-balances/:id} : delete the "id" supplierAccountBalance.
     *
     * @param id the id of the supplierAccountBalance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/supplier-account-balances/{id}")
    public ResponseEntity<Void> deleteSupplierAccountBalance(@PathVariable Long id) {
        log.debug("REST request to delete SupplierAccountBalance : {}", id);
        supplierAccountBalanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
