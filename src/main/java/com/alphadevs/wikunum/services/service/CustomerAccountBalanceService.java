package com.alphadevs.wikunum.services.service;

import com.alphadevs.wikunum.services.domain.CustomerAccountBalance;
import com.alphadevs.wikunum.services.repository.CustomerAccountBalanceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomerAccountBalance}.
 */
@Service
@Transactional
public class CustomerAccountBalanceService {

    private final Logger log = LoggerFactory.getLogger(CustomerAccountBalanceService.class);

    private final CustomerAccountBalanceRepository customerAccountBalanceRepository;

    public CustomerAccountBalanceService(CustomerAccountBalanceRepository customerAccountBalanceRepository) {
        this.customerAccountBalanceRepository = customerAccountBalanceRepository;
    }

    /**
     * Save a customerAccountBalance.
     *
     * @param customerAccountBalance the entity to save.
     * @return the persisted entity.
     */
    public CustomerAccountBalance save(CustomerAccountBalance customerAccountBalance) {
        log.debug("Request to save CustomerAccountBalance : {}", customerAccountBalance);
        return customerAccountBalanceRepository.save(customerAccountBalance);
    }

    /**
     * Update a customerAccountBalance.
     *
     * @param customerAccountBalance the entity to save.
     * @return the persisted entity.
     */
    public CustomerAccountBalance update(CustomerAccountBalance customerAccountBalance) {
        log.debug("Request to update CustomerAccountBalance : {}", customerAccountBalance);
        return customerAccountBalanceRepository.save(customerAccountBalance);
    }

    /**
     * Partially update a customerAccountBalance.
     *
     * @param customerAccountBalance the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerAccountBalance> partialUpdate(CustomerAccountBalance customerAccountBalance) {
        log.debug("Request to partially update CustomerAccountBalance : {}", customerAccountBalance);

        return customerAccountBalanceRepository
            .findById(customerAccountBalance.getId())
            .map(existingCustomerAccountBalance -> {
                if (customerAccountBalance.getBalance() != null) {
                    existingCustomerAccountBalance.setBalance(customerAccountBalance.getBalance());
                }
                if (customerAccountBalance.getLocationCode() != null) {
                    existingCustomerAccountBalance.setLocationCode(customerAccountBalance.getLocationCode());
                }
                if (customerAccountBalance.getTenantCode() != null) {
                    existingCustomerAccountBalance.setTenantCode(customerAccountBalance.getTenantCode());
                }

                return existingCustomerAccountBalance;
            })
            .map(customerAccountBalanceRepository::save);
    }

    /**
     * Get all the customerAccountBalances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerAccountBalance> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerAccountBalances");
        return customerAccountBalanceRepository.findAll(pageable);
    }

    /**
     * Get all the customerAccountBalances with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CustomerAccountBalance> findAllWithEagerRelationships(Pageable pageable) {
        return customerAccountBalanceRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one customerAccountBalance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerAccountBalance> findOne(Long id) {
        log.debug("Request to get CustomerAccountBalance : {}", id);
        return customerAccountBalanceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the customerAccountBalance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomerAccountBalance : {}", id);
        customerAccountBalanceRepository.deleteById(id);
    }
}
