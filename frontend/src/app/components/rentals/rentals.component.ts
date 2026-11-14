import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { RentalService } from '../../services/rental.service';
import { Rental, WatchLinkResponse } from '../../models/media.model';

@Component({
  selector: 'app-rentals',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './rentals.component.html',
  styleUrls: ['./rentals.component.css']
})
export class RentalsComponent implements OnInit {
  private rentalService = inject(RentalService);

  rentals = signal<Rental[]>([]);
  activeTab = signal<'active' | 'history'>('active');
  loading = signal(true);
  error = signal<string | null>(null);

  // Video Player Modal State
  activeWatchLink = signal<WatchLinkResponse | null>(null);
  watchLoading = signal<number | null>(null); // rentalId currently fetching watch link
  watchError = signal<string | null>(null);

  ngOnInit(): void {
    this.loadRentals();
  }

  loadRentals(): void {
    this.loading.set(true);
    this.error.set(null);
    this.rentalService.getMyRentals().subscribe({
      next: (list) => {
        this.rentals.set(list);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Impossible de charger vos locations.');
        this.loading.set(false);
      }
    });
  }

  getActiveRentals(): Rental[] {
    return this.rentals().filter(r => r.active);
  }

  getExpiredRentals(): Rental[] {
    return this.rentals().filter(r => !r.active);
  }

  onWatch(rental: Rental): void {
    this.watchLoading.set(rental.id);
    this.watchError.set(null);

    this.rentalService.getWatchLink(rental.id).subscribe({
      next: (linkRes) => {
        this.watchLoading.set(null);
        this.activeWatchLink.set(linkRes);
      },
      error: (err) => {
        this.watchLoading.set(null);
        this.watchError.set(err?.error?.message || 'Erreur lors de la génération du lien sécurisé.');
      }
    });
  }

  closePlayer(): void {
    this.activeWatchLink.set(null);
  }
}
