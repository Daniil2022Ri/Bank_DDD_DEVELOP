package service;

import dto.SuspiciousAccountTransferDto;
import dto.SuspiciousCardTransferDto;
import dto.SuspiciousPhoneTransferDto;

import java.util.List;

public interface SuspiciousTransferService {
    SuspiciousCardTransferDto createCard(SuspiciousCardTransferDto dto);
    SuspiciousPhoneTransferDto createPhone(SuspiciousPhoneTransferDto dto);
    SuspiciousAccountTransferDto createAccount(SuspiciousAccountTransferDto dto);

    SuspiciousCardTransferDto updateCard(Long id, SuspiciousCardTransferDto dto);
    SuspiciousPhoneTransferDto updatePhone(Long id, SuspiciousPhoneTransferDto dto);
    SuspiciousAccountTransferDto updateAccount(Long id, SuspiciousAccountTransferDto dto);

    List<SuspiciousCardTransferDto> getAllCards();
    List<SuspiciousPhoneTransferDto> getAllPhones();
    List<SuspiciousAccountTransferDto> getAllAccounts();

    SuspiciousCardTransferDto getCardById(Long id);
    SuspiciousPhoneTransferDto getPhoneById(Long id);
    SuspiciousAccountTransferDto getAccountById(Long id);

    void deleteSuspiciousTransfer(Long id, String type);
    List<?> getAll(String type);
    Object getById(Long id, String type);
}
