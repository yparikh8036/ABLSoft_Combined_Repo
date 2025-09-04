import { ChangeDetectorRef, Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { InvoiceService } from './invoice.service';
import { Invoice } from './invoice.model';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [MatTableModule, MatButtonModule, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'frontend';
  invoices: Invoice[] = [];
  page = 1;
  pageSize = 10;

  constructor(private invoiceService: InvoiceService) {}

  ngOnInit(): void {
    this.loadInvoices();
  }

  loadInvoices() {
    this.invoiceService.getInvoices().subscribe({
      next: (data) => {
        this.invoices = data;
      },
      error: (err) => console.error('Error loading invoices', err),
    });
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.invoiceService.uploadCsv(file).subscribe({
        next: (data) => {
          this.invoices.concat(data);
          this.loadInvoices();
        },
        error: (error) => {
          console.error('Error uploading file', error);
          if (error.status !== 200 && error.status !== 201) {
            alert('Error uploading the file');
          }
        },
      });
    }
  }
}
