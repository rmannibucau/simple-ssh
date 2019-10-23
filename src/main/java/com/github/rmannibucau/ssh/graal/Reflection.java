/**
 * Copyright (C) 2006-2019 Talend Inc. - www.talend.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License",
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rmannibucau.ssh.graal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Objects;
import java.util.concurrent.RunnableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

import com.jcraft.jsch.JSch;
import com.oracle.svm.core.annotate.AutomaticFeature;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

@AutomaticFeature
public class Reflection implements Feature {
    @Override
    public void beforeAnalysis(final BeforeAnalysisAccess access) {
        try { // JSch hosts config used by reflection in this config field, just enable it to be used at runtime
            final Field config = JSch.class.getDeclaredField("config");
            config.setAccessible(true);
            final Collection<String> values = Hashtable.class.cast(config.get(null)).values();

            final ClassLoader loader = Thread.currentThread().getContextClassLoader();
            final Function<String, Class<?>> load = name -> {
                try {
                    return loader.loadClass(name);
                } catch (final ClassNotFoundException e) {
                    return null;
                }
            };

            final Class<?>[] classes = values.stream()
                    .filter(it -> !"com.jcraft.jsch.jcraft.Compression".equalsIgnoreCase(it)) // requires other libs
                    .map(load)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toArray(Class[]::new);
            RuntimeReflection.register(classes);

            final Constructor<?>[] constructors = Stream.of(classes)
                    .flatMap(it -> Stream.of(it.getDeclaredConstructors()))
                    .toArray(Constructor[]::new);
            RuntimeReflection.register(constructors);
        } catch (final Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
