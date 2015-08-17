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
package org.spongepowered.common.data.processor.value.entity;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.spongepowered.common.data.util.ComparatorUtil.doubleComparator;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.api.data.DataTransactionBuilder;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.data.value.immutable.ImmutableBoundedValue;
import org.spongepowered.api.data.value.mutable.MutableBoundedValue;
import org.spongepowered.common.data.ValueProcessor;
import org.spongepowered.common.data.processor.value.AbstractSpongeProcessor;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeBoundedValue;
import org.spongepowered.common.data.value.mutable.SpongeBoundedValue;
import org.spongepowered.common.data.value.mutable.SpongeValue;

public class HealthValueProcessor extends AbstractSpongeProcessor<Double, MutableBoundedValue<Double>> {

    public HealthValueProcessor() {
        super(Keys.HEALTH);
    }

    @Override
    public Optional<Double> getValueFromContainer(ValueContainer<?> container) {
        if (container instanceof EntityLivingBase) {
            return Optional.of((double) ((EntityLivingBase) container).getHealth());
        }
        return Optional.absent();
    }

    @Override
    public Optional<MutableBoundedValue<Double>> getApiValueFromContainer(ValueContainer<?> container) {
        if (container instanceof EntityLivingBase) {
            final double health = ((EntityLivingBase) container).getHealth();
            final double maxHealth = ((EntityLivingBase) container).getMaxHealth();
            return Optional.<MutableBoundedValue<Double>>of(new SpongeBoundedValue<Double>(Keys.HEALTH, maxHealth, doubleComparator(),
                                                                                           1D, maxHealth, health));
        }
        return Optional.absent();
    }

    @Override
    public boolean supports(ValueContainer<?> container) {
        return container instanceof EntityLivingBase;
    }

    @Override
    public DataTransactionResult transform(ValueContainer<?> container, Function<Double, Double> function) {
        if (container instanceof EntityLivingBase) {
            final double oldMaxHealth = getValueFromContainer(container).get();
            final double newMaxHealth = checkNotNull(function.apply(oldMaxHealth));
            return offerToStore(container, newMaxHealth);
        } else {
            return DataTransactionBuilder.failNoData();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public DataTransactionResult offerToStore(ValueContainer<?> container, BaseValue<?> value) {
        final BaseValue<Double> actualValue = (BaseValue<Double>) value;
        final ImmutableBoundedValue<Double> proposedValue = new ImmutableSpongeBoundedValue<Double>(Keys.HEALTH, actualValue.get(), 20D,
                                                                                                    doubleComparator(), 1D,
                                                                                                    (double) Float.MAX_VALUE);
        if (container instanceof EntityLivingBase) {
            final DataTransactionBuilder builder = DataTransactionBuilder.builder();
            final double maxHealth = ((EntityLivingBase) container).getMaxHealth();
            final ImmutableBoundedValue<Double> newHealthValue = new ImmutableSpongeBoundedValue<Double>(Keys.HEALTH, actualValue.get(), maxHealth,
                                                                                                         doubleComparator(), 0D, maxHealth);
            final ImmutableBoundedValue<Double> oldHealthValue = getApiValueFromContainer(container).get().asImmutable();
            if (actualValue.get() > maxHealth) {
                return DataTransactionBuilder.errorResult(newHealthValue);
            }
            try {
                ((EntityLivingBase) container).setHealth(actualValue.get().floatValue());
            } catch (Exception e) {
                return DataTransactionBuilder.errorResult(newHealthValue);
            }
            return builder.success(newHealthValue).replace(oldHealthValue).result(DataTransactionResult.Type.SUCCESS).build();
        }
        return DataTransactionBuilder.failResult(proposedValue);
    }

    @Override
    public DataTransactionResult offerToStore(ValueContainer<?> container, Double value) {
        return offerToStore(container, new SpongeValue<Double>(Keys.HEALTH, value, value));
    }

    @Override
    public DataTransactionResult removeFrom(ValueContainer<?> container) {
        return DataTransactionBuilder.failNoData();
    }
}
