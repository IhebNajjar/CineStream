import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { CatalogueComponent } from './components/catalogue/catalogue.component';
import { MediaDetailsComponent } from './components/media-details/media-details.component';
import { ActorDetailsComponent } from './components/actor-details/actor-details.component';
import { DirectorDetailsComponent } from './components/director-details/director-details.component';
import { ProfileComponent } from './components/profile/profile.component';
import { RentalsComponent } from './components/rentals/rentals.component';
import { FavoritesComponent } from './components/favorites/favorites.component';
import { AdminDashboardComponent } from './components/admin/admin-dashboard.component';
import { ErrorComponent } from './components/error/error.component';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: 'CineStream - Accueil'
  },
  {
    path: 'films',
    component: CatalogueComponent,
    title: 'CineStream - Films'
  },
  {
    path: 'series',
    component: CatalogueComponent,
    title: 'CineStream - Séries'
  },
  {
    path: 'catalogue',
    component: CatalogueComponent,
    title: 'CineStream - Catalogue Général'
  },
  {
    path: 'media/:id',
    component: MediaDetailsComponent,
    title: 'CineStream - Détails du titre'
  },
  {
    path: 'actors/:id',
    component: ActorDetailsComponent,
    title: 'CineStream - Fiche Acteur'
  },
  {
    path: 'directors/:id',
    component: DirectorDetailsComponent,
    title: 'CineStream - Fiche Réalisateur'
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [authGuard],
    title: 'CineStream - Mon Profil & Portefeuille'
  },
  {
    path: 'rentals',
    component: RentalsComponent,
    canActivate: [authGuard],
    title: 'CineStream - Mes Locations'
  },
  {
    path: 'favorites',
    component: FavoritesComponent,
    canActivate: [authGuard],
    title: 'CineStream - Mes Favoris'
  },
  {
    path: 'admin',
    component: AdminDashboardComponent,
    canActivate: [adminGuard],
    title: 'CineStream - Administration'
  },
  {
    path: '403',
    component: ErrorComponent,
    data: { code: 403 },
    title: 'CineStream - Accès Interdit (403)'
  },
  {
    path: '404',
    component: ErrorComponent,
    data: { code: 404 },
    title: 'CineStream - Page Introuvable (404)'
  },
  {
    path: '500',
    component: ErrorComponent,
    data: { code: 500 },
    title: 'CineStream - Erreur Serveur (500)'
  },
  {
    path: '**',
    component: ErrorComponent,
    data: { code: 404 },
    title: 'CineStream - 404'
  }
];
