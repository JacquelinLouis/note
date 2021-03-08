package com.jac.mynote.security

import java.security.NoSuchAlgorithmException
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Security helper to crypt a message.
 * See https://developer.android.com/guide/topics/security/cryptography for details about
 * cryptography.
 */
class Crypt {
    companion object {

        /** Key size. */
        private const val KEY_LENGTH: Int = 256
        /** Iteration count */
        private const val ITERATION_COUNT = 10000

        /**
         * Get salt for the given source.
         * @param saltSource the source to generate salt from.
         * @return salt generated from source.
         */
        private fun getSalt(saltSource: String): String {
            return saltSource.substring(saltSource.length - 4)
        }

        /**
         * Encrypt the given plain input.
         * @param plainInput the plain input to encrypt.
         * @return the encrypted key.
         */
        fun encrypt(saltSource: String, plainInput: String): String? {
            val keyFactory: SecretKeyFactory
            try
            {
                keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            } catch (e: NoSuchAlgorithmException) {
                // Do not log details about failure here: possible security issue.
                return null
            }
            val salt: String = getSalt(saltSource)
            val keySpec: KeySpec = PBEKeySpec(plainInput.toCharArray(), salt.toByteArray(),
                ITERATION_COUNT, KEY_LENGTH)
            val  encryptedInput: SecretKey = keyFactory.generateSecret(keySpec)
            return String(encryptedInput.encoded)
        }

        /**
         * Check if provided inputs match.
         * @param saltSource the salt source.
         * @param plainInput the plain input to compare.
         * @param encryptedInput the encrypted input to compare.
         * @return true if parameters match, false else.
         */
        fun match(saltSource: String, plainInput: String, encryptedInput: String): Boolean {
            val encryptedKey: String? = encrypt(saltSource, plainInput)
            return encryptedInput == encryptedKey
        }
    }
}