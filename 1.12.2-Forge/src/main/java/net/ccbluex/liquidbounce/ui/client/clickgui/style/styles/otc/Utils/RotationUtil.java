/*
 * Decompiled with CFR 0_132.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class RotationUtil {
    public static float pitch() {
        return Helper.mc.player.rotationPitch;
    }

    public static void pitch(float pitch) {
        Helper.mc.player.rotationPitch = pitch;
    }

    public static float yaw() {
        return Helper.mc.player.rotationYaw;
    }

    public static void yaw(float yaw) {
        Helper.mc.player.rotationYaw = yaw;
    }

    public static float[] getAngles(EntityLivingBase entity) {
        if (entity == null) return null;
        final EntityPlayerSP player = Minecraft
                .getMinecraft().player;

        final double diffX = entity.posX - player.posX,
                diffY = entity.posY + entity.getEyeHeight() * 0.9 - (player.posY + player.getEyeHeight()),
                diffZ = entity.posZ - player.posZ, dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ); // @on

        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F,
                pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

        return new float[]{player.rotationYaw + MathHelper.wrapDegrees(
                yaw - player.rotationYaw), player.rotationPitch + MathHelper.wrapDegrees(pitch - player.rotationPitch)};
    }

    public static float[] faceTarget(Entity target, float p_706252, float p_706253, boolean miss) {
        double var6;
        double var4 = target.posX - Helper.mc.player.posX;
        double var8 = target.posZ - Helper.mc.player.posZ;
        if (target instanceof EntityLivingBase) {
            EntityLivingBase var10 = (EntityLivingBase)target;
            var6 = var10.posY + (double)var10.getEyeHeight() - (Helper.mc.player.posY + (double)Helper.mc.player.getEyeHeight());
        } else {
            var6 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (Helper.mc.player.posY + (double)Helper.mc.player.getEyeHeight());
        }
        Random rnd = new Random();
        double var14 = MathHelper.sqrt(var4 * var4 + var8 * var8);
        float var12 = (float)(Math.atan2(var8, var4) * 180.0 / 3.141592653589793) - 90.0f;
        float var13 = (float)(- Math.atan2(var6 - (target instanceof EntityPlayer ? 0.25 : 0.0), var14) * 180.0 / 3.141592653589793);
        float pitch = RotationUtil.changeRotation(Helper.mc.player.rotationPitch, var13, p_706253);
        float yaw = RotationUtil.changeRotation(Helper.mc.player.rotationYaw, var12, p_706252);
        return new float[]{yaw, pitch};
    }

    public static float changeRotation(float p_706631, float p_706632, float p_706633) {
        float var4 = MathHelper.wrapDegrees(p_706632 - p_706631);
        if (var4 > p_706633) {
            var4 = p_706633;
        }
        if (var4 < - p_706633) {
            var4 = - p_706633;
        }
        return p_706631 + var4;
    }

    public static double[] getRotationToEntity(Entity entity) {
        double pX = Helper.mc.player.posX;
        double pY = Helper.mc.player.posY + (double)Helper.mc.player.getEyeHeight();
        double pZ = Helper.mc.player.posZ;
        double eX = entity.posX;
        double eY = entity.posY + (double)(entity.height / 2.0f);
        double eZ = entity.posZ;
        double dX = pX - eX;
        double dY = pY - eY;
        double dZ = pZ - eZ;
        double dH = Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dZ, 2.0));
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        double pitch = Math.toDegrees(Math.atan2(dH, dY));
        return new double[]{yaw, 90.0 - pitch};
    }

    public static float[] getRotations(Entity entity) {
        double diffY;
        if (entity == null) {
            return null;
        }
        double diffX = entity.posX - Helper.mc.player.posX;
        double diffZ = entity.posZ - Helper.mc.player.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase elb = (EntityLivingBase)entity;
            diffY = elb.posY + ((double)elb.getEyeHeight() - 0.4) - (Helper.mc.player.posY + (double)Helper.mc.player.getEyeHeight());
        } else {
            diffY = (entity.getCollisionBoundingBox().minY + entity.getCollisionBoundingBox().maxY) / 2.0 - (Helper.mc.player.posY + (double)Helper.mc.player.getEyeHeight());
        }
        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 0.0f;
        }
        return angle3;
    }

    public static float[] grabBlockRotations(BlockPos pos) {
        return RotationUtil.getVecRotation(Helper.mc.player.getPositionVector().addVector(0.0, Helper.mc.player.getEyeHeight(), 0.0), new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5));
    }

    public static float[] getVecRotation(Vec3d position) {
        return RotationUtil.getVecRotation(Helper.mc.player.getPositionVector().addVector(0.0, Helper.mc.player.getEyeHeight(), 0.0), position);
    }

    public static Vec3d  flat(Vec3d  v) {
        return new Vec3d(v.x, 0.0, v.z);
    }


    public static float[] getVecRotation(Vec3d  origin, Vec3d position) {
        Vec3d  difference = position.subtract(origin);
        double distance = flat(difference).lengthVector();
        float yaw = (float)Math.toDegrees(Math.atan2(difference.z, difference.x)) - 90.0f;
        float pitch = (float)(- Math.toDegrees(Math.atan2(difference.y, distance)));
        return new float[]{yaw, pitch};
    }

    public static int wrapAngleToDirection(float yaw, int zones) {
        int angle = (int)((double)(yaw + (float)(360 / (2 * zones))) + 0.5) % 360;
        if (angle < 0) {
            angle += 360;
        }
        return angle / (360 / zones);
    }
}

