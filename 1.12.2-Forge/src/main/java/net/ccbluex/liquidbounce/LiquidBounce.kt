package net.ccbluex.liquidbounce

import net.ccbluex.liquidbounce.api.Wrapper
import net.ccbluex.liquidbounce.event.ClientShutdownEvent
import net.ccbluex.liquidbounce.event.EventManager
import net.ccbluex.liquidbounce.features.command.CommandManager
import net.ccbluex.liquidbounce.features.module.ModuleManager
import net.ccbluex.liquidbounce.features.special.AntiForge
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof
import net.ccbluex.liquidbounce.file.FileManager
import net.ccbluex.liquidbounce.management.CombatManager
import net.ccbluex.liquidbounce.management.MemoryManager
import net.ccbluex.liquidbounce.management.TipSoundManager
import net.ccbluex.liquidbounce.rename.modules.misc.MoreChecker
import net.ccbluex.liquidbounce.rename.verify.MoreIRC
import net.ccbluex.liquidbounce.rename.verify.base.GuiBase
import net.ccbluex.liquidbounce.rename.verify.base.UserData
import net.ccbluex.liquidbounce.rename.verify.impl.Login
import net.ccbluex.liquidbounce.script.ScriptManager
import net.ccbluex.liquidbounce.script.remapper.Remapper.loadSrg
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.OtcClickGUi
import net.ccbluex.liquidbounce.ui.client.hud.HUD
import net.ccbluex.liquidbounce.ui.client.hud.HUD.Companion.createDefault
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.ui.font.yalan.FontLoaders
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.InventoryUtils
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.misc.AutoDisableManager
import top.fl0wowp4rty.Heavy
import top.fl0wowp4rty.MethodParameter
import top.fl0wowp4rty.Native
import java.io.File
import java.net.Socket

@Heavy
@Native
object LiquidBounce {

    // Client information
    const val CLIENT_NAME = "More"
    const val CLIENT_NAME2 = "More"
    const val CLIENT_VERSION = "v2.4"
    const val CLIENT_CREATOR = "CCBlueX, LvZiQiao"
    const val CLIENT_CLOUD = "https://cloud.liquidbounce.net/LiquidBounce"

    var isStarting = false
    var height = -14f
    var aniHeight = -14f
    // Managers
    lateinit var moduleManager: ModuleManager
    lateinit var commandManager: CommandManager
    lateinit var eventManager: EventManager
    lateinit var fileManager: FileManager
    lateinit var scriptManager: ScriptManager
    lateinit var combatManager: CombatManager
    lateinit var tipSoundManager: TipSoundManager
    lateinit var ircThread: Thread
    lateinit var otc: OtcClickGUi
    lateinit var socket: Socket

    @JvmStatic
    var moreUsers = ArrayList<String>()
    @JvmStatic
    var data: UserData? = null
    @JvmStatic
    var useIP = false
    // HUD & ClickGUI
    lateinit var hud: HUD

    lateinit var clickGui: ClickGui

    lateinit var wrapper: Wrapper


    @JvmStatic
    var invest: GuiBase? = null
        set (value) {
            if (value == null) {
                if (useIP) field = null
            } else field = value
        }

    /**
     * Execute if client will be started
     */
    @MethodParameter
    fun startClient() {
        isStarting = true

        eventManager = EventManager()

        val start = System.currentTimeMillis()

        ClientUtils.getLogger().info("Starting $CLIENT_NAME ${CLIENT_VERSION}r, by $CLIENT_CREATOR")

        // Create file manager
        fileManager = FileManager()

        // Create combat manager
        combatManager = CombatManager()

        // Create tipSound manager
        tipSoundManager = TipSoundManager()

        // Register listeners
        eventManager.registerListener(RotationUtils())
        eventManager.registerListener(AntiForge())
        eventManager.registerListener(BungeeCordSpoof())
        eventManager.registerListener(AutoDisableManager())
        eventManager.registerListener(InventoryUtils())
        eventManager.registerListener(combatManager)
        eventManager.registerListener(MemoryManager())

        // Create command manager
        commandManager = CommandManager()

        // Load client fonts
        Fonts.loadFonts()
        FontLoaders.initFonts()
        // Setup module manager and register modules
        moduleManager = ModuleManager()
        moduleManager.registerModules()
        // Remapper
        try {
            loadSrg()

            // ScriptManager
            scriptManager = ScriptManager()
            scriptManager.loadScripts()
            scriptManager.enableScripts()
        } catch (throwable: Throwable) {
            ClientUtils.getLogger().error("Failed to load scripts.", throwable)
        }

        // Register commands
        commandManager.registerCommands()

        // Load configs
        fileManager.loadConfigs(
            fileManager.valuesConfig, fileManager.accountsConfig,
            fileManager.friendsConfig
        )

        // ClickGUI
        clickGui = ClickGui()
        otc = OtcClickGUi()
        fileManager.loadConfig(fileManager.clickGuiConfig)

        // Set HUD
        hud = createDefault()
        fileManager.loadConfig(fileManager.hudConfig)

        // Disable Optifine fast render
        ClientUtils.disableFastRender()

        // Load generators
        GuiAltManager.loadGenerators()

        // Set is starting status
        isStarting = false

        useIP = false

        MoreIRC.init()

        ircThread = Thread(MoreIRC())
        ircThread.start()

        invest = Login()

        ClientUtils.getLogger().info("Loaded client in " + (System.currentTimeMillis() - start) + " ms.")

        moduleManager.getModule(MoreChecker::class.java).state = true

        System.gc()
    }




    /**
     * Execute if client will be stopped
     */
    fun stopClient() {
        eventManager.callEvent(ClientShutdownEvent())

        fileManager.saveAllConfigs()
    }
}