import { Component, EventEmitter, Output, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-auth-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auth-modal.component.html',
  styleUrls: ['./auth-modal.component.css']
})
export class AuthModalComponent {
  private authService = inject(AuthService);

  @Output() close = new EventEmitter<void>();

  isRegister = signal(false);
  email = '';
  password = '';
  confirmPassword = '';
  loading = signal(false);
  errorMessage = signal<string | null>(null);
  successMessage = signal<string | null>(null);

  toggleMode(): void {
    this.isRegister.update(v => !v);
    this.errorMessage.set(null);
    this.successMessage.set(null);
  }

  onSubmit(): void {
    this.errorMessage.set(null);
    this.successMessage.set(null);

    if (!this.email || !this.password) {
      this.errorMessage.set('Veuillez remplir tous les champs obligatoires.');
      return;
    }

    if (this.isRegister() && this.password !== this.confirmPassword) {
      this.errorMessage.set('Les mots de passe ne correspondent pas.');
      return;
    }

    this.loading.set(true);

    if (this.isRegister()) {
      this.authService.register(this.email, this.password).subscribe({
        next: () => {
          this.loading.set(false);
          this.successMessage.set('Compte créé avec succès ! Bienvenue sur CineStream.');
          setTimeout(() => this.close.emit(), 1200);
        },
        error: (err) => {
          this.loading.set(false);
          this.errorMessage.set(err?.error?.message || 'Erreur lors de l\'inscription.');
        }
      });
    } else {
      this.authService.login(this.email, this.password).subscribe({
        next: () => {
          this.loading.set(false);
          this.successMessage.set('Connexion réussie !');
          setTimeout(() => this.close.emit(), 900);
        },
        error: (err) => {
          this.loading.set(false);
          this.errorMessage.set(err?.error?.message || 'Identifiants invalides.');
        }
      });
    }
  }
}
