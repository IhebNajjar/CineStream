import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { MediaService } from '../../services/media.service';
import { Actor, Director, Genre, MediaFilter, MediaSummary, PagedResponse } from '../../models/media.model';
import { MediaCardComponent } from '../media-card/media-card.component';
import { PaginationComponent } from '../pagination/pagination.component';

@Component({
  selector: 'app-catalogue',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, MediaCardComponent, PaginationComponent],
  templateUrl: './catalogue.component.html',
  styleUrls: ['./catalogue.component.css']
})
export class CatalogueComponent implements OnInit {
  private mediaService = inject(MediaService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  // Page mode & metadata
  pageMode: 'films' | 'series' | 'catalogue' = 'films';
  pageTitle: string = 'Films';
  pageBadge: string = 'COLLECTION FILMS';
  pageSubtitle: string = 'Découvrez notre collection de longs-métrages en streaming haute définition et 4K Ultra HD.';
  searchPlaceholder: string = 'Rechercher un film par titre... (ex: Inception, Dune, Oppenheimer)';

  // Data
  mediaList: MediaSummary[] = [];
  genres: Genre[] = [];
  actors: Actor[] = [];
  directors: Director[] = [];

  // Pagination state (US008: 12 per page)
  currentPage: number = 0;
  totalPages: number = 0;
  totalElements: number = 0;
  pageSize: number = 12;

  // Filter model (US009, US011)
  searchQuery: string = '';
  selectedType: string = 'FILM';
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
    this.detectPageMode();

    // Re-detect mode on navigation
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.detectPageMode();
      this.fetchMedia();
    });

    // Listen to query params changes
    this.route.queryParams.subscribe(params => {
      if (params['q'] !== undefined) {
        this.searchQuery = params['q'];
      }
      if (params['genre'] !== undefined) {
        this.selectedGenreId = Number(params['genre']) || null;
      }
      if (params['actor'] !== undefined) {
        this.selectedActorId = Number(params['actor']) || null;
      }
      if (params['director'] !== undefined) {
        this.selectedDirectorId = Number(params['director']) || null;
      }
      if (params['year'] !== undefined) {
        this.selectedYear = Number(params['year']) || null;
      }
      if (params['page'] !== undefined) {
        this.currentPage = Number(params['page']) || 0;
      }
      if (params['type'] !== undefined && this.pageMode === 'catalogue') {
        this.selectedType = params['type'];
      }
      this.fetchMedia();
    });
  }

  detectPageMode(): void {
    const url = this.router.url;
    if (url.includes('/series')) {
      this.pageMode = 'series';
      this.selectedType = 'SERIE';
      this.pageTitle = 'Toutes les Séries';
      this.pageBadge = 'COLLECTION SÉRIES';
      this.pageSubtitle = 'Explorez nos séries complètes et saisons intégrales en streaming haute définition.';
      this.searchPlaceholder = 'Rechercher une série par titre... (ex: Breaking Bad, Stranger Things, The Last of Us)';
    } else if (url.includes('/catalogue')) {
      this.pageMode = 'catalogue';
      this.selectedType = 'ALL';
      this.pageTitle = 'Catalogue Général';
      this.pageBadge = 'CATALOGUE GÉNÉRAL';
      this.pageSubtitle = 'Découvrez l\'ensemble des films et séries disponibles à la location sur CineStream.';
      this.searchPlaceholder = 'Rechercher un film ou une série par titre...';
    } else {
      // Default to Films
      this.pageMode = 'films';
      this.selectedType = 'FILM';
      this.pageTitle = 'Tous les Films';
      this.pageBadge = 'COLLECTION FILMS';
      this.pageSubtitle = 'Découvrez notre collection de longs-métrages en streaming haute définition et 4K Ultra HD.';
      this.searchPlaceholder = 'Rechercher un film par titre... (ex: Inception, Dune, Oppenheimer, The Dark Knight)';
    }
  }

  generateYearsList(): void {
    const currentYear = new Date().getFullYear();
    for (let y = currentYear; y >= 1970; y--) {
      this.years.push(y);
    }
  }

  loadFilterOptions(): void {
    this.mediaService.getGenres().subscribe({
      next: (data) => {
        this.genres = data;
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Erreur chargement genres', err)
    });

    this.mediaService.getActors().subscribe({
      next: (data) => {
        this.actors = data;
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Erreur chargement acteurs', err)
    });

    this.mediaService.getDirectors().subscribe({
      next: (data) => {
        this.directors = data;
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Erreur chargement réalisateurs', err)
    });
  }

  fetchMedia(): void {
    this.isLoading = true;
    this.hasError = false;
    this.cdr.markForCheck();

    const filters: MediaFilter = {
      q: this.searchQuery.trim() || undefined,
      type: this.selectedType,
      genre: this.selectedGenreId || undefined,
      actor: this.selectedActorId || undefined,
      director: this.selectedDirectorId || undefined,
      year: this.selectedYear || undefined,
      page: this.currentPage,
      size: this.pageSize
    };

    this.mediaService.getMediaList(filters).subscribe({
      next: (response: PagedResponse<MediaSummary>) => {
        this.mediaList = response.content || [];
        this.currentPage = response.page;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.isLoading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('[CatalogueComponent] fetchMedia ERROR:', err);
        this.isLoading = false;
        this.hasError = true;
        this.errorMessage = 'Impossible de charger le catalogue. Veuillez vérifier que le serveur backend est en ligne.';
        this.cdr.markForCheck();
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
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  selectGenre(genreId: number | null): void {
    this.selectedGenreId = genreId;
    this.currentPage = 0;
    this.fetchMedia();
  }

  clearSearch(): void {
    this.searchQuery = '';
    this.currentPage = 0;
    this.fetchMedia();
  }

  clearGenre(): void {
    this.selectedGenreId = null;
    this.currentPage = 0;
    this.fetchMedia();
  }

  clearActor(): void {
    this.selectedActorId = null;
    this.currentPage = 0;
    this.fetchMedia();
  }

  clearDirector(): void {
    this.selectedDirectorId = null;
    this.currentPage = 0;
    this.fetchMedia();
  }

  clearYear(): void {
    this.selectedYear = null;
    this.currentPage = 0;
    this.fetchMedia();
  }

  resetFilters(): void {
    this.searchQuery = '';
    this.selectedGenreId = null;
    this.selectedActorId = null;
    this.selectedDirectorId = null;
    this.selectedYear = null;
    this.currentPage = 0;
    if (this.pageMode === 'catalogue') {
      this.selectedType = 'ALL';
    }
    this.fetchMedia();
  }

  getActiveGenreName(): string {
    const g = this.genres.find(x => x.id === this.selectedGenreId);
    return g ? g.name : '';
  }

  getActiveActorName(): string {
    const a = this.actors.find(x => x.id === this.selectedActorId);
    return a ? a.name : '';
  }

  getActiveDirectorName(): string {
    const d = this.directors.find(x => x.id === this.selectedDirectorId);
    return d ? d.name : '';
  }

  getActiveFiltersCount(): number {
    let count = 0;
    if (this.searchQuery.trim()) count++;
    if (this.pageMode === 'catalogue' && this.selectedType !== 'ALL') count++;
    if (this.selectedGenreId) count++;
    if (this.selectedActorId) count++;
    if (this.selectedDirectorId) count++;
    if (this.selectedYear) count++;
    return count;
  }

  hasActiveFilters(): boolean {
    return this.getActiveFiltersCount() > 0;
  }
}
