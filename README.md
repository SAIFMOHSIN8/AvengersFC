# Coffee Feedback API Tests

Base URL:

```text
http://localhost:8080
```

Use Postman and set this header for POST and PUT requests:

```text
Content-Type: application/json
```

## 1. Create Feedback

Method:

```text
POST
```

URL:

```text
http://localhost:8080/feedback
```

Body:

```json
{
  "content": "The espresso tastes too bitter."
}
```

Expected result:

```text
201 Created
```

Example response:

```json
{
  "id": "c63c0038-2ce1-43c7-8ff5-6a1d4ed6bd88",
  "content": "The espresso tastes too bitter."
}
```

Copy the returned `id` for the next tests.

## 2. View All Feedback

Method:

```text
GET
```

URL:

```text
http://localhost:8080/feedback
```

Expected result:

```text
200 OK
```

Example response:

```json
[
  {
    "id": "c63c0038-2ce1-43c7-8ff5-6a1d4ed6bd88",
    "content": "The espresso tastes too bitter."
  }
]
```

## 3. View Feedback By ID

Method:

```text
GET
```

URL:

```text
http://localhost:8080/feedback/c63c0038-2ce1-43c7-8ff5-6a1d4ed6bd88
```

Expected result:

```text
200 OK
```

Invalid ID expected result:

```text
404 Not Found
```

## 4. Update Feedback

Method:

```text
PUT
```

URL:

```text
http://localhost:8080/feedback/c63c0038-2ce1-43c7-8ff5-6a1d4ed6bd88
```

Body:

```json
{
  "content": "The espresso tastes balanced now."
}
```

Expected result:

```text
200 OK
```

Example response:

```json
{
  "id": "c63c0038-2ce1-43c7-8ff5-6a1d4ed6bd88",
  "content": "The espresso tastes balanced now."
}
```

Invalid ID expected result:

```text
404 Not Found
```

Blank content expected result:

```text
400 Bad Request
```

## 5. Delete Feedback

Method:

```text
DELETE
```

URL:

```text
http://localhost:8080/feedback/c63c0038-2ce1-43c7-8ff5-6a1d4ed6bd88
```

Expected result:

```text
204 No Content
```

After deleting, run:

```text
GET http://localhost:8080/feedback
```

The deleted feedback should no longer appear.

Invalid ID expected result:

```text
404 Not Found
```
