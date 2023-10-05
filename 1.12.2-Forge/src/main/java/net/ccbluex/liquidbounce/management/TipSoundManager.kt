package net.ccbluex.liquidbounce.management

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.utils.TipSoundPlayer
import net.ccbluex.liquidbounce.utils.render.tenacity.FileUtils
import java.io.File

class TipSoundManager {
    var enableSound: TipSoundPlayer
    var disableSound: TipSoundPlayer
    var gameEndSound: TipSoundPlayer

    init {
        val enableSoundFile = File(LiquidBounce.fileManager.soundsDir, "on.wav")
        val disableSoundFile = File(LiquidBounce.fileManager.soundsDir, "off.wav")
        val gameEndSoundFile = File(LiquidBounce.fileManager.soundsDir, "ge.wav")

        if (!enableSoundFile.exists()) {
            FileUtils.unpackFile(enableSoundFile, "assets/minecraft/more/enable.wav")
        }
        if (!disableSoundFile.exists()) {
            FileUtils.unpackFile(disableSoundFile, "assets/minecraft/more/disable.wav")
        }
        if (!gameEndSoundFile.exists()) {
            FileUtils.unpackFile(gameEndSoundFile, "assets/minecraft/more/ge.wav")
        }

        enableSound = TipSoundPlayer(enableSoundFile)
        disableSound = TipSoundPlayer(disableSoundFile)
        gameEndSound = TipSoundPlayer(gameEndSoundFile)
    }
}