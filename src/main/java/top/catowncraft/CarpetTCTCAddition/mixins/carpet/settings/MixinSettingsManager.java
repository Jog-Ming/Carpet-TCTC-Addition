/*
 * Copyright (c) 2021 The Cat Town Craft and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package top.catowncraft.CarpetTCTCAddition.mixins.carpet.settings;

import carpet.settings.SettingsManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.catowncraft.CarpetTCTCAddition.Reference;
import top.catowncraft.CarpetTCTCAddition.utils.MessageUtil;

import static carpet.utils.Translations.tr;

@Mixin(SettingsManager.class)
public class MixinSettingsManager {
    @Inject(
            method = "listAllSettings",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/commands/CommandSourceStack;getPlayerOrException()Lnet/minecraft/server/level/ServerPlayer;"
            ),
            remap = false
    )
    private void printAdditionVersion(CommandSourceStack source, CallbackInfoReturnable<Integer> cir) {
        MessageUtil.sendMessage(source, (BaseComponent) new TextComponent(String.format(tr("carpet-tctc-addition.message.command.carpet.version", "%s Version: %s (%s)"), Reference.MOD_NAME, Reference.MOD_VERSION, tr(String.format("carpet-tctc-addition.label.versionType.%s", Reference.MOD_VERSION_TYPE), Reference.MOD_VERSION_TYPE))).setStyle(new Style().setColor(ChatFormatting.GRAY)));
    }
}