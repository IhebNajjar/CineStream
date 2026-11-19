import { Injectable, inject, signal, computed, effect } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Wallet, WalletTransaction } from '../models/media.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class WalletService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private apiUrl = 'http://localhost:8080/api/wallet';

  private walletSignal = signal<Wallet | null>(null);
  readonly wallet = this.walletSignal.asReadonly();
  readonly balance = computed(() => this.walletSignal()?.balance ?? 0);

  constructor() {
    effect(() => {
      if (this.authService.isLoggedIn()) {
        this.refreshWallet();
      } else {
        this.walletSignal.set(null);
      }
    }, { allowSignalWrites: true });
  }

  refreshWallet(): void {
    if (!this.authService.isLoggedIn()) return;
    this.getWallet().subscribe({
      next: (w) => this.walletSignal.set(w),
      error: () => {}
    });
  }

  getWallet(): Observable<Wallet> {
    return this.http.get<Wallet>(this.apiUrl).pipe(
      tap(w => this.walletSignal.set(w))
    );
  }

  topUp(amount: number): Observable<Wallet> {
    return this.http.post<Wallet>(`${this.apiUrl}/topup`, { amount }).pipe(
      tap(w => this.walletSignal.set(w))
    );
  }

  getTransactions(): Observable<WalletTransaction[]> {
    return this.http.get<WalletTransaction[]>(`${this.apiUrl}/transactions`);
  }
}
