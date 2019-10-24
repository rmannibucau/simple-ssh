package com.github.rmannibucau.ssh.graal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Objects;
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
