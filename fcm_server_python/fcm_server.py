from firebase_admin import messaging

import firebase_admin
from firebase_admin import credentials

cred = credentials.Certificate("fir-test-84e3e-firebase-adminsdk-t9us2-bbebe64ad9.json")
firebase_admin.initialize_app(cred)

# This registration token comes from the client FCM SDKs.
registration_token = 'fb1BqHc5Q5OU1PNNPIYAqk:APA91bEX9pDtmElRU7uJtFg3QLA1n4MAm1XMbD5uiMaf2GygyU_QuF3uFEs8IZ_J4kkJfl8VT_z7rMymZuwNvXDHz2EHSAaQUWuyPMGcjmZVk6rN3VbZNYQ'

# See documentation on defining a message payload.
message = messaging.Message(
    data={
        'score': '850',
        'time': '2:45',
    },
    token=registration_token,
)

# Send a message to the device corresponding to the provided
# registration token.
response = messaging.send(message)
# Response is a message ID string.
print('Successfully sent message:', response)
