package com.bank.publicinfo.utils;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.dto.LicenseDto;

import java.time.LocalDateTime;

public class TestConstants {

    public static final Long TEST_ID = 1L;
    public static final String TEST_BIC = "123456789";
    public static final String TEST_INN = "1234567890";
    public static final String TEST_KPP = "123456789";
    public static final String TEST_BANK_NAME = "Test Bank";
    public static final String TEST_ADDRESS = "Test Address";
    public static final String TEST_PHONE = "+7(495)123-45-67";
    public static final String TEST_EMAIL = "test@bank.com";

    public static BankDetailsDto createTestBankDetailsDto() {
        return BankDetailsDto.builder()
                .id(TEST_ID)
                .bic(TEST_BIC)
                .inn(TEST_INN)
                .kpp(TEST_KPP)
                .bankName(TEST_BANK_NAME)
                .address(TEST_ADDRESS)
                .phone(TEST_PHONE)
                .email(TEST_EMAIL)
                .build();
    }

    public static BankDetailsDto createTestBankDetailsDtoWithoutId() {
        return BankDetailsDto.builder()
                .bic(TEST_BIC)
                .inn(TEST_INN)
                .kpp(TEST_KPP)
                .bankName(TEST_BANK_NAME)
                .address(TEST_ADDRESS)
                .phone(TEST_PHONE)
                .email(TEST_EMAIL)
                .build();
    }

    public static BranchDto createTestBranchDto() {
        return BranchDto.builder()
                .id(TEST_ID)
                .address(TEST_ADDRESS)
                .phone(TEST_PHONE)
                .email(TEST_EMAIL)
                .startTime("09:00")
                .endTime("18:00")
                .serviceType("Full")
                .bankDetailsId(TEST_ID)
                .build();
    }

    public static AtmDto createTestAtmDto() {
        return AtmDto.builder()
                .id(TEST_ID)
                .address(TEST_ADDRESS)
                .atmName("ATM-001")
                .allDay(true)
                .availability(true)
                .bankDetailsId(TEST_ID)
                .build();
    }

    public static LicenseDto createTestLicenseDto() {
        return LicenseDto.builder()
                .id(TEST_ID)
                .licenseNumber("L-123456")
                .licenseType("General")
                .dateOfIssue(LocalDateTime.now())
                .status("Active")
                .bankDetailsId(TEST_ID)
                .build();
    }

    public static CertificateDto createTestCertificateDto() {
        return CertificateDto.builder()
                .id(TEST_ID)
                .certificateNumber("CERT-123456")
                .certificateType("SSL")
                .organizationName("Test Org")
                .dateOfIssue(LocalDateTime.now())
                .status("Active")
                .bankDetailsId(TEST_ID)
                .build();
    }

    public static AuditDto createTestAuditDto() {
        return AuditDto.builder()
                .id(TEST_ID)
                .entityType("BankDetails")
                .operationType("CREATE")
                .createdBy("testuser")
                .entityJson("{}")
                .build();
    }

    private TestConstants() {
    }
}
