<h1 align="center">Golden Time - Wear OS⌚</h1>
<h3 align="center">For 2023 Google Developers Solution Challenge</h3>
<p align="center">
  <img src="https://user-images.githubusercontent.com/11978494/228843932-c59e03fb-d4e7-458d-a548-58e80583a7ea.png" alt="icon" width="250" height="250">
</p>

## Overview
Wear OS for Golden Time has been made to assist an Mobile application for Golden Time. It monitors user's body condition and send alert to the application in case of an emergency. We took advantage of high quality data from **[Health Services](https://developer.android.com/training/wearables/health-services)**.

Our Wear OS continuously measures user's **heart rate** and detects **falling activity**. If the heart rate is outside of the normal range(40-10bpm) or fall activity is detected by Health Services, it finds the user is in emergent situation.

Wear device and a handheld communicate using [MessageClient](https://developer.android.com/training/wearables/data/messages). If a MessageClient listener on a mobile application recieves a message, new activity starts on the mobile device and emergency is handeled.

## Screenshots
<img src="https://velog.velcdn.com/images/bona-park/post/86a42ec2-247a-44fa-88c0-c53a315eef07/image.png" width="90%" height="60%">





## Features
The Wear OS for Golden Time lets you:
- Measure your heart rate
- Detect when you have fallen
- Send emergency message to Android application

## Technologies
- Andorid Studio
- Kotlin
- MVVM architecture
- Health Services



## Getting Started
### Prerequisites
- Min SDK version: 30
- Target SDK version: 32
### Clone
Clone this repo to your local machine using:

```
git clone https://github.com/gdsc-ys/golden-time-wearos/
```
## Permissions
On Android, Wear OS for Golden Time requires the following permissions:
- Sensors
- Physical activities

