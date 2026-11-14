import { Component, OnInit, inject, ChangeDetectorRef, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MediaService } from '../../services/media.service';
import { AuthService } from '../../services/auth.service';
import { RentalService } from '../../services/rental.service';
import { ReviewService, ReviewsResponse } from '../../services/review.service';
import { FavoriteService } from '../../services/favorite.service';
import { MediaDetails, Review } from '../../models/media.model';

@Component({
  selector: 'app-media-details',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './media-details.component.html',
  styleUrls: ['./media-details.component.css']
})
export class MediaDetailsComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private mediaService = inject(MediaService);
  authService = inject(AuthService);
  private rentalService = inject(RentalService);
  private reviewService = inject(ReviewService);
  private favoriteService = inject(FavoriteService);
  private cdr = inject(ChangeDetectorRef);

  media: MediaDetails | null = null;
  similarTitles: any[] = [];
  isLoading: boolean = true;
  notFound: boolean = false;
  errorMessage: string = '';

  // US023: Day selector
  rentalDays: number = 1;
  isRenting: boolean = false;
  rentalSuccessMsg: string = '';
  rentalErrorMsg: string = '';

  // US012: Seasons for series
  selectedSeasonIndex: number = 0;

  // US030: Favorite
  isFavorite: boolean = false;

  // US016, US017, US018, US019: Reviews
  reviews: Review[] = [];
  averageRating: number = 0;
  reviewsCount: number = 0;
  userRating: number = 5;
  userComment: string = '';
  submittingReview: boolean = false;
  reviewMsg: string = '';

  // US029: Trailer modal
  showTrailerModal: boolean = false;

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
          this.loadReviews(id);
          this.checkFavoriteStatus(id);
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
    this.rentalSuccessMsg = '';
    this.rentalErrorMsg = '';

    this.mediaService.getMediaDetails(id).subscribe({
      next: (data) => {
        this.media = data;
        this.isLoading = false;
        this.selectedSeasonIndex = 0;
        this.cdr.markForCheck();
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
        this.cdr.markForCheck();
      }
    });
  }

  loadSimilar(genreId: number, currentId: number): void {
    this.mediaService.getMediaList({ genre: genreId, size: 6 }).subscribe({
      next: (res) => {
        this.similarTitles = res.content.filter(item => item.id !== currentId).slice(0, 4);
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Erreur chargement similaires', err)
    });
  }

  loadReviews(mediaId: number): void {
    this.reviewService.getReviews(mediaId).subscribe({
      next: (res: ReviewsResponse) => {
        this.reviews = res.reviews;
        this.averageRating = res.averageRating;
        this.reviewsCount = res.count;
        this.cdr.markForCheck();
      },
      error: () => {}
    });
  }

  checkFavoriteStatus(mediaId: number): void {
    if (!this.authService.isLoggedIn()) {
      this.isFavorite = false;
      return;
    }
    this.favoriteService.checkFavorite(mediaId).subscribe({
      next: (res) => {
        this.isFavorite = res.isFavorite;
        this.cdr.markForCheck();
      },
      error: () => {}
    });
  }

  toggleFavorite(): void {
    if (!this.authService.isLoggedIn()) {
      alert('Veuillez vous connecter pour ajouter ce titre à vos favoris.');
      return;
    }
    if (!this.media) return;

    this.favoriteService.toggleFavorite(this.media.id).subscribe({
      next: (res) => {
        this.isFavorite = res.isFavorite;
        this.cdr.markForCheck();
      }
    });
  }

  // US023: Day selector buttons
  incrementDays(): void {
    if (this.rentalDays < 90) {
      this.rentalDays++;
    }
  }

  decrementDays(): void {
    if (this.rentalDays > 1) {
      this.rentalDays--;
    }
  }

  calculateTotalPrice(): number {
    if (!this.media) return 0;
    return this.rentalDays * this.media.rentalPricePerDay;
  }

  // US024, US025, US026, US027: Rental flow
  onRentClick(): void {
    if (!this.authService.isLoggedIn()) {
      alert('Veuillez vous connecter pour louer ce titre.');
      return;
    }

    if (!this.media) return;

    this.isRenting = true;
    this.rentalSuccessMsg = '';
    this.rentalErrorMsg = '';

    this.rentalService.rent(this.media.id, this.rentalDays).subscribe({
      next: () => {
        this.isRenting = false;
        this.rentalSuccessMsg = `Félicitations ! Vous avez loué « ${this.media?.title} » pour ${this.rentalDays} jour(s). Retrouvez-le dès maintenant dans « Mes locations ».`;
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.isRenting = false;
        this.rentalErrorMsg = err?.error?.message || 'Erreur lors de la location.';
        this.cdr.markForCheck();
      }
    });
  }

  // US016: Post rating and review
  onSubmitReview(): void {
    if (!this.authService.isLoggedIn()) {
      alert('Veuillez vous connecter pour laisser un avis.');
      return;
    }
    if (!this.media) return;

    this.submittingReview = true;
    this.reviewMsg = '';

    this.reviewService.postReview(this.media.id, this.userRating, this.userComment).subscribe({
      next: () => {
        this.submittingReview = false;
        this.userComment = '';
        this.reviewMsg = 'Votre avis a bien été publié !';
        this.loadReviews(this.media!.id);
      },
      error: (err) => {
        this.submittingReview = false;
        this.reviewMsg = err?.error?.message || 'Erreur lors de la publication de l\'avis.';
      }
    });
  }

  // US018, US019: Delete review
  onDeleteReview(reviewId: number): void {
    if (confirm('Confirmer la suppression de cet avis ?')) {
      this.reviewService.deleteReview(reviewId).subscribe({
        next: () => {
          if (this.media) {
            this.loadReviews(this.media.id);
          }
        }
      });
    }
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
    target.src = '/assets/posters/1.jpg';
  }
}
