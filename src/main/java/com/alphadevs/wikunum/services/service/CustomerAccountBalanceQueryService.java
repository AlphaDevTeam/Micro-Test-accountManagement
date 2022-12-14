package com.alphadevs.wikunum.services.service;

import com.alphadevs.wikunum.services.domain.*; // for static metamodels
import com.alphadevs.wikunum.services.domain.CustomerAccountBalance;
import com.alphadevs.wikunum.services.repository.CustomerAccountBalanceRepository;
import com.alphadevs.wikunum.services.service.criteria.CustomerAccountBalanceCriteria;
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
 * Service for executing complex queries for {@link CustomerAccountBalance} entities in the database.
 * The main input is a {@link CustomerAccountBalanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerAccountBalance} or a {@link Page} of {@link CustomerAccountBalance} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerAccountBalanceQueryService extends QueryService<CustomerAccountBalance> {

    private final Logger log = LoggerFactory.getLogger(CustomerAccountBalanceQueryService.class);

    private final CustomerAccountBalanceRepository customerAccountBalanceRepository;

    public CustomerAccountBalanceQueryService(CustomerAccountBalanceRepository customerAccountBalanceRepository) {
        this.customerAccountBalanceRepository = customerAccountBalanceRepository;
    }

    /**
     * Return a {@link List} of {@link CustomerAccountBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerAccountBalance> findByCriteria(CustomerAccountBalanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomerAccountBalance> specification = createSpecification(criteria);
        return customerAccountBalanceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CustomerAccountBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerAccountBalance> findByCriteria(CustomerAccountBalanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerAccountBalance> specification = createSpecification(criteria);
        return customerAccountBalanceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerAccountBalanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomerAccountBalance> specification = createSpecification(criteria);
        return customerAccountBalanceRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerAccountBalanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerAccountBalance> createSpecification(CustomerAccountBalanceCriteria criteria) {
        Specification<CustomerAccountBalance> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerAccountBalance_.id));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), CustomerAccountBalance_.balance));
            }
            if (criteria.getLocationCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLocationCode(), CustomerAccountBalance_.locationCode));
            }
            if (criteria.getTenantCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantCode(), CustomerAccountBalance_.tenantCode));
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(CustomerAccountBalance_.customer, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
