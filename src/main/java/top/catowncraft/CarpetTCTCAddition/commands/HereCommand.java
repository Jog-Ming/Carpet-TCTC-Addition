/*
 * Copyright (c) Copyright 2020 - 2021 The Cat Town Craft and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package top.catowncraft.CarpetTCTCAddition.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.dimension.DimensionType;
import top.catowncraft.CarpetTCTCAddition.CarpetTCTCAddition;
import top.catowncraft.CarpetTCTCAddition.CarpetTCTCAdditionSettings;
import top.catowncraft.CarpetTCTCAddition.utils.MessageUtil;

import java.util.Collection;

import static carpet.utils.Translations.tr;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class HereCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> here = literal("here")
                .requires(commandSourceStack -> SettingsManager.canUseCommand(commandSourceStack, CarpetTCTCAdditionSettings.commandHere))
                .executes(context -> print(context.getSource(), context.getSource().getPlayerOrException()))
                .then(argument("target", EntityArgument.players())
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                        .executes(context -> print(context.getSource(), EntityArgument.getPlayers(context, "target"))));
        dispatcher.register(here);
    }

    public static int print(CommandSourceStack commandSourceStack, Collection<ServerPlayer> serverPlayerCollection) {
        for (ServerPlayer serverPlayer : serverPlayerCollection) {
            print(commandSourceStack, serverPlayer);
        }
        return 1;
    }

    public static int print(CommandSourceStack commandSourceStack, ServerPlayer serverPlayer) {
        if (serverPlayer.dimension == DimensionType.OVERWORLD || serverPlayer.dimension == DimensionType.NETHER) {
            MessageUtil.sendServerMessage(CarpetTCTCAddition.getServer(),
                    new TranslatableComponent(tr("carpet-tctc-addition.message.command.fix.here.withTransformed", "%s @ %s %s -> %s"),
                            new TextComponent(serverPlayer.getName().getString()).withStyle(ChatFormatting.GRAY),
                            getDimension(serverPlayer.dimension),
                            new TextComponent(getOriginalPosition(serverPlayer)).withStyle(serverPlayer.dimension == DimensionType.OVERWORLD ? ChatFormatting.DARK_GREEN : ChatFormatting.DARK_RED),
                            new TextComponent(serverPlayer.dimension == DimensionType.OVERWORLD ? getDividedPosition(serverPlayer) : getMultipliedPosition(serverPlayer)).withStyle(serverPlayer.dimension == DimensionType.OVERWORLD ? ChatFormatting.DARK_RED : ChatFormatting.DARK_GREEN)
                    )
            );
        } else {
            MessageUtil.sendServerMessage(CarpetTCTCAddition.getServer(),
                    new TranslatableComponent(tr("carpet-tctc-addition.message.command.fix.here.withoutTransformed", "%s @ %s %s"),
                            new TextComponent(serverPlayer.getName().getString()).withStyle(ChatFormatting.GRAY),
                            getDimension(serverPlayer.dimension),
                            new TextComponent(getOriginalPosition(serverPlayer)).withStyle(serverPlayer.dimension == DimensionType.THE_END ? ChatFormatting.GOLD : ChatFormatting.DARK_AQUA)
                    )
            );
        }
        if (CarpetTCTCAdditionSettings.hereGlowTime > 0) {
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.GLOWING, CarpetTCTCAdditionSettings.hereGlowTime * 20, 0, false, false, false));
        }
        // TODO World map waypoint adder.
        return 1;
    }

    public static String getOriginalPosition(Entity entity) {
        return String.format("[x: %d, y: %d, z: %d]", (int) entity.getX(), (int) entity.getY(), (int) entity.getZ());
    }

    public static String getMultipliedPosition(Entity entity) {
        return String.format("[x: %d, y: %d, z: %d]", (int) (entity.getX() * 8), (int) (entity.getY() * 8), (int) (entity.getZ() * 8));
    }

    public static String getDividedPosition(Entity entity) {
        return String.format("[x: %d, y: %d, z: %d]", (int) (entity.getX() / 8), (int) (entity.getY() / 8), (int) (entity.getZ() / 8));
    }

    public static Component getDimension(DimensionType dimensionType) {
        switch (dimensionType.toString()) {
            case "minecraft:overworld":
                return new TextComponent(tr("carpet-tctc-addition.label.dimensionType.overworld", "Overworld")).withStyle(ChatFormatting.GREEN);
            case "minecraft:the_end":
                return new TextComponent(tr("carpet-tctc-addition.label.dimensionType.end", "End")).withStyle(ChatFormatting.YELLOW);
            case "minecraft:the_nether":
                return new TextComponent(tr("carpet-tctc-addition.label.dimensionType.nether", "Nether")).withStyle(ChatFormatting.RED);
            default:
                return new TextComponent(tr("carpet-tctc-addition.label.dimensionType.unknown", "Unknown")).withStyle(ChatFormatting.AQUA);
        }
    }
}