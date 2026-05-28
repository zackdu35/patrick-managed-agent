# Feature: Add a standard expandable Welcome Card at the top of the Home Screen using Jetpack Compose

## Context
To improve user onboarding metrics (KR 2.4 - Increase Onboarding Success by 15%), the product team has requested a new "Welcome Card" component at the very top of the main home screen. This card will serve to greet the user and prompt them to start exploring the app's features.

## Requirements
1. **Placement**: The Welcome Card must be rendered at the very top of the column in `MainScreen.kt` (above the existing Hello items).
2. **Visual Components**:
   - The card must use modern Material 3 design standards: a `Card` or `ElevatedCard` layout with rounded corners, padding (16.dp), and a slight shadow/elevation.
   - It must contain a title: **"Bienvenue sur l'application !"**
   - It must contain a descriptive subtitle: **"Explorez notre démonstration technique conçue pour mettre en valeur les meilleures pratiques Android."**
   - It must contain a clear call-to-action button: **"Explorer"**.
3. **Animations & Transitions**:
   - The card should collapse smoothly when the "Explorer" button is clicked.
   - Please utilize Jetpack Compose's modern animation features, such as `animateContentSize()` or an animated visibility wrapper to transition the height of the card to 0 when dismissed.
4. **State Management**:
   - The dismissed/expanded state of the card must be retained and survive activity configuration changes (such as device rotation). Please ensure you use `rememberSaveable` or delegate the state to the ViewModel.
5. **Quality Guidelines**:
   - The UI should feel premium, fluid, and fully leverage Compose state management.
