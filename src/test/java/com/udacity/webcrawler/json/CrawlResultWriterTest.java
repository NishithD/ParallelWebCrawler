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

import com.udacity.webcrawler.testing.CloseableStringWriter;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public final class CrawlResultWriterTest {
  @Test
  public void testBasicJsonFormatting() throws Exception {
    // We are using a LinkedHashMap because the iteration order of the map matters.
    Map<String, Integer> counts = new LinkedHashMap<>();
    counts.put("foo", 12);
    counts.put("bar", 1);
    counts.put("foobar", 98);
    CrawlResult result =
        new CrawlResult.Builder()
            .setUrlsVisited(17)
            .setWordCounts(counts)
            .build();

    CrawlResultWriter resultWriter = new CrawlResultWriter(result);
    CloseableStringWriter stringWriter = new CloseableStringWriter();
    resultWriter.write(stringWriter);
    assertWithMessage("Streams should usually be closed in the same scope where they were created")
        .that(stringWriter.isClosed())
        .isFalse();
    String written = stringWriter.toString();

    // The purpose of all the wildcard matchers (".*") is to make sure we allow the JSON output to
    // contain extra whitespace where it does not matter.
    Pattern expected =
        Pattern.compile(".*\\{" +
            ".*\"wordCounts\".*:.*\\{" +
            ".*\"foo\".*:12.*," +
            ".*\"bar\".*:.*1," +
            ".*\"foobar\".*:.*98" +
            ".*}.*,.*" +
            ".*\"urlsVisited\".*:.*17" +
            ".*}.*", Pattern.DOTALL);

    assertThat(written).matches(expected);
  }
}
