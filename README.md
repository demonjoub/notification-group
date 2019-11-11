# Notification Group

This example demonstrate about How to group android notifications like whatsapp

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
```
[![N|Preview](https://github.com/demonjoub/notification-group/blob/master/screenshot.jpg)](https://github.com/demonjoub/notification-group)
