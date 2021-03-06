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
package org.spongepowered.common.data.manipulator.immutable.common;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.common.util.GetterFunction;

public abstract class AbstractImmutableSingleData<T, I extends ImmutableDataManipulator<I, M>, M extends DataManipulator<M, I>>
        extends AbstractImmutableData<I, M> {

    protected final Key<? extends BaseValue<T>> usedKey;
    protected final T value;

    public AbstractImmutableSingleData(Class<I> immutableClass, T value, Key<? extends BaseValue<T>> usedKey) {
        super(immutableClass);
        this.value = checkNotNull(value);
        this.usedKey = checkNotNull(usedKey);
        registerGetters();
    }

    protected abstract ImmutableValue<?> getValueGetter();

    public T getValue() {
        return this.value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public I copy() {
        return (I) this;
    }

    @Override
    public abstract M asMutable();

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer();
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(this.usedKey, new GetterFunction<Object>() {
            @Override
            public Object get() {
                return getValue();
            }
        });
        registerKeyValue(this.usedKey, new GetterFunction<ImmutableValue<?>>() {
            @Override
            public ImmutableValue<?> get() {
                return getValueGetter();
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> Optional<E> get(Key<? extends BaseValue<E>> key) {
        return checkNotNull(key).equals(this.usedKey) ? Optional.of((E) this.value) : Optional.<E>absent();
    }

    @Override
    public boolean supports(Key<?> key) {
        return checkNotNull(key) == this.usedKey;
    }

    @Override
    public boolean supports(BaseValue<?> baseValue) {
        return checkNotNull(baseValue.getKey()) == this.usedKey;
    }

    @Override
    public ImmutableSet<Key<?>> getKeys() {
        return ImmutableSet.<Key<?>>of(this.usedKey);
    }

}
