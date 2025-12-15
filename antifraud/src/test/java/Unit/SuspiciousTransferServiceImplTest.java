package Unit;

import config.TestConstants;
import dto.SuspiciousCardTransferDto;
import jakarta.persistence.EntityNotFoundException;
import mappers.SuspiciousTransferMapper;
import model.SuspiciousCardTransfer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.SuspiciousCardTransferRepository;
import service.SuspiciousTransferServiceImpl;

import java.util.Optional;

import static config.ApplicationConstant.ERR_CARD_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuspiciousTransferServiceImplTest {


    @Mock
    private SuspiciousCardTransferRepository cardRepo;
    @Mock
    private SuspiciousTransferMapper mapper;

    @InjectMocks
    private SuspiciousTransferServiceImpl service;

    @Test
    @DisplayName("Создание карты: Успех")
    void createCard_Success() {
        SuspiciousCardTransferDto dto = createDto();
        SuspiciousCardTransfer entity = createEntity();
        SuspiciousCardTransfer savedEntity = createEntity();
        savedEntity.setId(TestConstants.ID_VALID);

        when(mapper.toCardEntity(dto)).thenReturn(entity);
        when(cardRepo.save(entity)).thenReturn(savedEntity);
        when(mapper.toCardDto(savedEntity)).thenReturn(dto);

        SuspiciousCardTransferDto result = service.createCard(dto);

        assertNotNull(result);
        verify(cardRepo).save(entity);
    }

    @Test
    @DisplayName("Обновление карты: ID не найден -> Ошибка")
    void updateCard_NotFound() {
        when(cardRepo.findById(TestConstants.ID_NOT_FOUND)).thenReturn(Optional.empty());

        SuspiciousCardTransferDto dto = createDto();

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.updateCard(TestConstants.ID_NOT_FOUND, dto));

        assertTrue(ex.getMessage().contains(ERR_CARD_NOT_FOUND));
        verify(cardRepo, never()).save(any());
    }

    private SuspiciousCardTransferDto createDto() {
        return SuspiciousCardTransferDto.builder()
                .blocked(TestConstants.BLOCKED_TRUE)
                .suspicious(TestConstants.SUSPICIOUS_FALSE)
                .build();
    }

    private SuspiciousCardTransfer createEntity() {
        return SuspiciousCardTransfer.builder()
                .blocked(TestConstants.BLOCKED_TRUE)
                .suspicious(TestConstants.SUSPICIOUS_FALSE)
                .build();
    }
}