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
import java.nio.file.Path;

/**
 * A utility that wraps an object that should be performance profiled.
 *
 * <p>The profiler aggregates information about profiled method calls, and how long they took. The
 * aggregate information can then be written to a file with {@link #writeData(Writer) writeData}.
 */
public interface Profiler {

    /**
     * Wraps the given delegate to have its methods profiled.
     *
     * @param klass    the class object representing the interface of the delegate.
     * @param delegate the object that should be profiled.
     * @param <T>      type of the delegate object, which must be an interface type. The interface
     *                 must have at least one of its methods annotated with the {@link Profiled}
     *                 annotation.
     * @return A wrapped version of the delegate that
     * @throws IllegalArgumentException if the given delegate does not have any methods annotated with
     *                                  the {@link Profiled} annotation.
     */
    <T> T wrap(Class<T> klass, T delegate);

    /**
     * Formats the profile data as a string and writes it to the given {@link Path}.
     *
     * <p>If a file already exists at the path, the existing file should not be deleted; new data
     * should be appended to it.
     *
     * @param path the destination where the formatted data should be written.
     * @throws IOException if there was a problem writing the data to file.
     */
    void writeData(Path path) throws IOException;

    /**
     * Formats the profile data as a string and writes it to the given {@link Writer}.
     *
     * @param writer the destination where the formatted data should be written.
     * @throws IOException if there was a problem writing the data.
     */
    void writeData(Writer writer) throws IOException;
}
