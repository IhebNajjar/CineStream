import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MediaService } from '../../services/media.service';
import { Actor, Director, Genre, MediaFilter, MediaSummary, PagedResponse } from '../../models/media.model';
import { MediaCardComponent } from '../media-card/media-card.component';
import { PaginationComponent } from '../pagination/pagination.component';

@Component({
  selector: 'app-catalogue',
  standalone: true,
  imports: [CommonModule, FormsModule, MediaCardComponent, PaginationComponent],
  templateUrl: './catalogue.component.html',
  styleUrls: ['./catalogue.component.css']
})
export class CatalogueComponent implements OnInit {
  private mediaService = inject(MediaService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  // Data
  mediaList: MediaSummary[] = [];
  genres: Genre[] = [];
  actors: Actor[] = [];
  directors: Director[] = [];

  // Pagination state
  currentPage: number = 0;
  totalPages: number = 0;
  totalElements: number = 0;
  pageSize: number = 12;

  // Filter model
  searchQuery: string = '';
  selectedType: string = 'ALL';
  selectedGenreId: number | null = null;
  selectedActorId: number | null = null;
  selectedDirectorId: number | null = null;
  selectedYear: number | null = null;

  // Available years for dropdown
  years: number[] = [];

  // UI state
  isLoading: boolean = false;
  hasError: boolean = false;
  errorMessage: string = '';

  ngOnInit(): void {
    this.generateYearsList();
    this.loadFilterOptions();

    // Listen to query params changes (e.g. from navbar navigation)
    this.route.queryParams.subscribe(params => {
      if (params['type']) {
        this.selectedType = params['type'];
      }
      if (params['q']) {
        this.searchQuery = params['q'];
      }
      if (params['genre']) {
        this.selectedGenreId = Number(params['genre']) || null;
      }
      if (params['page']) {
        this.currentPage = Number(params['page']) || 0;
      }
      this.fetchMedia();
    });
  }

  generateYearsList(): void {
    const currentYear = new Date().getFullYear();
    for (let y = currentYear; y >= 1970; y--) {
      this.years.push(y);
    }
  }

  loadFilterOptions(): void {
    this.mediaService.getGenres().subscribe({
      next: (data) => this.genres = data,
      error: (err) => console.error('Erreur chargement genres', err)
    });

    this.mediaService.getActors().subscribe({
      next: (data) => this.actors = data,
      error: (err) => console.error('Erreur chargement acteurs', err)
    });

    this.mediaService.getDirectors().subscribe({
      next: (data) => this.directors = data,
      error: (err) => console.error('Erreur chargement réalisateurs', err)
    });
  }

  fetchMedia(): void {
    this.isLoading = true;
    this.hasError = false;

    const filters: MediaFilter = {
      q: this.searchQuery,
      type: this.selectedType,
      genre: this.selectedGenreId,
      actor: this.selectedActorId,
      director: this.selectedDirectorId,
      year: this.selectedYear,
      page: this.currentPage,
      size: this.pageSize
    };

    this.mediaService.getMediaList(filters).subscribe({
      next: (response: PagedResponse<MediaSummary>) => {
        this.mediaList = response.content;
        this.currentPage = response.page;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur chargement catalogue', err);
        this.isLoading = false;
        this.hasError = true;
        this.errorMessage = 'Impossible de charger le catalogue. Veuillez vérifier que le serveur backend est en ligne.';
      }
    });
  }

  onSearchSubmit(): void {
    this.currentPage = 0;
    this.fetchMedia();
  }

  onFilterChange(): void {
    this.currentPage = 0;
    this.fetchMedia();
  }

  setType(type: string): void {
    this.selectedType = type;
    this.currentPage = 0;
    this.fetchMedia();
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.fetchMedia();
    window.scrollTo({ top: 380, behavior: 'smooth' });
  }

  resetFilters(): void {
    this.searchQuery = '';
    this.selectedType = 'ALL';
    this.selectedGenreId = null;
    this.selectedActorId = null;
    this.selectedDirectorId = null;
    this.selectedYear = null;
    this.currentPage = 0;
    this.router.navigate(['/']);
    this.fetchMedia();
  }

  hasActiveFilters(): boolean {
    return !!(
      this.searchQuery ||
      this.selectedType !== 'ALL' ||
      this.selectedGenreId ||
      this.selectedActorId ||
      this.selectedDirectorId ||
      this.selectedYear
    );
  }
}
