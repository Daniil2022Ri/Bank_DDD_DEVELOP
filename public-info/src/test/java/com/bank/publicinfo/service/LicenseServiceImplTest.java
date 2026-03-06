package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.exception.EntityNotFoundException;
import com.bank.publicinfo.mapper.LicenseMapper;
import com.bank.publicinfo.model.BankDetailsEntity;
import com.bank.publicinfo.model.LicenseEntity;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.repository.LicenseRepository;
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
class LicenseServiceImplTest {

    @Mock
    private LicenseRepository licenseRepository;

    @Mock
    private BankDetailsRepository bankDetailsRepository;

    @Mock
    private LicenseMapper licenseMapper;

    @InjectMocks
    private LicenseServiceImpl licenseService;

    private LicenseDto licenseDto;
    private LicenseEntity licenseEntity;
    private BankDetailsEntity bankDetailsEntity;

    @BeforeEach
    void setUp() {
        licenseDto = LicenseDto.builder()
                .id(1L)
                .licenseNumber("L-123456")
                .licenseType("General")
                .dateOfIssue(LocalDateTime.now())
                .status("Active")
                .bankDetailsId(1L)
                .build();

        licenseEntity = new LicenseEntity();
        licenseEntity.setId(1L);
        licenseEntity.setLicenseNumber("L-123456");

        bankDetailsEntity = new BankDetailsEntity();
        bankDetailsEntity.setId(1L);
    }

    @Test
    void findAll_ShouldReturnAllLicenses() {
        when(licenseRepository.findAll()).thenReturn(Arrays.asList(licenseEntity));
        when(licenseMapper.toDto(licenseEntity)).thenReturn(licenseDto);

        List<LicenseDto> result = licenseService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findById_WhenExists_ShouldReturnLicense() {
        when(licenseRepository.findById(1L)).thenReturn(Optional.of(licenseEntity));
        when(licenseMapper.toDto(licenseEntity)).thenReturn(licenseDto);

        LicenseDto result = licenseService.findById(1L);

        assertNotNull(result);
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        when(licenseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> licenseService.findById(1L));
    }

    @Test
    void findByBankDetailsId_ShouldReturnLicenses() {
        when(licenseRepository.findByBankDetailsId(1L)).thenReturn(Arrays.asList(licenseEntity));
        when(licenseMapper.toDto(licenseEntity)).thenReturn(licenseDto);

        List<LicenseDto> result = licenseService.findByBankDetailsId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void create_WhenLicenseNumberNotExists_ShouldCreateLicense() {
        when(licenseRepository.existsByLicenseNumber("L-123456")).thenReturn(false);
        when(bankDetailsRepository.findById(1L)).thenReturn(Optional.of(bankDetailsEntity));
        when(licenseMapper.toEntity(licenseDto)).thenReturn(licenseEntity);
        when(licenseRepository.save(licenseEntity)).thenReturn(licenseEntity);
        when(licenseMapper.toDto(licenseEntity)).thenReturn(licenseDto);

        LicenseDto result = licenseService.create(licenseDto);

        assertNotNull(result);
    }

    @Test
    void create_WhenLicenseNumberExists_ShouldThrowException() {
        when(licenseRepository.existsByLicenseNumber("L-123456")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> licenseService.create(licenseDto));
    }

    @Test
    void update_ShouldUpdateLicense() {
        when(licenseRepository.findById(1L)).thenReturn(Optional.of(licenseEntity));
        when(licenseRepository.save(any())).thenReturn(licenseEntity);
        when(licenseMapper.toDto(any())).thenReturn(licenseDto);

        LicenseDto result = licenseService.update(1L, licenseDto);

        assertNotNull(result);
    }

    @Test
    void delete_ShouldDeleteLicense() {
        when(licenseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(licenseRepository).deleteById(1L);

        licenseService.delete(1L);

        verify(licenseRepository, times(1)).deleteById(1L);
    }
}
