package com.alphadevs.wikunum.services.repository;

import com.alphadevs.wikunum.services.domain.CustomerAccountBalance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerAccountBalance entity.
 */
@Repository
public interface CustomerAccountBalanceRepository
    extends JpaRepository<CustomerAccountBalance, Long>, JpaSpecificationExecutor<CustomerAccountBalance> {
    default Optional<CustomerAccountBalance> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CustomerAccountBalance> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CustomerAccountBalance> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct customerAccountBalance from CustomerAccountBalance customerAccountBalance left join fetch customerAccountBalance.customer",
        countQuery = "select count(distinct customerAccountBalance) from CustomerAccountBalance customerAccountBalance"
    )
    Page<CustomerAccountBalance> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct customerAccountBalance from CustomerAccountBalance customerAccountBalance left join fetch customerAccountBalance.customer"
    )
    List<CustomerAccountBalance> findAllWithToOneRelationships();

    @Query(
        "select customerAccountBalance from CustomerAccountBalance customerAccountBalance left join fetch customerAccountBalance.customer where customerAccountBalance.id =:id"
    )
    Optional<CustomerAccountBalance> findOneWithToOneRelationships(@Param("id") Long id);
}
