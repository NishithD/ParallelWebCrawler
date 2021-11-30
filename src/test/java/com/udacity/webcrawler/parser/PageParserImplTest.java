/*
 * MIT License
 *
 * Copyright (c) 2021 Xi Chen
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/*The tests are modified according to the Q&A because I cannot run the tests in IntelliJ
 * Here is the link: https://knowledge.udacity.com/questions/549165
 * */

package com.udacity.webcrawler.parser;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.truth.Truth.assertThat;

public final class PageParserImplTest {
    private static final String DATA_DIR = System.getProperty("user.dir") +
            File.separator + "src"
            + File.separator + "test"
            + File.separator + "data";
    private final String testPage = Paths.get(DATA_DIR, "test-page.html").toUri().toString();

    @Test
    public void basicParsing() {
        PageParser.Result result = new PageParserImpl(testPage, Duration.ZERO, List.of()).parse();
        assertThat(result.getLinks())
                .containsExactly(Paths.get(DATA_DIR, "link-1.html").toUri().toString());
        assertThat(result.getWordCounts()).hasSize(9);
        assertThat(result.getWordCounts()).containsEntry("the", 2);
        assertThat(result.getWordCounts()).containsEntry("quick", 1);
        assertThat(result.getWordCounts()).containsEntry("brown", 1);
        assertThat(result.getWordCounts()).containsEntry("fox", 1);
        assertThat(result.getWordCounts()).containsEntry("jumped", 1);
        assertThat(result.getWordCounts()).containsEntry("over", 1);
        assertThat(result.getWordCounts()).containsEntry("lazy", 1);
        assertThat(result.getWordCounts()).containsEntry("dog", 1);
    }

    @Test
    public void parsingWithIgnoredWords() {
        PageParser.Result result =
                new PageParserImpl(testPage, Duration.ZERO, List.of(Pattern.compile("^...$"))).parse();
        assertThat(result.getLinks())
                .containsExactly(Paths.get(DATA_DIR, "link-1.html").toUri().toString());
        assertThat(result.getWordCounts()).hasSize(6);
        assertThat(result.getWordCounts()).containsEntry("quick", 1);
        assertThat(result.getWordCounts()).containsEntry("brown", 1);
        assertThat(result.getWordCounts()).containsEntry("jumped", 1);
        assertThat(result.getWordCounts()).containsEntry("over", 1);
        assertThat(result.getWordCounts()).containsEntry("lazy", 1);
    }
}