package com.alphadevs.wikunum.services.service;

import com.alphadevs.wikunum.services.domain.*; // for static metamodels
import com.alphadevs.wikunum.services.domain.SupplierAccountBalance;
import com.alphadevs.wikunum.services.repository.SupplierAccountBalanceRepository;
import com.alphadevs.wikunum.services.service.criteria.SupplierAccountBalanceCriteria;
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
 * Service for executing complex queries for {@link SupplierAccountBalance} entities in the database.
 * The main input is a {@link SupplierAccountBalanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SupplierAccountBalance} or a {@link Page} of {@link SupplierAccountBalance} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierAccountBalanceQueryService extends QueryService<SupplierAccountBalance> {

    private final Logger log = LoggerFactory.getLogger(SupplierAccountBalanceQueryService.class);

    private final SupplierAccountBalanceRepository supplierAccountBalanceRepository;

    public SupplierAccountBalanceQueryService(SupplierAccountBalanceRepository supplierAccountBalanceRepository) {
        this.supplierAccountBalanceRepository = supplierAccountBalanceRepository;
    }

    /**
     * Return a {@link List} of {@link SupplierAccountBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SupplierAccountBalance> findByCriteria(SupplierAccountBalanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SupplierAccountBalance> specification = createSpecification(criteria);
        return supplierAccountBalanceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SupplierAccountBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierAccountBalance> findByCriteria(SupplierAccountBalanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SupplierAccountBalance> specification = createSpecification(criteria);
        return supplierAccountBalanceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierAccountBalanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SupplierAccountBalance> specification = createSpecification(criteria);
        return supplierAccountBalanceRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierAccountBalanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SupplierAccountBalance> createSpecification(SupplierAccountBalanceCriteria criteria) {
        Specification<SupplierAccountBalance> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SupplierAccountBalance_.id));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), SupplierAccountBalance_.balance));
            }
            if (criteria.getLocationCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLocationCode(), SupplierAccountBalance_.locationCode));
            }
            if (criteria.getTenantCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantCode(), SupplierAccountBalance_.tenantCode));
            }
            if (criteria.getSupplierId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSupplierId(),
                            root -> root.join(SupplierAccountBalance_.supplier, JoinType.LEFT).get(Supplier_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
