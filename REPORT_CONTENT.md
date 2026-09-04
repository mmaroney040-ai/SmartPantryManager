# Smart Pantry Manager – Mobile App Development 700

**Student:** [Name and Surname]  
**Student ITS No:** [Student Number]  
**Module:** Mobile App Development 700  
**Date:** [Date]

## Table of Contents
1. Introduction
2. System Design
3. Screens and Core Functions
4. Key Code Snippets
5. Challenges and Solutions
6. Conclusion and Reflection
7. References

## 1. Introduction
Smart Pantry Manager is a Java Android application designed to help users reduce household food waste. The application allows users to record the ingredients they already have in their pantry and then suggests recipes that can be prepared without requiring a shopping trip. The target users are households and individuals who want to use leftover ingredients more effectively.

The main business rule is strict recipe matching. A recipe is displayed as a suggestion only when every ingredient required by the recipe is available in the user's pantry in at least the required quantity. This prevents the application from suggesting meals that cannot actually be prepared with the available ingredients.

## 2. System Design

### 2.1 Screen Flow
Pantry List → Add/Edit Ingredient → Pantry List  
Pantry List → Suggested Recipes → Recipe Detail  
Pantry List → Settings  
Suggested Recipes → Settings  
Settings → Pantry List / Suggested Recipes

### 2.2 Data Model
**pantry**
- id (Primary Key)
- name
- quantity
- unit
- expiry_date

**recipes**
- id (Primary Key)
- name
- method

**recipe_ingredients**
- id (Primary Key)
- recipe_id (Foreign Key)
- ingredient_name
- quantity
- unit

The `recipes` table has a one-to-many relationship with `recipe_ingredients`. Pantry records are stored separately and are compared with the recipe requirements.

## 3. Screens and Core Functions

### 3.1 Pantry List
[INSERT REAL RUNNING-APP SCREENSHOT HERE]
**Figure 1:** Pantry list displaying ingredients loaded from SQLite.

### 3.2 Add Ingredient
[INSERT REAL RUNNING-APP SCREENSHOT HERE]
**Figure 2:** Add Ingredient form.

### 3.3 Validation
[INSERT REAL RUNNING-APP SCREENSHOT HERE]
**Figure 3:** Validation message when required data is missing or invalid.

### 3.4 Edit Ingredient
[INSERT REAL RUNNING-APP SCREENSHOT HERE]
**Figure 4:** Existing pantry record being edited.

### 3.5 Delete Ingredient
[INSERT REAL RUNNING-APP SCREENSHOT HERE]
**Figure 5:** Delete confirmation and updated pantry list.

### 3.6 Suggested Recipes
[INSERT REAL RUNNING-APP SCREENSHOT HERE]
**Figure 6:** Strictly matched recipes.

### 3.7 Strict-Matching Test
[INSERT REAL RUNNING-APP SCREENSHOT HERE]
**Figure 7:** Recipe disappears when one required ingredient is removed and reappears when the ingredient is restored.

### 3.8 Recipe Detail
[INSERT REAL RUNNING-APP SCREENSHOT HERE]
**Figure 8:** Recipe ingredients and preparation method.

### 3.9 Settings
[INSERT REAL RUNNING-APP SCREENSHOT HERE]
**Figure 9:** Settings screen and expiry-alert preference.

## 4. Key Code Snippets

### 4.1 SQLite CRUD
Insert, update and delete methods in `DatabaseHelper` provide persistent pantry CRUD operations.

### 4.2 RecyclerView Adapter
`PantryAdapter` binds database records to the RecyclerView and provides edit and delete actions.

### 4.3 Strict Matching Algorithm
`getStrictSuggestions()` checks each recipe using `canMake()`. Every required ingredient is compared against pantry data. A recipe is included only when all required ingredients are present and the available quantity is sufficient.

### 4.4 Ingredient Normalisation
`normalizeName()` handles simple singular/plural differences such as `tomato` and `tomatoes`. `convert()` handles compatible units such as grams/kilograms and millilitres/litres.

## 5. Challenges and Solutions

One challenge was ensuring that recipes were not suggested when even one ingredient was missing. This was solved by checking every recipe ingredient and rejecting the recipe as soon as one requirement was not satisfied.

A second challenge was handling simple differences in ingredient names and units. The application uses basic name normalisation and compatible unit conversion so that trivial differences do not cause unnecessary mismatches.

A third challenge was maintaining data after the application closes. SQLite was used through `SQLiteOpenHelper`, allowing pantry records to remain stored locally between sessions.

## 6. Conclusion and Reflection
The project demonstrated how Java, Android Activities, Intents, RecyclerView, custom Adapters and SQLite can be combined to create a functional mobile application. The most important lesson was translating a business rule into reliable application logic.

Given more development time, the application could include richer expiry notifications, recipe search, categories, improved accessibility, better visual design and a more advanced ingredient/unit normalisation system.

## 7. References
Android Developers (n.d.) *Activities and activity lifecycle*. Available through the official Android Developers documentation.

Android Developers (n.d.) *SQLite databases*. Available through the official Android Developers documentation.

Android Developers (n.d.) *RecyclerView*. Available through the official Android Developers documentation.

**Note:** Replace this section with the exact Harvard references for any tutorials/documentation actually consulted during development.
