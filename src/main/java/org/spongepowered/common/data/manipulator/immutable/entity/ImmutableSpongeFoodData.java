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
package org.spongepowered.common.data.manipulator.immutable.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static org.spongepowered.common.data.util.ComparatorUtil.doubleComparator;
import static org.spongepowered.common.data.util.ComparatorUtil.intComparator;

import com.google.common.collect.ComparisonChain;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableFoodData;
import org.spongepowered.api.data.manipulator.mutable.entity.FoodData;
import org.spongepowered.api.data.value.immutable.ImmutableBoundedValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.common.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.common.data.manipulator.mutable.entity.SpongeFoodData;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeBoundedValue;
import org.spongepowered.common.util.GetterFunction;

public class ImmutableSpongeFoodData extends AbstractImmutableData<ImmutableFoodData, FoodData> implements ImmutableFoodData {

    private final int food;
    private final double exhaustion;
    private final double saturation;

    public ImmutableSpongeFoodData(int food, double exhaustion, double saturation) {
        super(ImmutableFoodData.class);
        checkArgument(food >= 0);
        checkArgument(exhaustion >= 0);
        checkArgument(saturation >= 0);
        this.food = food;
        this.exhaustion = exhaustion;
        this.saturation = saturation;
    }

    @Override
    public ImmutableBoundedValue<Integer> foodLevel() {
        return new ImmutableSpongeBoundedValue<Integer>(Keys.FOOD_LEVEL, this.food, 20, intComparator(), 0, 20);
    }

    @Override
    public ImmutableBoundedValue<Double> exhaustion() {
        return new ImmutableSpongeBoundedValue<Double>(Keys.EXHAUSTION, this.exhaustion, 0D, doubleComparator(), 0D, 20D);
    }

    @Override
    public ImmutableBoundedValue<Double> saturation() {
        return new ImmutableSpongeBoundedValue<Double>(Keys.SATURATION, this.saturation, 20D, doubleComparator(), 0D, 20D);
    }

    @Override
    public ImmutableFoodData copy() {
        return new ImmutableSpongeFoodData(this.food, this.exhaustion, this.saturation);
    }

    @Override
    public FoodData asMutable() {
        return new SpongeFoodData(this.food, this.exhaustion, this.saturation);
    }

    @Override
    public int compareTo(ImmutableFoodData o) {
        return ComparisonChain.start()
            .compare(o.foodLevel().get().intValue(), this.food)
            .compare(o.exhaustion().get().doubleValue(), this.exhaustion)
            .compare(o.saturation().get().doubleValue(), this.saturation)
            .result();
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer()
            .set(Keys.FOOD_LEVEL, this.food)
            .set(Keys.SATURATION, this.saturation)
            .set(Keys.EXHAUSTION, this.exhaustion);
    }

    public int getFood() {
        return this.food;
    }

    public double getExhaustion() {
        return this.exhaustion;
    }

    public double getSaturation() {
        return this.saturation;
    }

    private void registerStuff() {
        registerFieldGetter(Keys.FOOD_LEVEL, new GetterFunction<Object>() {
            @Override
            public Object get() {
                return getFood();
            }
        });
        registerKeyValue(Keys.FOOD_LEVEL, new GetterFunction<ImmutableValue<?>>() {
            @Override
            public ImmutableValue<?> get() {
                return foodLevel();
            }
        });

        registerFieldGetter(Keys.EXHAUSTION, new GetterFunction<Object>() {
            @Override
            public Object get() {
                return getExhaustion();
            }
        });
        registerKeyValue(Keys.EXHAUSTION, new GetterFunction<ImmutableValue<?>>() {
            @Override
            public ImmutableValue<?> get() {
                return exhaustion();
            }
        });

        registerFieldGetter(Keys.SATURATION, new GetterFunction<Object>() {
            @Override
            public Object get() {
                return getSaturation();
            }
        });
        registerKeyValue(Keys.SATURATION, new GetterFunction<ImmutableValue<?>>() {
            @Override
            public ImmutableValue<?> get() {
                return saturation();
            }
        });

    }
}
