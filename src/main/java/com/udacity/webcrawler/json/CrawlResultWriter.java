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

package com.udacity.webcrawler.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * Utility class to write a {@link CrawlResult} to file.
 */
public final class CrawlResultWriter {
    private final CrawlResult result;

    /**
     * Creates a new {@link CrawlResultWriter} that will write the given {@link CrawlResult}.
     */
    public CrawlResultWriter(CrawlResult result) {
        this.result = Objects.requireNonNull(result);
    }

    /**
     * Formats the {@link CrawlResult} as JSON and writes it to the given {@link Path}.
     *
     * <p>If a file already exists at the path, the existing file should not be deleted; new data
     * should be appended to it.
     *
     * @param path the file path where the crawl result data should be written.
     */
    public void write(Path path) {
        // This is here to get rid of the unused variable warning.
        Objects.requireNonNull(path);
        try (Writer bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, CREATE, APPEND)) {
            write(bufferedWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Formats the {@link CrawlResult} as JSON and writes it to the given {@link Writer}.
     *
     * @param writer the destination where the crawl result data should be written.
     */
    public void write(Writer writer) {
        // This is here to get rid of the unused variable warning.
        Objects.requireNonNull(writer);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        try {
            objectMapper.writeValue(writer, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
