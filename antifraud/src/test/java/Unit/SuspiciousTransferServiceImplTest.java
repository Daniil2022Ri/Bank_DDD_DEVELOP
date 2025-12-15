package Unit;

import config.TestConstants;
import dto.SuspiciousAccountTransferDto;
import dto.SuspiciousCardTransferDto;
import dto.SuspiciousPhoneTransferDto;
import jakarta.persistence.EntityNotFoundException;
import mappers.SuspiciousAccountTransferMapper;
import mappers.SuspiciousCardTransferMapper;
import mappers.SuspiciousPhoneTransferMapper;
import model.SuspiciousAccountTransfer;
import model.SuspiciousCardTransfer;
import model.SuspiciousPhoneTransfer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.SuspiciousAccountTransferRepository;
import repository.SuspiciousCardTransferRepository;
import repository.SuspiciousPhoneTransferRepository;
import service.SuspiciousTransferServiceImpl;

import java.util.List;
import java.util.Optional;

import static config.ApplicationConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuspiciousTransferServiceImplTest {

    @Mock
    private SuspiciousCardTransferRepository cardRepo;

    @Mock
    private SuspiciousPhoneTransferRepository phoneRepo;

    @Mock
    private SuspiciousAccountTransferRepository accountRepo;

    @Mock
    private SuspiciousCardTransferMapper cardMapper;

    @Mock
    private SuspiciousPhoneTransferMapper phoneMapper;

    @Mock
    private SuspiciousAccountTransferMapper accountMapper;

    @InjectMocks
    private SuspiciousTransferServiceImpl service;

    @Test
    @DisplayName("Создание карты: Успех")
    void createCard_Success() {
        SuspiciousCardTransferDto dto = createCardDto();
        SuspiciousCardTransfer entity = createCardEntity();
        SuspiciousCardTransfer savedEntity = createCardEntity();
        savedEntity.setId(TestConstants.ID_VALID);

        when(cardMapper.toEntity(dto)).thenReturn(entity);
        when(cardRepo.save(entity)).thenReturn(savedEntity);
        when(cardMapper.toDto(savedEntity)).thenReturn(dto);

        SuspiciousCardTransferDto result = service.createCard(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        verify(cardRepo).save(entity);
        verify(cardMapper).toEntity(dto);
        verify(cardMapper).toDto(savedEntity);
    }

    @Test
    @DisplayName("Обновление карты: Успех")
    void updateCard_Success() {
        Long id = TestConstants.ID_VALID;
        SuspiciousCardTransferDto dto = createCardDto().toBuilder()
                .blocked(true)
                .suspicious(true)
                .blockedReason("Новая причина")
                .build();

        SuspiciousCardTransfer existingEntity = createCardEntity();
        SuspiciousCardTransfer updatedEntity = createCardEntity();
        updatedEntity.setBlocked(true);
        updatedEntity.setSuspicious(true);
        updatedEntity.setBlockedReason("Новая причина");

        when(cardRepo.findById(id)).thenReturn(Optional.of(existingEntity));
        when(cardRepo.save(existingEntity)).thenReturn(updatedEntity);
        when(cardMapper.toDto(updatedEntity)).thenReturn(dto);

        SuspiciousCardTransferDto result = service.updateCard(id, dto);

        assertNotNull(result);
        assertTrue(result.isBlocked());
        assertTrue(result.isSuspicious());
        assertEquals("Новая причина", result.getBlockedReason());

        verify(cardRepo).findById(id);
        verify(cardRepo).save(existingEntity);
        assertEquals(true, existingEntity.isBlocked());
        assertEquals(true, existingEntity.isSuspicious());
        assertEquals("Новая причина", existingEntity.getBlockedReason());
    }

    @Test
    @DisplayName("Обновление карты: ID не найден -> Исключение")
    void updateCard_NotFound_ThrowsException() {
        Long id = TestConstants.ID_NOT_FOUND;
        SuspiciousCardTransferDto dto = createCardDto();

        when(cardRepo.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> service.updateCard(id, dto)
        );

        assertTrue(exception.getMessage().contains(ERR_CARD_NOT_FOUND));
        verify(cardRepo, never()).save(any());
    }

    @Test
    @DisplayName("Удаление карты: Успех")
    void deleteCard_Success() {
        Long id = TestConstants.ID_VALID;

        service.deleteSuspiciousTransfer(id, TYPE_CARD);

        verify(cardRepo).deleteById(id);
    }

    @Test
    @DisplayName("Удаление с неверным типом: Исключение")
    void delete_InvalidType_ThrowsException() {
        Long id = TestConstants.ID_VALID;
        String invalidType = "invalid";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.deleteSuspiciousTransfer(id, invalidType)
        );

        assertTrue(exception.getMessage().contains(ERR_INVALID_TYPE));
        verify(cardRepo, never()).deleteById(any());
    }

    @Test
    @DisplayName("Получение карты по ID: Успех")
    void getCardById_Success() {
        Long id = TestConstants.ID_VALID;
        SuspiciousCardTransfer entity = createCardEntity();
        SuspiciousCardTransferDto dto = createCardDto();

        when(cardRepo.findById(id)).thenReturn(Optional.of(entity));
        when(cardMapper.toDto(entity)).thenReturn(dto);

        SuspiciousCardTransferDto result = service.getCardById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(cardRepo).findById(id);
        verify(cardMapper).toDto(entity);
    }

    @Test
    @DisplayName("Получение карты по ID: Не найден -> Исключение")
    void getCardById_NotFound_ThrowsException() {
        Long id = TestConstants.ID_NOT_FOUND;

        when(cardRepo.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> service.getCardById(id)
        );

        assertTrue(exception.getMessage().contains(ERR_CARD_NOT_FOUND));
    }

    @Test
    @DisplayName("Получение всех карт: Успех")
    void getAllCards_Success() {
        List<SuspiciousCardTransfer> entities = List.of(createCardEntity());
        List<SuspiciousCardTransferDto> dtos = List.of(createCardDto());

        when(cardRepo.findAll()).thenReturn(entities);
        when(cardMapper.toDto(any(SuspiciousCardTransfer.class))).thenReturn(dtos.get(0));

        List<SuspiciousCardTransferDto> result = service.getAllCards();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cardRepo).findAll();
        verify(cardMapper).toDto(entities.get(0));
    }

    @Test
    @DisplayName("Создание телефона: Успех")
    void createPhone_Success() {
        SuspiciousPhoneTransferDto dto = createPhoneDto();
        SuspiciousPhoneTransfer entity = createPhoneEntity();
        SuspiciousPhoneTransfer savedEntity = createPhoneEntity();
        savedEntity.setId(TestConstants.ID_VALID);

        when(phoneMapper.toEntity(dto)).thenReturn(entity);
        when(phoneRepo.save(entity)).thenReturn(savedEntity);
        when(phoneMapper.toDto(savedEntity)).thenReturn(dto);

        SuspiciousPhoneTransferDto result = service.createPhone(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        verify(phoneRepo).save(entity);
    }

    @Test
    @DisplayName("Создание аккаунта: Успех")
    void createAccount_Success() {
        SuspiciousAccountTransferDto dto = createAccountDto();
        SuspiciousAccountTransfer entity = createAccountEntity();
        SuspiciousAccountTransfer savedEntity = createAccountEntity();
        savedEntity.setId(TestConstants.ID_VALID);

        when(accountMapper.toEntity(dto)).thenReturn(entity);
        when(accountRepo.save(entity)).thenReturn(savedEntity);
        when(accountMapper.toDto(savedEntity)).thenReturn(dto);

        SuspiciousAccountTransferDto result = service.createAccount(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        verify(accountRepo).save(entity);
    }

    private SuspiciousCardTransferDto createCardDto() {
        return SuspiciousCardTransferDto.builder()
                .id(TestConstants.ID_VALID)
                .blocked(TestConstants.BLOCKED_TRUE)
                .suspicious(TestConstants.SUSPICIOUS_TRUE)
                .blockedReason(TestConstants.REASON_BLOCKED)
                .suspiciousReason(TestConstants.REASON_SUSPICIOUS)
                .build();
    }

    private SuspiciousCardTransfer createCardEntity() {
        return SuspiciousCardTransfer.builder()
                .id(TestConstants.ID_VALID)
                .blocked(TestConstants.BLOCKED_TRUE)
                .suspicious(TestConstants.SUSPICIOUS_TRUE)
                .blockedReason(TestConstants.REASON_BLOCKED)
                .suspiciousReason(TestConstants.REASON_SUSPICIOUS)
                .build();
    }

    private SuspiciousPhoneTransferDto createPhoneDto() {
        return SuspiciousPhoneTransferDto.builder()
                .id(TestConstants.ID_VALID)
                .blocked(TestConstants.BLOCKED_TRUE)
                .suspicious(TestConstants.SUSPICIOUS_TRUE)
                .blockedReason(TestConstants.REASON_BLOCKED)
                .suspiciousReason(TestConstants.REASON_SUSPICIOUS)
                .build();
    }

    private SuspiciousPhoneTransfer createPhoneEntity() {
        return SuspiciousPhoneTransfer.builder()
                .id(TestConstants.ID_VALID)
                .blocked(TestConstants.BLOCKED_TRUE)
                .suspicious(TestConstants.SUSPICIOUS_TRUE)
                .blockedReason(TestConstants.REASON_BLOCKED)
                .suspiciousReason(TestConstants.REASON_SUSPICIOUS)
                .build();
    }

    private SuspiciousAccountTransferDto createAccountDto() {
        return SuspiciousAccountTransferDto.builder()
                .id(TestConstants.ID_VALID)
                .blocked(TestConstants.BLOCKED_TRUE)
                .suspicious(TestConstants.SUSPICIOUS_TRUE)
                .blockedReason(TestConstants.REASON_BLOCKED)
                .suspiciousReason(TestConstants.REASON_SUSPICIOUS)
                .build();
    }

    private SuspiciousAccountTransfer createAccountEntity() {
        return SuspiciousAccountTransfer.builder()
                .id(TestConstants.ID_VALID)
                .blocked(TestConstants.BLOCKED_TRUE)
                .suspicious(TestConstants.SUSPICIOUS_TRUE)
                .blockedReason(TestConstants.REASON_BLOCKED)
                .suspiciousReason(TestConstants.REASON_SUSPICIOUS)
                .build();
    }
}