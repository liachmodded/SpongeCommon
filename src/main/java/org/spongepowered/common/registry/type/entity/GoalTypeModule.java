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
import org.spongepowered.api.entity.ai.goal.Goal;
import org.spongepowered.api.entity.ai.goal.GoalType;
import org.spongepowered.api.entity.ai.goal.GoalTypes;
import org.spongepowered.api.entity.ai.goal.builtin.SwimGoal;
import org.spongepowered.api.entity.ai.goal.builtin.creature.AttackLivingGoal;
import org.spongepowered.api.entity.ai.goal.builtin.creature.AvoidLivingGoal;
import org.spongepowered.api.entity.ai.goal.builtin.creature.RandomWalkingGoal;
import org.spongepowered.api.entity.ai.goal.builtin.LookAtGoal;
import org.spongepowered.api.entity.ai.goal.builtin.creature.horse.RunAroundLikeCrazyGoal;
import org.spongepowered.api.entity.ai.goal.builtin.creature.target.FindNearestAttackableTargetGoal;
import org.spongepowered.api.entity.living.Agent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.registry.AlternateCatalogRegistryModule;
import org.spongepowered.api.registry.util.RegisterCatalog;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.entity.ai.goal.SpongeGoalType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class GoalTypeModule implements AlternateCatalogRegistryModule<GoalType> {

    public static GoalTypeModule getInstance() {
        return Holder.INSTANCE;
    }

    @RegisterCatalog(GoalTypes.class)
    private final Map<CatalogKey, GoalType> goalTypes = new ConcurrentHashMap<>();

    @Override
    public Map<CatalogKey, GoalType> provideCatalogMap() {
        return new HashMap<>(this.goalTypes);
    }

    @Override
    public Optional<GoalType> get(CatalogKey key) {
        checkNotNull(key);
        return Optional.ofNullable(this.goalTypes.get(key));
    }

    @Override
    public Collection<GoalType> getAll() {
        return ImmutableList.copyOf(this.goalTypes.values());
    }

    public Optional<GoalType> getByGoalClass(Class<?> clazz) {
        for (GoalType type : this.goalTypes.values()) {
            if (type.getGoalClass().isAssignableFrom(clazz)) {
                return Optional.of(type);
            }
        }

        return Optional.empty();
    }

    @Override
    public void registerDefaults() {
        // todo bork
        this.createGoalType("minecraft:wander", "Wander", RandomWalkingGoal.class);
        this.createGoalType("minecraft:avoid_entity", "Avoid Entity", AvoidLivingGoal.class);
        this.createGoalType("minecraft:run_around_like_crazy", "Run Around Like Crazy", RunAroundLikeCrazyGoal.class);
        this.createGoalType("minecraft:swimming", "Swimming", SwimGoal.class);
        this.createGoalType("minecraft:watch_closest", "Watch Closest", LookAtGoal.class);
        this.createGoalType("minecraft:find_nearest_attackable_target", "Find Nearest Attackable Target", FindNearestAttackableTargetGoal.class);
        this.createGoalType("minecraft:attack_living", "Attack Living", AttackLivingGoal.class);
    }

    private GoalType createGoalType(CatalogKey combinedId, Class<? extends Goal<? extends Agent>> aiClass) {
        final SpongeGoalType newType = new SpongeGoalType(combinedId, aiClass);
        this.goalTypes.put(combinedId, newType);
        return newType;
    }

    public GoalType createGoalType(Object plugin, String id, Class<? extends Goal<? extends Agent>> aiClass) {
        final Optional<PluginContainer> optPluginContainer = SpongeImpl.getGame().getPluginManager().fromInstance(plugin);
        Preconditions.checkArgument(optPluginContainer.isPresent());
        final PluginContainer pluginContainer = optPluginContainer.get();
        final CatalogKey combinedId = CatalogKey.builder().namespace(pluginContainer).value(id).build();

        return this.createGoalType(combinedId, aiClass);
    }

    GoalTypeModule() {}

    private static final class Holder {
        static final GoalTypeModule INSTANCE = new GoalTypeModule();
    }
}
