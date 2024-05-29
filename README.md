# BM Currency Converter App

## Overview

The Currency Converter App is a robust and user-friendly application designed to provide accurate currency conversion and historical data. Built using modern Android development tools and best practices, this app offers a seamless experience for users to convert currencies on-the-go and view historical exchange rate trends.

## Screenshots

<div style="display: flex; justify-content: space-around;">
    <div>
        <h4>Currency Conversion</h4>
        <img src="https://github.com/Billoxinogen18/bm_currency_converter/blob/main/images/Screenshot%202024-05-29%20at%2017.17.52.png" alt="Currency Conversion" width="400">
    </div>
    <div>
        <h4>Historical Data</h4>
        <img src="https://github.com/Billoxinogen18/bm_currency_converter/blob/main/images/Screenshot%202024-05-29%20at%2017.17.42.png" alt="Historical Data" width="400">
    </div>
</div>



## Features

- **Real-time Currency Conversion**: Convert between multiple currencies with up-to-date exchange rates.
- **Historical Data Visualization**: View historical exchange rate data for selected currencies over different time periods.
- **Adaptive UI**: Optimized for various screen sizes, ensuring a consistent user experience across all devices.
- **Error Handling**: Comprehensive error handling mechanisms to ensure smooth operation even under poor network conditions.
- **Popular Rates Storage**: Uses Shared Preferences to store and quickly access popular currency rates.
- **Search Functionality**: Easily search for and select currencies for conversion.
- **Unit Tests**: Thoroughly tested components to ensure reliability and correctness.

## Technology Stack

### Architecture
- **MVVM (Model-View-ViewModel)**: Ensures a clear separation of concerns and promotes a clean, testable codebase.

### Libraries and Tools
- **Jetpack Components**:
  - **Navigation Component**: Simplifies navigation and supports consistent and predictable user interactions.
  - **Data Binding**: Binds UI components in layouts to data sources in your app using a declarative format.
  - **LiveData**: Data objects that notify views when the underlying database changes.
- **KTX**: Kotlin extensions that enhance productivity and readability.
- **Hilt**: Dependency injection framework for managing the lifecycle and dependencies of various components.
- **Coroutines**: Simplifies asynchronous programming and ensures smooth UI performance.
- **Retrofit**: Type-safe HTTP client for making API calls and handling network requests.

### Additional Tools
- **Gradle**: Build automation tool for managing dependencies and building the project.
- **Lint**: Static code analysis tool to identify and correct potential issues.
- **ktlint**: Kotlin linter and formatter to ensure code quality and consistency.

## Installation

. **Clone the repository**:
   ```sh
   git clone https://github.com/yourusername/currency-converter-app.git
   cd currency-converter-app
