import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Favorite } from '../models/media.model';

@Injectable({
  providedIn: 'root'
})
export class FavoriteService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/favorites';

  getMyFavorites(): Observable<Favorite[]> {
    return this.http.get<Favorite[]>(this.apiUrl);
  }

  toggleFavorite(mediaId: number): Observable<{ isFavorite: boolean; message: string }> {
    return this.http.post<{ isFavorite: boolean; message: string }>(`${this.apiUrl}/toggle/${mediaId}`, {});
  }

  checkFavorite(mediaId: number): Observable<{ isFavorite: boolean }> {
    return this.http.get<{ isFavorite: boolean }>(`${this.apiUrl}/check/${mediaId}`);
  }
}
