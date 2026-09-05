package sikv.lingomate.data.keyvaluestorage

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.job
import kotlinx.coroutines.test.runTest
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DataStoreKeyValueStorageTest {

    private lateinit var filePath: Path
    private lateinit var keyValueStorage: KeyValueStorage

    @BeforeTest
    fun setUp() {
        // A DataStore owns its file, so every test gets one of its own.
        filePath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
            .resolve("${Random.nextLong()}.preferences_pb")

        keyValueStorage = DataStoreKeyValueStorage(
            createKeyValueDataStore { filePath.toString() }
        )
    }

    @AfterTest
    fun tearDown() {
        FileSystem.SYSTEM.delete(filePath, mustExist = false)
    }

    @Test
    fun get_returnsNullWhenNothingStored() = runTest {
        assertNull(keyValueStorage.get("key"))
    }

    @Test
    fun get_returnsTheStoredValue() = runTest {
        keyValueStorage.put("key", "value")

        assertEquals("value", keyValueStorage.get("key"))
    }

    @Test
    fun put_overwritesTheStoredValue() = runTest {
        keyValueStorage.put("key", "old")
        keyValueStorage.put("key", "new")

        assertEquals("new", keyValueStorage.get("key"))
    }

    @Test
    fun put_keepsOtherKeys() = runTest {
        keyValueStorage.put("first", "1")
        keyValueStorage.put("second", "2")

        assertEquals("1", keyValueStorage.get("first"))
        assertEquals("2", keyValueStorage.get("second"))
    }

    @Test
    fun remove_deletesOnlyThatKey() = runTest {
        keyValueStorage.put("first", "1")
        keyValueStorage.put("second", "2")

        keyValueStorage.remove("first")

        assertNull(keyValueStorage.get("first"))
        assertEquals("2", keyValueStorage.get("second"))
    }

    @Test
    fun remove_missingKeyDoesNothing() = runTest {
        keyValueStorage.remove("key")

        assertNull(keyValueStorage.get("key"))
    }

    @Test
    fun clear_removesEverything() = runTest {
        keyValueStorage.put("first", "1")
        keyValueStorage.put("second", "2")

        keyValueStorage.clear()

        assertNull(keyValueStorage.get("first"))
        assertNull(keyValueStorage.get("second"))
    }

    @Test
    fun storedValues_areReadBackFromTheFile() = runTest {
        val path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
            .resolve("${Random.nextLong()}.preferences_pb")
        val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

        try {
            val storage = DataStoreKeyValueStorage(
                createKeyValueDataStore(scope) { path.toString() }
            )
            storage.put("key", "value")

            // Release the file the way process teardown would, then read it as a fresh launch does.
            scope.coroutineContext.job.cancelAndJoin()

            val reopened = DataStoreKeyValueStorage(
                createKeyValueDataStore { path.toString() }
            )

            assertEquals("value", reopened.get("key"))
        } finally {
            FileSystem.SYSTEM.delete(path, mustExist = false)
        }
    }
}
