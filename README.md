# Account Management

A full-stack application for managing customers and their bank accounts. The backend exposes a REST API built with Spring Boot, and the frontend is a React application that communicates with it.

The core flow is straightforward: a customer record exists in the system, and you can open a new account for that customer with an optional initial credit. If the initial credit is greater than zero, the system automatically creates the first transaction for that account at the same time.

---

## Tech Stack

**Backend**
- Java 11
- Spring Boot 2.5.1
- Spring Data JPA / Hibernate
- H2 in-memory database
- Bean Validation (javax.validation)
- SpringDoc OpenAPI (Swagger UI)

**Frontend**
- React 18
- React Router v6
- Bootstrap 5 / Reactstrap
- Create React App

---

## Running the Application

**Backend** — runs on `http://localhost:8080`

```bash
cd account-backend
./mvnw spring-boot:run
```

Once the backend is up, the Swagger UI is available at:
`http://localhost:8080/swagger-ui.html`

**Frontend** — runs on `http://localhost:3000`

```bash
cd account-frontend
npm install
npm start
```

The frontend is pre-configured to proxy API requests to `localhost:8080`, so no extra setup is needed.

---

## API Endpoints

### Customer

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/v1/customer` | Returns a list of all customers |
| `GET` | `/v1/customer/{customerId}` | Returns a single customer by ID |

### Account

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/v1/account` | Creates a new account for a customer |

**POST /v1/account — Request body**

```json
{
  "customerId": "string (required)",
  "initialCredit": 100.00
}
```

`initialCredit` must be zero or greater. If it is greater than zero, the account is created with that amount and a corresponding transaction is recorded automatically.

**Example response**

```json
{
  "id": "account-uuid",
  "balance": 100.00,
  "customer": {
    "id": "customer-uuid",
    "name": "John",
    "surname": "Doe"
  },
  "transactions": [
    {
      "amount": 100.00,
      "createdAt": "2024-01-01T10:00:00"
    }
  ]
}
```

---

## Tests

The project has two layers of tests.

**Unit tests** (`CustomerServiceTest`) use Mockito to test the service layer in isolation. The repository and converter are mocked, so no database is involved. These tests cover the happy path (customer found) and the error path (customer not found → `CustomerNotFoundException`).

**Integration tests** (`AccountControllerTest`) use `@SpringBootTest` with MockMvc and run against the real application context with an H2 in-memory database. They verify the full request/response cycle — correct status codes, response body structure, and validation behaviour for bad inputs.

A notable design decision is that `AccountService` receives a `Clock` instance via constructor injection. This makes it easy to pass a fixed clock in tests and get deterministic timestamps without any mocking hacks.

To run all tests:

```bash
cd account-backend
./mvnw test
```
