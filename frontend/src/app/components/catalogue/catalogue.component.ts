import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
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

  // Popular of the week (SaintStream V2 feature)
  popularList: MediaSummary[] = [];

  // Hero carousel slides with authentic widescreen 1280x720/1920x1080 landscape backdrops
  heroSlides = [
    {
      id: 6,
      title: 'Dune : Deuxième Partie',
      backdrop: '/assets/backdrops/6.jpg',
      tag: 'TENDANCE N°1',
      rating: '8.9',
      year: 2024,
      age: '12+',
      duration: '2h 46m',
      genres: 'Science-Fiction, Aventure, Action',
      synopsis: 'Paul Atreides s\'unit à Chani et aux Fremen pour mener la révolte contre ceux qui ont anéanti sa famille. Pris entre l\'amour de sa vie et le destin de l\'univers, il lutte pour empêcher un avenir terrifiant.',
      trailerUrl: 'https://www.youtube.com/watch?v=Way9Dexny3w'
    },
    {
      id: 8,
      title: 'The Last of Us',
      backdrop: '/assets/backdrops/8.jpg',
      tag: 'SÉRIE ÉVÉNEMENT',
      rating: '8.8',
      year: 2023,
      age: '16+',
      duration: '1 Saison (9 Ép.)',
      genres: 'Drame, Action, Post-Apocalyptique',
      synopsis: 'Dans une Amérique post-apocalyptique dévastée par une infection fongique, Joel, un survivant endurci, est chargé d\'escorter la jeune Ellie hors d\'une zone de quarantaine oppressante.',
      trailerUrl: 'https://www.youtube.com/watch?v=uLtkt8BonwM'
    },
    {
      id: 2,
      title: 'Interstellar',
      backdrop: '/assets/backdrops/2.jpg',
      tag: 'CHEF-D\'ŒUVRE',
      rating: '8.7',
      year: 2014,
      age: '10+',
      duration: '2h 49m',
      genres: 'Science-Fiction, Drame, Aventure',
      synopsis: 'Face à l\'épuisement des ressources terrestres, une équipe d\'explorateurs utilise une faille dans l\'espace-temps pour franchir les distances interstellaires et trouver un avenir pour l\'humanité.',
      trailerUrl: 'https://www.youtube.com/watch?v=zSWdZVtXT7E'
    },
    {
      id: 3,
      title: 'The Dark Knight',
      backdrop: '/assets/backdrops/3.jpg',
      tag: 'CULTE',
      rating: '9.0',
      year: 2008,
      age: '12+',
      duration: '2h 32m',
      genres: 'Action, Crime, Thriller',
      synopsis: 'Batman, le lieutenant Gordon et Harvey Dent tentent d\'éradiquer la pègre à Gotham, mais un criminel sadique et imprévisible connu sous le nom du Joker sème l\'anarchie totale.',
      trailerUrl: 'https://www.youtube.com/watch?v=EXeTwQWrcwY'
    }
  ];
  activeHeroIndex: number = 0;

  // Studios strip
  studios = [
    { name: 'Netflix', badge: 'NETFLIX', icon: 'netflix' },
    { name: 'HBO Max', badge: 'HBO MAX', icon: 'hbo' },
    { name: 'Marvel Studios', badge: 'MARVEL', icon: 'marvel' },
    { name: 'Lucasfilm', badge: 'STAR WARS', icon: 'starwars' },
    { name: 'Pixar Animation', badge: 'PIXAR', icon: 'pixar' },
    { name: 'Disney+', badge: 'DISNEY+', icon: 'disney' },
    { name: 'Warner Bros.', badge: 'WARNER BROS', icon: 'wb' }
  ];

  // UI state
  isLoading: boolean = false;
  hasError: boolean = false;
  errorMessage: string = '';

  ngOnInit(): void {
    console.log('[CatalogueComponent] ngOnInit running');
    this.generateYearsList();
    this.loadFilterOptions();
    this.loadPopularMedia();

    // Listen to query params changes (e.g. from navbar navigation)
    this.route.queryParams.subscribe(params => {
      console.log('[CatalogueComponent] queryParams received:', params);
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

  loadPopularMedia(): void {
    this.mediaService.getMediaList({ page: 0, size: 5 }).subscribe({
      next: (res) => {
        this.popularList = res.content.slice(0, 5);
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Erreur chargement titres populaires', err)
    });
  }

  setHeroIndex(index: number): void {
    this.activeHeroIndex = index;
    this.cdr.markForCheck();
  }

  nextHero(): void {
    this.activeHeroIndex = (this.activeHeroIndex + 1) % this.heroSlides.length;
    this.cdr.markForCheck();
  }

  prevHero(): void {
    this.activeHeroIndex = (this.activeHeroIndex - 1 + this.heroSlides.length) % this.heroSlides.length;
    this.cdr.markForCheck();
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
      q: this.searchQuery,
      type: this.selectedType,
      genre: this.selectedGenreId,
      actor: this.selectedActorId,
      director: this.selectedDirectorId,
      year: this.selectedYear,
      page: this.currentPage,
      size: this.pageSize
    };

    console.log('[CatalogueComponent] fetchMedia calling API with filters:', filters);
    this.mediaService.getMediaList(filters).subscribe({
      next: (response: PagedResponse<MediaSummary>) => {
        console.log('[CatalogueComponent] fetchMedia SUCCESS, received elements:', response.totalElements);
        this.mediaList = response.content;
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

  showAdvancedFilters: boolean = true;

  toggleAdvancedFilters(): void {
    this.showAdvancedFilters = !this.showAdvancedFilters;
  }

  selectGenre(genreId: number | null): void {
    this.selectedGenreId = genreId;
    this.currentPage = 0;
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

  clearSearch(): void {
    this.searchQuery = '';
    this.currentPage = 0;
    this.fetchMedia();
  }

  getActiveFiltersCount(): number {
    let count = 0;
    if (this.searchQuery) count++;
    if (this.selectedType !== 'ALL') count++;
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
