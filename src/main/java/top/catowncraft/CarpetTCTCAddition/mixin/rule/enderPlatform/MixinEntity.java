/*
 * Copyright (c) Copyright 2020 - 2021 The Cat Town Craft and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package top.catowncraft.carpettctcaddition.mixin.rule.enderPlatform;

//#if MC < 11600
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.server.MinecraftServer;
//#endif
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
//#if MC < 11600
//$$ import net.minecraft.world.level.block.Blocks;
//$$ import net.minecraft.world.level.dimension.DimensionType;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//#if MC >= 11600
import org.spongepowered.asm.mixin.injection.Redirect;
//#else
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//$$ import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//#endif
import top.catowncraft.carpettctcaddition.CarpetTCTCAdditionSettings;

@Mixin(Entity.class)
public class MixinEntity {
    //#if MC >= 11600
    @Redirect(
            method = "changeDimension",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;makeObsidianPlatform(Lnet/minecraft/server/level/ServerLevel;)V"
            )
    )
    private void makeObsidianPlatform(ServerLevel serverLevel) {
        if (CarpetTCTCAdditionSettings.enderPlatform == CarpetTCTCAdditionSettings.EnderPlatformOptions.ALL) {
            ServerLevel.makeObsidianPlatform(serverLevel);
        }
    }
    //#else
    //$$ @Inject(
    //$$         method = "changeDimension",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lnet/minecraft/server/level/ServerLevel;getDimensionSpecificSpawn()Lnet/minecraft/core/BlockPos;",
    //$$                 shift = At.Shift.AFTER
    //$$         ),
    //$$         locals = LocalCapture.CAPTURE_FAILEXCEPTION
    //$$ )
    //$$ private void makeObsidianPlatform(DimensionType dimensionType, CallbackInfoReturnable<Entity> cir, MinecraftServer minecraftServer, DimensionType dimensionType2, ServerLevel serverLevel, ServerLevel serverLevel2) {
    //$$     if (CarpetTCTCAdditionSettings.enderPlatform == CarpetTCTCAdditionSettings.EnderPlatformOptions.ALL) {
    //$$         // From Minecraft-116
    //$$         BlockPos blockPos1 = new BlockPos(100, 50, 0);
    //$$         int i = blockPos1.getX();
    //$$         int j = blockPos1.getY() - 2;
    //$$         int k = blockPos1.getZ();
    //$$         BlockPos.betweenClosed(i - 2, j + 1, k - 2, i + 2, j + 3, k + 2).forEach((blockPos2) -> {
    //$$             serverLevel2.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
    //$$         });
    //$$         BlockPos.betweenClosed(i - 2, j, k - 2, i + 2, j, k + 2).forEach((blockPos2) -> {
    //$$             serverLevel2.setBlockAndUpdate(blockPos2, Blocks.OBSIDIAN.defaultBlockState());
    //$$         });
    //$$     }
    //$$ }
    //#endif
}
