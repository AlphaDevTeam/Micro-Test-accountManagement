package com.alphadevs.wikunum.services.web.rest;

import com.alphadevs.wikunum.services.domain.SupplierAccount;
import com.alphadevs.wikunum.services.repository.SupplierAccountRepository;
import com.alphadevs.wikunum.services.service.SupplierAccountQueryService;
import com.alphadevs.wikunum.services.service.SupplierAccountService;
import com.alphadevs.wikunum.services.service.criteria.SupplierAccountCriteria;
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
 * REST controller for managing {@link com.alphadevs.wikunum.services.domain.SupplierAccount}.
 */
@RestController
@RequestMapping("/api")
public class SupplierAccountResource {

    private final Logger log = LoggerFactory.getLogger(SupplierAccountResource.class);

    private static final String ENTITY_NAME = "accountManagementSupplierAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierAccountService supplierAccountService;

    private final SupplierAccountRepository supplierAccountRepository;

    private final SupplierAccountQueryService supplierAccountQueryService;

    public SupplierAccountResource(
        SupplierAccountService supplierAccountService,
        SupplierAccountRepository supplierAccountRepository,
        SupplierAccountQueryService supplierAccountQueryService
    ) {
        this.supplierAccountService = supplierAccountService;
        this.supplierAccountRepository = supplierAccountRepository;
        this.supplierAccountQueryService = supplierAccountQueryService;
    }

    /**
     * {@code POST  /supplier-accounts} : Create a new supplierAccount.
     *
     * @param supplierAccount the supplierAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierAccount, or with status {@code 400 (Bad Request)} if the supplierAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/supplier-accounts")
    public ResponseEntity<SupplierAccount> createSupplierAccount(@Valid @RequestBody SupplierAccount supplierAccount)
        throws URISyntaxException {
        log.debug("REST request to save SupplierAccount : {}", supplierAccount);
        if (supplierAccount.getId() != null) {
            throw new BadRequestAlertException("A new supplierAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SupplierAccount result = supplierAccountService.save(supplierAccount);
        return ResponseEntity
            .created(new URI("/api/supplier-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /supplier-accounts/:id} : Updates an existing supplierAccount.
     *
     * @param id the id of the supplierAccount to save.
     * @param supplierAccount the supplierAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierAccount,
     * or with status {@code 400 (Bad Request)} if the supplierAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/supplier-accounts/{id}")
    public ResponseEntity<SupplierAccount> updateSupplierAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierAccount supplierAccount
    ) throws URISyntaxException {
        log.debug("REST request to update SupplierAccount : {}, {}", id, supplierAccount);
        if (supplierAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SupplierAccount result = supplierAccountService.update(supplierAccount);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /supplier-accounts/:id} : Partial updates given fields of an existing supplierAccount, field will ignore if it is null
     *
     * @param id the id of the supplierAccount to save.
     * @param supplierAccount the supplierAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierAccount,
     * or with status {@code 400 (Bad Request)} if the supplierAccount is not valid,
     * or with status {@code 404 (Not Found)} if the supplierAccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/supplier-accounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierAccount> partialUpdateSupplierAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierAccount supplierAccount
    ) throws URISyntaxException {
        log.debug("REST request to partial update SupplierAccount partially : {}, {}", id, supplierAccount);
        if (supplierAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierAccount> result = supplierAccountService.partialUpdate(supplierAccount);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierAccount.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-accounts} : get all the supplierAccounts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierAccounts in body.
     */
    @GetMapping("/supplier-accounts")
    public ResponseEntity<List<SupplierAccount>> getAllSupplierAccounts(
        SupplierAccountCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SupplierAccounts by criteria: {}", criteria);
        Page<SupplierAccount> page = supplierAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /supplier-accounts/count} : count all the supplierAccounts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/supplier-accounts/count")
    public ResponseEntity<Long> countSupplierAccounts(SupplierAccountCriteria criteria) {
        log.debug("REST request to count SupplierAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(supplierAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /supplier-accounts/:id} : get the "id" supplierAccount.
     *
     * @param id the id of the supplierAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/supplier-accounts/{id}")
    public ResponseEntity<SupplierAccount> getSupplierAccount(@PathVariable Long id) {
        log.debug("REST request to get SupplierAccount : {}", id);
        Optional<SupplierAccount> supplierAccount = supplierAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierAccount);
    }

    /**
     * {@code DELETE  /supplier-accounts/:id} : delete the "id" supplierAccount.
     *
     * @param id the id of the supplierAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/supplier-accounts/{id}")
    public ResponseEntity<Void> deleteSupplierAccount(@PathVariable Long id) {
        log.debug("REST request to delete SupplierAccount : {}", id);
        supplierAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
