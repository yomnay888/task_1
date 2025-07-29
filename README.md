#  Library Management System (Dockerized)

A simple Java console-based Library Management System containerized with Docker and connected to a MySQL database. This project demonstrates basic database interaction using Hibernate and runs in a multi-container setup via Docker Compose.

---

##  Prerequisites

Make sure the following are installed on your machine:

- [Docker](https://www.docker.com/products/docker-desktop)


---

##  Setup Instructions

Clone the repository:

```bash
git clone https://github.com/yomnay888/task_1.git
cd task_1
```

Run the project:

```bash
docker-compose up --build
```

This will:
- Build the Java application.
- Start a MySQL container.
- Launch Adminer for database access.

---

##   Environment Variables

Environment variables are defined in the `.env` file:

| Variable        | Description                      | Example           |
|----------------|----------------------------------|-------------------|
| `DB_HOST`       | Database host                    | `mysql`           |
| `DB_PORT`       | MySQL port                       | `3306`            |
| `DB_NAME`       | MySQL database name              | `task_1`          |
| `DB_USER`       | MySQL username                   | `root`            |
| `DB_PASSWORD`   | MySQL password                   | `1248`            |
| `ADMINER_PORT`  | Port on which Adminer runs       | `8080`            |

---

##   Access Instructions

- **Java App**: Automatically runs in the container when the stack is up.
- **Adminer UI**: Visit [http://localhost:8080](http://localhost:8080)

To log into Adminer:

- **System**: `MySQL`
- **Server**: `db`
- **Username**: `root`
- **Password**: `1248`
- **Database**: `task_1`

---
##   Troubleshooting Tips

- 1 - **Can not access the console stdIn**  
    You can open a new terminal and write **docker attach java-app** and this will allow you to write input and interact with the java-app
- 2 - **MySQL Port (3306) Already in Use**
     Kill the process using this port (the most easy approach)
      On Windows:
```bash
netstat -aon | findstr :3306
taskkill /PID <PID> /F
``` 
