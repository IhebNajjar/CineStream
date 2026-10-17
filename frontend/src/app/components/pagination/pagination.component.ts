import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent {
  @Input({ required: true }) currentPage: number = 0;
  @Input({ required: true }) totalPages: number = 0;
  @Input() totalElements: number = 0;
  @Input() pageSize: number = 12;

  @Output() pageChange = new EventEmitter<number>();

  goToPage(page: number) {
    if (page >= 0 && page < this.totalPages && page !== this.currentPage) {
      this.pageChange.emit(page);
    }
  }

  getPages(): (number | string)[] {
    const pages: (number | string)[] = [];
    const total = this.totalPages;
    const current = this.currentPage;

    if (total <= 7) {
      for (let i = 0; i < total; i++) {
        pages.push(i);
      }
      return pages;
    }

    // Always include first page
    pages.push(0);

    if (current > 2) {
      pages.push('...');
    }

    const start = Math.max(1, current - 1);
    const end = Math.min(total - 2, current + 1);

    for (let i = start; i <= end; i++) {
      pages.push(i);
    }

    if (current < total - 3) {
      pages.push('...');
    }

    // Always include last page
    pages.push(total - 1);

    return pages;
  }
}
