package Unit;

import AOP.TestableAuditAspect;
import config.ApplicationConstant;
import dto.SuspiciousCardTransferDto;
import mappers.SuspiciousAccountTransferMapper;
import mappers.SuspiciousCardTransferMapper;
import mappers.SuspiciousPhoneTransferMapper;
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
import service.AuditService;
import service.SuspiciousTransferService;
import service.SuspiciousTransferServiceImpl;

import java.util.Optional;

import static config.TestConstants.ID_VALID;
import static config.TestConstants.BLOCKED_TRUE;
import static config.TestConstants.SUSPICIOUS_TRUE;
import static config.TestConstants.REASON_BLOCKED;
import static config.TestConstants.REASON_SUSPICIOUS;
import static config.TestConstants.CARD;
import static config.TestConstants.CREATE_CARD;
import static config.TestConstants.GET_CARD_BY_ID;
import static config.TestConstants.DATABASE_ERROR;
import static config.TestConstants.AUDIT_SERVICE;
import static config.TestConstants.FAILED_INJECT_AUDIT_SERVICE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditAspectTest {

    @Mock
    private TestableAuditAspect auditAspect;

    @Mock private SuspiciousCardTransferRepository cardRepo;
    @Mock private SuspiciousPhoneTransferRepository phoneRepo;
    @Mock private SuspiciousAccountTransferRepository accountRepo;
    @Mock private SuspiciousCardTransferMapper cardMapper;
    @Mock private SuspiciousPhoneTransferMapper phoneMapper;
    @Mock private SuspiciousAccountTransferMapper accountMapper;
    @Mock private AuditService auditService;
    private SuspiciousTransferService proxyService;

    @BeforeEach
    void setUp() {
        auditAspect = new TestableAuditAspect();
        try {
            var field = auditAspect.getClass().getSuperclass().getDeclaredField(AUDIT_SERVICE);
            field.setAccessible(true);
            field.set(auditAspect, auditService);
        } catch (Exception e) {
            throw new RuntimeException(FAILED_INJECT_AUDIT_SERVICE, e);
        }

        SuspiciousTransferServiceImpl realService = new SuspiciousTransferServiceImpl(
                cardRepo, phoneRepo, accountRepo,
                cardMapper, phoneMapper, accountMapper
        );

        AspectJProxyFactory factory = new AspectJProxyFactory(realService);
        factory.setProxyTargetClass(true);
        factory.addAspect(auditAspect);
        proxyService = factory.getProxy();
    }

    @Test
    @DisplayName("Aspect: Log CREATE Operation for Card")
    void logCreate() {
        SuspiciousCardTransferDto dto = createCardDto();
        SuspiciousCardTransfer entity = createCardEntity();
        SuspiciousCardTransfer savedEntity = createCardEntity();
        savedEntity.setId(ID_VALID);

        when(cardMapper.toEntity(dto)).thenReturn(entity);
        when(cardRepo.save(entity)).thenReturn(savedEntity);
        when(cardMapper.toDto(savedEntity)).thenReturn(dto);

        proxyService.createCard(dto);
        verify(auditAspect).info(
                eq(ApplicationConstant.MSG_CREATED),
                eq(CARD),
                eq(CREATE_CARD),
                anyString()
        );
    }


    @Test
    @DisplayName("Aspect: Log UPDATE Operation for Card")
    void logUpdateCardOperation() {
        SuspiciousCardTransferDto dto = createCardDto();
        SuspiciousCardTransfer entity = createCardEntity();

        when(cardRepo.findById(ID_VALID)).thenReturn(Optional.of(entity));
        when(cardRepo.save(entity)).thenReturn(entity);
        when(cardMapper.toDto(entity)).thenReturn(dto);

        proxyService.updateCard(ID_VALID, dto);

        verify(auditAspect).warn(
                eq(ApplicationConstant.MSG_UPDATED),
                eq(CARD),
                eq(ID_VALID),
                anyString()
        );
    }

    @Test
    @DisplayName("Aspect: Log DELETE Operation for Card")
    void logDeleteCardOperation() {
        doNothing().when(cardRepo).deleteById(ID_VALID);

        proxyService.deleteSuspiciousTransfer(ID_VALID, ApplicationConstant.TYPE_CARD);

        verify(auditAspect).info(
                eq(ApplicationConstant.MSG_DELETED),
                eq(ApplicationConstant.TYPE_CARD),
                eq(ID_VALID)
        );
    }

    @Test
    @DisplayName("Aspect: Log GET_BY_ID Operation")
    void logGetByIdOperation() {
        SuspiciousCardTransfer entity = createCardEntity();
        SuspiciousCardTransferDto dto = createCardDto();

        when(cardRepo.findById(ID_VALID)).thenReturn(Optional.of(entity));
        when(cardMapper.toDto(entity)).thenReturn(dto);

        proxyService.getCardById(ID_VALID);

        verify(auditAspect).debug(
                eq(ApplicationConstant.MSG_RECEIVED),
                eq(CARD),
                eq(ID_VALID)
        );
    }

    @Test
    @DisplayName("Aspect: Log EXCEPTION in service")
    void logExceptionInService() {
        when(cardRepo.findById(ID_VALID))
                .thenThrow(new RuntimeException(DATABASE_ERROR));

        try {
            proxyService.getCardById(ID_VALID);
        } catch (Exception ignored) {}

        verify(auditAspect).error(
                eq(ApplicationConstant.MSG_ERR_SERVICE),
                eq(GET_CARD_BY_ID),
                contains(DATABASE_ERROR)
        );
    }

    @Test
    @DisplayName("Aspect: Test captureOldEntity on update")
    void testCaptureOldEntity() throws Exception {
        SuspiciousCardTransferDto dto = createCardDto();
        SuspiciousCardTransfer entity = createCardEntity();

        when(cardRepo.findById(ID_VALID)).thenReturn(Optional.of(entity));
        when(cardRepo.save(entity)).thenReturn(entity);
        when(cardMapper.toDto(entity)).thenReturn(dto);

        proxyService.updateCard(ID_VALID, dto);

        verify(auditService).logUpdate(
                eq(CARD),
                any(),
                any(),
                anyString()
        );
    }

    private SuspiciousCardTransferDto createCardDto() {
        return SuspiciousCardTransferDto.builder()
                .id(ID_VALID)
                .blocked(BLOCKED_TRUE)
                .suspicious(SUSPICIOUS_TRUE)
                .blockedReason(REASON_BLOCKED)
                .suspiciousReason(REASON_SUSPICIOUS)
                .build();
    }

    private SuspiciousCardTransfer createCardEntity() {
        return SuspiciousCardTransfer.builder()
                .id(ID_VALID)
                .blocked(BLOCKED_TRUE)
                .suspicious(SUSPICIOUS_TRUE)
                .blockedReason(REASON_BLOCKED)
                .suspiciousReason(REASON_SUSPICIOUS)
                .build();
    }

}
