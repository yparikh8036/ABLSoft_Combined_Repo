# 📄 Invoice Web Application

This is a **Full-Stack Application** to upload and view invoice records. The application supports uploading a CSV file containing invoice data, which is then stored in a **MySQL** database and displayed via a modern **Angular web UI**.

---

## 🛠️ Tech Stack
- **Backend:** Spring Boot (Java, REST API, JPA/Hibernate)
- **Frontend:** Angular (Material UI, HTTP Client)
- **Database:** MySQL
- **Containerization:** Docker, Docker Compose

---

## 🚀 Features
- 📤 Upload invoice records via CSV file
- 🖥️ Frontend: Angular
- ⚙️ Backend: Spring Boot (Java)
- 🗄️ Database: MySQL
- 🐳 Containerized using Docker & Docker Compose

---

## 📂 CSV Sameple Format

The CSV file used for uploading invoices must follow this format:

- customerId;invoiceNum;date;description;amount
- 101;5001;2025-09-01;Website development;1500
- 102;5002;2025-09-02;Mobile app design;2500
- 103;5003;2025-09-03;Cloud hosting services;1200
- 104;5004;2025-09-03;Consulting session;800
- 105;5005;2025-09-03;Annual subscription;3000

---

### Notes:
- **Customer ID:** Unique identifier for the customer  
- **Invoice Number:** Unique invoice reference  
- **Invoice Date:** Format `YYYY-MM-DD`  
- **Description:** Brief description of the invoice  
- **Amount:** Invoice amount in decimal format 

---

## Running Locally with Docker

1. Clone the repository:
   ```bash
   git clone https://github.com/yparikh8036/Invoice_App_Repo.git
   
   cd Invoice_App_Repo
   ```

2. Start the services:
   ```bash
   docker-compose up --build
   ```

3. Access the applications:
   - Backend API: `http://localhost:8080`
   - Frontend UI: `http://localhost:4200`

---


