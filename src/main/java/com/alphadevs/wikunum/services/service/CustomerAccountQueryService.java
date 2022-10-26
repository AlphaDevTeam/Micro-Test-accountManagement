package com.alphadevs.wikunum.services.service;

import com.alphadevs.wikunum.services.domain.*; // for static metamodels
import com.alphadevs.wikunum.services.domain.CustomerAccount;
import com.alphadevs.wikunum.services.repository.CustomerAccountRepository;
import com.alphadevs.wikunum.services.service.criteria.CustomerAccountCriteria;
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
 * Service for executing complex queries for {@link CustomerAccount} entities in the database.
 * The main input is a {@link CustomerAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerAccount} or a {@link Page} of {@link CustomerAccount} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerAccountQueryService extends QueryService<CustomerAccount> {

    private final Logger log = LoggerFactory.getLogger(CustomerAccountQueryService.class);

    private final CustomerAccountRepository customerAccountRepository;

    public CustomerAccountQueryService(CustomerAccountRepository customerAccountRepository) {
        this.customerAccountRepository = customerAccountRepository;
    }

    /**
     * Return a {@link List} of {@link CustomerAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerAccount> findByCriteria(CustomerAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomerAccount> specification = createSpecification(criteria);
        return customerAccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CustomerAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerAccount> findByCriteria(CustomerAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerAccount> specification = createSpecification(criteria);
        return customerAccountRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerAccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomerAccount> specification = createSpecification(criteria);
        return customerAccountRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerAccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerAccount> createSpecification(CustomerAccountCriteria criteria) {
        Specification<CustomerAccount> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerAccount_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CustomerAccount_.description));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), CustomerAccount_.transactionDate));
            }
            if (criteria.getCreditAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreditAmount(), CustomerAccount_.creditAmount));
            }
            if (criteria.getDebitAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDebitAmount(), CustomerAccount_.debitAmount));
            }
            if (criteria.getBalanceAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalanceAmount(), CustomerAccount_.balanceAmount));
            }
            if (criteria.getTransactionID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionID(), CustomerAccount_.transactionID));
            }
            if (criteria.getLocationCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocationCode(), CustomerAccount_.locationCode));
            }
            if (criteria.getTenantCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantCode(), CustomerAccount_.tenantCode));
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(CustomerAccount_.customer, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
