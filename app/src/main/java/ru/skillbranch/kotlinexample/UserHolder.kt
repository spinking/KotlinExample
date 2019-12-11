package ru.skillbranch.kotlinexample

import java.lang.IllegalArgumentException

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun resetMap() {
        map.clear()
    }

    fun registerUser(
        fullName: String,
        email:String,
        password:String
    ) : User {
        return User.makeUser(fullName, email = email, password = password)
            .also { user ->
                if (map.containsKey(user.login.trim()))
                    throw IllegalArgumentException("A user with this phone already exists")

                map[user.login.trim()] = user
            }
    }

    fun registerUserByPhone(fullName: String,
                            rawPhone: String
    ): User {
        return User.makeUser(fullName, phone = rawPhone)
            .also { user ->
                val pattern = "^[+]*[0-9]?[ ]?[(]?[0-9]{1,4}[)]?[ ]?[0-9]{3}[- ]?[0-9]{2}[- ]?[0-9]{2}\$".toRegex()
                 if (pattern.matches(rawPhone)) {
                    if (map.containsKey(user.login.trim())) {
                        throw IllegalArgumentException("A user with this phone already exists")
                    } else {
                        map[user.login.trim()] = user
                         }
                } else {
                    throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
                }
            }
    }

    fun loginUser(login: String, password: String) : String? {
        val user = map[login.trim()] ?: map[login.replace("[^+\\d]".toRegex(), "")]
        return user?.run {
            if(checkPassword(password)) this.userInfo
            else null
        }
    }

    fun requestAccessCode(login: String) {
        val user = map[login.trim()] ?: map[login.replace("[^+\\d]".toRegex(), "")]
        user?.newAuthoriationCode()
    }

    fun importUsers(list: List<String>): List<User> {
        return list.map {
            User.parseCSV(it).also {user ->
                map[user.login] = user
            }
        }
    }

}
