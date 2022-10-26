package com.alphadevs.wikunum.services.service;

import com.alphadevs.wikunum.services.domain.SupplierAccountBalance;
import com.alphadevs.wikunum.services.repository.SupplierAccountBalanceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SupplierAccountBalance}.
 */
@Service
@Transactional
public class SupplierAccountBalanceService {

    private final Logger log = LoggerFactory.getLogger(SupplierAccountBalanceService.class);

    private final SupplierAccountBalanceRepository supplierAccountBalanceRepository;

    public SupplierAccountBalanceService(SupplierAccountBalanceRepository supplierAccountBalanceRepository) {
        this.supplierAccountBalanceRepository = supplierAccountBalanceRepository;
    }

    /**
     * Save a supplierAccountBalance.
     *
     * @param supplierAccountBalance the entity to save.
     * @return the persisted entity.
     */
    public SupplierAccountBalance save(SupplierAccountBalance supplierAccountBalance) {
        log.debug("Request to save SupplierAccountBalance : {}", supplierAccountBalance);
        return supplierAccountBalanceRepository.save(supplierAccountBalance);
    }

    /**
     * Update a supplierAccountBalance.
     *
     * @param supplierAccountBalance the entity to save.
     * @return the persisted entity.
     */
    public SupplierAccountBalance update(SupplierAccountBalance supplierAccountBalance) {
        log.debug("Request to update SupplierAccountBalance : {}", supplierAccountBalance);
        return supplierAccountBalanceRepository.save(supplierAccountBalance);
    }

    /**
     * Partially update a supplierAccountBalance.
     *
     * @param supplierAccountBalance the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierAccountBalance> partialUpdate(SupplierAccountBalance supplierAccountBalance) {
        log.debug("Request to partially update SupplierAccountBalance : {}", supplierAccountBalance);

        return supplierAccountBalanceRepository
            .findById(supplierAccountBalance.getId())
            .map(existingSupplierAccountBalance -> {
                if (supplierAccountBalance.getBalance() != null) {
                    existingSupplierAccountBalance.setBalance(supplierAccountBalance.getBalance());
                }
                if (supplierAccountBalance.getLocationCode() != null) {
                    existingSupplierAccountBalance.setLocationCode(supplierAccountBalance.getLocationCode());
                }
                if (supplierAccountBalance.getTenantCode() != null) {
                    existingSupplierAccountBalance.setTenantCode(supplierAccountBalance.getTenantCode());
                }

                return existingSupplierAccountBalance;
            })
            .map(supplierAccountBalanceRepository::save);
    }

    /**
     * Get all the supplierAccountBalances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierAccountBalance> findAll(Pageable pageable) {
        log.debug("Request to get all SupplierAccountBalances");
        return supplierAccountBalanceRepository.findAll(pageable);
    }

    /**
     * Get all the supplierAccountBalances with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierAccountBalance> findAllWithEagerRelationships(Pageable pageable) {
        return supplierAccountBalanceRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one supplierAccountBalance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierAccountBalance> findOne(Long id) {
        log.debug("Request to get SupplierAccountBalance : {}", id);
        return supplierAccountBalanceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the supplierAccountBalance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SupplierAccountBalance : {}", id);
        supplierAccountBalanceRepository.deleteById(id);
    }
}
