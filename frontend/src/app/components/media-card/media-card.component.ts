import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MediaSummary } from '../../models/media.model';

@Component({
  selector: 'app-media-card',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './media-card.component.html',
  styleUrls: ['./media-card.component.css']
})
export class MediaCardComponent {
  @Input({ required: true }) media!: MediaSummary;

  formatDuration(duration?: number): string {
    if (!duration) return '';
    const hours = Math.floor(duration / 60);
    const minutes = duration % 60;
    if (hours === 0) return `${minutes}m`;
    return `${hours}h ${minutes.toString().padStart(2, '0')}m`;
  }

  onImageError(event: Event) {
    const target = event.target as HTMLImageElement;
    target.src = 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600';
  }
}
