package Unit;

import dto.SuspiciousAccountTransferDto;
import dto.SuspiciousCardTransferDto;
import dto.SuspiciousPhoneTransferDto;
import mappers.SuspiciousAccountTransferMapper;
import mappers.SuspiciousCardTransferMapper;
import mappers.SuspiciousPhoneTransferMapper;
import model.SuspiciousAccountTransfer;
import model.SuspiciousCardTransfer;
import model.SuspiciousPhoneTransfer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SuspiciousTransferMapperTest {

    @Autowired
    private SuspiciousCardTransferMapper cardMapper;

    @Autowired
    private SuspiciousPhoneTransferMapper phoneMapper;

    @Autowired
    private SuspiciousAccountTransferMapper accountMapper;

    private final Long TEST_ID = 1L;
    private final boolean BLOCKED_TRUE = true;
    private final boolean SUSPICIOUS_TRUE = true;
    private final String SUSPICIOUS_REASON = "Подозрительные действия";
    private final String BLOCKED_REASON = "Причина блокировки";

    @Test
    void toCardEntity_WithValidDto_ShouldReturnEntity() {
        SuspiciousCardTransferDto dto = createTestCardDto();

        SuspiciousCardTransfer entity = cardMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.isBlocked(), entity.isBlocked());
        assertEquals(dto.isSuspicious(), entity.isSuspicious());
        assertEquals(dto.getBlockedReason(), entity.getBlockedReason());
        assertEquals(dto.getSuspiciousReason(), entity.getSuspiciousReason());
    }

    @Test
    void toCardDto_WithValidEntity_ShouldReturnDto() {
        SuspiciousCardTransfer entity = createTestCardEntity();

        SuspiciousCardTransferDto dto = cardMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.isBlocked(), dto.isBlocked());
        assertEquals(entity.isSuspicious(), dto.isSuspicious());
        assertEquals(entity.getBlockedReason(), dto.getBlockedReason());
        assertEquals(entity.getSuspiciousReason(), dto.getSuspiciousReason());
    }

    @Test
    void toPhoneEntity_WithValidDto_ShouldReturnEntity() {
        SuspiciousPhoneTransferDto dto = createTestPhoneDto();

        SuspiciousPhoneTransfer entity = phoneMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.isBlocked(), entity.isBlocked());
        assertEquals(dto.isSuspicious(), entity.isSuspicious());
        assertEquals(dto.getBlockedReason(), entity.getBlockedReason());
        assertEquals(dto.getSuspiciousReason(), entity.getSuspiciousReason());
    }

    @Test
    void toPhoneDto_WithValidEntity_ShouldReturnDto() {
        SuspiciousPhoneTransfer entity = createTestPhoneEntity();

        SuspiciousPhoneTransferDto dto = phoneMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.isBlocked(), dto.isBlocked());
        assertEquals(entity.isSuspicious(), dto.isSuspicious());
        assertEquals(entity.getBlockedReason(), dto.getBlockedReason());
        assertEquals(entity.getSuspiciousReason(), dto.getSuspiciousReason());
    }

    @Test
    void toAccountEntity_WithValidDto_ShouldReturnEntity() {
        SuspiciousAccountTransferDto dto = createTestAccountDto();

        SuspiciousAccountTransfer entity = accountMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.isBlocked(), entity.isBlocked());
        assertEquals(dto.isSuspicious(), entity.isSuspicious());
        assertEquals(dto.getBlockedReason(), entity.getBlockedReason());
        assertEquals(dto.getSuspiciousReason(), entity.getSuspiciousReason());
    }

    @Test
    void toAccountDto_WithValidEntity_ShouldReturnDto() {
        SuspiciousAccountTransfer entity = createTestAccountEntity();

        SuspiciousAccountTransferDto dto = accountMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.isBlocked(), dto.isBlocked());
        assertEquals(entity.isSuspicious(), dto.isSuspicious());
        assertEquals(entity.getBlockedReason(), dto.getBlockedReason());
        assertEquals(entity.getSuspiciousReason(), dto.getSuspiciousReason());
    }

    @Test
    void testCardMapper_NullSafety() {
        assertNull(cardMapper.toDto(null));
        assertNull(cardMapper.toEntity(null));
    }

    @Test
    void testPhoneMapper_NullSafety() {
        assertNull(phoneMapper.toDto(null));
        assertNull(phoneMapper.toEntity(null));
    }

    @Test
    void testAccountMapper_NullSafety() {
        assertNull(accountMapper.toDto(null));
        assertNull(accountMapper.toEntity(null));
    }

    private SuspiciousCardTransferDto createTestCardDto() {
        return SuspiciousCardTransferDto.builder()
                .id(TEST_ID)
                .blocked(BLOCKED_TRUE)
                .suspicious(SUSPICIOUS_TRUE)
                .blockedReason(BLOCKED_REASON)
                .suspiciousReason(SUSPICIOUS_REASON)
                .build();
    }

    private SuspiciousCardTransfer createTestCardEntity() {
        return SuspiciousCardTransfer.builder()
                .id(TEST_ID)
                .blocked(BLOCKED_TRUE)
                .suspicious(SUSPICIOUS_TRUE)
                .blockedReason(BLOCKED_REASON)
                .suspiciousReason(SUSPICIOUS_REASON)
                .build();
    }

    private SuspiciousPhoneTransferDto createTestPhoneDto() {
        return SuspiciousPhoneTransferDto.builder()
                .id(TEST_ID)
                .blocked(BLOCKED_TRUE)
                .suspicious(SUSPICIOUS_TRUE)
                .blockedReason(BLOCKED_REASON)
                .suspiciousReason(SUSPICIOUS_REASON)
                .build();
    }

    private SuspiciousPhoneTransfer createTestPhoneEntity() {
        return SuspiciousPhoneTransfer.builder()
                .id(TEST_ID)
                .blocked(BLOCKED_TRUE)
                .suspicious(SUSPICIOUS_TRUE)
                .blockedReason(BLOCKED_REASON)
                .suspiciousReason(SUSPICIOUS_REASON)
                .build();
    }

    private SuspiciousAccountTransferDto createTestAccountDto() {
        return SuspiciousAccountTransferDto.builder()
                .id(TEST_ID)
                .blocked(BLOCKED_TRUE)
                .suspicious(SUSPICIOUS_TRUE)
                .blockedReason(BLOCKED_REASON)
                .suspiciousReason(SUSPICIOUS_REASON)
                .build();
    }

    private SuspiciousAccountTransfer createTestAccountEntity() {
        return SuspiciousAccountTransfer.builder()
                .id(TEST_ID)
                .blocked(BLOCKED_TRUE)
                .suspicious(SUSPICIOUS_TRUE)
                .blockedReason(BLOCKED_REASON)
                .suspiciousReason(SUSPICIOUS_REASON)
                .build();
    }
}