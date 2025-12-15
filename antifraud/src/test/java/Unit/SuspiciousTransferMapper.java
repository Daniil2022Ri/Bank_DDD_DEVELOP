package Unit;

import config.TestConstants;
import dto.SuspiciousAccountTransferDto;
import dto.SuspiciousCardTransferDto;
import dto.SuspiciousPhoneTransferDto;
import mappers.SuspiciousTransferMapper;
import model.SuspiciousAccountTransfer;
import model.SuspiciousCardTransfer;
import model.SuspiciousPhoneTransfer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class SuspiciousTransferMapperTest {

    private final SuspiciousTransferMapper mapper = Mappers.getMapper(SuspiciousTransferMapper.class);

    private final Long TEST_ID = 1L;
    private final boolean BLOCKED_TRUE = true;
    private final boolean SUSPICIOUS_TRUE = true;
    private final String SUSPICIOUS_REASON = "Подозрительные действия";
    private final String BLOCKED_REASON = "Причина блокировки";

    @Test
    @DisplayName("Mapping: DTO -> Entity (Card)")
    void toCardEntity() {
        SuspiciousCardTransferDto dto = SuspiciousCardTransferDto.builder()
                .id(TestConstants.ID_VALID)
                .blocked(TestConstants.BLOCKED_TRUE)
                .suspicious(TestConstants.SUSPICIOUS_FALSE)
                .blockedReason(TestConstants.REASON_BLOCKED)
                .build();

        SuspiciousCardTransfer entity = mapper.toCardEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.isBlocked(), entity.isBlocked());
        assertEquals(dto.getBlockedReason(), entity.getBlockedReason());
    }

    @Test
    @DisplayName("Mapping: Entity -> DTO (Card)")
    void toCardDto() {
        SuspiciousCardTransfer entity = SuspiciousCardTransfer.builder()
                .id(TestConstants.ID_VALID)
                .blocked(TestConstants.BLOCKED_FALSE)
                .suspicious(TestConstants.SUSPICIOUS_TRUE)
                .suspiciousReason(TestConstants.REASON_SUSPICIOUS)
                .build();

        SuspiciousCardTransferDto dto = mapper.toCardDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.isSuspicious(), dto.isSuspicious());
        assertEquals(entity.getSuspiciousReason(), dto.getSuspiciousReason());
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
