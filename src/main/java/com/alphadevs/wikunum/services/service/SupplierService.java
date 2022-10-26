package com.alphadevs.wikunum.services.service;

import com.alphadevs.wikunum.services.domain.Supplier;
import com.alphadevs.wikunum.services.repository.SupplierRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Supplier}.
 */
@Service
@Transactional
public class SupplierService {

    private final Logger log = LoggerFactory.getLogger(SupplierService.class);

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    /**
     * Save a supplier.
     *
     * @param supplier the entity to save.
     * @return the persisted entity.
     */
    public Supplier save(Supplier supplier) {
        log.debug("Request to save Supplier : {}", supplier);
        return supplierRepository.save(supplier);
    }

    /**
     * Update a supplier.
     *
     * @param supplier the entity to save.
     * @return the persisted entity.
     */
    public Supplier update(Supplier supplier) {
        log.debug("Request to update Supplier : {}", supplier);
        return supplierRepository.save(supplier);
    }

    /**
     * Partially update a supplier.
     *
     * @param supplier the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Supplier> partialUpdate(Supplier supplier) {
        log.debug("Request to partially update Supplier : {}", supplier);

        return supplierRepository
            .findById(supplier.getId())
            .map(existingSupplier -> {
                if (supplier.getCode() != null) {
                    existingSupplier.setCode(supplier.getCode());
                }
                if (supplier.getName() != null) {
                    existingSupplier.setName(supplier.getName());
                }
                if (supplier.getCreditLimit() != null) {
                    existingSupplier.setCreditLimit(supplier.getCreditLimit());
                }
                if (supplier.getRating() != null) {
                    existingSupplier.setRating(supplier.getRating());
                }
                if (supplier.getIsActive() != null) {
                    existingSupplier.setIsActive(supplier.getIsActive());
                }
                if (supplier.getPhone() != null) {
                    existingSupplier.setPhone(supplier.getPhone());
                }
                if (supplier.getAddressLine1() != null) {
                    existingSupplier.setAddressLine1(supplier.getAddressLine1());
                }
                if (supplier.getAddressLine2() != null) {
                    existingSupplier.setAddressLine2(supplier.getAddressLine2());
                }
                if (supplier.getCity() != null) {
                    existingSupplier.setCity(supplier.getCity());
                }
                if (supplier.getCountry() != null) {
                    existingSupplier.setCountry(supplier.getCountry());
                }
                if (supplier.getEmail() != null) {
                    existingSupplier.setEmail(supplier.getEmail());
                }
                if (supplier.getTransactionID() != null) {
                    existingSupplier.setTransactionID(supplier.getTransactionID());
                }
                if (supplier.getLocationCode() != null) {
                    existingSupplier.setLocationCode(supplier.getLocationCode());
                }
                if (supplier.getTenantCode() != null) {
                    existingSupplier.setTenantCode(supplier.getTenantCode());
                }

                return existingSupplier;
            })
            .map(supplierRepository::save);
    }

    /**
     * Get all the suppliers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Supplier> findAll(Pageable pageable) {
        log.debug("Request to get all Suppliers");
        return supplierRepository.findAll(pageable);
    }

    /**
     * Get one supplier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Supplier> findOne(Long id) {
        log.debug("Request to get Supplier : {}", id);
        return supplierRepository.findById(id);
    }

    /**
     * Delete the supplier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Supplier : {}", id);
        supplierRepository.deleteById(id);
    }
}
