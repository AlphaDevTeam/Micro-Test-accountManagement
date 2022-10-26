package com.alphadevs.wikunum.services.service;

import com.alphadevs.wikunum.services.domain.*; // for static metamodels
import com.alphadevs.wikunum.services.domain.SupplierAccount;
import com.alphadevs.wikunum.services.repository.SupplierAccountRepository;
import com.alphadevs.wikunum.services.service.criteria.SupplierAccountCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SupplierAccount} entities in the database.
 * The main input is a {@link SupplierAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SupplierAccount} or a {@link Page} of {@link SupplierAccount} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierAccountQueryService extends QueryService<SupplierAccount> {

    private final Logger log = LoggerFactory.getLogger(SupplierAccountQueryService.class);

    private final SupplierAccountRepository supplierAccountRepository;

    public SupplierAccountQueryService(SupplierAccountRepository supplierAccountRepository) {
        this.supplierAccountRepository = supplierAccountRepository;
    }

    /**
     * Return a {@link List} of {@link SupplierAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SupplierAccount> findByCriteria(SupplierAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SupplierAccount> specification = createSpecification(criteria);
        return supplierAccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SupplierAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierAccount> findByCriteria(SupplierAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SupplierAccount> specification = createSpecification(criteria);
        return supplierAccountRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierAccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SupplierAccount> specification = createSpecification(criteria);
        return supplierAccountRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierAccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SupplierAccount> createSpecification(SupplierAccountCriteria criteria) {
        Specification<SupplierAccount> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SupplierAccount_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), SupplierAccount_.description));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), SupplierAccount_.transactionDate));
            }
            if (criteria.getCreditAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreditAmount(), SupplierAccount_.creditAmount));
            }
            if (criteria.getDebitAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDebitAmount(), SupplierAccount_.debitAmount));
            }
            if (criteria.getBalanceAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalanceAmount(), SupplierAccount_.balanceAmount));
            }
            if (criteria.getTransactionID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionID(), SupplierAccount_.transactionID));
            }
            if (criteria.getLocationCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocationCode(), SupplierAccount_.locationCode));
            }
            if (criteria.getTenantCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantCode(), SupplierAccount_.tenantCode));
            }
            if (criteria.getSupplierId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSupplierId(),
                            root -> root.join(SupplierAccount_.supplier, JoinType.LEFT).get(Supplier_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
