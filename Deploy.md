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



# Notes

- This API uses in-memory storage only.
- No database is required.
- Docker is not required for this task.
- If the app restarts, feedback data will be lost.
- This behavior is expected because the project requirement says to use in-memory storage only.
