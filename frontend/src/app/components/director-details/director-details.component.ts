import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MediaService } from '../../services/media.service';
import { DirectorDetails } from '../../models/media.model';
import { MediaCardComponent } from '../media-card/media-card.component';

@Component({
  selector: 'app-director-details',
  standalone: true,
  imports: [CommonModule, RouterLink, MediaCardComponent],
  templateUrl: './director-details.component.html',
  styleUrls: ['./director-details.component.css']
})
export class DirectorDetailsComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private mediaService = inject(MediaService);

  director = signal<DirectorDetails | null>(null);
  loading = signal(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      if (id) {
        this.loadDirector(id);
      }
    });
  }

  loadDirector(id: number): void {
    this.loading.set(true);
    this.error.set(null);
    this.mediaService.getDirector(id).subscribe({
      next: (data) => {
        this.director.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Réalisateur introuvable.');
        this.loading.set(false);
      }
    });
  }
}
