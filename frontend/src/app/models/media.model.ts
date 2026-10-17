export type MediaType = 'FILM' | 'SERIE';

export interface Genre {
  id: number;
  name: string;
  description?: string;
}

export interface Actor {
  id: number;
  name: string;
  bio?: string;
  birthDate?: string;
  photoUrl?: string;
}

export interface Director {
  id: number;
  name: string;
  bio?: string;
  photoUrl?: string;
}

export interface MediaActor {
  actorId: number;
  name: string;
  photoUrl?: string;
  characterName: string;
}

export interface MediaSummary {
  id: number;
  title: string;
  releaseYear: number;
  type: MediaType;
  duration?: number;
  minimumAge: number;
  posterUrl?: string;
  rentalPricePerDay: number;
  genres: Genre[];
}

export interface MediaDetails {
  id: number;
  title: string;
  description: string;
  releaseYear: number;
  type: MediaType;
  duration?: number;
  language: string;
  country: string;
  minimumAge: number;
  posterUrl?: string;
  trailerUrl?: string;
  rentalPricePerDay: number;
  genres: Genre[];
  directors: Director[];
  actors: MediaActor[];
}

export interface PagedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface MediaFilter {
  q?: string;
  type?: string;
  genre?: number | null;
  actor?: number | null;
  director?: number | null;
  year?: number | null;
  page?: number;
  size?: number;
}
