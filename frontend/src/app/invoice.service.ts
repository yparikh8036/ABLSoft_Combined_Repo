import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Invoice } from './invoice.model';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {

private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  /** Upload CSV File */
  uploadCsv(file: File): Observable<Invoice[]> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<Invoice[]>(`${this.apiUrl}/upload`, formData);
  }

  /** Get All Invoices */
  getInvoices(): Observable<Invoice[]> {
    return this.http.get<Invoice[]>(`${this.apiUrl}/files`);
  }

  /** Get Invoice by ID */
  getInvoice(id: number): Observable<Invoice> {
    return this.http.get<Invoice>(`${this.apiUrl}/files/${id}`);
  }

  /** Create a new Invoice */
  createInvoice(invoice: Invoice): Observable<Invoice> {
    return this.http.post<Invoice>(`${this.apiUrl}/upload-file`, invoice);
  }

  /** Update Invoice */
  updateInvoice(id: number, invoice: Invoice): Observable<Invoice> {
    return this.http.put<Invoice>(`${this.apiUrl}/upload-file/${id}`, invoice);
  }

}
