import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { MediaService } from '../../services/media.service';
import { MediaDetails } from '../../models/media.model';

@Component({
  selector: 'app-media-details',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './media-details.component.html',
  styleUrls: ['./media-details.component.css']
})
export class MediaDetailsComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private mediaService = inject(MediaService);

  media: MediaDetails | null = null;
  similarTitles: any[] = [];
  isLoading: boolean = true;
  notFound: boolean = false;
  errorMessage: string = '';

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        const id = Number(idParam);
        if (isNaN(id) || id <= 0) {
          this.notFound = true;
          this.isLoading = false;
        } else {
          this.loadDetails(id);
          window.scrollTo({ top: 0, behavior: 'smooth' });
        }
      }
    });
  }

  loadDetails(id: number): void {
    this.isLoading = true;
    this.notFound = false;
    this.errorMessage = '';
    this.similarTitles = [];

    this.mediaService.getMediaDetails(id).subscribe({
      next: (data) => {
        this.media = data;
        this.isLoading = false;
        if (data.genres && data.genres.length > 0) {
          this.loadSimilar(data.genres[0].id, id);
        }
      },
      error: (err) => {
        this.isLoading = false;
        if (err.status === 404) {
          this.notFound = true;
        } else {
          this.errorMessage = 'Une erreur est survenue lors du chargement des détails.';
        }
      }
    });
  }

  loadSimilar(genreId: number, currentId: number): void {
    this.mediaService.getMediaList({ genre: genreId, size: 6 }).subscribe({
      next: (res) => {
        this.similarTitles = res.content.filter(item => item.id !== currentId).slice(0, 4);
      },
      error: (err) => console.error('Erreur chargement similaires', err)
    });
  }

  formatDuration(duration?: number): string {
    if (!duration) return 'Format Série';
    const hours = Math.floor(duration / 60);
    const minutes = duration % 60;
    if (hours === 0) return `${minutes} min`;
    return `${hours}h ${minutes.toString().padStart(2, '0')}m`;
  }

  onImageError(event: Event) {
    const target = event.target as HTMLImageElement;
    target.src = 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600';
  }

  onRentClick(): void {
    alert('Veuillez vous connecter à votre compte CineStream pour procéder à la location.');
  }
}
