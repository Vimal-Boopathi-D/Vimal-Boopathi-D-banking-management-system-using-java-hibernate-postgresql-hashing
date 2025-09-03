# Banking Management System (Console)

This is a console-based Banking Management System built with Java, Hibernate, PostgreSQL, and Maven.

## Prerequisites
- Java 11+
- Maven 3.6+
- PostgreSQL

## Setup
1. Create database and user in PostgreSQL:

```sql
CREATE DATABASE bank_db;
CREATE USER bank_user WITH ENCRYPTED PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE bank_db TO bank_user;
```

2. Update `src/main/resources/hibernate.cfg.xml` if you changed DB credentials.

3. Build:
```bash
mvn clean compile
```

4. Run:
```bash
mvn exec:java -Dexec.mainClass="com.bank.App"
```

Default admin created: username `admin`, password `admin` (change it after first login).

## Features
- Register & Login (ADMIN/CUSTOMER)
- Create accounts (Savings/Current)
- Deposit, Withdraw, Transfer
- Transaction history

## Next steps / Improvements
- Add locking for transfers
- Add validations and better CLI
- Convert to Spring Boot + REST API + React UI
