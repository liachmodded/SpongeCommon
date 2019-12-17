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

import com.google.common.collect.Maps;
import org.spongepowered.api.CatalogKey;
import org.spongepowered.api.data.type.ParrotType;
import org.spongepowered.common.entity.SpongeParrotVariant;
import org.spongepowered.common.registry.type.AbstractCatalogRegistryModule;

import java.util.Map;

public class ParrotVariantRegistryModule extends AbstractCatalogRegistryModule<ParrotType> {

    public static final Map<String, ParrotType> PARROT_VARIANTS = Maps.newHashMap();
    public static final Map<Integer, ParrotType> PARROT_VARIANT_IDMAP = Maps.newHashMap();

    public static final SpongeParrotVariant RED_PARROT = new SpongeParrotVariant(0, "RED");
    public static final SpongeParrotVariant BLUE_PARROT = new SpongeParrotVariant(1, "BLUE");
    public static final SpongeParrotVariant GREEN_PARROT = new SpongeParrotVariant(2, "GREEN");
    public static final SpongeParrotVariant CYAN_PARROT = new SpongeParrotVariant(3, "CYAN");
    public static final SpongeParrotVariant GRAY_PARROT = new SpongeParrotVariant(4, "GRAY");

    @Override
    public void registerDefaults() {
        for (Map.Entry<String, ParrotType> entry : PARROT_VARIANTS.entrySet()) {
            this.register(CatalogKey.minecraft(entry.getKey()), entry.getValue());
        }
    }

    static {
        ParrotVariantRegistryModule.PARROT_VARIANTS.put("red", ParrotVariantRegistryModule.RED_PARROT);
        ParrotVariantRegistryModule.PARROT_VARIANTS.put("blue", ParrotVariantRegistryModule.BLUE_PARROT);
        ParrotVariantRegistryModule.PARROT_VARIANTS.put("green", ParrotVariantRegistryModule.GREEN_PARROT);
        ParrotVariantRegistryModule.PARROT_VARIANTS.put("cyan", ParrotVariantRegistryModule.CYAN_PARROT);
        ParrotVariantRegistryModule.PARROT_VARIANTS.put("gray", ParrotVariantRegistryModule.GRAY_PARROT);

        ParrotVariantRegistryModule.PARROT_VARIANT_IDMAP.put(0, ParrotVariantRegistryModule.RED_PARROT);
        ParrotVariantRegistryModule.PARROT_VARIANT_IDMAP.put(1, ParrotVariantRegistryModule.BLUE_PARROT);
        ParrotVariantRegistryModule.PARROT_VARIANT_IDMAP.put(2, ParrotVariantRegistryModule.GREEN_PARROT);
        ParrotVariantRegistryModule.PARROT_VARIANT_IDMAP.put(3, ParrotVariantRegistryModule.CYAN_PARROT);
        ParrotVariantRegistryModule.PARROT_VARIANT_IDMAP.put(4, ParrotVariantRegistryModule.GRAY_PARROT);

    }

}
