import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Rental, WatchLinkResponse } from '../models/media.model';
import { WalletService } from './wallet.service';

@Injectable({
  providedIn: 'root'
})
export class RentalService {
  private http = inject(HttpClient);
  private walletService = inject(WalletService);
  private apiUrl = 'http://localhost:8080/api/rentals';

  rent(mediaId: number, days: number): Observable<Rental> {
    return this.http.post<Rental>(this.apiUrl, { mediaId, days }).pipe(
      tap(() => this.walletService.refreshWallet())
    );
  }

  getMyRentals(): Observable<Rental[]> {
    return this.http.get<Rental[]>(`${this.apiUrl}/my`);
  }

  getWatchLink(rentalId: number): Observable<WatchLinkResponse> {
    return this.http.get<WatchLinkResponse>(`${this.apiUrl}/${rentalId}/watch-link`);
  }
}
