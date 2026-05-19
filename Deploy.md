# Coffee-Corner Feedback API

A simple Spring Boot REST API that allows users to submit, view, update, and delete coffee feedback.

This project uses **in-memory storage only**, so no database is required. When the application restarts, all feedback data will be cleared.

---

## Project Requirements

- Use Spring Initializr
- Maven project only
- Use in-memory storage only
- Request and response body must use JSON
- Content-Type must be `application/json`
- Deploy and test the API on Oracle Linux
- Keep the API running after terminal closure
- Team members should collaborate using GitHub branches

---

## Technologies Used

- Java 17
- Spring Boot
- Spring Web
- Maven
- Oracle Linux
- PuTTY
- PSCP
- Postman / curl

---

## Project Structure

```txt
AvengersFC/
├── pom.xml
├── README.md
├── mvnw
├── mvnw.cmd
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── avengersfc/
    │   │           ├── AvengersFcApplication.java
    │   │           ├── controller/
    │   │           │   └── FeedbackController.java
    │   │           ├── model/
    │   │           │   └── Feedback.java
    │   │           └── service/
    │   │               └── FeedbackService.java
    │   └── resources/
    │       └── application.properties
    └── test/
```

---

## Feedback Model

```json
{
  "id": "generated-uuid",
  "content": "The espresso tastes too bitter."
}
```

The `id` is generated automatically by the API using UUID.

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/feedback` | Create new feedback |
| GET | `/feedback` | Get all feedback entries |
| GET | `/feedback/{id}` | Get one feedback by ID |
| PUT | `/feedback/{id}` | Update feedback by ID |
| DELETE | `/feedback/{id}` | Delete feedback by ID |

---

# Local API Testing

## Run the application locally

From the project folder:

```bash
mvn spring-boot:run
```

Or build and run the JAR:

```bash
mvn clean package
java -jar target/AvengersFC-0.0.1.jar
```

The application should start on:

```txt
http://localhost:8080
```

---

# Postman Testing

## 1. Create Feedback

### Request

```http
POST http://localhost:8080/feedback
Content-Type: application/json
```

### Body

```json
{
  "content": "The espresso tastes too bitter."
}
```

### Expected Response

Status:

```txt
201 Created
```

Body:

```json
{
  "id": "generated-uuid",
  "content": "The espresso tastes too bitter."
}
```

---

## 2. Get All Feedbacks

### Request

```http
GET http://localhost:8080/feedback
```

### Expected Response

Status:

```txt
200 OK
```

Body:

```json
[
  {
    "id": "generated-uuid",
    "content": "The espresso tastes too bitter."
  }
]
```

---

## 3. Get Feedback By ID

### Request

```http
GET http://localhost:8080/feedback/{id}
```

Example:

```http
GET http://localhost:8080/feedback/9a74b812-8c65-4c3f-9c2f-2a96d9e7db42
```

### Expected Response

Status:

```txt
200 OK
```

Body:

```json
{
  "id": "9a74b812-8c65-4c3f-9c2f-2a96d9e7db42",
  "content": "The espresso tastes too bitter."
}
```

If the ID does not exist:

```txt
404 Not Found
```

---

## 4. Update Feedback

### Request

```http
PUT http://localhost:8080/feedback/{id}
Content-Type: application/json
```

### Body

```json
{
  "content": "The cappuccino tastes better now."
}
```

### Expected Response

Status:

```txt
200 OK
```

Body:

```json
{
  "id": "same-feedback-id",
  "content": "The cappuccino tastes better now."
}
```

If the ID does not exist:

```txt
404 Not Found
```

If the content is empty:

```txt
400 Bad Request
```

---

## 5. Delete Feedback

### Request

```http
DELETE http://localhost:8080/feedback/{id}
```

### Expected Response

Status:

```txt
204 No Content
```

If the ID does not exist:

```txt
404 Not Found
```

---

# curl Testing

## Get all feedbacks

```bash
curl http://localhost:8080/feedback
```

Expected response:

```json
[]
```

## Create feedback

```bash
curl -X POST http://localhost:8080/feedback \
  -H "Content-Type: application/json" \
  -d '{"content":"The espresso tastes too bitter."}'
```

## Update feedback

```bash
curl -X PUT http://localhost:8080/feedback/YOUR_ID_HERE \
  -H "Content-Type: application/json" \
  -d '{"content":"The coffee tastes better now."}'
