package com.edaakyil.app.android.basicviews.data.service

import android.content.Context
import com.edaakyil.app.android.basicviews.constant.USERS_FILE_PATH
import com.edaakyil.app.android.basicviews.constant.USERS_FORMAT
import com.edaakyil.app.android.basicviews.model.RegisterInfoModel
import com.edaakyil.data.exception.DataServiceException
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.EOFException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

private const val DELIMITER = ":"

class UserService(context: Context) {
    private val mContext = context

    private fun userFilterCallback(fis: FileInputStream, predicate: (RegisterInfoModel) -> Boolean): Boolean {
        var result = false

        try {
            while (true) {
                val ois = ObjectInputStream(fis)
                val registerInfo = ois.readObject() as RegisterInfoModel
                if (predicate(registerInfo)) {
                    result = true
                    break
                }
            }
        } catch (_: EOFException) {

        }

        return result
    }

    private fun loadRegisterInfo(br: BufferedReader, username: String): RegisterInfoModel? {
        val str = br.readLine() ?: throw IOException()
        val info = str.split(DELIMITER)

        return RegisterInfoModel(username, info[1], info[2],info[3][0], info[4].toInt())
    }

    private fun writeRegisterInfo(bw: BufferedWriter, registerInfoModel: RegisterInfoModel) {
        bw.write("${registerInfoModel.username}$DELIMITER")
        bw.write("${registerInfoModel.name}$DELIMITER")
        bw.write("${registerInfoModel.email}$DELIMITER")
        bw.write("${registerInfoModel.maritalStatus}$DELIMITER")
        bw.write("${registerInfoModel.lastEducationDegree}")
    }

    fun findByUsername(username: String): RegisterInfoModel? {
        try {
            val file = File(mContext.filesDir, USERS_FORMAT.format("$username.txt"))

            return if (file.exists()) BufferedReader(InputStreamReader(FileInputStream(File(mContext.filesDir, USERS_FORMAT.format("$username.txt"))), StandardCharsets.UTF_8)).use { loadRegisterInfo(it, username) }
                else null
        } catch (ex: IOException) {
            throw DataServiceException("UserService.findByUsername", ex)
        }
    }

    fun saveUserData(registerInfoModel: RegisterInfoModel) {
        try {
            val username = registerInfoModel.username
            BufferedWriter(OutputStreamWriter(FileOutputStream(File(mContext.filesDir, USERS_FORMAT.format("$username.txt"))), StandardCharsets.UTF_8)).use { writeRegisterInfo(it, registerInfoModel) }
        } catch (ex: IOException) {
            throw DataServiceException("UserService.saveUserData", ex)
        }
    }

    fun registerUser(registerInfoModel: RegisterInfoModel) {
        try {
            ObjectOutputStream(FileOutputStream(File(mContext.filesDir, USERS_FILE_PATH), true)).use { it.writeObject(registerInfoModel) }
            File(mContext.filesDir, USERS_FORMAT.format("${registerInfoModel.username}.txt")).delete()
        } catch (ex: IOException) {
            throw DataServiceException("UserService.registerUser", ex)
        }
    }

    fun isUserSaved(username: String): Boolean {
        try {
            return File(mContext.filesDir, USERS_FORMAT.format("$username.txt")).exists()
        } catch (ex: IOException) {
            throw DataServiceException("UserService.existsByUsername", ex)
        }
    }
    fun existByUsername(username: String): Boolean {
        try {
            return FileInputStream(File(mContext.filesDir, USERS_FILE_PATH)).use { userFilterCallback(it) { it.username == username } }
        } catch (ex: IOException) {
            throw DataServiceException("UserService.existsByUsername", ex)
        }
    }

    fun existsByUsernameAndPassword(username: String, password: String): Boolean {
        try {
            return FileInputStream(File(mContext.filesDir, USERS_FILE_PATH)).use { userFilterCallback(it) { it.username == username && it.password == password } }
        } catch (ex: IOException) {
            throw DataServiceException("UserService.existsByUsernameAndPassword", ex)
        }
    }
}