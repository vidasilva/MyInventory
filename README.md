# 🧾 Inventory & Sales Tracker

> A modern desktop inventory system built with JavaFX and SQLite, featuring user authentication, SKU-based product tracking.

---

## ✨ Features

* 🔐 User authentication (signup, login)
* 🗂️ Per-user data isolation
* 📦 Add/edit/delete products
* 🔎 Instant FTS5 search (name, brand, supplier, SKU, and attributes)
* 📊 Custom product attributes (color, size, etc.)
* 🔄 SKU generation with collision prevention
* ⚙️ JavaFX front-end with clean FXML separation
* ✅ Full DAO-Service-Controller architecture

---

## 🛠 Tech Stack

| Layer        | Tech                               |
| ------------ | ---------------------------------- |
| UI           | JavaFX + FXML                      |
| Logic        | Java 17+                           |
| Database     | SQLite                             |
| Architecture | MVC-ish (DAO, Service, Controller) |

---

## 🚀 Getting Started

### 1. Clone the repo

```bash
git clone https://github.com/vidasilva/MyInventory.git
cd MyInventory
```

### 2. Build and run

Use your favorite IDE (like IntelliJ or VSCode with Java support) or compile directly:

```bash
javac -d out src/**/*.java
java -cp out app.Main
```

### 3. Login / Signup

* On first run, create a user account.
* User data is isolated — each user gets their own set of products.

---

## 🧠 About the Design

### User System

* Passwords are hashed using SHA-256.
* Duplicate usernames are handled gracefully.
* Optional: add roles (e.g. admin, user) by extending the `user` table schema.

### Product System

* Every product has a unique SKU.
* Products support dynamic key-value attributes.
* Full CRUD via a JavaFX dashboard.

---

## 🧪 Future Improvements

* Switch from SHA-256 encryption to BCrypt
* Export to CSV / Excel
* Unit tests for service layer
* Cloud sync with remote SQLite or switch to PostgreSQL
* A virtual FTS5 table indexes `name`, `brand`, `supplier` and also `attributes`
* Proper bug logging for production
---

## 👨‍💻 Author

Built by Vitor D.G. Silva for portfolio purposes. Inspired by real-world retail and warehouse needs.

* GitHub: [@vidasilva](https://github.com/vidasilva)
* LinkedIn: [Vitor D.G. Silva](https://www.linkedin.com/in/vidasilva/)

---

## 📄 License

MIT License. Free to use, fork, and learn from.

