package com.cinestream.service;

import com.cinestream.dto.TopupRequest;
import com.cinestream.dto.TransactionDto;
import com.cinestream.dto.WalletDto;
import com.cinestream.exception.ResourceNotFoundException;
import com.cinestream.model.TransactionType;
import com.cinestream.model.User;
import com.cinestream.model.Wallet;
import com.cinestream.model.WalletTransaction;
import com.cinestream.repository.WalletRepository;
import com.cinestream.repository.WalletTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository, WalletTransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Wallet getOrCreateWallet(User user) {
        return walletRepository.findByUserId(user.getId())
                .orElseGet(() -> walletRepository.save(new Wallet(user, BigDecimal.ZERO)));
    }

    public WalletDto getWalletDto(User user) {
        Wallet wallet = getOrCreateWallet(user);
        return new WalletDto(wallet.getId(), user.getId(), wallet.getBalance());
    }

    @Transactional
    public WalletDto topUp(User user, TopupRequest request) {
        Wallet wallet = getOrCreateWallet(user);
        BigDecimal newBalance = wallet.getBalance().add(request.getAmount());
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        // Record RECHARGE transaction
        WalletTransaction transaction = new WalletTransaction(
                wallet,
                request.getAmount(),
                TransactionType.RECHARGE,
                "Recharge de portefeuille virtuel"
        );
        transactionRepository.save(transaction);

        return new WalletDto(wallet.getId(), user.getId(), wallet.getBalance());
    }

    public List<TransactionDto> getTransactions(User user) {
        Wallet wallet = getOrCreateWallet(user);
        return transactionRepository.findByWalletIdOrderByCreatedAtDesc(wallet.getId())
                .stream()
                .map(t -> new TransactionDto(t.getId(), t.getAmount(), t.getType(), t.getDescription(), t.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
