package net.ccbluex.liquidbounce.features.module

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.Listenable
import net.ccbluex.liquidbounce.injection.backend.Backend
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.value.Value
import org.lwjgl.input.Keyboard

open class Module : MinecraftInstance(), Listenable {
    enum class AutoDisableMode(s: String) {
        WORLDCHANGE("WorldChange"),
        DEAD("Dead"),
        LAG("Lag"),
        NONE("None");
    }

    var isSupported: Boolean
    // Module information
    // TODO: Remove ModuleInfo and change to constructor (#Kotlin)
    var expanded: Boolean = false
    var keyBindY: Float = 0f
    @JvmField
    var alpha = 0f
    var name: String
    var spacedName: String
    var description: String
    var autoDisableMode: AutoDisableMode
    var category: ModuleCategory
    var keyBind = Keyboard.CHAR_NONE
        set(keyBind) {
            field = keyBind

            if (!LiquidBounce.isStarting)
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.valuesConfig)
        }
    var array = true
        set(array) {
            field = array

            if (!LiquidBounce.isStarting)
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.valuesConfig)
        }
    private val canEnable: Boolean

    var slideStep = 0F

    private fun addSpaces(str: String): String {
        val sb = StringBuilder()
        var lastCharIsUpper = false
        for (i in str.indices) {
            val c = str[i]
            lastCharIsUpper = if (c.isUpperCase()) {
                if (!lastCharIsUpper) {
                    sb.append(' ')
                }
                true
            } else {
                false
            }
            sb.append(c)
        }
        return sb.toString()
    }

    init {
        val moduleInfo = javaClass.getAnnotation(ModuleInfo::class.java)!!

        name = moduleInfo.name
        spacedName = addSpaces(name)
        autoDisableMode = moduleInfo.autoDisableMode
        description = moduleInfo.description
        category = moduleInfo.category
        keyBind = moduleInfo.keyBind
        array = moduleInfo.array
        canEnable = moduleInfo.canEnable
        isSupported = Backend.REPRESENTED_BACKEND_VERSION in moduleInfo.supportedVersions
    }

    // Current state of module
    var state = false
        set(value) {
            if (field == value)
                return

            // Call toggle
            onToggle(value)

            // Play sound and add notification
            if (!LiquidBounce.isStarting) {
                if(value){
                    LiquidBounce.tipSoundManager.enableSound.asyncPlay()
                    LiquidBounce.hud.addNotification(Notification(name, "Enabled", NotifyType.SUCCESS))
                }else{
                    LiquidBounce.tipSoundManager.disableSound.asyncPlay()
                    LiquidBounce.hud.addNotification(Notification(name, "Disabled", NotifyType.ERROR))
                }
            }
            // Call on enabled or disabled
            if (value) {
                onEnable()

                if (canEnable)
                    field = true
            } else {
                field = false
                onDisable()
            }

            // Save module state
            LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.valuesConfig)
        }


    // HUD
    val hue = Math.random().toFloat()
    var slide = 0F
    var higt = 0F

    // Tag
    open val tag: String?
        get() = null

    /**
     * Toggle module
     */
    fun toggle() {
        state = !state
    }

    /**
     * Called when module toggled
     */
    open fun onToggle(state: Boolean) {}

    /**
     * Called when module enabled
     */
    open fun onEnable() {}

    /**
     * Called when module disabled
     */
    open fun onDisable() {}

    /**
     * Get module by [valueName]
     */
    open fun getValue(valueName: String) = values.find { it.name.equals(valueName, ignoreCase = true) }

    /**
     * Get all values of module
     */
    open val values: List<Value<*>>
        get() = javaClass.declaredFields.map { valueField ->
            valueField.isAccessible = true
            valueField[this]
        }.filterIsInstance<Value<*>>()

    /**
     * Events should be handled when module is enabled
     */
    override fun handleEvents() = state
}