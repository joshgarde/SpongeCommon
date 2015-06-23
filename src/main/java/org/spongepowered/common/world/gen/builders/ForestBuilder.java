/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
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
package org.spongepowered.common.world.gen.builders;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import org.spongepowered.api.util.VariableAmount;
import org.spongepowered.api.util.weighted.WeightedObject;
import org.spongepowered.api.world.gen.populator.Forest;
import org.spongepowered.api.world.gen.populator.Forest.Builder;
import org.spongepowered.api.world.gen.type.BiomeTreeType;
import org.spongepowered.common.world.gen.populators.ForestPopulator;

import java.util.Collection;
import java.util.List;

public class ForestBuilder implements Forest.Builder {
    
    private VariableAmount count;
    private List<WeightedObject<BiomeTreeType>> types;
    
    public ForestBuilder() {
        reset();
    }

    @Override
    public Builder perChunk(VariableAmount count) {
        this.count = count;
        return this;
    }

    @Override
    public Builder types(WeightedObject<BiomeTreeType>... types) {
        checkNotNull(types, "types");
        this.types.clear();
        for (WeightedObject<BiomeTreeType> type : types) {
            if (type != null) {
                this.types.add(type);
            }
        }
        return this;
    }

    @Override
    public Builder types(Collection<WeightedObject<BiomeTreeType>> types) {
        checkNotNull(types, "types");
        this.types.clear();
        for (WeightedObject<BiomeTreeType> type : types) {
            if (type != null) {
                this.types.add(type);
            }
        }
        return this;
    }

    @Override
    public Builder reset() {
        this.types = Lists.newArrayList();
        this.count = VariableAmount.fixed(5);
        return this;
    }

    @Override
    public Forest build() throws IllegalStateException {
        ForestPopulator pop = new ForestPopulator();
        pop.setTreesPerChunk(this.count);
        pop.getType().clear();
        pop.getType().addAll(this.types);
        return null;
    }

}