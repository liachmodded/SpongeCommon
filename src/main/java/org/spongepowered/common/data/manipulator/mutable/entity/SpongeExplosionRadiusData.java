package org.spongepowered.common.data.manipulator.mutable.entity;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableExplosionRadiusData;
import org.spongepowered.api.data.manipulator.mutable.entity.ExplosionRadiusData;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.data.value.mutable.OptionalValue;
import org.spongepowered.common.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.common.data.value.mutable.SpongeOptionalValue;

import java.util.Optional;

public class SpongeExplosionRadiusData extends AbstractSingleData<Optional<Integer>, ExplosionRadiusData, ImmutableExplosionRadiusData>
        implements ExplosionRadiusData {

    public SpongeExplosionRadiusData(Optional<Integer> explosionRadius) {
        super(ExplosionRadiusData.class, explosionRadius, Keys.EXPLOSION_RADIUS);
    }

    public SpongeExplosionRadiusData() {
        this(Optional.empty());
    }

    @Override
    protected OptionalValue<Integer> getValueGetter() {
        return explosionRadius();
    }

    @Override
    public ExplosionRadiusData copy() {
        return new SpongeExplosionRadiusData(getValue());
    }

    @Override
    public ImmutableExplosionRadiusData asImmutable() {
        return null;
    }

    @Override
    public int compareTo(ExplosionRadiusData that) {
        return compare(this, that);
    }

    @Override
    public OptionalValue<Integer> explosionRadius() {
        return new SpongeOptionalValue<>(Keys.EXPLOSION_RADIUS, getValue());
    }

    @SuppressWarnings("unchecked")
    public static int compare(ValueContainer dis, ValueContainer dat) {
        Optional<Integer> value = ((OptionalValue<Integer>) dis.get(Keys.EXPLOSION_RADIUS).get()).get();
        Optional<Integer> other = ((OptionalValue<Integer>) dat.get(Keys.EXPLOSION_RADIUS).get()).get();
        if (value.isPresent()) {
            if (other.isPresent()) {
                return value.get() - other.get();
            } else {
                return 1;
            }
        } else if (other.isPresent()) {
            return -1;
        }
        return 0;
    }

}
