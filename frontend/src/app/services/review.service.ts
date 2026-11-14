import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Review } from '../models/media.model';

export interface ReviewsResponse {
  reviews: Review[];
  averageRating: number;
  count: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api';

  getReviews(mediaId: number): Observable<ReviewsResponse> {
    return this.http.get<ReviewsResponse>(`${this.apiUrl}/media/${mediaId}/reviews`);
  }

  postReview(mediaId: number, rating: number, comment?: string): Observable<Review> {
    return this.http.post<Review>(`${this.apiUrl}/media/${mediaId}/reviews`, { rating, comment });
  }

  updateReview(reviewId: number, rating: number, comment?: string): Observable<Review> {
    return this.http.put<Review>(`${this.apiUrl}/reviews/${reviewId}`, { rating, comment });
  }

  deleteReview(reviewId: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.apiUrl}/reviews/${reviewId}`);
  }
}
