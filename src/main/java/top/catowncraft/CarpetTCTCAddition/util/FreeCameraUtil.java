/*
 * Copyright (c) Copyright 2020 - 2021 The Cat Town Craft and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package top.catowncraft.carpettctcaddition.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
//#if MC >= 11600
import net.minecraft.resources.ResourceKey;
//#endif
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
//#if MC >= 11600
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
//#else
//$$ import net.minecraft.world.level.dimension.DimensionType;
//#endif
import net.minecraft.world.phys.Vec3;
import top.catowncraft.carpettctcaddition.CarpetTCTCAddition;
import top.catowncraft.carpettctcaddition.CarpetTCTCAdditionReference;
import top.catowncraft.carpettctcaddition.helper.FreeCameraData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//#if MC >= 11600
import static net.minecraft.core.Registry.DIMENSION_REGISTRY;
//#endif

public class FreeCameraUtil {
    private static Map<UUID, FreeCameraData> cameraData = new HashMap<>();

    public static Map<UUID, FreeCameraData> getCameraData() {
        return FreeCameraUtil.cameraData;
    }

    public static void setCameraData(Map<UUID, FreeCameraData> cameraData) {
        FreeCameraUtil.cameraData = cameraData;
    }

    public static Path getFile() {
        //#if MC >= 11600
        return CarpetTCTCAddition.getServer().getWorldPath(LevelResource.ROOT).resolve(String.format("%s_freeCameraStorage.json", CarpetTCTCAdditionReference.getModId()));
        //#else
        //$$ return CarpetTCTCAddition.getServer().getStorageSource().getFile(CarpetTCTCAddition.getServer().getLevelIdName(), String.format("%s_freeCameraStorage.json", CarpetTCTCAdditionReference.getModId())).toPath();
        //#endif
    }

    public static FreeCameraData createEntry(JsonObject jsonObject) {
        GameType gameType = jsonObject.has("gameType") ? GameType.byName(jsonObject.get("gameType").getAsString()) : GameType.SURVIVAL;
        //#if MC >= 11600
        ResourceKey<Level> dimension = jsonObject.has("dimension") ? ResourceKey.create(DIMENSION_REGISTRY, new ResourceLocation(jsonObject.get("dimension").getAsString())) : null;
        //#else
        //$$ DimensionType dimension = jsonObject.has("dimension") ? DimensionType.getByName(new ResourceLocation(jsonObject.get("dimension").getAsString())) : null;
        //#endif
        double x = jsonObject.has("x") ? jsonObject.get("x").getAsDouble() : 0;
        double y = jsonObject.has("y") ? jsonObject.get("y").getAsDouble() : 0;
        double z = jsonObject.has("z") ? jsonObject.get("z").getAsDouble() : 0;
        float xRot = jsonObject.has("xRot") ? jsonObject.get("xRot").getAsFloat() : 0;
        float yRot = jsonObject.has("yRot") ? jsonObject.get("yRot").getAsFloat() : 0;
        boolean isFreecam = jsonObject.has("isFreecam") && jsonObject.get("isFreecam").getAsBoolean();
        //#if MC >= 11600
        return new FreeCameraData(gameType, dimension != null ? dimension : Level.OVERWORLD, new Vec3(x, y, z), xRot, yRot, isFreecam);
        //#else
        //$$ return new FreeCameraData(gameType, dimension != null ? dimension : DimensionType.OVERWORLD, new Vec3(x, y, z), xRot, yRot, isFreecam);
        //#endif
    }

    static class Serializer implements JsonDeserializer<FreeCameraData>, JsonSerializer<FreeCameraData> {

        @Override
        public JsonElement serialize(FreeCameraData src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            src.serialize(jsonObject);
            return jsonObject;
        }

        @Override
        public FreeCameraData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject jsonObject = json.getAsJsonObject();
                return FreeCameraUtil.createEntry(jsonObject);
            }
            return null;
        }
    }

    public static void load() {
        try {
            FreeCameraUtil.setCameraData(FreeCameraUtil.loadFreeCameraData());
        } catch (IOException e) {
            CarpetTCTCAddition.getLogger().error("Cannot load free camera data from file!");
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            FreeCameraUtil.saveFreeCameraData(FreeCameraUtil.cameraData);
        } catch (IOException e) {
            CarpetTCTCAddition.getLogger().error("Cannot save free camera data to file!");
            e.printStackTrace();
        }
    }

    public static void saveFreeCameraData(Map<UUID, FreeCameraData> data) throws IOException {
        Path file = getFile();

        if (data.isEmpty()) {
            Files.deleteIfExists(file);
            return;
        }

        String string = new GsonBuilder().registerTypeAdapter(FreeCameraData.class, new Serializer()).create().toJson(data);

        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write(string);
        } catch (Throwable throwable) {
            CarpetTCTCAddition.getLogger().error("Cannot write freeCameraData: {}", string);
        }
    }

    public static Map<UUID, FreeCameraData> loadFreeCameraData() throws IOException {
        Path file = getFile();

        if (!Files.isRegularFile(file)) {
            return new HashMap<>();
        }
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            return new GsonBuilder().registerTypeAdapter(FreeCameraData.class, new Serializer()).create().fromJson(reader, new TypeToken<Map<UUID, FreeCameraData>>() {
            }.getType());
        }
    }
}