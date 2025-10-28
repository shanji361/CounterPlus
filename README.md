# Counter++

Counter++ is a reactive Android app that demonstrates unidirectional data flow and coroutine-based background operations. It allows users to increment, decrement, and reset a counter, as well as enable an automatic increment mode that runs periodically in the background.

---

## Features

- Reactive state management using **ViewModel** and **StateFlow**
- Manual controls for **+1**, **-1**, and **Reset**
- **Auto-increment mode** that increases the counter automatically every few seconds
- **Coroutine-based** background updates
- Real-time display of the current count and auto mode status ("Auto mode: ON/OFF")
- **Settings screen** to configure the auto-increment interval

---

## How to Run the App

1. Clone this repository:
   ```bash
   git clone https://github.com/shanji361/CounterPlus.git
   ```
2. Open the project in Android Studio.

3. Run the app on an emulator or a physical Android device.   
---

## Reference
- Based on **Lecture 4, Example 2** from the CS501 course materials.  
- [Kotlin Documentation on StateFlow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/)
- [Android Developers Documentation on ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
