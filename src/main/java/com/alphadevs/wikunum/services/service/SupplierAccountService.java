package com.alphadevs.wikunum.services.service;

import com.alphadevs.wikunum.services.domain.SupplierAccount;
import com.alphadevs.wikunum.services.repository.SupplierAccountRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SupplierAccount}.
 */
@Service
@Transactional
public class SupplierAccountService {

    private final Logger log = LoggerFactory.getLogger(SupplierAccountService.class);

    private final SupplierAccountRepository supplierAccountRepository;

    public SupplierAccountService(SupplierAccountRepository supplierAccountRepository) {
        this.supplierAccountRepository = supplierAccountRepository;
    }

    /**
     * Save a supplierAccount.
     *
     * @param supplierAccount the entity to save.
     * @return the persisted entity.
     */
    public SupplierAccount save(SupplierAccount supplierAccount) {
        log.debug("Request to save SupplierAccount : {}", supplierAccount);
        return supplierAccountRepository.save(supplierAccount);
    }

    /**
     * Update a supplierAccount.
     *
     * @param supplierAccount the entity to save.
     * @return the persisted entity.
     */
    public SupplierAccount update(SupplierAccount supplierAccount) {
        log.debug("Request to update SupplierAccount : {}", supplierAccount);
        return supplierAccountRepository.save(supplierAccount);
    }

    /**
     * Partially update a supplierAccount.
     *
     * @param supplierAccount the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierAccount> partialUpdate(SupplierAccount supplierAccount) {
        log.debug("Request to partially update SupplierAccount : {}", supplierAccount);

        return supplierAccountRepository
            .findById(supplierAccount.getId())
            .map(existingSupplierAccount -> {
                if (supplierAccount.getDescription() != null) {
                    existingSupplierAccount.setDescription(supplierAccount.getDescription());
                }
                if (supplierAccount.getTransactionDate() != null) {
                    existingSupplierAccount.setTransactionDate(supplierAccount.getTransactionDate());
                }
                if (supplierAccount.getCreditAmount() != null) {
                    existingSupplierAccount.setCreditAmount(supplierAccount.getCreditAmount());
                }
                if (supplierAccount.getDebitAmount() != null) {
                    existingSupplierAccount.setDebitAmount(supplierAccount.getDebitAmount());
                }
                if (supplierAccount.getBalanceAmount() != null) {
                    existingSupplierAccount.setBalanceAmount(supplierAccount.getBalanceAmount());
                }
                if (supplierAccount.getTransactionID() != null) {
                    existingSupplierAccount.setTransactionID(supplierAccount.getTransactionID());
                }
                if (supplierAccount.getLocationCode() != null) {
                    existingSupplierAccount.setLocationCode(supplierAccount.getLocationCode());
                }
                if (supplierAccount.getTenantCode() != null) {
                    existingSupplierAccount.setTenantCode(supplierAccount.getTenantCode());
                }

                return existingSupplierAccount;
            })
            .map(supplierAccountRepository::save);
    }

    /**
     * Get all the supplierAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierAccount> findAll(Pageable pageable) {
        log.debug("Request to get all SupplierAccounts");
        return supplierAccountRepository.findAll(pageable);
    }

    /**
     * Get all the supplierAccounts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierAccount> findAllWithEagerRelationships(Pageable pageable) {
        return supplierAccountRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one supplierAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierAccount> findOne(Long id) {
        log.debug("Request to get SupplierAccount : {}", id);
        return supplierAccountRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the supplierAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SupplierAccount : {}", id);
        supplierAccountRepository.deleteById(id);
    }
}
