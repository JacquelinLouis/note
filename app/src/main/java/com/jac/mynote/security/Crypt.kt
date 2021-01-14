package com.jac.mynote.security

/**
 * Security helper to crypt or read a message.
 * See https://developer.android.com/guide/topics/security/cryptography for details about
 * cryptography.
 */
class Crypt {
    companion object {

        /** AES key size. */
        private val AES_KEY_SIZE: Int = 256;

        /**
         * Encrypt the given plain input.
         * @param plainInput the plain input to encrypt.
         * @return the encrypted key.
         */
        fun encrypt(plainInput: String): String {
            val encryptedInput: String = plainInput.capitalize()
            encryptedInput
            return encryptedInput
        }
    }
}