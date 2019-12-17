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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.spongepowered.api.CatalogKey;
import org.spongepowered.api.entity.ai.GoalExecutor;
import org.spongepowered.api.entity.ai.GoalExecutorType;
import org.spongepowered.api.entity.ai.GoalExecutorTypes;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.registry.AlternateCatalogRegistryModule;
import org.spongepowered.api.registry.util.RegisterCatalog;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.entity.ai.SpongeGoalExecutorType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import net.minecraft.entity.ai.goal.GoalSelector;

public class GoalExecutorTypeModule implements AlternateCatalogRegistryModule<GoalExecutorType> {

    public static GoalExecutorTypeModule getInstance() {
        return Holder.INSTANCE;
    }

    @RegisterCatalog(GoalExecutorTypes.class)
    private final Map<CatalogKey, GoalExecutorType> goalExecutorTypes = new HashMap<>();

    @Override
    public Map<CatalogKey, GoalExecutorType> provideCatalogMap() {
        return new HashMap<>(this.goalExecutorTypes);
    }

    @Override
    public Optional<GoalExecutorType> get(CatalogKey id) {
        checkNotNull(id);
        return Optional.ofNullable(this.goalExecutorTypes.get(id));
    }

    @Override
    public Collection<GoalExecutorType> getAll() {
        return ImmutableList.copyOf(this.goalExecutorTypes.values());
    }

    @Override
    public void registerDefaults() {
        // todo bork
        this.createGoalType(CatalogKey.minecraft("normal"), "Normal");
        this.createGoalType(CatalogKey.minecraft("target"), "Target");
    }

    private GoalExecutorType createGoalType(CatalogKey combinedId) {
        @SuppressWarnings("unchecked")
        final SpongeGoalExecutorType newType = new SpongeGoalExecutorType(combinedId, (Class<GoalExecutor<?>>) (Class<?>) GoalSelector.class);
        this.goalExecutorTypes.put(combinedId, newType);
        return newType;
    }

    public GoalExecutorType createGoalType(Object plugin, String id) {
        final Optional<PluginContainer> optPluginContainer = SpongeImpl.getGame().getPluginManager().fromInstance(plugin);
        Preconditions.checkArgument(optPluginContainer.isPresent());
        final PluginContainer pluginContainer = optPluginContainer.get();
        final CatalogKey combinedId = CatalogKey.builder().namespace(pluginContainer).value(id).build();

        return this.createGoalType(combinedId);
    }

    GoalExecutorTypeModule() {
    }

    private static final class Holder {

        static final GoalExecutorTypeModule INSTANCE = new GoalExecutorTypeModule();
    }
}
