package com.alphadevs.wikunum.services.repository;

import com.alphadevs.wikunum.services.domain.SupplierAccountBalance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SupplierAccountBalance entity.
 */
@Repository
public interface SupplierAccountBalanceRepository
    extends JpaRepository<SupplierAccountBalance, Long>, JpaSpecificationExecutor<SupplierAccountBalance> {
    default Optional<SupplierAccountBalance> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SupplierAccountBalance> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SupplierAccountBalance> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct supplierAccountBalance from SupplierAccountBalance supplierAccountBalance left join fetch supplierAccountBalance.supplier",
        countQuery = "select count(distinct supplierAccountBalance) from SupplierAccountBalance supplierAccountBalance"
    )
    Page<SupplierAccountBalance> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct supplierAccountBalance from SupplierAccountBalance supplierAccountBalance left join fetch supplierAccountBalance.supplier"
    )
    List<SupplierAccountBalance> findAllWithToOneRelationships();

    @Query(
        "select supplierAccountBalance from SupplierAccountBalance supplierAccountBalance left join fetch supplierAccountBalance.supplier where supplierAccountBalance.id =:id"
    )
    Optional<SupplierAccountBalance> findOneWithToOneRelationships(@Param("id") Long id);
}
