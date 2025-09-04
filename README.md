# ABLSoft Combined Repo

This repository combines the **ABLSoft** backend and frontend components into a unified development setup.

## Repository Structure

- `ablsoft/` – Backend components and services (e.g., API, business logic)
- `frontend/` – Frontend application (web UI)
- `docker-compose.yml` – Docker Compose configuration to orchestrate services together

---

## Getting Started

### Prerequisites

Ensure you have the following installed:
- [Docker](https://docs.docker.com/get-docker/) & [Docker Compose](https://docs.docker.com/compose/install/)
- (Optional) Node.js and npm/yarn if you prefer running frontend locally
- (Optional) Your preferred IDE or editor

---

## Running Locally with Docker

1. Clone the repository:
   ```bash
   git clone https://github.com/yparikh8036/ABLSoft_Combined_Repo.git
   cd ABLSoft_Combined_Repo
   ```

2. Start the services:
   ```bash
   docker-compose up --build
   ```

3. Access the applications:
   - Backend API: `http://localhost:<backend-port>`
   - Frontend UI: `http://localhost:<frontend-port>`

---


## Features

- **Backend** (`ablsoft/`):
  - RESTful API endpoints for asset-based lending workflows
  - Borrowing Base calculations, data persistence, user authentication (update as needed)

- **Frontend** (`frontend/`):
  - User interface for loan data, dashboards, forms
  - Real-time updates and responsive design

---

