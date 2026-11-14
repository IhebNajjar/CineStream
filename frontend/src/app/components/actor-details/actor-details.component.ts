import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MediaService } from '../../services/media.service';
import { ActorDetails } from '../../models/media.model';

@Component({
  selector: 'app-actor-details',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './actor-details.component.html',
  styleUrls: ['./actor-details.component.css']
})
export class ActorDetailsComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private mediaService = inject(MediaService);

  actor = signal<ActorDetails | null>(null);
  loading = signal(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      if (id) {
        this.loadActor(id);
      }
    });
  }

  loadActor(id: number): void {
    this.loading.set(true);
    this.error.set(null);
    this.mediaService.getActor(id).subscribe({
      next: (data) => {
        this.actor.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Acteur introuvable.');
        this.loading.set(false);
      }
    });
  }
}
