/*
 * Copyright (c) Copyright 2020 - 2023 The Cat Town Craft and contributors.
 * This source code is subject to the terms of the GNU Lesser General Public
 * License, version 3. If a copy of the LGPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/lgpl-3.0.txt
 */
package top.catowncraft.carpettctcaddition.rule;

import top.catowncraft.carpettctcaddition.CarpetTCTCAdditionReference;
import top.hendrixshen.magiclib.api.rule.WrapperSettingManager;
public class CarpetTCTCAdditionSettingManager extends WrapperSettingManager {
    public CarpetTCTCAdditionSettingManager(String version, String identifier, String fancyName) {
        super(version, identifier, fancyName);
    }

    @Override
    public String getVersion() {
        return String.format("%s (%s)", super.getVersion(), this.tr(this.getCurrentLanguageCode(),
                String.format("magiclib.misc.versionType.%s", CarpetTCTCAdditionReference.getModVersionType())));
    }
}