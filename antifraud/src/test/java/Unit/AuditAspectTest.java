package Unit;

import AOP.TestableAuditAspect;
import config.ApplicationConstant;
import config.TestConstants;
import dto.SuspiciousCardTransferDto;
import mappers.SuspiciousTransferMapper;
import model.SuspiciousCardTransfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import repository.SuspiciousAccountTransferRepository;
import repository.SuspiciousCardTransferRepository;
import repository.SuspiciousPhoneTransferRepository;
import service.SuspiciousTransferService;
import service.SuspiciousTransferServiceImpl;

import java.util.Optional;

import static config.TestConstants.GET_CONTAINS_CARD_ID;
import static config.TestConstants.ASPECT_TYPE_CARD;
import static config.TestConstants.ASPECT_METHOD_CREATE_CARD;
import static config.TestConstants.ID_VALID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditAspectTest {

    @Spy
    private TestableAuditAspect auditAspect;

    @Mock private SuspiciousCardTransferRepository cardRepo;
    @Mock private SuspiciousPhoneTransferRepository phoneRepo;
    @Mock private SuspiciousAccountTransferRepository accountRepo;
    @Mock private SuspiciousTransferMapper mapper;
    private SuspiciousTransferService proxyService;

    @BeforeEach
    void setUp() {
        SuspiciousTransferServiceImpl realService = new SuspiciousTransferServiceImpl(
                cardRepo, phoneRepo, accountRepo, mapper
        );
        AspectJProxyFactory factory = new AspectJProxyFactory(realService);
        factory.setProxyTargetClass(true);
        factory.addAspect(auditAspect);
        proxyService = factory.getProxy();
    }

    @Test
    @DisplayName("Aspect: Log CREATE Operation")
    void logCreate() {
        SuspiciousCardTransferDto dto = getDto();
        when(mapper.toCardEntity(any())).thenReturn(new SuspiciousCardTransfer());
        when(cardRepo.save(any())).thenReturn(new SuspiciousCardTransfer());
        when(mapper.toCardDto(any())).thenReturn(dto);
        proxyService.createCard(dto);

        verify(auditAspect).info(
                eq(ApplicationConstant.MSG_CREATED),
                eq(ASPECT_TYPE_CARD),
                eq(ASPECT_METHOD_CREATE_CARD),
                anyString()
        );
    }

    @Test
    @DisplayName("Aspect: Log UPDATE Operation")
    void logUpdate() {
        SuspiciousCardTransferDto dto = getDto();
        when(cardRepo.findById(anyLong())).thenReturn(Optional.of(new SuspiciousCardTransfer()));
        when(cardRepo.save(any())).thenReturn(new SuspiciousCardTransfer());
        when(mapper.toCardDto(any())).thenReturn(dto);
        proxyService.updateCard(ID_VALID, dto);
        verify(auditAspect).warn(
                eq(ApplicationConstant.MSG_UPDATED),
                eq(ASPECT_TYPE_CARD),
                eq(ID_VALID),
                anyString()
        );
    }

    @Test
    @DisplayName("Aspect: Log DELETE Operation")
    void logDelete() {
        doNothing().when(cardRepo).deleteById(anyLong());
        proxyService.deleteSuspiciousTransfer(ID_VALID, ApplicationConstant.TYPE_CARD);
        verify(auditAspect).info(
                eq(ApplicationConstant.MSG_DELETED),
                eq(ApplicationConstant.TYPE_CARD),
                eq(ID_VALID)
        );
    }

    @Test
    @DisplayName("Aspect: Log EXCEPTION")
    void logException() {
        RuntimeException ex = new RuntimeException("DB Error");
        when(cardRepo.findById(anyLong())).thenThrow(ex);
        try {
            proxyService.getCardById(ID_VALID);
        } catch (Exception ignored) {}
        verify(auditAspect).error(
                eq(ApplicationConstant.MSG_ERR_SERVICE),
                contains(GET_CONTAINS_CARD_ID),
                eq(ex.getMessage())
        );
    }

    private SuspiciousCardTransferDto getDto() {
        return SuspiciousCardTransferDto.builder()
                .id(TestConstants.ID_VALID)
                .blocked(true)
                .suspicious(true)
                .build();
    }
}