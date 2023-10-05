
package net.ccbluex.liquidbounce.api.minecraft.client.render.texture

import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation
import net.minecraft.client.renderer.texture.ITextureObject
import net.minecraft.client.renderer.texture.SimpleTexture
import net.minecraft.util.ResourceLocation

interface ITextureManager {
    fun loadTexture(textureLocation: IResourceLocation, textureObj: IAbstractTexture): Boolean
    fun loadTexture(textureLocation: ResourceLocation, textureObj: ITextureObject): Boolean
    fun bindTexture(image: IResourceLocation)
    fun bindTexture(image: ResourceLocation)
    fun getTexture(image: ResourceLocation): ITextureObject?
}