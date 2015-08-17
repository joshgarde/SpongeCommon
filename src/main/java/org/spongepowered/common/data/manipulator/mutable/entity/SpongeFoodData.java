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
package org.spongepowered.common.data.manipulator.mutable.entity;

import static org.spongepowered.common.data.util.ComparatorUtil.doubleComparator;
import static org.spongepowered.common.data.util.ComparatorUtil.intComparator;

import com.google.common.collect.ComparisonChain;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableFoodData;
import org.spongepowered.api.data.manipulator.mutable.entity.FoodData;
import org.spongepowered.api.data.value.mutable.MutableBoundedValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.common.data.manipulator.immutable.entity.ImmutableSpongeFoodData;
import org.spongepowered.common.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.common.data.value.mutable.SpongeBoundedValue;
import org.spongepowered.common.util.GetterFunction;
import org.spongepowered.common.util.SetterFunction;

public class SpongeFoodData extends AbstractData<FoodData, ImmutableFoodData> implements FoodData {

    private int food;
    private double exhaustion;
    private double saturation;

    public SpongeFoodData(int food, double exhaustion, double saturation) {
        super(FoodData.class);
        this.food = food;
        this.exhaustion = exhaustion;
        this.saturation = saturation;
    }

    @Override
    public MutableBoundedValue<Integer> foodLevel() {
        return new SpongeBoundedValue<Integer>(Keys.FOOD_LEVEL, 20, intComparator(), 0, 20, this.food);
    }

    @Override
    public MutableBoundedValue<Double> exhaustion() {
        return new SpongeBoundedValue<Double>(Keys.EXHAUSTION, 0D, doubleComparator(), 0D, 20D, this.exhaustion);
    }

    @Override
    public MutableBoundedValue<Double> saturation() {
        return new SpongeBoundedValue<Double>(Keys.SATURATION, 20D, doubleComparator(), 0D, 20D, this.saturation);
    }

    @Override
    public FoodData copy() {
        return new SpongeFoodData(this.food, this.exhaustion, this.saturation);
    }

    @Override
    public ImmutableFoodData asImmutable() {
        return new ImmutableSpongeFoodData(this.food, this.exhaustion, this.saturation);
    }

    @Override
    public int compareTo(FoodData o) {
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

    public void setFood(int food) {
        this.food = food;
    }

    public void setExhaustion(double exhaustion) {
        this.exhaustion = exhaustion;
    }

    public void setSaturation(double saturation) {
        this.saturation = saturation;
    }

    private void registerStuff() {
        registerFieldGetter(Keys.FOOD_LEVEL, new GetterFunction<Object>() {
            @Override
            public Object get() {
                return getFood();
            }
        });
        registerFieldSetter(Keys.FOOD_LEVEL, new SetterFunction<Object>() {
            @Override
            public void set(Object value) {
                setFood((Integer) value);
            }
        });
        registerKeyValue(Keys.FOOD_LEVEL, new GetterFunction<Value<?>>() {
            @Override
            public Value<?> get() {
                return foodLevel();
            }
        });

        registerFieldGetter(Keys.EXHAUSTION, new GetterFunction<Object>() {
            @Override
            public Object get() {
                return getExhaustion();
            }
        });
        registerFieldSetter(Keys.EXHAUSTION, new SetterFunction<Object>() {
            @Override
            public void set(Object value) {
                setExhaustion((Double) value);
            }
        });
        registerKeyValue(Keys.EXHAUSTION, new GetterFunction<Value<?>>() {
            @Override
            public Value<?> get() {
                return exhaustion();
            }
        });

        registerFieldGetter(Keys.SATURATION, new GetterFunction<Object>() {
            @Override
            public Object get() {
                return getSaturation();
            }
        });
        registerFieldSetter(Keys.SATURATION, new SetterFunction<Object>() {
            @Override
            public void set(Object value) {
                setSaturation((Double) value);
            }
        });
        registerKeyValue(Keys.SATURATION, new GetterFunction<Value<?>>() {
            @Override
            public Value<?> get() {
                return saturation();
            }
        });

    }
}
