# Library Management System – Spring Boot

A backend-focused **Library Management System** built using **Java & Spring Boot**, designed to simulate a real-world digital library where users can browse, borrow, and manage books with fine calculation logic.

---

## 🚀 Tech Stack
- Java
- Spring Boot
- Spring Security (JWT Authentication)
- MySQL
- Maven

---

## ✨ Key Features

### 👤 User Features
- User Registration & Login
- JWT-based Authentication
- Browse available books
- Borrow books **for free up to 15 days**
- View borrowed books
- Automatic **fine calculation (₹5/day)** after 15 days
- Paid borrowing logic after free period

### 🛠 Admin Features
- Admin login with role-based access
- Add, update, delete books (CRUD)
- Manage users
- View all borrowed books and transactions

---

## 🔐 Security
- JWT-based authentication
- Role-based authorization (ADMIN / USER)
- Secure REST APIs using Spring Security

---

## 🗄 Database
- MySQL used for persistent storage
- Relationships between Users, Books, and Borrow Records
- Optimized SQL queries for performance

---

## 📡 REST APIs
- Authentication APIs (Signup / Login)
- Book APIs (View / Borrow / Return)
- Admin APIs (Book & User Management)

---

## 🧪 Tools Used
- Postman (API Testing)
- Git & GitHub (Version Control)

---

## ▶️ How to Run the Project

1. Clone the repository
   ```bash
   git clone https://github.com/Tushar424-png/library-management-system-springboot.git
