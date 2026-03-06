package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.exception.EntityNotFoundException;
import com.bank.publicinfo.mapper.BranchMapper;
import com.bank.publicinfo.model.BankDetailsEntity;
import com.bank.publicinfo.model.BranchEntity;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.repository.BranchRepository;
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
class BranchServiceImplTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private BankDetailsRepository bankDetailsRepository;

    @Mock
    private BranchMapper branchMapper;

    @InjectMocks
    private BranchServiceImpl branchService;

    private BranchDto branchDto;
    private BranchEntity branchEntity;
    private BankDetailsEntity bankDetailsEntity;

    @BeforeEach
    void setUp() {
        branchDto = BranchDto.builder()
                .id(1L)
                .address("Test Address")
                .phone("+7(495)123-45-67")
                .bankDetailsId(1L)
                .build();

        branchEntity = new BranchEntity();
        branchEntity.setId(1L);
        branchEntity.setAddress("Test Address");

        bankDetailsEntity = new BankDetailsEntity();
        bankDetailsEntity.setId(1L);
    }

    @Test
    void findAll_ShouldReturnAllBranches() {
        when(branchRepository.findAll()).thenReturn(Arrays.asList(branchEntity));
        when(branchMapper.toDto(branchEntity)).thenReturn(branchDto);

        List<BranchDto> result = branchService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findById_WhenExists_ShouldReturnBranch() {
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branchEntity));
        when(branchMapper.toDto(branchEntity)).thenReturn(branchDto);

        BranchDto result = branchService.findById(1L);

        assertNotNull(result);
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> branchService.findById(1L));
    }

    @Test
    void findByBankDetailsId_ShouldReturnBranches() {
        when(branchRepository.findByBankDetailsId(1L)).thenReturn(Arrays.asList(branchEntity));
        when(branchMapper.toDto(branchEntity)).thenReturn(branchDto);

        List<BranchDto> result = branchService.findByBankDetailsId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void create_ShouldCreateBranch() {
        when(bankDetailsRepository.findById(1L)).thenReturn(Optional.of(bankDetailsEntity));
        when(branchMapper.toEntity(branchDto)).thenReturn(branchEntity);
        when(branchRepository.save(branchEntity)).thenReturn(branchEntity);
        when(branchMapper.toDto(branchEntity)).thenReturn(branchDto);

        BranchDto result = branchService.create(branchDto);

        assertNotNull(result);
        verify(branchRepository, times(1)).save(branchEntity);
    }

    @Test
    void update_ShouldUpdateBranch() {
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branchEntity));
        when(branchRepository.save(any())).thenReturn(branchEntity);
        when(branchMapper.toDto(any())).thenReturn(branchDto);

        BranchDto result = branchService.update(1L, branchDto);

        assertNotNull(result);
    }

    @Test
    void delete_ShouldDeleteBranch() {
        when(branchRepository.existsById(1L)).thenReturn(true);
        doNothing().when(branchRepository).deleteById(1L);

        branchService.delete(1L);

        verify(branchRepository, times(1)).deleteById(1L);
    }
}
