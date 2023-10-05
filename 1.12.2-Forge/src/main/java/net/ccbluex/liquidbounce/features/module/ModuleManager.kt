package net.ccbluex.liquidbounce.features.module

import net.ccbluex.liquidbounce.features.module.modules.combat.KillFix
import net.ccbluex.liquidbounce.rename.modules.world.ScaffoldFix
import net.ccbluex.liquidbounce.features.module.modules.render.CustomBlur
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.KeyEvent
import net.ccbluex.liquidbounce.event.Listenable
import net.ccbluex.liquidbounce.features.module.modules.combat.*
import net.ccbluex.liquidbounce.features.module.modules.exploit.*
import net.ccbluex.liquidbounce.rename.modules.misc.HYTParty
import net.ccbluex.liquidbounce.features.module.modules.movement.*
import net.ccbluex.liquidbounce.features.module.modules.render.*
import net.ccbluex.liquidbounce.rename.modules.hyt.*
import net.ccbluex.liquidbounce.rename.modules.misc.*
import net.ccbluex.liquidbounce.rename.modules.player.*
import net.ccbluex.liquidbounce.rename.modules.world.*
import net.ccbluex.liquidbounce.rename.modules.world.Timer
import net.ccbluex.liquidbounce.utils.ClientUtils
import top.fl0wowp4rty.MethodParameter
import java.util.*


class ModuleManager : Listenable {
    val modules = TreeSet<Module> { module1, module2 -> module1.name.compareTo(module2.name) }
    private val moduleClassMap = hashMapOf<Class<*>, Module>()
    init {
        LiquidBounce.eventManager.registerListener(this)
    }

    /**
     * Register all modules
     */

    @MethodParameter
    fun registerModules() {
        registerModules(
            HytDisabler::class.java,
            HytNoLagBack::class.java,
            Scaffold2::class.java,
            CustomBlur::class.java,
            ScaffoldHelper::class.java,
            KillAura::class.java,
            KbHelper::class.java,
            AutoSave::class.java,
            CustomColor::class.java,
            AutoGG::class.java,
            AutoL::class.java,
            HytAntiVoid::class.java,
            AntiHunger::class.java,
            HytGapple::class.java,
            HytNoFucker::class.java,
            HytGetName::class.java,
            CustomFont::class.java,
            KillFix::class.java,
            ScaffoldFix::class.java,
            CustomHUD::class.java,
            Title::class.java,
            AutoArmor::class.java,
            AutoBow::class.java,
            AutoRunaway::class.java,
            AutoPot::class.java,
            AutoSoup::class.java,
            AutoWeapon::class.java,
            BowAimbot::class.java,
            Criticals::class.java,
            Trigger::class.java,
            Fly::class.java,
            ClickGUI::class.java,
            HighJump::class.java,
            InventoryMove::class.java,
            LiquidWalk::class.java,
            SafeWalk::class.java,
            WallClimb::class.java,
            Strafe::class.java,
            Sprint::class.java,
            Teams::class.java,
            NoRotateSet::class.java,
            ChestStealer::class.java,
            Scaffold::class.java,
            CivBreak::class.java,
            Tower::class.java,
            FastBreak::class.java,
            FastPlace::class.java,
            ESP::class.java,
            HytNoHurt::class.java,
            HytSpeed::class.java,
            NoSlow::class.java,
            Velocity::class.java,
            Speed::class.java,
            Tracers::class.java,
            NameTags::class.java,
            FastUse::class.java,
            Teleport::class.java,
            Fullbright::class.java,
            ItemESP::class.java,
            StorageESP::class.java,
            Projectiles::class.java,
            NoClip::class.java,
            Nuker::class.java,
            PingSpoof::class.java,
            FastClimb::class.java,
            Disabler::class.java,
            Step::class.java,
            AutoRespawn::class.java,
            AutoTool::class.java,
            NoWeb::class.java,
            Spammer::class.java,
            IceSpeed::class.java,
            Regen::class.java,
            NoFall::class.java,
            Blink::class.java,
            NameProtect::class.java,
            NoHurtCam::class.java,
            MidClick::class.java,
            XRay::class.java,
            Timer::class.java,
            Sneak::class.java,
            FreeCam::class.java,
            Aimbot::class.java,
            Eagle::class.java,
            HitBox::class.java,
            AntiCactus::class.java,
            Plugins::class.java,
            ConsoleSpammer::class.java,
            LongJump::class.java,
            Parkour::class.java,
            FastBow::class.java,
            AutoClicker::class.java,
            NoBob::class.java,
            BlockOverlay::class.java,
            NoFriends::class.java,
            BlockESP::class.java,
            Chams::class.java,
            Clip::class.java,
            Phase::class.java,
            ServerCrasher::class.java,
            NoFOV::class.java,
            FastStairs::class.java,
            TNTBlock::class.java,
            InventoryCleaner::class.java,
            TrueSight::class.java,
            AntiBlind::class.java,
            NoSwing::class.java,
            Breadcrumbs::class.java,
            AbortBreaking::class.java,
            PotionSaver::class.java,
            CameraClip::class.java,
            KillAura2::class.java,
            NoC03s::class.java,
            Ignite::class.java,
            NoPitchLimit::class.java,
            Liquids::class.java,
            AtAllProvider::class.java,
            AirLadder::class.java,
            TeleportHit::class.java,
            ForceUnicodeChat::class.java,
            ItemTeleport::class.java,
            BufferSpeed::class.java,
            SuperKnockback::class.java,
            ProphuntESP::class.java,
            AutoFish::class.java,
            Damage::class.java,
            Freeze::class.java,
            KeepContainer::class.java,
            VehicleOneHit::class.java,
            Reach::class.java,
            Rotations::class.java,
            NoJumpDelay::class.java,
            AntiAFK::class.java,
            HUD::class.java,
            MoreChecker::class.java,
            AutoLeave::class.java,
            TNTESP::class.java,
            ComponentOnHover::class.java,
            ResourcePackSpoof::class.java,
            NoSlowBreak::class.java,
            PortalMenu::class.java,
            EnchantEffect::class.java,
            SpeedMine::class.java,
            AutoHead::class.java,
            Animations::class.java,
            Test::class.java,
            BlurSettings::class.java,
            HytBlink::class.java,
            HytVelocity::class.java
        )

        registerModule(NoScoreboard)
        registerModule(Fucker)
        registerModule(ChestAura)
        registerModule(HYTParty)
        registerModule(HYTJoin4v4)
        registerModule(AntiBot)
    }

