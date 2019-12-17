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
import org.spongepowered.api.data.type.RabbitType;
import org.spongepowered.common.entity.SpongeRabbitType;
import org.spongepowered.common.registry.type.AbstractCatalogRegistryModule;

import java.util.Map;

public class RabbitTypeRegistryModule extends AbstractCatalogRegistryModule<RabbitType> {

    public static final Map<String, RabbitType> RABBIT_TYPES = Maps.newHashMap();
    public static final Map<Integer, RabbitType> RABBIT_IDMAP = Maps.newHashMap();
    // rabbit types
    public static final SpongeRabbitType BROWN_RABBIT = new SpongeRabbitType(0, "BROWN");
    public static final RabbitType WHITE_RABBIT = new SpongeRabbitType(1, "WHITE");
    public static final RabbitType BLACK_RABBIT = new SpongeRabbitType(2, "BLACK");
    public static final RabbitType BLACK_AND_WHITE_RABBIT = new SpongeRabbitType(3, "BLACK_AND_WHITE");
    public static final RabbitType GOLD_RABBIT = new SpongeRabbitType(4, "GOLD");
    public static final RabbitType SALT_AND_PEPPER_RABBIT = new SpongeRabbitType(5, "SALT_AND_PEPPER");
    public static final RabbitType KILLER_RABBIT = new SpongeRabbitType(99, "KILLER");

    @Override
    public void registerDefaults() {
        RabbitTypeRegistryModule.RABBIT_TYPES.put("brown", RabbitTypeRegistryModule.BROWN_RABBIT);
        RabbitTypeRegistryModule.RABBIT_TYPES.put("white", RabbitTypeRegistryModule.WHITE_RABBIT);
        RabbitTypeRegistryModule.RABBIT_TYPES.put("black", RabbitTypeRegistryModule.BLACK_RABBIT);
        RabbitTypeRegistryModule.RABBIT_TYPES.put("black_and_white", RabbitTypeRegistryModule.BLACK_AND_WHITE_RABBIT);
        RabbitTypeRegistryModule.RABBIT_TYPES.put("gold", RabbitTypeRegistryModule.GOLD_RABBIT);
        RabbitTypeRegistryModule.RABBIT_TYPES.put("salt_and_pepper", RabbitTypeRegistryModule.SALT_AND_PEPPER_RABBIT);
        RabbitTypeRegistryModule.RABBIT_TYPES.put("killer", RabbitTypeRegistryModule.KILLER_RABBIT);

        RabbitTypeRegistryModule.RABBIT_IDMAP.put(0, RabbitTypeRegistryModule.BROWN_RABBIT);
        RabbitTypeRegistryModule.RABBIT_IDMAP.put(1, RabbitTypeRegistryModule.WHITE_RABBIT);
        RabbitTypeRegistryModule.RABBIT_IDMAP.put(2, RabbitTypeRegistryModule.BLACK_RABBIT);
        RabbitTypeRegistryModule.RABBIT_IDMAP.put(3, RabbitTypeRegistryModule.BLACK_AND_WHITE_RABBIT);
        RabbitTypeRegistryModule.RABBIT_IDMAP.put(4, RabbitTypeRegistryModule.GOLD_RABBIT);
        RabbitTypeRegistryModule.RABBIT_IDMAP.put(5, RabbitTypeRegistryModule.SALT_AND_PEPPER_RABBIT);
        RabbitTypeRegistryModule.RABBIT_IDMAP.put(99, RabbitTypeRegistryModule.KILLER_RABBIT);
        for (Map.Entry<String, RabbitType> entry : RABBIT_TYPES.entrySet()) {
            this.register(CatalogKey.minecraft(entry.getKey()), entry.getValue());
        }
    }

}
