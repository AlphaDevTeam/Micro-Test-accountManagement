package com.alphadevs.wikunum.services.repository;

import com.alphadevs.wikunum.services.domain.CustomerAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerAccount entity.
 */
@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long>, JpaSpecificationExecutor<CustomerAccount> {
    default Optional<CustomerAccount> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CustomerAccount> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CustomerAccount> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct customerAccount from CustomerAccount customerAccount left join fetch customerAccount.customer",
        countQuery = "select count(distinct customerAccount) from CustomerAccount customerAccount"
    )
    Page<CustomerAccount> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct customerAccount from CustomerAccount customerAccount left join fetch customerAccount.customer")
    List<CustomerAccount> findAllWithToOneRelationships();

    @Query(
        "select customerAccount from CustomerAccount customerAccount left join fetch customerAccount.customer where customerAccount.id =:id"
    )
    Optional<CustomerAccount> findOneWithToOneRelationships(@Param("id") Long id);
}
