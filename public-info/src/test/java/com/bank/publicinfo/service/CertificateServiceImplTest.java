package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.exception.EntityNotFoundException;
import com.bank.publicinfo.mapper.CertificateMapper;
import com.bank.publicinfo.model.BankDetailsEntity;
import com.bank.publicinfo.model.CertificateEntity;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.repository.CertificateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

    @Mock
    private CertificateRepository certificateRepository;

    @Mock
    private BankDetailsRepository bankDetailsRepository;

    @Mock
    private CertificateMapper certificateMapper;

    @InjectMocks
    private CertificateServiceImpl certificateService;

    private CertificateDto certificateDto;
    private CertificateEntity certificateEntity;
    private BankDetailsEntity bankDetailsEntity;

    @BeforeEach
    void setUp() {
        certificateDto = CertificateDto.builder()
                .id(1L)
                .certificateNumber("CERT-123456")
                .certificateType("SSL")
                .organizationName("Test Org")
                .dateOfIssue(LocalDateTime.now())
                .status("Active")
                .bankDetailsId(1L)
                .build();

        certificateEntity = new CertificateEntity();
        certificateEntity.setId(1L);
        certificateEntity.setCertificateNumber("CERT-123456");

        bankDetailsEntity = new BankDetailsEntity();
        bankDetailsEntity.setId(1L);
    }

    @Test
    void findAll_ShouldReturnAllCertificates() {
        when(certificateRepository.findAll()).thenReturn(Arrays.asList(certificateEntity));
        when(certificateMapper.toDto(certificateEntity)).thenReturn(certificateDto);

        List<CertificateDto> result = certificateService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findById_WhenExists_ShouldReturnCertificate() {
        when(certificateRepository.findById(1L)).thenReturn(Optional.of(certificateEntity));
        when(certificateMapper.toDto(certificateEntity)).thenReturn(certificateDto);

        CertificateDto result = certificateService.findById(1L);

        assertNotNull(result);
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        when(certificateRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> certificateService.findById(1L));
    }

    @Test
    void findByBankDetailsId_ShouldReturnCertificates() {
        when(certificateRepository.findByBankDetailsId(1L)).thenReturn(Arrays.asList(certificateEntity));
        when(certificateMapper.toDto(certificateEntity)).thenReturn(certificateDto);

        List<CertificateDto> result = certificateService.findByBankDetailsId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void create_WhenCertificateNumberNotExists_ShouldCreateCertificate() {
        when(certificateRepository.existsByCertificateNumber("CERT-123456")).thenReturn(false);
        when(bankDetailsRepository.findById(1L)).thenReturn(Optional.of(bankDetailsEntity));
        when(certificateMapper.toEntity(certificateDto)).thenReturn(certificateEntity);
        when(certificateRepository.save(certificateEntity)).thenReturn(certificateEntity);
        when(certificateMapper.toDto(certificateEntity)).thenReturn(certificateDto);

        CertificateDto result = certificateService.create(certificateDto);

        assertNotNull(result);
    }

    @Test
    void create_WhenCertificateNumberExists_ShouldThrowException() {
        when(certificateRepository.existsByCertificateNumber("CERT-123456")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> certificateService.create(certificateDto));
    }

    @Test
    void update_ShouldUpdateCertificate() {
        when(certificateRepository.findById(1L)).thenReturn(Optional.of(certificateEntity));
        when(certificateRepository.save(any())).thenReturn(certificateEntity);
        when(certificateMapper.toDto(any())).thenReturn(certificateDto);

        CertificateDto result = certificateService.update(1L, certificateDto);

        assertNotNull(result);
    }

    @Test
    void delete_ShouldDeleteCertificate() {
        when(certificateRepository.existsById(1L)).thenReturn(true);
        doNothing().when(certificateRepository).deleteById(1L);

        certificateService.delete(1L);

        verify(certificateRepository, times(1)).deleteById(1L);
    }
}
