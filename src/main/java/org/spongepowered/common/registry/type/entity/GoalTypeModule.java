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
import org.spongepowered.api.registry.AlternateCatalogRegistryModule;
import org.spongepowered.api.registry.util.RegisterCatalog;
import org.spongepowered.common.entity.ai.goal.SpongeGoalType;
import org.spongepowered.common.registry.type.AbstractCatalogRegistryModule;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class GoalTypeModule extends AbstractCatalogRegistryModule<GoalType> implements AlternateCatalogRegistryModule<GoalType> {

    public static GoalTypeModule getInstance() {
        return Holder.INSTANCE;
    }

    @RegisterCatalog(GoalTypes.class)
    private final Map<CatalogKey, GoalType> goalTypes = new ConcurrentHashMap<>();

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
        this.register("wander", RandomWalkingGoal.class);
        this.register("avoid_entity", AvoidLivingGoal.class);
        this.register("run_around_like_crazy", RunAroundLikeCrazyGoal.class);
        this.register("swimming", SwimGoal.class);
        this.register("watch_closest", LookAtGoal.class);
        this.register("find_nearest_attackable_target", FindNearestAttackableTargetGoal.class);
        this.register("attack_living", AttackLivingGoal.class);
    }

    private void register(String value, Class<? extends Goal<? extends Agent>> aiClass) {
        CatalogKey combinedId = CatalogKey.minecraft(value);
        final SpongeGoalType newType = new SpongeGoalType(combinedId, aiClass);
        this.goalTypes.put(combinedId, newType);
    }

    GoalTypeModule() {}

    private static final class Holder {
        static final GoalTypeModule INSTANCE = new GoalTypeModule();
    }
}
