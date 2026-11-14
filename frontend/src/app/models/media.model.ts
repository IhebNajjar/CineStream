export type MediaType = 'FILM' | 'SERIE';
export type Role = 'USER' | 'ADMIN';

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

export interface Episode {
  id: number;
  episodeNumber: number;
  title: string;
  description?: string;
  duration?: number;
  videoKey?: string;
}

export interface Season {
  id: number;
  seasonNumber: number;
  title: string;
  releaseYear?: number;
  episodes: Episode[];
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
  ratingAverage?: number;
  ratingCount?: number;
  videoKey?: string;
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
  videoKey?: string;
  rentalPricePerDay: number;
  genres: Genre[];
  directors: Director[];
  actors: MediaActor[];
  seasons?: Season[];
  ratingAverage?: number;
  reviewsCount?: number;
}

export interface ActorRole {
  mediaId: number;
  mediaTitle: string;
  posterUrl?: string;
  releaseYear: number;
  type: string;
  characterName: string;
}

export interface ActorDetails extends Actor {
  filmography: ActorRole[];
}

export interface DirectorDetails extends Director {
  filmography: MediaSummary[];
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
  sortBy?: string;
  sortDir?: string;
  page?: number;
  size?: number;
}

export interface User {
  id: number;
  email: string;
  role: Role;
}

export interface AuthResponse {
  token: string;
  id: number;
  email: string;
  role: Role;
}

export interface Wallet {
  id: number;
  userId: number;
  balance: number;
}

export interface WalletTransaction {
  id: number;
  amount: number;
  type: 'RECHARGE' | 'LOCATION';
  description?: string;
  createdAt: string;
}

export interface Rental {
  id: number;
  userId: number;
  mediaId: number;
  mediaTitle: string;
  mediaPosterUrl?: string;
  mediaType: string;
  startDate: string;
  endDate: string;
  days: number;
  totalPrice: number;
  status: 'EN_COURS' | 'TERMINEE';
  daysLeft?: number;
  hoursLeft?: number;
  active: boolean;
}

export interface WatchLinkResponse {
  watchUrl: string;
  expiresAt: string;
  title: string;
  mediaType: string;
  rentalId: number;
}

export interface Review {
  id: number;
  userId: number;
  userEmail: string;
  mediaId: number;
  rating: number;
  comment?: string;
  createdAt: string;
  ownReview?: boolean;
}

export interface Favorite {
  id: number;
  userId: number;
  mediaId: number;
  media: MediaSummary;
  addedAt: string;
}

export interface DashboardStats {
  totalMedia: number;
  moviesCount: number;
  seriesCount: number;
  actorsCount: number;
  directorsCount: number;
  genresCount: number;
  activeRentalsCount: number;
  totalRevenue: number;
  usersCount: number;
}
