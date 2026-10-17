import { Routes } from '@angular/router';
import { CatalogueComponent } from './components/catalogue/catalogue.component';
import { MediaDetailsComponent } from './components/media-details/media-details.component';

export const routes: Routes = [
  {
    path: '',
    component: CatalogueComponent,
    title: 'CineStream - Catalogue de Films & Séries'
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
