import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { CatalogueComponent } from './components/catalogue/catalogue.component';
import { MediaDetailsComponent } from './components/media-details/media-details.component';

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
    path: '**',
    redirectTo: ''
  }
];
