package com.cinestream.controller;

import com.cinestream.dto.TopupRequest;
import com.cinestream.dto.TransactionDto;
import com.cinestream.dto.WalletDto;
import com.cinestream.model.User;
import com.cinestream.security.SecurityUtils;
import com.cinestream.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;
    private final SecurityUtils securityUtils;

    public WalletController(WalletService walletService, SecurityUtils securityUtils) {
        this.walletService = walletService;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public ResponseEntity<WalletDto> getWallet() {
        User user = securityUtils.getCurrentUserOrThrow();
        return ResponseEntity.ok(walletService.getWalletDto(user));
    }

    @PostMapping("/topup")
    public ResponseEntity<WalletDto> topUp(@Valid @RequestBody TopupRequest request) {
        User user = securityUtils.getCurrentUserOrThrow();
        return ResponseEntity.ok(walletService.topUp(user, request));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions() {
        User user = securityUtils.getCurrentUserOrThrow();
        return ResponseEntity.ok(walletService.getTransactions(user));
    }
}
