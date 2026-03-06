package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.mapper.AuditMapper;
import com.bank.publicinfo.model.AuditEntity;
import com.bank.publicinfo.repository.AuditRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private AuditMapper auditMapper;

    @InjectMocks
    private AuditServiceImpl auditService;

    private AuditDto auditDto;
    private AuditEntity auditEntity;

    @BeforeEach
    void setUp() {
        auditDto = AuditDto.builder()
                .id(1L)
                .entityType("BankDetails")
                .operationType("CREATE")
                .createdBy("testuser")
                .entityJson("{}")
                .build();

        auditEntity = new AuditEntity();
        auditEntity.setId(1L);
        auditEntity.setEntityType("BankDetails");
        auditEntity.setOperationType("CREATE");
    }

    @Test
    void findAll_ShouldReturnAllAudits() {
        when(auditRepository.findAll()).thenReturn(Arrays.asList(auditEntity));
        when(auditMapper.toDto(auditEntity)).thenReturn(auditDto);

        List<AuditDto> result = auditService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findById_WhenExists_ShouldReturnAudit() {
        when(auditRepository.findById(1L)).thenReturn(Optional.of(auditEntity));
        when(auditMapper.toDto(auditEntity)).thenReturn(auditDto);

        AuditDto result = auditService.findById(1L);

        assertNotNull(result);
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        when(auditRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> auditService.findById(1L));
    }

    @Test
    void findByEntityType_ShouldReturnAudits() {
        when(auditRepository.findByEntityType("BankDetails")).thenReturn(Arrays.asList(auditEntity));
        when(auditMapper.toDto(auditEntity)).thenReturn(auditDto);

        List<AuditDto> result = auditService.findByEntityType("BankDetails");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void save_ShouldSaveAudit() {
        when(auditMapper.toEntity(auditDto)).thenReturn(auditEntity);
        doNothing().when(auditRepository).save(auditEntity);

        auditService.save(auditDto);

        verify(auditRepository, times(1)).save(auditEntity);
    }
}
