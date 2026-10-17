import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Actor, Director, Genre, MediaDetails, MediaFilter, MediaSummary, PagedResponse } from '../models/media.model';

@Injectable({
  providedIn: 'root'
})
export class MediaService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api';

  getMediaList(filters: MediaFilter = {}): Observable<PagedResponse<MediaSummary>> {
    let params = new HttpParams();

    if (filters.q && filters.q.trim()) {
      params = params.set('q', filters.q.trim());
    }
    if (filters.type && filters.type !== 'ALL') {
      params = params.set('type', filters.type);
    }
    if (filters.genre !== undefined && filters.genre !== null && filters.genre > 0) {
      params = params.set('genre', filters.genre.toString());
    }
    if (filters.actor !== undefined && filters.actor !== null && filters.actor > 0) {
      params = params.set('actor', filters.actor.toString());
    }
    if (filters.director !== undefined && filters.director !== null && filters.director > 0) {
      params = params.set('director', filters.director.toString());
    }
    if (filters.year !== undefined && filters.year !== null && filters.year > 0) {
      params = params.set('year', filters.year.toString());
    }
    if (filters.page !== undefined && filters.page !== null) {
      params = params.set('page', filters.page.toString());
    }
    if (filters.size !== undefined && filters.size !== null) {
      params = params.set('size', filters.size.toString());
    } else {
      params = params.set('size', '12'); // 12 titles per page default as per US008
    }

    return this.http.get<PagedResponse<MediaSummary>>(`${this.apiUrl}/media`, { params });
  }

  getMediaDetails(id: number): Observable<MediaDetails> {
    return this.http.get<MediaDetails>(`${this.apiUrl}/media/${id}`);
  }

  getGenres(): Observable<Genre[]> {
    return this.http.get<Genre[]>(`${this.apiUrl}/genres`);
  }

  getActors(): Observable<Actor[]> {
    return this.http.get<Actor[]>(`${this.apiUrl}/actors`);
  }

  getDirectors(): Observable<Director[]> {
    return this.http.get<Director[]>(`${this.apiUrl}/directors`);
  }
}
