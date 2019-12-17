/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.registry.type.entity;

import org.spongepowered.api.entity.living.monster.boss.dragon.phase.DragonPhaseType;
import org.spongepowered.api.entity.living.monster.boss.dragon.phase.DragonPhaseTypes;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.api.registry.util.RegisterCatalog;
import org.spongepowered.common.mixin.accessor.entity.boss.dragon.phase.PhaseTypeAccessor;
import org.spongepowered.common.registry.type.AbstractCatalogRegistryModule;
import org.spongepowered.common.registry.type.entity.EnderDragonPhaseTypeRegistryModule.Holder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.entity.boss.dragon.phase.PhaseType;

public class EnderDragonPhaseTypeRegistryModule extends AbstractCatalogRegistryModule<DragonPhaseType> {

    @Override
    public void registerDefaults() {
        for (final PhaseType<?> phaseType : PhaseTypeAccessor.accessor$getPhases()) {
            this.register((DragonPhaseType) phaseType);
        }
    }

    public static EnderDragonPhaseTypeRegistryModule getInstance() {
        return Holder.INSTANCE;
    }

    static final class Holder {
        static final EnderDragonPhaseTypeRegistryModule INSTANCE = new EnderDragonPhaseTypeRegistryModule();
        static {
            try {
                Class.forName("net.minecraft.entity.boss.dragon.phase.PhaseList");
            } catch (final ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
