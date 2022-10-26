package com.alphadevs.wikunum.services.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.alphadevs.wikunum.services.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SupplierAccountBalanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierAccountBalance.class);
        SupplierAccountBalance supplierAccountBalance1 = new SupplierAccountBalance();
        supplierAccountBalance1.setId(1L);
        SupplierAccountBalance supplierAccountBalance2 = new SupplierAccountBalance();
        supplierAccountBalance2.setId(supplierAccountBalance1.getId());
        assertThat(supplierAccountBalance1).isEqualTo(supplierAccountBalance2);
        supplierAccountBalance2.setId(2L);
        assertThat(supplierAccountBalance1).isNotEqualTo(supplierAccountBalance2);
        supplierAccountBalance1.setId(null);
        assertThat(supplierAccountBalance1).isNotEqualTo(supplierAccountBalance2);
    }
}
