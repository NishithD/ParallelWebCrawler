/*
 * PROJECT LICENSE
 *
 * This project was submitted by Xi Chen as part of the Nanodegree At Udacity.
 *
 * As part of Udacity Honor code, your submissions must be your own work, hence
 * submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account.
 *
 * Me, the author of the project, allow you to check the code as a reference, but if
 * you submit it, it's your own responsibility if you get expelled.
 *
 * Copyright (c) 2021 Xi Chen
 *
 * Besides the above notice, the following license applies and this license notice
 * must be included in all works derived from this project.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.udacity.webcrawler.profiler;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Helper class that records method performance data from the method interceptor.
 */
final class ProfilingState {
    private final Map<String, Duration> data = new ConcurrentHashMap<>();

    /**
     * Formats the given method call for writing to a text file.
     *
     * @param callingClass the Java class of the object whose method was invoked.
     * @param method       the Java method that was invoked.
     * @return a string representation of the method call.
     */
    private static String formatMethodCall(Class<?> callingClass, Method method) {
        return String.format("%s#%s", callingClass.getName(), method.getName());
    }

    /**
     * Formats the given {@link Duration} for writing to a text file.
     */
    private static String formatDuration(Duration duration) {
        return String.format(
                "%sm %ss %sms", duration.toMinutes(), duration.toSecondsPart(), duration.toMillisPart());
    }

    /**
     * Records the given method invocation data.
     *
     * @param callingClass the Java class of the object that called the method.
     * @param method       the method that was called.
     * @param elapsed      the amount of time that passed while the method was called.
     */
    void record(Class<?> callingClass, Method method, Duration elapsed) {
        Objects.requireNonNull(callingClass);
        Objects.requireNonNull(method);
        Objects.requireNonNull(elapsed);
        if (elapsed.isNegative()) {
            throw new IllegalArgumentException("negative elapsed time");
        }
        String key = formatMethodCall(callingClass, method);
        data.compute(key, (k, v) -> (v == null) ? elapsed : v.plus(elapsed));
    }

    /**
     * Writes the method invocation data to the given {@link Writer}.
     *
     * <p>Recorded data is aggregated across calls to the same method. For example, suppose
     * {@link #record(Class, Method, Duration) record} is called three times for the same method
     * {@code M()}, with each invocation taking 1 second. The total {@link Duration} reported by
     * this {@code write()} method for {@code M()} should be 3 seconds.
     */
    void write(Writer writer) throws IOException {
        List<String> entries =
                data.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(e -> e.getKey() + " took " + formatDuration(e.getValue()) + System.lineSeparator())
                        .collect(Collectors.toList());

        // We have to use a for-loop here instead of a Stream API method because Writer#write() can
        // throw an IOException, and lambdas are not allowed to throw checked exceptions.
        for (String entry : entries) {
            writer.write(entry);
        }
    }
}