    /**
     * Register [module]
     */
    fun registerModule(module: Module) {
        if (!module.isSupported)
            return
        modules += module
        moduleClassMap[module.javaClass] = module

        generateCommand(module)
        LiquidBounce.eventManager.registerListener(module)
    }

    /**
     * Register [moduleClass]
     */
    private fun registerModule(moduleClass: Class<out Module>) {
        try {
            registerModule(moduleClass.newInstance())
        } catch (e: Throwable) {
            ClientUtils.getLogger()
                .error("Failed to load module: ${moduleClass.name} (${e.javaClass.name}: ${e.message})")
        }
    }

    /**
     * Register a list of modules
     */
    @MethodParameter
    @SafeVarargs
    fun registerModules(vararg modules: Class<out Module>) {
        modules.forEach(this::registerModule)
    }

    fun unregisterallModule() {
        modules.clear()
        moduleClassMap.clear()
    }

    /**
     * Unregister module
     */
    fun unregisterModule(module: Module) {
        modules.remove(module)
        moduleClassMap.remove(module::class.java)
        LiquidBounce.eventManager.unregisterListener(module)
    }

    /**
     * Generate command for [module]
     */
    internal fun generateCommand(module: Module) {
        val values = module.values

        if (values.isEmpty())
            return

        LiquidBounce.commandManager.registerCommand(ModuleCommand(module, values))
    }

    /**
     * Legacy stuff
     *
     * TODO: Remove later when everything is translated to Kotlin
     */

    /**
     * Get module by [moduleClass]
     */
    fun getModule(moduleClass: Class<*>) = moduleClassMap[moduleClass]!!

    operator fun get(clazz: Class<*>) = getModule(clazz)

    /**
     * Get module by [moduleName]
     */
    fun getModule(moduleName: String?) = modules.find { it.name.equals(moduleName, ignoreCase = true) }

    /**
     * Module related events
     */

    /**
     * Handle incoming key presses
     */
    
    @EventTarget
    private fun onKey(event: KeyEvent) {
        modules.filter { it.keyBind == event.key }.forEach { it.toggle() }
    }

    override fun handleEvents() = true
}
