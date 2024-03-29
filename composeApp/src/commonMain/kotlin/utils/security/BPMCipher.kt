package utils.security

import java.security.SecureRandom
import java.security.Security
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object BPMCipher {
    /**
     * Generate a key from a password and a salt
     * @param password The password to generate the key from
     * @param salt The salt to use
     * @param keyLength The length of the key to generate (optional, default is 256)
     * @param iterationCount The number of iterations to use (optional, default is 65536)
     */
    private fun passwordToKey(password: String, salt: ByteArray, keyLength: Int = 256, iterationCount: Int = 65536): SecretKeySpec {
        val keySpec = PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength)
        val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val keyBytes = keyFactory.generateSecret(keySpec).encoded

        return SecretKeySpec(keyBytes, "AES")
    }

    /**
     * Encrypt a string
     * @param content The content to encrypt
     * @param key The key to use
     */
    fun encrypt(content: String, key: String): ByteArray {
        val secureRandom = SecureRandom()

        val salt = ByteArray(16)
        secureRandom.nextBytes(salt)

        val iv = ByteArray(16)
        secureRandom.nextBytes(iv)

        val secretKey = passwordToKey(key, salt)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        val encryptedContent = cipher.doFinal(content.toByteArray())

        return salt + iv + encryptedContent
    }

    fun encrypt(content: ByteArray, key: String): ByteArray {
        val secureRandom = SecureRandom()

        val salt = ByteArray(16)
        secureRandom.nextBytes(salt)

        val iv = ByteArray(16)
        secureRandom.nextBytes(iv)

        val secretKey = passwordToKey(key, salt)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        val encryptedContent = cipher.doFinal(content)

        return salt + iv + encryptedContent
    }

    /**
     * Decrypt a string
     * @param content The content to decrypt
     * @param password The password to use
     */
    fun decrypt(content: ByteArray, password: String): String {
        val salt = content.copyOfRange(0, 16)
        val iv = content.copyOfRange(16, 32)
        val encryptedContent = content.copyOfRange(32, content.size)

        val secretKey = passwordToKey(password, salt)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        val decryptedContent = cipher.doFinal(encryptedContent)

        return String(decryptedContent)
    }

    fun decrypt2(content: ByteArray, password: String): ByteArray {
        val salt = content.copyOfRange(0, 16)
        val iv = content.copyOfRange(16, 32)
        val encryptedContent = content.copyOfRange(32, content.size)

        val secretKey = passwordToKey(password, salt)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

        return cipher.doFinal(encryptedContent)
    }
}