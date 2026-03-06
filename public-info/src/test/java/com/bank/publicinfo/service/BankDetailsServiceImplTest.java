package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.exception.EntityNotFoundException;
import com.bank.publicinfo.mapper.BankDetailsMapper;
import com.bank.publicinfo.model.BankDetailsEntity;
import com.bank.publicinfo.repository.BankDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankDetailsServiceImplTest {

    @Mock
    private BankDetailsRepository bankDetailsRepository;

    @Mock
    private BankDetailsMapper bankDetailsMapper;

    @InjectMocks
    private BankDetailsServiceImpl bankDetailsService;

    private BankDetailsDto bankDetailsDto;
    private BankDetailsEntity bankDetailsEntity;

    @BeforeEach
    void setUp() {
        bankDetailsDto = BankDetailsDto.builder()
                .id(1L)
                .bic("123456789")
                .inn("1234567890")
                .kpp("123456789")
                .bankName("Test Bank")
                .build();

        bankDetailsEntity = new BankDetailsEntity();
        bankDetailsEntity.setId(1L);
        bankDetailsEntity.setBic("123456789");
        bankDetailsEntity.setInn("1234567890");
        bankDetailsEntity.setKpp("123456789");
        bankDetailsEntity.setBankName("Test Bank");
    }

    @Test
    void findAll_ShouldReturnAllBanks() {
        when(bankDetailsRepository.findAll()).thenReturn(Arrays.asList(bankDetailsEntity));
        when(bankDetailsMapper.toDto(bankDetailsEntity)).thenReturn(bankDetailsDto);

        List<BankDetailsDto> result = bankDetailsService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bankDetailsRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenExists_ShouldReturnBank() {
        when(bankDetailsRepository.findById(1L)).thenReturn(Optional.of(bankDetailsEntity));
        when(bankDetailsMapper.toDto(bankDetailsEntity)).thenReturn(bankDetailsDto);

        BankDetailsDto result = bankDetailsService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Bank", result.getBankName());
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        when(bankDetailsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bankDetailsService.findById(1L));
    }

    @Test
    void create_WhenBicNotExists_ShouldCreateBank() {
        when(bankDetailsRepository.existsByBic("123456789")).thenReturn(false);
        when(bankDetailsMapper.toEntity(bankDetailsDto)).thenReturn(bankDetailsEntity);
        when(bankDetailsRepository.save(bankDetailsEntity)).thenReturn(bankDetailsEntity);
        when(bankDetailsMapper.toDto(bankDetailsEntity)).thenReturn(bankDetailsDto);

        BankDetailsDto result = bankDetailsService.create(bankDetailsDto);

        assertNotNull(result);
        verify(bankDetailsRepository, times(1)).save(bankDetailsEntity);
    }

    @Test
    void create_WhenBicExists_ShouldThrowException() {
        when(bankDetailsRepository.existsByBic("123456789")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> bankDetailsService.create(bankDetailsDto));
    }

    @Test
    void update_WhenExists_ShouldUpdateBank() {
        when(bankDetailsRepository.findById(1L)).thenReturn(Optional.of(bankDetailsEntity));
        when(bankDetailsRepository.save(any())).thenReturn(bankDetailsEntity);
        when(bankDetailsMapper.toDto(any())).thenReturn(bankDetailsDto);

        BankDetailsDto result = bankDetailsService.update(1L, bankDetailsDto);

        assertNotNull(result);
        verify(bankDetailsRepository, times(1)).save(bankDetailsEntity);
    }

    @Test
    void update_WhenNotExists_ShouldThrowException() {
        when(bankDetailsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bankDetailsService.update(1L, bankDetailsDto));
    }

    @Test
    void delete_WhenExists_ShouldDeleteBank() {
        when(bankDetailsRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bankDetailsRepository).deleteById(1L);

        bankDetailsService.delete(1L);

        verify(bankDetailsRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_WhenNotExists_ShouldThrowException() {
        when(bankDetailsRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> bankDetailsService.delete(1L));
    }
}
