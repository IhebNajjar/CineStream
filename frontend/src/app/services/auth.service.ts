import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { AuthResponse, User } from '../models/media.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private apiUrl = 'http://localhost:8080/api';

  private currentUserSignal = signal<User | null>(this.getStoredUser());
  readonly currentUser = this.currentUserSignal.asReadonly();
  readonly isLoggedIn = computed(() => this.currentUserSignal() !== null);
  readonly isAdmin = computed(() => this.currentUserSignal()?.role === 'ADMIN');

  constructor() {
    // Check if token exists on boot
    const token = this.getToken();
    if (token && !this.currentUserSignal()) {
      this.fetchCurrentUser().subscribe({
        error: () => this.logout()
      });
    }
  }

  getToken(): string | null {
    return localStorage.getItem('cinestream_token');
  }

  private setSession(auth: AuthResponse): void {
    localStorage.setItem('cinestream_token', auth.token);
    const user: User = { id: auth.id, email: auth.email, role: auth.role };
    localStorage.setItem('cinestream_user', JSON.stringify(user));
    this.currentUserSignal.set(user);
  }

  private getStoredUser(): User | null {
    const raw = localStorage.getItem('cinestream_user');
    if (!raw) return null;
    try {
      return JSON.parse(raw);
    } catch {
      return null;
    }
  }

  register(email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/register`, { email, password }).pipe(
      tap(res => this.setSession(res))
    );
  }

  login(email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, { email, password }).pipe(
      tap(res => this.setSession(res))
    );
  }

  fetchCurrentUser(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${this.apiUrl}/auth/me`).pipe(
      tap(res => {
        const user: User = { id: res.id, email: res.email, role: res.role };
        localStorage.setItem('cinestream_user', JSON.stringify(user));
        this.currentUserSignal.set(user);
      })
    );
  }

  updateEmail(email: string): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/user/email`, { email }).pipe(
      tap(res => {
        if (res.token) {
          localStorage.setItem('cinestream_token', res.token);
        }
        const current = this.currentUserSignal();
        if (current) {
          const updated: User = { ...current, email: res.email || email };
          localStorage.setItem('cinestream_user', JSON.stringify(updated));
          this.currentUserSignal.set(updated);
        }
      })
    );
  }

  updatePassword(currentPassword: string, newPassword: string): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/user/password`, { currentPassword, newPassword });
  }

  logout(): void {
    localStorage.removeItem('cinestream_token');
    localStorage.removeItem('cinestream_user');
    this.currentUserSignal.set(null);
    this.router.navigate(['/']);
  }
}
