# Notification Group

Notification Grouping and badge in Home scene by FCM

## Test FCM Notification with POSTMAN

```json
URL
- https://fcm.googleapis.com/fcm/send
HTTP HEADER
- Content-Type:application/json
- Authorization:key=SERVER_KEY
Body Messages (JSON)
 "data": {
     "body": "notification body",
     "title": "notification title",
     "key_1": "Value for key_1",
     "key_2": "Value for key_2",
     "group_key": "(group-key)",
     "badge": 3
 }
}
