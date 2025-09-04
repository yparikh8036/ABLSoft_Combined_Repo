export interface Invoice {
  customerId: number;
  invoiceNum: number;
  date: string;       // ISO string from backend
  description: string;
  amount: number;
  invoiceAge?: number; // calculated in frontend
}
