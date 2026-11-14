import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FavoriteService } from '../../services/favorite.service';
import { Favorite } from '../../models/media.model';
import { MediaCardComponent } from '../media-card/media-card.component';

@Component({
  selector: 'app-favorites',
  standalone: true,
  imports: [CommonModule, RouterLink, MediaCardComponent],
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.css']
})
export class FavoritesComponent implements OnInit {
  private favoriteService = inject(FavoriteService);

  favorites = signal<Favorite[]>([]);
  loading = signal(true);

  ngOnInit(): void {
    this.loadFavorites();
  }

  loadFavorites(): void {
    this.loading.set(true);
    this.favoriteService.getMyFavorites().subscribe({
      next: (list) => {
        this.favorites.set(list);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  removeFavorite(mediaId: number): void {
    this.favoriteService.toggleFavorite(mediaId).subscribe({
      next: () => {
        this.favorites.update(list => list.filter(f => f.mediaId !== mediaId));
      }
    });
  }
}
