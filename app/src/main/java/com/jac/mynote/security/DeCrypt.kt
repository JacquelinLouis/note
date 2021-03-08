package com.jac.mynote.security

import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * Security helper to crypt or decrypt a message.
 * See https://developer.android.com/guide/topics/security/cryptography for details about
 * cryptography.
 */
class DeCrypt {

    companion object {

        /** Algorithm name. */
        private const val ALGORITHM: String = "AES"

        /** Transformation to use for encryption. */
        private const val ALGORITHM_TRANSFORMATION: String = "AES/CBC/PKCS5PADDING"

        private const val ALGORITHM_IV = "IV"

        /**
         * Get (generate) a key for the [ALGORITHM] algorithm.
         * @return the key.
         * @throws NoSuchAlgorithmException if [ALGORITHM] is not supported by the device.
         */
        private fun getKey(): SecretKey {
            val keygen = KeyGenerator.getInstance(ALGORITHM)
            keygen.init(256)
            return keygen.generateKey()
        }

        /**
         * Get [ALGORITHM] algorithm in given mode.
         * @param opMode the mode to use algorithm, must be either [Cipher.ENCRYPT_MODE] or
         * [Cipher.DECRYPT_MODE].
         * @return the algorithm.
         * @throws NoSuchAlgorithmException if [ALGORITHM] is not supported by the device.
         */
        private fun getAlgorithm(opMode: Int): Cipher {
            val key: SecretKey = getKey()
            val algorithm: Cipher = Cipher.getInstance(ALGORITHM_TRANSFORMATION)
            algorithm.init(opMode, key)
            return algorithm
        }

        /**
         * Encrypt the given input string with [ALGORITHM] algorithm.
         * @param plainInput the data to encrypt.
         * @return the data encrypted.
         * @throws NoSuchAlgorithmException if [ALGORITHM] is not supported by the device.
         */
        fun encrypt(plainInput: String): String {
            val cipher: Cipher = getAlgorithm(Cipher.ENCRYPT_MODE)
            return String(cipher.doFinal(plainInput.toByteArray()))
        }

        /**
         * Decrypt the given input string with [ALGORITHM] algorithm.
         * @param encryptedInput the data to decrypt.
         * @return the data decrypted.
         * @throws NoSuchAlgorithmException if [ALGORITHM] is not supported by the device.
         */
        fun decrypt(encryptedInput: String): String {
            val cipher = getAlgorithm(Cipher.DECRYPT_MODE)
            return String(cipher.doFinal(encryptedInput.toByteArray()))
        }
    }
}