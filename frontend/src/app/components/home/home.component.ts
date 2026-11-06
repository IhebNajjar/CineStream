import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MediaService } from '../../services/media.service';
import { MediaSummary } from '../../models/media.model';
import { MediaCardComponent } from '../media-card/media-card.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, MediaCardComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  private mediaService = inject(MediaService);
  private cdr = inject(ChangeDetectorRef);

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
      duration: '1 Saison (9 ép.)',
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

  popularList: MediaSummary[] = [];
  featuredFilms: MediaSummary[] = [];
  featuredSeries: MediaSummary[] = [];
  isLoading: boolean = false;

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;
    
    // Top 5 Popular media
    this.mediaService.getMediaList({ page: 0, size: 5 }).subscribe({
      next: (res) => {
        this.popularList = res.content || [];
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Erreur chargement populaires', err)
    });

    // 4 Featured Films
    this.mediaService.getMediaList({ page: 0, size: 4, type: 'FILM' }).subscribe({
      next: (res) => {
        this.featuredFilms = res.content || [];
        this.isLoading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erreur chargement films vedettes', err);
        this.isLoading = false;
      }
    });

    // 4 Featured Series
    this.mediaService.getMediaList({ page: 0, size: 4, type: 'SERIE' }).subscribe({
      next: (res) => {
        this.featuredSeries = res.content || [];
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Erreur chargement séries vedettes', err)
    });
  }

  goToHeroSlide(index: number): void {
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
}
