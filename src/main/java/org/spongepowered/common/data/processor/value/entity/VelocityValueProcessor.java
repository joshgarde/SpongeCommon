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

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import net.minecraft.entity.Entity;
import org.spongepowered.api.data.DataTransactionBuilder;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.common.data.ValueProcessor;
import org.spongepowered.common.data.processor.value.AbstractSpongeProcessor;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeValue;
import org.spongepowered.common.data.value.mutable.SpongeValue;

public class VelocityValueProcessor extends AbstractSpongeProcessor<Vector3d, Value<Vector3d>> {

    public VelocityValueProcessor() {
        super(Keys.VELOCITY);
    }

    @Override
    public Optional<Vector3d> getValueFromContainer(ValueContainer<?> container) {
        if (container instanceof Entity) {
            return Optional.of(new Vector3d(((Entity) container).motionX, ((Entity) container).motionY, ((Entity) container).motionZ));
        }
        return Optional.absent();
    }

    @Override
    public Optional<Value<Vector3d>> getApiValueFromContainer(ValueContainer<?> container) {
        if (container instanceof Entity) {
            final double x = ((Entity) container).motionX;
            final double y = ((Entity) container).motionY;
            final double z = ((Entity) container).motionZ;
            return Optional.<Value<Vector3d>>of(new SpongeValue<Vector3d>(Keys.VELOCITY, Vector3d.ZERO, new Vector3d(x, y, z)));
        }
        return Optional.absent();
    }

    @Override
    public boolean supports(ValueContainer<?> container) {
        return container instanceof Entity;
    }

    @Override
    public DataTransactionResult transform(ValueContainer<?> container, Function<Vector3d, Vector3d> function) {
        if (container instanceof Entity) {
            final Vector3d old = getValueFromContainer(container).get();
            final ImmutableValue<Vector3d> oldValue = new ImmutableSpongeValue<Vector3d>(Keys.VELOCITY, Vector3d.ZERO, old);
            final Vector3d newVec = checkNotNull(checkNotNull(function, "function").apply(old), "The function returned a null value!");
            final ImmutableValue<Vector3d> newVal = new ImmutableSpongeValue<Vector3d>(Keys.VELOCITY, Vector3d.ZERO, newVec);
            try {
                ((Entity) container).motionX = newVec.getX();
                ((Entity) container).motionY = newVec.getY();
                ((Entity) container).motionZ = newVec.getZ();
            } catch (Exception e) {
                return DataTransactionBuilder.errorResult(newVal);
            }
            return DataTransactionBuilder.successReplaceResult(newVal, oldValue);
        }
        return DataTransactionBuilder.failNoData();
    }

    @Override
    public DataTransactionResult offerToStore(ValueContainer<?> container, BaseValue<?> value) {
        return offerToStore(container, ((Vector3d) value.get()));
    }

    @Override
    public DataTransactionResult offerToStore(ValueContainer<?> container, Vector3d value) {
        final ImmutableValue<Vector3d> newValue = new ImmutableSpongeValue<Vector3d>(Keys.VELOCITY, Vector3d.ZERO, value);
        if (container instanceof Entity) {
            final Vector3d old = getValueFromContainer(container).get();
            final ImmutableValue<Vector3d> oldValue = new ImmutableSpongeValue<Vector3d>(Keys.VELOCITY, Vector3d.ZERO, old);
            try {
                ((Entity) container).motionX = value.getX();
                ((Entity) container).motionY = value.getY();
                ((Entity) container).motionZ = value.getZ();
                return DataTransactionBuilder.successReplaceResult(newValue, oldValue);
            } catch (Exception e) {
                DataTransactionBuilder.errorResult(newValue);
            }
        }
        return DataTransactionBuilder.failResult(newValue);
    }

    @Override
    public DataTransactionResult removeFrom(ValueContainer<?> container) {
        return DataTransactionBuilder.failNoData();
    }
}
