package org.spongepowered.common.data.manipulator.immutable.entity;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableExplosionRadiusData;
import org.spongepowered.api.data.manipulator.mutable.entity.ExplosionRadiusData;
import org.spongepowered.api.data.value.immutable.ImmutableOptionalValue;
import org.spongepowered.common.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.common.data.manipulator.mutable.entity.SpongeExplosionRadiusData;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeOptionalValue;

import java.util.Optional;

public class ImmutableSpongeExplosionRadiusData
        extends AbstractImmutableSingleData<Optional<Integer>, ImmutableExplosionRadiusData, ExplosionRadiusData>
        implements ImmutableExplosionRadiusData {

    private final ImmutableOptionalValue<Integer> value;

    protected ImmutableSpongeExplosionRadiusData(Optional<Integer> explosionRadius) {
        super(ImmutableExplosionRadiusData.class, explosionRadius, Keys.EXPLOSION_RADIUS);
        this.value = new ImmutableSpongeOptionalValue<>(Keys.EXPLOSION_RADIUS, explosionRadius);
    }

    @Override
    protected ImmutableOptionalValue<Integer> getValueGetter() {
        return explosionRadius();
    }

    @Override
    public ExplosionRadiusData asMutable() {
        return new SpongeExplosionRadiusData(getValue());
    }

    @Override
    public ImmutableOptionalValue<Integer> explosionRadius() {
        return this.value;
    }

    @Override
    public int compareTo(ImmutableExplosionRadiusData that) {
        return SpongeExplosionRadiusData.compare(this, that);
    }

}
