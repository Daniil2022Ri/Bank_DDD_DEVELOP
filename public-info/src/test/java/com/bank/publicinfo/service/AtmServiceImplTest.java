package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.exception.EntityNotFoundException;
import com.bank.publicinfo.mapper.AtmMapper;
import com.bank.publicinfo.model.AtmEntity;
import com.bank.publicinfo.model.BankDetailsEntity;
import com.bank.publicinfo.repository.AtmRepository;
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
class AtmServiceImplTest {

    @Mock
    private AtmRepository atmRepository;

    @Mock
    private BankDetailsRepository bankDetailsRepository;

    @Mock
    private AtmMapper atmMapper;

    @InjectMocks
    private AtmServiceImpl atmService;

    private AtmDto atmDto;
    private AtmEntity atmEntity;
    private BankDetailsEntity bankDetailsEntity;

    @BeforeEach
    void setUp() {
        atmDto = AtmDto.builder()
                .id(1L)
                .address("Test Address")
                .atmName("ATM-001")
                .allDay(true)
                .bankDetailsId(1L)
                .build();

        atmEntity = new AtmEntity();
        atmEntity.setId(1L);
        atmEntity.setAddress("Test Address");

        bankDetailsEntity = new BankDetailsEntity();
        bankDetailsEntity.setId(1L);
    }

    @Test
    void findAll_ShouldReturnAllAtms() {
        when(atmRepository.findAll()).thenReturn(Arrays.asList(atmEntity));
        when(atmMapper.toDto(atmEntity)).thenReturn(atmDto);

        List<AtmDto> result = atmService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findById_WhenExists_ShouldReturnAtm() {
        when(atmRepository.findById(1L)).thenReturn(Optional.of(atmEntity));
        when(atmMapper.toDto(atmEntity)).thenReturn(atmDto);

        AtmDto result = atmService.findById(1L);

        assertNotNull(result);
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        when(atmRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> atmService.findById(1L));
    }

    @Test
    void findByBankDetailsId_ShouldReturnAtms() {
        when(atmRepository.findByBankDetailsId(1L)).thenReturn(Arrays.asList(atmEntity));
        when(atmMapper.toDto(atmEntity)).thenReturn(atmDto);

        List<AtmDto> result = atmService.findByBankDetailsId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void create_ShouldCreateAtm() {
        when(bankDetailsRepository.findById(1L)).thenReturn(Optional.of(bankDetailsEntity));
        when(atmMapper.toEntity(atmDto)).thenReturn(atmEntity);
        when(atmRepository.save(atmEntity)).thenReturn(atmEntity);
        when(atmMapper.toDto(atmEntity)).thenReturn(atmDto);

        AtmDto result = atmService.create(atmDto);

        assertNotNull(result);
    }

    @Test
    void update_ShouldUpdateAtm() {
        when(atmRepository.findById(1L)).thenReturn(Optional.of(atmEntity));
        when(atmRepository.save(any())).thenReturn(atmEntity);
        when(atmMapper.toDto(any())).thenReturn(atmDto);

        AtmDto result = atmService.update(1L, atmDto);

        assertNotNull(result);
    }

    @Test
    void delete_ShouldDeleteAtm() {
        when(atmRepository.existsById(1L)).thenReturn(true);
        doNothing().when(atmRepository).deleteById(1L);

        atmService.delete(1L);

        verify(atmRepository, times(1)).deleteById(1L);
    }
}
