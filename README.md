Currency Converter App
Currency Converter is an Android application that allows users to convert currencies from different bases, view historical exchange rates, and see conversions to popular currencies. The app is designed with a focus on clean architecture, responsive layouts, and robust error handling.

Features
Currency Conversion
Convert currencies from EUR to various other currencies.
Select from a dropdown list of available currencies.
Automatically update the converted value when the amount or currency selection changes.
Swap the base and target currencies with a single click.
Historical Data
View historical exchange rates for the past three days.
Display historical data in both list and chart formats.
Access historical data from the details screen.
Popular Currencies
View conversions from EUR to popular currencies (USD, GBP, INR, and more).
Automatically fetch and display the latest rates for these popular currencies.
Technical Stack
Architecture
Jetpack MVVM: Implements the Model-View-ViewModel pattern using Android Jetpack libraries.
Clean Architecture: Ensures a clear separation of concerns and modularity.
Libraries and Tools
Kotlin: The primary programming language.
Coroutines: For asynchronous programming.
Retrofit: For making API calls to the Fixer.io service.
Hilt: For dependency injection.
Data Binding: For binding UI components to data sources.
Navigation Component: For handling navigation and screen transitions.
MPAndroidChart: For displaying historical data in chart format.
Error Handling
Differentiates between internet connection errors and API errors.
Displays appropriate messages to the user based on the error type.
UI Design
Responsive and adaptive layouts for different device screens (excluding tablets).
Supports both portrait and landscape modes.
Follows Material Design guidelines.
Storage
SharedPreferences: Used to persist the latest rates for popular currencies, ensuring data is available even after app restarts.
Project Structure
Modules
app: Contains the main application code.
data: Contains data models, repository interfaces, and network API definitions.
di: Contains dependency injection modules.
ui: Contains UI components and screens.
viewmodels: Contains ViewModel classes.
Key Components
CurrencyViewModel: Manages data for currency conversion and historical data.
CurrencyRepository: Handles data fetching from the Fixer.io API.
FixerApi: Defines API endpoints for currency conversion and historical data.
Setup and Installation
Clone the repository:

sh
Copy code
git clone https://github.com/your-username/currency-converter.git
cd currency-converter
Open the project in Android Studio.

Add your Fixer.io API key to the project. You can do this by updating the ApiKey in the CurrencyViewModel class.

Build and run the project on an emulator or a physical device.

Testing
Unit tests are included for ViewModel and Repository classes.
Ensure code is formatted and free of linting issues before committing.
Contributing
Fork the repository.
Create a new branch (git checkout -b feature-branch).
Commit your changes (git commit -am 'Add new feature').
Push to the branch (git push origin feature-branch).
Create a new Pull Request.
License
This project is licensed under the MIT License - see the LICENSE file for details.
