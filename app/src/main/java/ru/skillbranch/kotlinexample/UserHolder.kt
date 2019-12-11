package ru.skillbranch.kotlinexample

import java.lang.IllegalArgumentException

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email:String,
        password:String
    ) : User {
        return User.makeUser(fullName, email = email, password = password)
            .also { user ->
                if (map.containsKey(user.login.trim())) {
                    throw IllegalArgumentException("A user with this phone already exissts")
                } else {
                    map[user.login.trim()] = user
                }
            }
    }

    fun registerUserByPhone(fullName: String,
                            rawPhone: String
    ): User {
        return User.makeUser(fullName, phone = rawPhone)
            .also { user ->
                val pattern = "^[+]*[0-9]{11}\$".toRegex()
                if (pattern.matches(rawPhone)) {
                    if(map.containsKey(user.login.trim())) {
                        throw IllegalArgumentException("A user with this phone already exists")
                    } else {
                        map[user.login.trim()] = user
                    }
                } else {
                    throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
                }
            }
    }

    //логичнее использовать let
    fun loginUser(login: String, password: String) : String? {
        return map[login.trim()]?.run {
            if(checkPassword(password)) this.userInfo
            else null
        }
    }

    fun requestAccessCode(login: String) : Unit {
        val user = map[login.trim()]
        user?.newAuthoriationCode()
    }

    fun importUsers(list: List<User>): List<User> {
        TODO()
    }

}
