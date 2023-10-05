package net.ccbluex.liquidbounce.rename.modules.misc


import com.allatori.annotations.ControlFlowObfuscation
import com.allatori.annotations.Rename
import com.allatori.annotations.StringEncryption
import com.allatori.annotations.StringEncryptionType
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.germmod.packet.BasePacket
import net.ccbluex.liquidbounce.utils.germmod.packet.packets.PacketBoolean
import net.ccbluex.liquidbounce.utils.germmod.packet.packets.PacketDigging
import net.ccbluex.liquidbounce.utils.germmod.packet.packets.PacketUUID
import net.ccbluex.liquidbounce.utils.germmod.uncode.Password
import net.ccbluex.liquidbounce.utils.germmod.uncode.UnCoder
import net.minecraft.network.PacketBuffer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLEventChannel
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket
import java.net.InterfaceAddress
import java.net.NetworkInterface
import java.nio.charset.StandardCharsets
import java.util.*


@Rename
@ControlFlowObfuscation("enable")
@StringEncryption("enable")
@StringEncryptionType("fast")
@ModuleInfo(name = "HYTJoin4v4", description = "IDK.", category = ModuleCategory.MISC)
object HYTJoin4v4: Module() {
    private var channel: FMLEventChannel? = null
    val packetMap = hashMapOf<Int, BasePacket>()
    private var baseCode: String? = null

    override fun onEnable() {
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("germplugin-netease")
        init()
        channel!!.register(this)
        MinecraftForge.EVENT_BUS.register(this)
    }

    override fun onDisable() {
        channel!!.unregister(this)
        MinecraftForge.EVENT_BUS.unregister(this)
    }

    @JvmStatic
    fun init() {
        put(PacketDigging())
        put(PacketBoolean())
    }

    fun put(p: BasePacket) {
        packetMap[p.packId()] = p
    }

    private fun getCodeList(): CharArray {
        var cArray: CharArray = charArrayOf('1', '2', '3')
        try {
            cArray = UnCoder.getID(listCode().toString()).toCharArray()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        var cArray2: CharArray = charArrayOf('4', '5', '6')
        try {
            cArray2 = UnCoder.getID(Password.getCPUID()).toCharArray()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        var cArray3: CharArray = charArrayOf('7', '8', '9')
        try {
            cArray3 = UnCoder.getID(Password.getWin32()).toCharArray()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        val cArray4: CharArray = UnCoder.bondArray(cArray, cArray2, cArray3)
        for (i in cArray4.indices) {
            cArray4[i] = Character.toUpperCase(cArray4[i])
        }
        return cArray4
    }

    private fun listCode(): List<String> {
        val arrayList = ArrayList<String>()
        val stringBuilder = StringBuilder()
        try {
            val enumeration = NetworkInterface.getNetworkInterfaces()
            while (enumeration.hasMoreElements()) {
                val iterator: Iterator<InterfaceAddress> = enumeration.nextElement().interfaceAddresses.iterator()
                while (iterator.hasNext()) {
                    var byArray: ByteArray = byteArrayOf()
                    var networkInterface: NetworkInterface = enumeration.nextElement()
                    val inetAddress = iterator.next().address
                    if (inetAddress.isLinkLocalAddress || NetworkInterface.getByInetAddress(inetAddress).also {
                            networkInterface = it
                        } == null || networkInterface.hardwareAddress.also { byArray = it } == null) continue
                    stringBuilder.delete(0, stringBuilder.length)
                    for (i in byArray.indices) {
                        stringBuilder.append(String.format("%02X%s", byArray[i], if (i < byArray.size - 1) "-" else ""))
                    }
                    if (arrayList.contains(stringBuilder.toString())) continue
                    arrayList.add(stringBuilder.toString())
                }
            }
            return arrayList
        } catch (exception: java.lang.Exception) {
            exception.printStackTrace()
        }
        return arrayList
    }

    fun getBasePacket(): String? {
        if (baseCode == null) {
            baseCode = Base64.getEncoder().encodeToString(
                String(getCodeList()).replace("\r\n", "").toByteArray(StandardCharsets.UTF_8)
            )
        }
        return baseCode
    }

    @SubscribeEvent
    fun onPacket(event: FMLNetworkEvent.ClientCustomPacketEvent) {
        writePacket(event.packet.payload())
    }

    private fun writePacket(byteBuf: ByteBuf) {
        val pb = PacketBuffer(byteBuf)
        val num = pb.readInt()
        ClientUtils.info("Packet write, Id:${num}")
        val packet = packetMap[num]
        packet!!.write(pb)
        packet.encode()
    }

    fun sendPacket(basePacket: BasePacket) {
        val packetBuffer = PacketBuffer(Unpooled.buffer())
        packetBuffer.writeInt(basePacket.packId())
        basePacket.read(packetBuffer)
        ClientUtils.info("Packet send, Id:${basePacket.packId()}")
        val fMLProxyPacket = FMLProxyPacket(packetBuffer, "germmod-netease")
        channel?.sendToServer(fMLProxyPacket)
    }
}