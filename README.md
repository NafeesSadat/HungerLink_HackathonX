---

# HungerLink App

HungerLink is a mobile application designed to connect food donors with food banks, community kitchens, and NGOs. It aims to reduce food waste and improve food security by facilitating donations and providing a platform for users to find available food resources nearby.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Requirements](#requirements)
- [Setup Instructions](#setup-instructions)
- [Dependencies](#dependencies)
- [Usage](#usage)
- [Download](#download)
- [License](#license)

## Features

- **Centralized Dashboard**: A unified interface for food banks and NGOs to track and accept donations.
- **Location-Based Services**: Real-time tracking of donor locations for optimized pick-up and resource allocation.
- **Effortless Donation Process**: Simple interface for scheduling donations, ensuring trust through verified NGO partnerships.
- **Impact Visibility**: Donors can view the status of their donations and locations served.
- **Data Insights**: Government and NGOs can access anonymized reports on food donation metrics.

## Technologies Used

- Android SDK
- Firebase (Authentication, Realtime Database, Storage)
- Google Maps API
- Glide for image loading
- Java for app development
- Gradle for build management

## Requirements

- Android Studio (latest version)
- Android SDK 34
- Minimum SDK Version: 29
- Java Development Kit (JDK) 11

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/hungerlink.git
   cd hungerlink
   ```

2. **Open in Android Studio**
   - Launch Android Studio and open the cloned project.

3. **Sync Gradle**
   - Allow Android Studio to sync the Gradle files to download necessary dependencies.

4. **Configure API Keys**
   - Obtain your Google Maps API key and replace it in `AndroidManifest.xml`:
     ```xml
     <meta-data
         android:name="com.google.android.geo.API_KEY"
         android:value="YOUR_API_KEY_HERE"/>
     ```
    - Remember to enable Places, Navigation and other relevant API's on Google Cloud Console.

5. **Run the App**
   - Connect your Android device or start an emulator.
   - Click the **Run** button in Android Studio.

## Dependencies

This app uses several libraries and dependencies, which are defined in the `build.gradle.kts` file:

```
dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.activity)
    implementation(libs.firebase.storage)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    implementation(libs.places)
    implementation(libs.android.maps.utils)
    implementation(libs.play.services.location)
    implementation(libs.play.services.places)
    implementation(libs.poi)
    implementation(libs.poi.ooxml)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
```

## Usage

1. **User Registration/Login**
      - **User Login**
         - Users can register or log in to the app using Firebase Authentication.
            <img src="https://github.com/user-attachments/assets/cd13b45d-e401-4bac-a237-e94d0e1fe7d7" alt="User Login" width="200"/>

        
      - **User Registration**
           <img src="https://github.com/user-attachments/assets/24a65b72-8155-4bfd-84f6-a38ce8bd2aed" alt="User Registration" width="200"/>


3. **Donation Management**
   - Donors can easily schedule a donation through the app.
     <img src="https://github.com/user-attachments/assets/a85857ef-7e14-4bb6-bccf-8ddd4dedbff5" alt="Donation Management" width="200"/>
     <img src="https://github.com/user-attachments/assets/b8968bce-c6dc-43b0-82f3-4ddd47892016" alt="Donation Management" width="200"/>

   - NGOs, Food Banks or individuals in need can view, accept and manage incoming donations via the dashboard.
     <img src="https://github.com/user-attachments/assets/ccd8dfcb-ebb8-4e55-87da-72cc8b666403" alt="Donation Management" width="200"/>
     <img src="https://github.com/user-attachments/assets/868e7925-9fa4-4d84-862c-462b966d9847" alt="Donation Management" width="200"/>


4. **Finding Resources**
   - Users can search for nearby food resources based on their location.
     <img src="https://github.com/user-attachments/assets/4e9e39b4-76ef-4b37-8aec-7ee5d7a75619" alt="Donation Management" width="200"/>


5. **View Donation Status**
   - Donors can track the status of their donations and see where their contributions are making an impact.
     <img src="https://github.com/user-attachments/assets/56f1194d-0c41-4974-9566-0ecf163af1b9" alt="Donation Management" width="200"/>
     <img src="https://github.com/user-attachments/assets/1b1d249e-eade-491c-bf51-a2057b9d277c" alt="Donation Management" width="200"/>


6. **View Recieve History with Advanced Analytics**
   - Reciever can check their donation status, keep track of it with an option to export their recieve history data to do advanced data analytics.
     <img src="https://github.com/user-attachments/assets/71440cc8-a3e1-4f94-a32b-d8b99ddc3f60" alt="Donation Management" width="200"/>
     <img src="https://github.com/user-attachments/assets/ab65912b-c35c-49dc-ba83-8b203431cf1c" alt="Donation Management" width="200"/>


## Download

You can download the APK for HungerLink from the latest release here: [HungerLink Version 1.0.0](https://github.com/NafeesSadat/HungerLink_HackathonX/releases/tag/HungerLinkv1.0.0).
     

## License

This project is licensed under the Apache-2.0 License - see the [LICENSE](LICENSE) file for details.

---
