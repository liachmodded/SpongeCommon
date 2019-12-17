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

import static com.google.common.base.Preconditions.checkNotNull;

import org.spongepowered.api.CatalogKey;
import org.spongepowered.api.entity.ai.GoalExecutor;
import org.spongepowered.api.entity.ai.GoalExecutorType;
import org.spongepowered.api.registry.AlternateCatalogRegistryModule;
import org.spongepowered.common.entity.ai.SpongeGoalExecutorType;

import net.minecraft.entity.ai.goal.GoalSelector;
import org.spongepowered.common.registry.type.AbstractCatalogRegistryModule;

public class GoalExecutorTypeModule extends AbstractCatalogRegistryModule<GoalExecutorType> implements AlternateCatalogRegistryModule<GoalExecutorType> {

    public static GoalExecutorTypeModule getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void registerDefaults() {
        this.register("normal");
        this.register("target");
    }

    private void register(String id) {
        CatalogKey combinedId = CatalogKey.minecraft(id);
        @SuppressWarnings("unchecked")
        final SpongeGoalExecutorType newType = new SpongeGoalExecutorType(combinedId, (Class<GoalExecutor<?>>) (Class<?>) GoalSelector.class);
        this.register(combinedId, newType);
    }

    GoalExecutorTypeModule() {
    }

    private static final class Holder {

        static final GoalExecutorTypeModule INSTANCE = new GoalExecutorTypeModule();
    }
}
