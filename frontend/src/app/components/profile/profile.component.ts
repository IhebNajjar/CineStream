import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { WalletService } from '../../services/wallet.service';
import { Wallet, WalletTransaction } from '../../models/media.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  authService = inject(AuthService);
  private walletService = inject(WalletService);

  wallet = signal<Wallet | null>(null);
  transactions = signal<WalletTransaction[]>([]);

  // Email form
  newEmail = '';
  emailSuccess = signal<string | null>(null);
  emailError = signal<string | null>(null);
  emailLoading = signal(false);

  // Password form
  currentPassword = '';
  newPassword = '';
  confirmPassword = '';
  passwordSuccess = signal<string | null>(null);
  passwordError = signal<string | null>(null);
  passwordLoading = signal(false);

  // Topup form
  topupAmount = 20;
  topupSuccess = signal<string | null>(null);
  topupError = signal<string | null>(null);
  topupLoading = signal(false);

  ngOnInit(): void {
    const user = this.authService.currentUser();
    if (user) {
      this.newEmail = user.email;
    }
    this.loadWallet();
    this.loadTransactions();
  }

  loadWallet(): void {
    this.walletService.getWallet().subscribe({
      next: (w) => this.wallet.set(w),
      error: () => {}
    });
  }

  loadTransactions(): void {
    this.walletService.getTransactions().subscribe({
      next: (txs) => this.transactions.set(txs),
      error: () => {}
    });
  }

  onUpdateEmail(): void {
    this.emailSuccess.set(null);
    this.emailError.set(null);

    if (!this.newEmail) {
      this.emailError.set('Veuillez renseigner un email.');
      return;
    }

    this.emailLoading.set(true);
    this.authService.updateEmail(this.newEmail).subscribe({
      next: (res) => {
        this.emailLoading.set(false);
        this.emailSuccess.set(res.message || 'Email mis à jour avec succès.');
      },
      error: (err) => {
        this.emailLoading.set(false);
        this.emailError.set(err?.error?.message || 'Erreur lors de la modification de l\'email.');
      }
    });
  }

  onChangePassword(): void {
    this.passwordSuccess.set(null);
    this.passwordError.set(null);

    if (!this.currentPassword || !this.newPassword) {
      this.passwordError.set('Veuillez renseigner tous les champs.');
      return;
    }

    if (this.newPassword !== this.confirmPassword) {
      this.passwordError.set('Les nouveaux mots de passe ne correspondent pas.');
      return;
    }

    this.passwordLoading.set(true);
    this.authService.updatePassword(this.currentPassword, this.newPassword).subscribe({
      next: (res) => {
        this.passwordLoading.set(false);
        this.passwordSuccess.set(res.message || 'Mot de passe modifié avec succès.');
        this.currentPassword = '';
        this.newPassword = '';
        this.confirmPassword = '';
      },
      error: (err) => {
        this.passwordLoading.set(false);
        this.passwordError.set(err?.error?.message || 'Mot de passe actuel incorrect.');
      }
    });
  }

  onTopUp(): void {
    this.topupSuccess.set(null);
    this.topupError.set(null);

    if (!this.topupAmount || this.topupAmount < 1 || this.topupAmount > 500) {
      this.topupError.set('Le montant doit être compris entre 1 € et 500 €.');
      return;
    }

    this.topupLoading.set(true);
    this.walletService.topUp(this.topupAmount).subscribe({
      next: (w) => {
        this.topupLoading.set(false);
        this.wallet.set(w);
        this.topupSuccess.set(`Portefeuille rechargé avec succès de ${this.topupAmount.toFixed(2)} € !`);
        this.loadTransactions();
      },
      error: (err) => {
        this.topupLoading.set(false);
        this.topupError.set(err?.error?.message || 'Erreur lors de la recharge.');
      }
    });
  }
}
