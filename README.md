# 🍽️ Restaurant Handling System

A **Java Swing desktop application** with **MySQL database** for managing a restaurant's daily operations — food menu, tables, customers, orders, billing and more.

---

## 📌 Project Info

| Field | Details |
|---|---|
| **Project Title** | Restaurant Handling System |
| **Course / Division** | BCA 3C |
| **College** | Marwadi University |
| **Guide / Teacher** | Tirth Bhadeshiya Sir |
| **Group Members** | Fiza Zahid, Vishwani |
| **Total Members** | 2 |

---

## ✨ Features

- 🔐 **Secure Login System** — username & password authentication
- 🍕 **Food Menu Management** — view & add food items with category and price
- 🪑 **Table Management** — view & add restaurant tables with status
- 👤 **Customer Management** — store customer name & phone number
- 🧾 **Orders & Billing** — place orders with live total calculation
- 💾 **Smart Bill Generator** — itemized bill with **GST (5%)** and grand total, saveable as `.txt` file
- 📊 **Live Dashboard Stats** — real-time total sales, total orders, food items & customer count
- 🌙 **Dark Mode** — switch between light & dark theme with one click
- 🎨 **Modern UI** — colorful cards, hover effects, shake animation on wrong login
- 📋 **Data in Table Format** — all records displayed in clean, styled tables

---

## 🛠️ Technologies Used

- **Java 17** — Core programming language
- **Java Swing (JFC)** — Graphical User Interface
- **MySQL** — Database
- **JDBC** — Java Database Connectivity
- **Maven** — Project build & dependency management
- **IntelliJ IDEA** — IDE

---

## 📁 Project Structure

```
src/
 └── com/
      └── restaurant/
            ├── Main.java               → Application entry point (UI)
            ├── dao/
            │     ├── FoodDAO.java      → Food database operations
            │     ├── CustomerDAO.java  → Customer database operations
            │     ├── TableDAO.java     → Table database operations
            │     └── OrderDAO.java     → Order database operations
            ├── model/
            │     ├── Food.java         → Food entity
            │     ├── Customer.java     → Customer entity
            │     ├── RestaurantTable.java → Table entity
            │     └── Order.java        → Order entity
            └── util/
                  └── DBConnection.java → Database connection
```

---

## ▶️ How to Run

1. **Clone / download** the repository
2. Import the project in **IntelliJ IDEA** (Maven project)
3. Create the **MySQL database** using the provided `.sql` file
4. Update database credentials in `util/DBConnection.java` if needed
5. Run **`Main.java`**

### 🔑 Login Credentials

| Username | Password |
|---|---|
| `RestoChef` | `Taste@2026` |

---

## 📸 Screenshots

*(Add screenshots of Login, Dashboard, Food Menu, Orders & Billing here)*

---

## 👨‍🏫 Guide

**Tirth Bhadeshiya Sir** — Marwadi University

---

*Made with ❤️ by Fiza Zahid & Vishwani (BCA 3C)*
