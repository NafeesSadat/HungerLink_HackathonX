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
      **User Login**
      - Users can register or log in to the app using Firebase Authentication.
      - ![WhatsApp Image 2024-10-31 at 18 26 35_0bdd113d](https://github.com/user-attachments/assets/cd13b45d-e401-4bac-a237-e94d0e1fe7d7)


3. **Donation Management**
   - Donors can easily schedule a donation through the app.
   - NGOs can accept and manage incoming donations via the dashboard.

4. **Finding Resources**
   - Users can search for nearby food resources based on their location.

5. **View Donation Status**
   - Donors can track the status of their donations and see where their contributions are making an impact.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