```

## Delete feedback

```bash
curl -X DELETE http://localhost:8080/feedback/YOUR_ID_HERE
```

---

# Oracle Linux Deployment

This section explains how the Coffee-Corner Feedback API was deployed on Oracle Linux using a Spring Boot JAR file.

---

## Step 1: Build the JAR on Windows

Open CMD, PowerShell, or IntelliJ terminal inside the project folder:

```bash
mvn clean package
```

After the build finishes, the JAR file will be created inside the `target` folder.

Example path:

```txt
C:\Users\Codeline\Documents\GitHub\AvengersFC\target\AvengersFC-0.0.1.jar
```

---

## Step 2: Check Java on Oracle Linux

Connect to Oracle Linux and run:

```bash
java -version
```

If Java is not installed, install Java 17:

```bash
sudo dnf install -y java-17-openjdk
```

Check again:

```bash
java -version
```

---

## Step 3: Enable SSH on Oracle Linux

Check SSH service:

```bash
sudo systemctl status sshd
```

If SSH is not running:

```bash
sudo systemctl start sshd
sudo systemctl enable sshd
```

---

## Step 4: Configure VirtualBox Port Forwarding for SSH

Because the Oracle Linux VM uses NAT networking and the VM IP is usually like:

```txt
10.0.2.15
```

Windows cannot connect directly to that IP. So VirtualBox port forwarding is used.

Go to:

```txt
VirtualBox → Select VM → Settings → Network → Adapter 1 → Advanced → Port Forwarding
```

Add this rule:

```txt
Name: SSH
Protocol: TCP
Host IP: 127.0.0.1
Host Port: 2222
Guest IP: 10.0.2.15
Guest Port: 22
```

Then connect with PuTTY:

```txt
Host Name: 127.0.0.1
Port: 2222
Connection type: SSH
```

Login with the Oracle Linux username and password.

---

## Step 5: Transfer the JAR to Oracle Linux

Run this command from **Windows CMD**, not inside Oracle Linux:

```cmd
pscp -P 2222 "C:\Users\Codeline\Documents\GitHub\AvengersFC\target\AvengersFC-0.0.1.jar" sulaiman@127.0.0.1:/home/sulaiman/
```

After transfer, go to PuTTY and check the file:

```bash
cd /home/sulaiman
ls -la
```

You should see:

```txt
AvengersFC-0.0.1.jar
```

---

## Step 6: Run the JAR on Oracle Linux

Inside Oracle Linux:

```bash
cd /home/sulaiman
java -jar AvengersFC-0.0.1.jar
```

If successful, the terminal should show something like:

```txt
Tomcat started on port 8080
Started AvengersFcApplication
```

At this point, the API is running on Oracle Linux.

---

## Step 7: Test the API Locally on Oracle Linux

Open a second PuTTY session and run:

```bash
curl http://localhost:8080/feedback
```

Expected response:

```json
[]
```

Test POST:

```bash
curl -X POST http://localhost:8080/feedback \
  -H "Content-Type: application/json" \
  -d '{"content":"The espresso tastes too bitter."}'
```

Expected response:

```json
{
  "id": "generated-uuid",
  "content": "The espresso tastes too bitter."
}
```

This confirms that the API is accessible locally after deployment.

---

## Step 8: Keep the API Running After Terminal Closure

If the app was started using:

```bash
java -jar AvengersFC-0.0.1.jar
```

it will stop when the terminal is closed.

To keep it running in the background, stop the app first:

```txt
Ctrl + C
```

Then run:

```bash
nohup java -jar AvengersFC-0.0.1.jar > app.log 2>&1 &
```

Check that the app is running:

```bash
ps aux | grep AvengersFC
```

View logs:

```bash
tail -f app.log
```

Stop the app if needed:

```bash
pkill -f AvengersFC-0.0.1.jar
```

Now the API remains running even after closing PuTTY.

---

## Step 9: Test from Windows Postman

To test from Windows Postman, add another VirtualBox port forwarding rule.

Go to:

```txt
VirtualBox → Select VM → Settings → Network → Adapter 1 → Advanced → Port Forwarding
```

Add:

```txt
Name: SpringBoot
Protocol: TCP
Host IP: 127.0.0.1
Host Port: 8080
Guest IP: 10.0.2.15
Guest Port: 8080
```

Then use this URL in Postman:

```txt
http://127.0.0.1:8080/feedback
```

Example POST request:

```http
POST http://127.0.0.1:8080/feedback
Content-Type: application/json
```

Body:

```json
{
  "content": "The espresso tastes too bitter."
}
```

---

# Deployment Acceptance Criteria Checklist

| Requirement | Status |
|---|---|
| Java installed on Oracle Linux | Done |
| Spring Boot JAR created | Done |
| JAR transferred to Oracle Linux | Done |
| Application runs successfully on Oracle Linux | Done |
| API is accessible locally after deployment | Done |
| API tested using curl/Postman | Done |
| Application remains running after terminal closure using nohup | Done |
| Deployment steps documented | Done |
| No database used | Done |
| Content-Type uses application/json | Done |

---

# Git Branch Workflow

Each team member should work on a separate branch.

Example branches:

```bash
git checkout -b feature/create-feedback
```

```bash
git checkout -b feature/view-feedbacks
```

```bash
git checkout -b feature/edit-feedback
```

```bash
git checkout -b feature/delete-feedback
```

```bash
git checkout -b feature/oracle-linux-deployment
```

Example commits:

```bash
git add .
git commit -m "Add feedback model"
```

```bash
git add .
git commit -m "Add create feedback endpoint"
```

```bash
git add .
git commit -m "Add get all feedback endpoint"
```

```bash
git add .
git commit -m "Add update feedback endpoint"
```

```bash
git add .
git commit -m "Add delete feedback endpoint"
```

```bash
git add .
git commit -m "Document Oracle Linux deployment steps"
```

Push branch:

```bash
git push origin feature/oracle-linux-deployment
```

---

# Notes

- This API uses in-memory storage only.
- No database is required.
- Docker is not required for this task.
- If the app restarts, feedback data will be lost.
- This behavior is expected because the project requirement says to use in-memory storage only.
