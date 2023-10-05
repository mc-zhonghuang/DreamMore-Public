package net.ccbluex.liquidbounce.utils.login

import com.mojang.authlib.Agent
import com.mojang.authlib.exceptions.AuthenticationException
import com.mojang.authlib.exceptions.AuthenticationUnavailableException
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.SessionEvent
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.login.UserUtils.getUUID
import java.net.Proxy

object LoginUtils : MinecraftInstance() {

    @JvmStatic
    fun login(username: String?, password: String?): LoginResult {
        val userAuthentication = YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT) as YggdrasilUserAuthentication

        userAuthentication.setUsername(username)
        userAuthentication.setPassword(password)

        return try {
            userAuthentication.logIn()
            mc.session = classProvider.createSession(userAuthentication.selectedProfile.name,
                    userAuthentication.selectedProfile.id.toString(), userAuthentication.authenticatedToken, "mojang")
            LiquidBounce.eventManager.callEvent(SessionEvent())
            LoginResult.LOGGED
        } catch (exception: AuthenticationUnavailableException) {
            LoginResult.NO_CONTACT
        } catch (exception: AuthenticationException) {
            val message = exception.message!!
            when {
                message.contains("invalid username or password.", ignoreCase = true) -> LoginResult.INVALID_ACCOUNT_DATA
                message.contains("account migrated", ignoreCase = true) -> LoginResult.MIGRATED
                else -> LoginResult.NO_CONTACT
            }
        } catch (exception: NullPointerException) {
            LoginResult.WRONG_PASSWORD
        }
    }

    @JvmStatic
    fun loginCracked(username: String?) {
        mc.session = classProvider.createSession(username!!, getUUID(username), "-", "legacy")
        LiquidBounce.eventManager.callEvent(SessionEvent())
    }

    enum class LoginResult {
        WRONG_PASSWORD, NO_CONTACT, INVALID_ACCOUNT_DATA, MIGRATED, LOGGED, FAILED_PARSE_TOKEN
    }
}