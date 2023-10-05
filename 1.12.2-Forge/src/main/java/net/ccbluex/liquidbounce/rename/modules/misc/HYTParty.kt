package net.ccbluex.liquidbounce.rename.modules.misc


import com.allatori.annotations.ControlFlowObfuscation
import com.allatori.annotations.Rename
import com.allatori.annotations.StringEncryption
import com.allatori.annotations.StringEncryptionType
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.netty.buffer.Unpooled
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.ui.client.ListSwitcher
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.client.CPacketCustomPayload
import net.minecraft.network.play.server.SPacketCustomPayload
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream


@Rename
@ControlFlowObfuscation("enable")
@StringEncryption("enable")
@StringEncryptionType("fast")
@ModuleInfo(name = "HYTParty", description = "Party.", category = ModuleCategory.MISC, array = false)
object HYTParty : Module() {
    private val debugValue = BoolValue("Debug", true)

    private val jsonParser = JsonParser()
    const val jsonCloseGUI =
        "{\"packet_sub_type\":\"null\",\"packet_data\":\"null\",\"packet_type\":\"gui_close\"}"

    private const val JsonOpenGUI =
        "{\"packet_sub_type\":\"null\",\"packet_data\":\"null\",\"packet_type\":\"opengui\"}"

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val p = event.packet.unwrap()
        if (p is SPacketCustomPayload) {
            val bytes = ByteArray(p.bufferData.readableBytes())
            p.bufferData.readBytes(bytes)
            /**
             * 获取Payload传输的字符串
             */
            val packetString: String = try {
                decode(bytes)
            } catch (e: Exception) {
                val stream = ByteArrayOutputStream()
                stream.write(bytes)
                stream.toString("UTF-8")
            }
            if (p.channelName == "VexView") {
                ClientUtils.getLogger().info("Received:${packetString}")
                val packetReader = VexViewPacketReader(packetString)
                if (packetReader.packetType == "hud") {
                    //ClientUtils.logger.info(packetString)
                    event.cancelEvent()
                    return
                }
                if (debugValue.get()) {
                    ClientUtils.info("PacketSubType:${packetReader.packetSubType} PacketData:${packetReader.packetData} PacketType:${packetReader.packetType}")
                }
                if (packetReader.packetType == "ver") {
                    if (packetReader.packetSubType == "get") {
                        ClientUtils.info("发送VexView版本数据包.")
                        sendDebugPacket(arrayListOf("post", "2.6.10", "ver"))
                    } else if (packetReader.packetSubType == "ok") {
                        ClientUtils.success("VexView版本效验成功!")
                    }
                    return
                }
                if (packetReader.packetType == "gui") {
                    ClientUtils.info("======VexView尝试打开一个GUI======")
                    val data = Unpooled.wrappedBuffer(encode(JsonOpenGUI))
                    mc2.connection!!.sendPacket(CPacketCustomPayload("VexView", PacketBuffer(data)))
                    for (i in packetReader.buttonList)
                        ClientUtils.info("${i.key}: ${i.value}")
                    mc2.displayGuiScreen(ListSwitcher(packetReader.buttonList))
                }
                if (packetReader.packetType == "flowview") {
                    ClientUtils.displayChatMessage(packetReader.packetData.asString)
                }
            } else if (p.channelName.contains("germ")) {
                ClientUtils.getLogger().info(p.channelName + " Received:${packetString}")
            } else {
                ClientUtils.info(p.channelName + " $packetString")
            }
        }
    }

    /**
     * 按下按钮：id / null / button
     */
    fun sendDebugPacket(params: List<String>) {
        if (params.size < 3) {
            ClientUtils.error("参数长度错误!")
            return
        }
        val data =
            Unpooled.wrappedBuffer(encode("{\"packet_sub_type\":\"${params[0]}\",\"packet_data\":\"${params[1]}\",\"packet_type\":\"${params[2]}\"}"))
        //data = Unpooled.wrappedBuffer(encode(JsonCloseGUI))
        mc2.connection!!.sendPacket(CPacketCustomPayload("VexView", PacketBuffer(data)))
    }

    @Throws(IOException::class)
    private fun decode(byte: ByteArray): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val gzipInputStream = GZIPInputStream(ByteArrayInputStream(byte))
        val array = ByteArray(256)
        var read: Int
        while (gzipInputStream.read(array).also { read = it } >= 0) {
            byteArrayOutputStream.write(array, 0, read)
        }
        return byteArrayOutputStream.toString("UTF-8")
    }

    @Throws(IOException::class)
    fun encode(json: String): ByteArray {
        val arrayInputStream = ByteArrayInputStream(json.toByteArray(charset("UTF-8")))
        val bout = ByteArrayOutputStream()
        val out = GZIPOutputStream(bout)
        val array = ByteArray(256)
        var read: Int
        while (arrayInputStream.read(array).also { read = it } >= 0) out.write(array, 0, read)
        out.close()
        out.finish()
        return bout.toByteArray()
    }

    class VexViewPacketReader(val json: String) {
        val packetSubType: String
        val packetType: String
        val packetData: JsonElement
        val buttonList: HashMap<String, String> = hashMapOf()
        var buttonMode: String = "normal"

        init {
            val jsonObject = jsonParser.parse(json).asJsonObject
            packetSubType = jsonObject.get("packet_sub_type").asString
            packetType = jsonObject.get("packet_type").asString
            packetData = run {
                val data = jsonObject.get("packet_data")
                try {
                    return@run jsonParser.parse(data.asString).asJsonObject
                } catch (e: Exception) {
                    return@run data
                }
            }
            if (packetData is JsonObject) {
                ClientUtils.getLogger().info("Processing packet data...")
                val data = packetData.entrySet()
                for (it in data) {
                    when (it.key) {
                        "base" -> {
                            buttonMode = "normal"
                            val elements = it.value.asString.split("<#>")
                            var index = 0
                            while (index < elements.size) {
                                if (elements[index].contains("[but]")) {
                                    val sub = elements[index].split("<&>")
                                    buttonList[sub[0].drop(5)] = sub[6]
                                }
                                index++
                            }
                        }
                        "scrollinglist" -> {
                            buttonMode = "switcher"
                            val elements = it.value.asString.split("<#>")
                            var index = 0
                            while (index < elements.size) {
                                if (elements[index].contains("[but]")) {
                                    val sub = elements[index].split("<&>")
                                    buttonList[sub[0].drop(5)] = sub[6]
                                }
                                index++
                            }
                        }
                    }
                }
            }
        }
    }
}