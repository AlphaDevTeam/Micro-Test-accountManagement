package com.alphadevs.wikunum.services.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.alphadevs.wikunum.services.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SupplierAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierAccount.class);
        SupplierAccount supplierAccount1 = new SupplierAccount();
        supplierAccount1.setId(1L);
        SupplierAccount supplierAccount2 = new SupplierAccount();
        supplierAccount2.setId(supplierAccount1.getId());
        assertThat(supplierAccount1).isEqualTo(supplierAccount2);
        supplierAccount2.setId(2L);
        assertThat(supplierAccount1).isNotEqualTo(supplierAccount2);
        supplierAccount1.setId(null);
        assertThat(supplierAccount1).isNotEqualTo(supplierAccount2);
    }
}
