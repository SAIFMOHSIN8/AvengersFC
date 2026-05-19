# Oracle Linux Deployment Guide

This document explains how the Coffee Feedback API was deployed and tested on Oracle Linux.

---

## Project Information

Application name:

```text
AvengersFC / Coffee Feedback API
```

JAR file name:

```text
AvengersFC-0.0.1.jar
```

Oracle Linux username:

```text
sulaiman
```

Oracle Linux VM IP after changing to Bridged Adapter:

```text
192.168.100.246
```

API base URL on Oracle Linux:

```text
http://192.168.100.246:8080
```

Feedback endpoint:

```text
http://192.168.100.246:8080/feedback
```

---

## 1. Build the Spring Boot JAR on Windows

From the Spring Boot project folder, run:

```bash
mvn clean package
```

After the build finishes, the JAR file is created inside the `target` folder.

JAR path on Windows:

```text
C:\Users\Codeline\Documents\GitHub\AvengersFC\target\AvengersFC-0.0.1.jar
```

---

## 2. Check Java on Oracle Linux

Inside Oracle Linux, check if Java is installed:

```bash
java -version
```

If Java is not installed, install Java 17:

```bash
sudo dnf install -y java-17-openjdk
```

Check Java again:

```bash
java -version
```

---

## 3. Enable SSH on Oracle Linux

Check SSH service:

```bash
sudo systemctl status sshd
```

If SSH is not running, start and enable it:

```bash
sudo systemctl start sshd
sudo systemctl enable sshd
```

---

## 4. Transfer the JAR to Oracle Linux

The JAR was transferred from Windows to Oracle Linux using PSCP.

When the VM was using NAT, SSH port forwarding was used:

```text
Host IP: 127.0.0.1
Host Port: 2222
Guest IP: 10.0.2.15
Guest Port: 22
```

PSCP command from Windows CMD:

```cmd
pscp -P 2222 "C:\Users\Codeline\Documents\GitHub\AvengersFC\target\AvengersFC-0.0.1.jar" sulaiman@127.0.0.1:/home/sulaiman/
```

Important:

```text
Run PSCP from Windows CMD, not inside Oracle Linux.
```

---

## 5. Confirm the JAR Exists on Oracle Linux

Inside Oracle Linux:

```bash
cd /home/sulaiman
ls -la
```

Expected file:

```text
AvengersFC-0.0.1.jar
```

---

## 6. Run the JAR on Oracle Linux

Run the application:

```bash
cd /home/sulaiman
java -jar AvengersFC-0.0.1.jar
```

If successful, Spring Boot should show that Tomcat started on port `8080`.

Example:

```text
Tomcat started on port 8080
Started AvengersFcApplication
```

At this point, the API is running on Oracle Linux.

---

## 7. Change VirtualBox Network to Bridged Adapter

The VirtualBox network was changed from NAT to:

```text
Bridged Adapter
```

After changing to Bridged Adapter, Oracle Linux received this IP:

```text
192.168.100.246
```

The IP was checked using:

```bash
ifconfig
```

Output included:

```text
inet 192.168.100.246
```

After using Bridged Adapter, PuTTY can connect directly using:

```text
Host Name: 192.168.100.246
Port: 22
```

---

## 8. Open Port 8080 in Oracle Linux Firewall

To allow Windows/Postman to access the API, port `8080` was opened:

```bash
sudo firewall-cmd --add-port=8080/tcp --permanent
sudo firewall-cmd --reload
```

Expected result:

```text
success
success
```

---

## 9. Test the API Inside Oracle Linux

Open a second terminal or PuTTY session and test:

```bash
curl http://localhost:8080/feedback
```

Expected response if there is no data:

```json
[]
```

Test creating feedback:

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

---

## 10. Test the API from Windows/Postman

Because the VM is using Bridged Adapter, Windows can access the API using the Oracle Linux IP.

Base URL:

```text
http://192.168.100.246:8080
```

### GET all feedback

```text
GET http://192.168.100.246:8080/feedback
```

### POST create feedback

```text
POST http://192.168.100.246:8080/feedback
```

Header:

```text
Content-Type: application/json
```

Body:

```json
{
  "content": "The espresso tastes too bitter."
}
```

Expected status:

```text
201 Created
```

---

## 11. Keep the API Running After Terminal Closure

If the app is started like this:

```bash
java -jar AvengersFC-0.0.1.jar
```

it stops when the terminal closes.

To keep it running in the background, stop the current app first:

```text
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

Check logs:

```bash
tail -f app.log
```

Stop the app if needed:

```bash
pkill -f AvengersFC-0.0.1.jar
```

---

## 12. Deployment Status

The Coffee Feedback API was deployed successfully on Oracle Linux.

Completed:

```text
Java installed on Oracle Linux
Spring Boot JAR built using Maven
JAR transferred to Oracle Linux
Application runs on Oracle Linux
Port 8080 opened in firewall
VirtualBox network changed to Bridged Adapter
Oracle Linux VM IP is 192.168.100.246
API accessible from Oracle Linux using localhost
API accessible from Windows/Postman using 192.168.100.246
API can remain running after terminal closure using nohup
```

---

## Important Note

This is a local Oracle Linux VM deployment for testing.

This URL:

```text
http://192.168.100.246:8080/feedback
```

works only from the same local network.

It is not a public internet deployment.

---

## Final Summary

The Coffee Feedback API was deployed on an Oracle Linux VM. The Spring Boot JAR file was built using Maven, transferred to Oracle Linux, and executed using Java. The VirtualBox network was changed to Bridged Adapter, allowing the API to be accessed from Windows/Postman using the Oracle Linux IP address. Port `8080` was opened in the firewall, and the API can be kept running in the background using `nohup`.
