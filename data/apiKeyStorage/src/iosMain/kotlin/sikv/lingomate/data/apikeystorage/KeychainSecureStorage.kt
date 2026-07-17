package sikv.lingomate.data.apikeystorage

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFMutableDictionaryRef
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFTypeRef
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFBooleanTrue
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly
import platform.Security.kSecAttrAccessible
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData

/**
 * [SecureStorage] backed by the iOS Keychain (generic password items).
 *
 * Values are only accessible after the device has been unlocked once and are
 * never synced off-device ([kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly]).
 */
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal class KeychainSecureStorage(
    private val service: String = SERVICE,
) : SecureStorage {

    override fun put(key: String, value: String) {
        // Keychain items are unique per (service, account); replace any existing one.
        remove(key)

        val valueData = (NSString.create(string = value))
            .dataUsingEncoding(NSUTF8StringEncoding) ?: return

        keychainQuery { query, retained ->
            CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
            query.addString(kSecAttrService, service, retained)
            query.addString(kSecAttrAccount, key, retained)
            query.add(kSecValueData, CFBridgingRetain(valueData), retained)
            CFDictionaryAddValue(query, kSecAttrAccessible, kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly)
            SecItemAdd(query, null)
        }
    }

    override fun get(key: String): String? {
        return keychainQuery { query, retained ->
            CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
            query.addString(kSecAttrService, service, retained)
            query.addString(kSecAttrAccount, key, retained)
            CFDictionaryAddValue(query, kSecReturnData, kCFBooleanTrue)
            CFDictionaryAddValue(query, kSecMatchLimit, kSecMatchLimitOne)

            memScoped {
                val result = alloc<CFTypeRefVar>()
                val status = SecItemCopyMatching(query, result.ptr)
                if (status != errSecSuccess) return@keychainQuery null

                val data = CFBridgingRelease(result.value) as? NSData ?: return@keychainQuery null
                NSString.create(data = data, encoding = NSUTF8StringEncoding) as String?
            }
        }
    }

    override fun remove(key: String) {
        keychainQuery { query, retained ->
            CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
            query.addString(kSecAttrService, service, retained)
            query.addString(kSecAttrAccount, key, retained)
            SecItemDelete(query)
        }
    }

    override fun clear() {
        keychainQuery { query, retained ->
            CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
            query.addString(kSecAttrService, service, retained)
            SecItemDelete(query)
        }
    }

    /**
     * Builds a mutable keychain query dictionary, runs [block], then releases the
     * dictionary and every value bridged into it via CFBridgingRetain.
     */
    private inline fun <R> keychainQuery(
        block: (query: CFMutableDictionaryRef, retained: MutableList<CFTypeRef?>) -> R,
    ): R {
        val query = CFDictionaryCreateMutable(null, 0, null, null)
            ?: error("Unable to allocate keychain query")
        val retained = mutableListOf<CFTypeRef?>()
        try {
            return block(query, retained)
        } finally {
            retained.forEach { it?.let(::CFRelease) }
            CFRelease(query)
        }
    }

    private fun CFMutableDictionaryRef.addString(
        key: CValuesRef<*>?,
        value: String,
        retained: MutableList<CFTypeRef?>,
    ) {
        val ref = CFBridgingRetain(NSString.create(string = value))
        retained += ref
        CFDictionaryAddValue(this, key, ref)
    }

    private fun CFMutableDictionaryRef.add(
        key: CValuesRef<*>?,
        value: CFTypeRef?,
        retained: MutableList<CFTypeRef?>,
    ) {
        retained += value
        CFDictionaryAddValue(this, key, value)
    }

    private companion object {
        const val SERVICE = "sikv.lingomate.data.apiKeyStorage"
    }
}
