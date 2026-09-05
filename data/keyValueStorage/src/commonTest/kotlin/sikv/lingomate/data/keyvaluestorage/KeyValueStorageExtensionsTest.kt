package sikv.lingomate.data.keyvaluestorage

import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

private enum class Season {
    SPRING,
    SUMMER
}

class KeyValueStorageExtensionsTest {

    private lateinit var keyValueStorage: FakeKeyValueStorage

    @BeforeTest
    fun setUp() {
        keyValueStorage = FakeKeyValueStorage()
    }

    @Test
    fun putOrRemove_storesTheValue() = runTest {
        keyValueStorage.putOrRemove("key", "value")

        assertEquals("value", keyValueStorage.get("key"))
    }

    @Test
    fun putOrRemove_dropsTheEntryWhenTheValueIsNull() = runTest {
        keyValueStorage.put("key", "value")

        keyValueStorage.putOrRemove("key", null)

        assertNull(keyValueStorage.get("key"))
        assertEquals(0, keyValueStorage.entries.size)
    }

    @Test
    fun putEnum_storesTheEntryByName() = runTest {
        keyValueStorage.putEnum("key", Season.SUMMER)

        assertEquals("SUMMER", keyValueStorage.get("key"))
    }

    @Test
    fun putEnum_dropsTheEntryWhenTheValueIsNull() = runTest {
        keyValueStorage.putEnum("key", Season.SUMMER)

        keyValueStorage.putEnum<Season>("key", null)

        assertNull(keyValueStorage.get("key"))
    }

    @Test
    fun getEnum_returnsTheStoredEntry() = runTest {
        keyValueStorage.putEnum("key", Season.SUMMER)

        assertEquals(Season.SUMMER, keyValueStorage.getEnum<Season>("key"))
    }

    @Test
    fun getEnum_returnsNullWhenNothingStored() = runTest {
        assertNull(keyValueStorage.getEnum<Season>("key"))
    }

    @Test
    fun getEnum_returnsNullWhenTheStoredNameIsNoLongerKnown() = runTest {
        keyValueStorage.put("key", "AUTUMN")

        assertNull(keyValueStorage.getEnum<Season>("key"))
    }

    @Test
    fun getEnum_readsBackWhatPutEnumStoredForEveryEntry() = runTest {
        Season.entries.forEach { season ->
            keyValueStorage.putEnum("key", season)

            assertEquals(season, keyValueStorage.getEnum("key", Season.entries))
        }
    }
}
