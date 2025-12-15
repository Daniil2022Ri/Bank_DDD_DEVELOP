package service;

import dto.SuspiciousAccountTransferDto;
import dto.SuspiciousCardTransferDto;
import dto.SuspiciousPhoneTransferDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mappers.SuspiciousAccountTransferMapper;
import mappers.SuspiciousCardTransferMapper;
import mappers.SuspiciousPhoneTransferMapper;
import model.SuspiciousAccountTransfer;
import model.SuspiciousCardTransfer;
import model.SuspiciousPhoneTransfer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.SuspiciousAccountTransferRepository;
import repository.SuspiciousCardTransferRepository;
import repository.SuspiciousPhoneTransferRepository;

import java.util.List;


import static config.ApplicationConstant.ERR_ACCOUNT_NOT_FOUND;
import static config.ApplicationConstant.ERR_CARD_NOT_FOUND;
import static config.ApplicationConstant.ERR_INVALID_TYPE;
import static config.ApplicationConstant.ERR_NOT_FOUND_TEMPLATE;
import static config.ApplicationConstant.ERR_PHONE_NOT_FOUND;
import static config.ApplicationConstant.TYPE_ACCOUNT;
import static config.ApplicationConstant.TYPE_CARD;
import static config.ApplicationConstant.TYPE_PHONE;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SuspiciousTransferServiceImpl implements SuspiciousTransferService {

    private final SuspiciousCardTransferRepository cardRepo;
    private final SuspiciousPhoneTransferRepository phoneRepo;
    private final SuspiciousAccountTransferRepository accountRepo;
    private final SuspiciousCardTransferMapper cardMapper;
    private final SuspiciousPhoneTransferMapper phoneMapper;
    private final SuspiciousAccountTransferMapper accountMapper;

    @Override
    public SuspiciousCardTransferDto createCard(SuspiciousCardTransferDto dto) {
        log.info("Запрос на создание карты");
        SuspiciousCardTransfer entity = cardMapper.toEntity(dto);
        SuspiciousCardTransfer saved = cardRepo.save(entity);
        return cardMapper.toDto(saved);
    }

    @Override
    public SuspiciousPhoneTransferDto createPhone(SuspiciousPhoneTransferDto dto) {
        log.info("Запрос на создание телефона");
        SuspiciousPhoneTransfer entity = phoneMapper.toEntity(dto);
        return phoneMapper.toDto(phoneRepo.save(entity));
    }

    @Override
    public SuspiciousAccountTransferDto createAccount(SuspiciousAccountTransferDto dto) {
        log.info("Запрос на создание аккаунта");
        SuspiciousAccountTransfer entity = accountMapper.toEntity(dto);
        return accountMapper.toDto(accountRepo.save(entity));
    }

    @Override
    public SuspiciousCardTransferDto updateCard(Long id, SuspiciousCardTransferDto dto) {
        SuspiciousCardTransfer entity = cardRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ERR_NOT_FOUND_TEMPLATE, ERR_CARD_NOT_FOUND, id)));

        entity.setBlocked(dto.isBlocked());
        entity.setSuspicious(dto.isSuspicious());
        entity.setBlockedReason(dto.getBlockedReason());
        entity.setSuspiciousReason(dto.getSuspiciousReason());

        return cardMapper.toDto(cardRepo.save(entity));
    }

    @Override
    public SuspiciousPhoneTransferDto updatePhone(Long id, SuspiciousPhoneTransferDto dto) {
        SuspiciousPhoneTransfer entity = phoneRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ERR_NOT_FOUND_TEMPLATE, ERR_PHONE_NOT_FOUND, id)));

        entity.setBlocked(dto.isBlocked());
        entity.setSuspicious(dto.isSuspicious());
        entity.setBlockedReason(dto.getBlockedReason());
        entity.setSuspiciousReason(dto.getSuspiciousReason());

        return phoneMapper.toDto(phoneRepo.save(entity));
    }

    @Override
    public SuspiciousAccountTransferDto updateAccount(Long id, SuspiciousAccountTransferDto dto) {
        SuspiciousAccountTransfer entity = accountRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ERR_NOT_FOUND_TEMPLATE, ERR_ACCOUNT_NOT_FOUND, id)));

        entity.setBlocked(dto.isBlocked());
        entity.setSuspicious(dto.isSuspicious());
        entity.setBlockedReason(dto.getBlockedReason());
        entity.setSuspiciousReason(dto.getSuspiciousReason());

        return accountMapper.toDto(accountRepo.save(entity));
    }

    @Override
    public void deleteSuspiciousTransfer(Long id, String type) {
        switch (type.toLowerCase()) {
            case TYPE_CARD -> cardRepo.deleteById(id);
            case TYPE_PHONE -> phoneRepo.deleteById(id);
            case TYPE_ACCOUNT -> accountRepo.deleteById(id);
            default -> throw new IllegalArgumentException(ERR_INVALID_TYPE + type);
        }
    }

    @Override
    public List<SuspiciousCardTransferDto> getAllCards() {
        return cardRepo.findAll().stream().map(cardMapper::toDto).toList();
    }

    @Override
    public List<SuspiciousPhoneTransferDto> getAllPhones() {
        return phoneRepo.findAll().stream().map(phoneMapper::toDto).toList();
    }

    @Override
    public List<SuspiciousAccountTransferDto> getAllAccounts() {
        return accountRepo.findAll().stream().map(accountMapper::toDto).toList();
    }

    @Override
    public SuspiciousCardTransferDto getCardById(Long id) {
        return cardMapper.toDto(cardRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ERR_NOT_FOUND_TEMPLATE, ERR_CARD_NOT_FOUND, id))));
    }

    @Override
    public SuspiciousPhoneTransferDto getPhoneById(Long id) {
        return phoneMapper.toDto(phoneRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ERR_NOT_FOUND_TEMPLATE, ERR_PHONE_NOT_FOUND, id))));
    }

    @Override
    public SuspiciousAccountTransferDto getAccountById(Long id) {
        return accountMapper.toDto(accountRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ERR_NOT_FOUND_TEMPLATE, ERR_ACCOUNT_NOT_FOUND, id))));
    }

    @Override
    public List<?> getAll(String type) {
        return switch (type.toLowerCase()) {
            case TYPE_CARD -> getAllCards();
            case TYPE_PHONE -> getAllPhones();
            case TYPE_ACCOUNT -> getAllAccounts();
            default -> throw new IllegalArgumentException(ERR_INVALID_TYPE + type);
        };
    }

    @Override
    public Object getById(Long id, String type) {
        return switch (type.toLowerCase()) {
            case TYPE_CARD -> getCardById(id);
            case TYPE_PHONE -> getPhoneById(id);
            case TYPE_ACCOUNT -> getAccountById(id);
            default -> throw new IllegalArgumentException(ERR_INVALID_TYPE + type);
        };
    }
}