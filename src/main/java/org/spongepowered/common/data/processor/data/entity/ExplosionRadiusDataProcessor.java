package org.spongepowered.common.data.processor.data.entity;

import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableExplosionRadiusData;
import org.spongepowered.api.data.manipulator.mutable.entity.ExplosionRadiusData;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.OptionalValue;
import org.spongepowered.api.entity.explosive.Explosive;
import org.spongepowered.common.data.manipulator.mutable.entity.SpongeExplosionRadiusData;
import org.spongepowered.common.data.processor.common.AbstractSingleDataSingleTargetProcessor;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeOptionalValue;
import org.spongepowered.common.data.value.mutable.SpongeOptionalValue;
import org.spongepowered.common.interfaces.entity.explosive.IMixinExplosive;

import java.util.Optional;

public class ExplosionRadiusDataProcessor extends AbstractSingleDataSingleTargetProcessor<Explosive, Optional<Integer>, OptionalValue<Integer>,
        ExplosionRadiusData, ImmutableExplosionRadiusData> {

    public ExplosionRadiusDataProcessor() {
        super(Keys.EXPLOSION_RADIUS, Explosive.class);
    }

    @Override
    protected boolean set(Explosive explosive, Optional<Integer> value) {
        ((IMixinExplosive) explosive).setExplosionRadius(value);
        return true;
    }

    @Override
    protected Optional<Optional<Integer>> getVal(Explosive explosive) {
        return Optional.of(((IMixinExplosive) explosive).getExplosionRadius());
    }

    @Override
    protected ImmutableValue<Optional<Integer>> constructImmutableValue(Optional<Integer> value) {
        return new ImmutableSpongeOptionalValue<>(Keys.EXPLOSION_RADIUS, value);
    }

    @Override
    protected OptionalValue<Integer> constructValue(Optional<Integer> actualValue) {
        return new SpongeOptionalValue<>(Keys.EXPLOSION_RADIUS, actualValue);
    }

    @Override
    protected ExplosionRadiusData createManipulator() {
        return new SpongeExplosionRadiusData();
    }

    @Override
    public DataTransactionResult removeFrom(ValueContainer<?> container) {
        return DataTransactionResult.failNoData();
    }

}
