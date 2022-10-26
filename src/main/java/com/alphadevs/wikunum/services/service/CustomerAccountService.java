package com.alphadevs.wikunum.services.service;

import com.alphadevs.wikunum.services.domain.CustomerAccount;
import com.alphadevs.wikunum.services.repository.CustomerAccountRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomerAccount}.
 */
@Service
@Transactional
public class CustomerAccountService {

    private final Logger log = LoggerFactory.getLogger(CustomerAccountService.class);

    private final CustomerAccountRepository customerAccountRepository;

    public CustomerAccountService(CustomerAccountRepository customerAccountRepository) {
        this.customerAccountRepository = customerAccountRepository;
    }

    /**
     * Save a customerAccount.
     *
     * @param customerAccount the entity to save.
     * @return the persisted entity.
     */
    public CustomerAccount save(CustomerAccount customerAccount) {
        log.debug("Request to save CustomerAccount : {}", customerAccount);
        return customerAccountRepository.save(customerAccount);
    }

    /**
     * Update a customerAccount.
     *
     * @param customerAccount the entity to save.
     * @return the persisted entity.
     */
    public CustomerAccount update(CustomerAccount customerAccount) {
        log.debug("Request to update CustomerAccount : {}", customerAccount);
        return customerAccountRepository.save(customerAccount);
    }

    /**
     * Partially update a customerAccount.
     *
     * @param customerAccount the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerAccount> partialUpdate(CustomerAccount customerAccount) {
        log.debug("Request to partially update CustomerAccount : {}", customerAccount);

        return customerAccountRepository
            .findById(customerAccount.getId())
            .map(existingCustomerAccount -> {
                if (customerAccount.getDescription() != null) {
                    existingCustomerAccount.setDescription(customerAccount.getDescription());
                }
                if (customerAccount.getTransactionDate() != null) {
                    existingCustomerAccount.setTransactionDate(customerAccount.getTransactionDate());
                }
                if (customerAccount.getCreditAmount() != null) {
                    existingCustomerAccount.setCreditAmount(customerAccount.getCreditAmount());
                }
                if (customerAccount.getDebitAmount() != null) {
                    existingCustomerAccount.setDebitAmount(customerAccount.getDebitAmount());
                }
                if (customerAccount.getBalanceAmount() != null) {
                    existingCustomerAccount.setBalanceAmount(customerAccount.getBalanceAmount());
                }
                if (customerAccount.getTransactionID() != null) {
                    existingCustomerAccount.setTransactionID(customerAccount.getTransactionID());
                }
                if (customerAccount.getLocationCode() != null) {
                    existingCustomerAccount.setLocationCode(customerAccount.getLocationCode());
                }
                if (customerAccount.getTenantCode() != null) {
                    existingCustomerAccount.setTenantCode(customerAccount.getTenantCode());
                }

                return existingCustomerAccount;
            })
            .map(customerAccountRepository::save);
    }

    /**
     * Get all the customerAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerAccount> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerAccounts");
        return customerAccountRepository.findAll(pageable);
    }

    /**
     * Get all the customerAccounts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CustomerAccount> findAllWithEagerRelationships(Pageable pageable) {
        return customerAccountRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one customerAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerAccount> findOne(Long id) {
        log.debug("Request to get CustomerAccount : {}", id);
        return customerAccountRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the customerAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomerAccount : {}", id);
        customerAccountRepository.deleteById(id);
    }
}
