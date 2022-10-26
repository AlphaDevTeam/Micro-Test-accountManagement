package com.alphadevs.wikunum.services.repository;

import com.alphadevs.wikunum.services.domain.SupplierAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SupplierAccount entity.
 */
@Repository
public interface SupplierAccountRepository extends JpaRepository<SupplierAccount, Long>, JpaSpecificationExecutor<SupplierAccount> {
    default Optional<SupplierAccount> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SupplierAccount> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SupplierAccount> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct supplierAccount from SupplierAccount supplierAccount left join fetch supplierAccount.supplier",
        countQuery = "select count(distinct supplierAccount) from SupplierAccount supplierAccount"
    )
    Page<SupplierAccount> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct supplierAccount from SupplierAccount supplierAccount left join fetch supplierAccount.supplier")
    List<SupplierAccount> findAllWithToOneRelationships();

    @Query(
        "select supplierAccount from SupplierAccount supplierAccount left join fetch supplierAccount.supplier where supplierAccount.id =:id"
    )
    Optional<SupplierAccount> findOneWithToOneRelationships(@Param("id") Long id);
}
