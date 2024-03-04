package utils.security

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object BPMCipher {
    /**
     * Generate a random salt
     * @param size The size of the salt to generate (optional, default is 16)
     */
    fun generateSalt(size: Int = 16): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(size)
        random.nextBytes(salt)

        return salt
    }

    /**
     * Generate a key from a password and a salt
     * @param password The password to generate the key from
     * @param salt The salt to use
     * @param keyLength The length of the key to generate (optional, default is 256)
     * @param iterationCount The number of iterations to use (optional, default is 65536)
     */
    fun passwordToKey(password: String, salt: ByteArray, keyLength: Int = 256, iterationCount: Int = 65536): SecretKeySpec {
        val keySpec = PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength)
        val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val keyBytes = keyFactory.generateSecret(keySpec).encoded

        return SecretKeySpec(keyBytes, "AES")
    }

    fun encrypt(content: String, key: ByteArray): String {
        val secretKey = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")

        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val cipherText: ByteArray = cipher.doFinal(content.toByteArray())
        return Base64.getEncoder().encodeToString(cipherText)
    }

    fun decrypt(content: ByteArray, password: String) {

    }
}