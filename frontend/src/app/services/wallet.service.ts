import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Wallet, WalletTransaction } from '../models/media.model';

@Injectable({
  providedIn: 'root'
})
export class WalletService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/wallet';

  getWallet(): Observable<Wallet> {
    return this.http.get<Wallet>(this.apiUrl);
  }

  topUp(amount: number): Observable<Wallet> {
    return this.http.post<Wallet>(`${this.apiUrl}/topup`, { amount });
  }

  getTransactions(): Observable<WalletTransaction[]> {
    return this.http.get<WalletTransaction[]>(`${this.apiUrl}/transactions`);
  }
}
