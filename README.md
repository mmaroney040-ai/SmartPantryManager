# Smart Pantry Manager

Smart Pantry Manager is a Java Android application that helps users reduce food waste by recording ingredients already available at home and showing only recipes that can be made strictly from those ingredients.

## Database
SQLite was selected because it is local, simple to integrate with Android through `SQLiteOpenHelper`, requires no server setup, and demonstrates genuine persistent CRUD storage.

## Main features
- Add, edit and delete pantry ingredients.
- RecyclerView pantry list.
- SQLite persistence.
- 18 pre-loaded recipes.
- Strict recipe matching by ingredient name and required quantity.
- Simple normalisation for singular/plural names and compatible unit conversions.
- Recipe detail screen.
- Settings screen with an expiry-alert preference.
- Bottom navigation between Pantry, Recipes and Settings.

## Setup
1. Open the project in Android Studio.
2. Allow Gradle to sync.
3. Use an Android emulator or Android device.
4. Run the `app` configuration.
5. Add pantry ingredients and open Suggested Recipes.

## GitHub
Create a PUBLIC GitHub repository and push the project from the start. Use at least 10 genuine incremental commits as required by the assignment.
