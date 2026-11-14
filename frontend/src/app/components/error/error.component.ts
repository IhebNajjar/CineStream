import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-error',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {
  private route = inject(ActivatedRoute);

  errorCode = 404;
  title = 'Page Introuvable';
  description = 'Le titre ou la ressource que vous recherchez semble introuvable ou a été déplacé.';

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      if (data['code']) {
        this.errorCode = data['code'];
      }
      this.configureError();
    });
  }

  private configureError(): void {
    if (this.errorCode === 403) {
      this.title = 'Accès Interdit (403)';
      this.description = 'Vous n\'avez pas les autorisations suffisantes pour accéder à cet espace d\'administration.';
    } else if (this.errorCode === 500) {
      this.title = 'Erreur Serveur (500)';
      this.description = 'Une erreur inattendue est survenue sur nos serveurs. Notre équipe a été prévenue.';
    } else {
      this.errorCode = 404;
      this.title = 'Titre ou Page Introuvable (404)';
      this.description = 'L\'adresse demandée n\'existe pas dans notre catalogue CineStream.';
    }
  }
}
