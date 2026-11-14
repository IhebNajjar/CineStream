import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DashboardStats, MediaDetails } from '../models/media.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/admin';

  getStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.apiUrl}/dashboard/stats`);
  }

  createMedia(mediaData: any): Observable<MediaDetails> {
    return this.http.post<MediaDetails>(`${this.apiUrl}/media`, mediaData);
  }

  updateMedia(id: number, mediaData: any): Observable<MediaDetails> {
    return this.http.put<MediaDetails>(`${this.apiUrl}/media/${id}`, mediaData);
  }

  deleteMedia(id: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.apiUrl}/media/${id}`);
  }

  addSeason(mediaId: number, seasonData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/media/${mediaId}/seasons`, seasonData);
  }

  deleteSeason(seasonId: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.apiUrl}/seasons/${seasonId}`);
  }

  addEpisode(seasonId: number, episodeData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/seasons/${seasonId}/episodes`, episodeData);
  }

  deleteEpisode(episodeId: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.apiUrl}/episodes/${episodeId}`);
  }

  createGenre(name: string, description?: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/genres`, { name, description });
  }

  deleteGenre(id: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.apiUrl}/genres/${id}`);
  }

  createActor(actorData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/actors`, actorData);
  }

  deleteActor(id: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.apiUrl}/actors/${id}`);
  }

  createDirector(directorData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/directors`, directorData);
  }

  deleteDirector(id: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.apiUrl}/directors/${id}`);
  }
}
