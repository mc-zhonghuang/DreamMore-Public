/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package net.ccbluex.liquidbounce.utils.misc

import com.allatori.annotations.ControlFlowObfuscation
import com.google.common.io.ByteStreams
import org.apache.http.message.BasicNameValuePair
import top.fl0wowp4rty.Heavy
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


/**
 * LiquidBounce Hacked Client
 * A minecraft forge injection client using Mixin
 *
 * @game Minecraft
 * @author CCBlueX
 */
@ControlFlowObfuscation("enable")
@Heavy
object HttpUtils {

    private const val DEFAULT_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0"

    init {
        HttpURLConnection.setFollowRedirects(true)
    }

    private fun make(url: String, method: String,
                     agent: String = DEFAULT_AGENT): HttpURLConnection {
        val httpConnection = URL(url).openConnection() as HttpURLConnection

        httpConnection.requestMethod = method
        httpConnection.connectTimeout = 2000
        httpConnection.readTimeout = 10000

        httpConnection.setRequestProperty("User-Agent", agent)

        httpConnection.instanceFollowRedirects = true
        httpConnection.doOutput = true

        return httpConnection
    }

    @JvmStatic
    fun bend(name: String?, value: String?): BasicNameValuePair {
        return BasicNameValuePair(name, value)
    }

    @Throws(IOException::class)
    fun request(url: String, method: String,
                agent: String = DEFAULT_AGENT): String {
        val connection = make(url, method, agent)

        return connection.inputStream.reader().readText()
    }

    @Throws(IOException::class)
    fun requestStream(url: String, method: String,
                      agent: String = DEFAULT_AGENT): InputStream? {
        val connection = make(url, method, agent)

        return connection.inputStream
    }

    @Throws(IOException::class)
    @JvmStatic
    fun get(url: String) = request(url, "GET")

    @Throws(IOException::class)
    @JvmStatic
    fun download(url: String, file: File) = FileOutputStream(file).use { ByteStreams.copy(make(url, "GET").inputStream, it) }

    @JvmStatic
    fun download2(sUrl: String, output: FileOutputStream) {
        try {
            val buf = ByteArray(1024)
            var size: Int
            val url = URL(sUrl)
            val httpUrl = url.openConnection() as HttpURLConnection
            httpUrl.readTimeout = 60000
            httpUrl.connectTimeout = 60000
            httpUrl.connect()
            val bis = BufferedInputStream(httpUrl.inputStream)
            while (bis.read(buf).also { size = it } != -1) {
                output.write(buf, 0, size)
            }
            output.close()
            bis.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @JvmStatic
    @Throws(IOException::class)
    fun doPost(url: String, params: Map<String, String>): String {
        val paramStr = StringBuilder()
        for ((key, value) in params) {
            paramStr.append("&")
                .append(key)
                .append("=")
                .append(value)
        }
        return get(url + paramStr.toString())
    }
}