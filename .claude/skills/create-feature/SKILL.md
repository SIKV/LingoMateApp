---
name: create-feature
description: This skill should be used when the user asks to create a new feature.
---

# Create Feature

Create a new shared feature.

## Requirements

1. Create a new KMP module under the `/feature` directory.
2. Name the module using **camelCase**.
3. Every feature must include a state class based on the following template:

```kotlin
import kotlin.native.ObjCName

@ObjCName("ChatState", exact = true)
data class ExampleState(
    val field1: List<Int> = emptyList(),
    val field2: String? = null,
) {
    companion object {
        fun empty() = ExampleState()
    }
}
```

4. Every feature must include a ViewModel based on the following template:

```kotlin
import androidx.lifecycle.ViewModel
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.native.ObjCName

@ObjCName("ExampleViewModel", exact = true)
class ExampleViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExampleState.empty())

    @NativeCoroutinesState
    val uiState: StateFlow<ExampleState> = _uiState.asStateFlow()
}
```
