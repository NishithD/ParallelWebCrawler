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

/*The tests are modified according to the Q&A because I cannot run the tests in IntelliJ
 * Here is the link: https://knowledge.udacity.com/questions/552176
 * */

package com.udacity.webcrawler;

import com.google.inject.Guice;
import com.udacity.webcrawler.json.CrawlResult;
import com.udacity.webcrawler.json.CrawlerConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public final class WebCrawlerTest {
    private static final String DATA_DIR = System.getProperty("user.dir") +
            File.separator + "src"
            + File.separator + "test"
            + File.separator + "data";
    @Inject
    private WebCrawler crawler;

    static Stream<Class<?>> provideTestParameters() {
        List<Class<?>> classes = new ArrayList<>();
        classes.add(SequentialWebCrawler.class);
        return classes.stream();
    }

    @Test
    public void testOverrideToSequential() {
        CrawlerConfiguration config =
                new CrawlerConfiguration.Builder()
                        .setImplementationOverride(SequentialWebCrawler.class.getName())
                        .setParallelism(12)
                        .build();
        Guice.createInjector(new WebCrawlerModule(config), new NoOpProfilerModule())
                .injectMembers(this);
        assertThat(crawler.getClass()).isAssignableTo(SequentialWebCrawler.class);
    }

    @Test
    public void testOverrideToParallel() {
        CrawlerConfiguration config =
                new CrawlerConfiguration.Builder()
                        .setImplementationOverride(ParallelWebCrawler.class.getName())
                        .setParallelism(12)
                        .build();
        Guice.createInjector(new WebCrawlerModule(config), new NoOpProfilerModule())
                .injectMembers(this);
        assertThat(crawler.getClass()).isAssignableTo(ParallelWebCrawler.class);
    }

    @Test
    public void testSequentialParallelism() {
        CrawlerConfiguration config =
                new CrawlerConfiguration.Builder()
                        .setParallelism(1)
                        .build();
        Guice.createInjector(new WebCrawlerModule(config), new com.udacity.webcrawler.NoOpProfilerModule())
                .injectMembers(this);
        assertThat(crawler.getClass()).isAssignableTo(SequentialWebCrawler.class);
    }

    @Test
    public void testParallelParallelism() {
        CrawlerConfiguration config =
                new CrawlerConfiguration.Builder()
                        .setParallelism(2)
                        .build();
        Guice.createInjector(new WebCrawlerModule(config), new com.udacity.webcrawler.NoOpProfilerModule())
                .injectMembers(this);
        assertThat(crawler.getClass()).isAssignableTo(ParallelWebCrawler.class);
    }

    @ParameterizedTest
    @MethodSource("provideTestParameters")
    public void zeroMaxDepth(Class<?> crawlerClass) {
        CrawlerConfiguration config =
                new CrawlerConfiguration.Builder()
                        .setImplementationOverride(crawlerClass.getName())
                        .setMaxDepth(0)
                        .setPopularWordCount(3)
                        .addStartPages(Paths.get(DATA_DIR, "test-page.html").toUri().toString())
                        .build();
        Guice.createInjector(new WebCrawlerModule(config), new com.udacity.webcrawler.NoOpProfilerModule())
                .injectMembers(this);
        assertThat(crawler.getClass()).isAssignableTo(crawlerClass);
        CrawlResult result = crawler.crawl(config.getStartPages());
        assertThat(result.getUrlsVisited()).isEqualTo(0);
        assertThat(result.getWordCounts()).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideTestParameters")
    public void noStartPages(Class<?> crawlerClass) {
        CrawlerConfiguration config =
                new CrawlerConfiguration.Builder()
                        .setImplementationOverride(crawlerClass.getName())
                        .setMaxDepth(10)
                        .setPopularWordCount(3)
                        .build();
        Guice.createInjector(new WebCrawlerModule(config), new com.udacity.webcrawler.NoOpProfilerModule())
                .injectMembers(this);
        assertThat(crawler.getClass()).isAssignableTo(crawlerClass);
        CrawlResult result = crawler.crawl(config.getStartPages());
        assertThat(result.getUrlsVisited()).isEqualTo(0);
        assertThat(result.getWordCounts()).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideTestParameters")
    public void testBasicCrawl(Class<?> crawlerClass) {
        CrawlerConfiguration config =
                new CrawlerConfiguration.Builder()
                        .setImplementationOverride(crawlerClass.getName())
                        .setMaxDepth(10)
                        .setPopularWordCount(3)
                        .addStartPages(Paths.get(DATA_DIR, "test-page.html").toUri().toString())
                        .build();
        Guice.createInjector(new WebCrawlerModule(config), new NoOpProfilerModule())
                .injectMembers(this);
        assertThat(crawler.getClass()).isAssignableTo(crawlerClass);
        CrawlResult result = crawler.crawl(config.getStartPages());
        assertWithMessage("Returned the wrong number of popular words")
                .that(result.getUrlsVisited())
                .isEqualTo(3);
        assertWithMessage("Returned the correct number of popular words, but the wrong words or counts")
                .that(result.getWordCounts())
                .containsEntry("the", 4);
        assertWithMessage("Returned the correct number of popular words, but the wrong words or counts")
                .that(result.getWordCounts())
                .containsEntry("jumped", 2);
        assertWithMessage("Returned the correct number of popular words, but the wrong words or counts")
                .that(result.getWordCounts())
                .containsEntry("brown", 2);
        assertWithMessage("Returned the correct words, but they are in the wrong order")
                .that(result.getWordCounts().entrySet())
                .containsExactly(
                        Map.entry("the", 4),
                        Map.entry("jumped", 2),
                        Map.entry("brown", 2))
                .inOrder();
    }

    @ParameterizedTest
    @MethodSource("provideTestParameters")
    public void respectsIgnoredUrls(Class<?> crawlerClass) {
        CrawlerConfiguration config =
                new CrawlerConfiguration.Builder()
                        .setImplementationOverride(crawlerClass.getName())
                        .setMaxDepth(10)
                        .setPopularWordCount(3)
                        .addStartPages(Paths.get(DATA_DIR, "test-page.html").toUri().toString())
                        .addStartPages(Paths.get(DATA_DIR, "infinite-loop.html").toUri().toString())
                        .addIgnoredUrls(".*-loop\\.html$")
                        .addIgnoredUrls(".*dead-.*")
                        .build();
        Guice.createInjector(new WebCrawlerModule(config), new NoOpProfilerModule())
                .injectMembers(this);
        assertThat(crawler.getClass()).isAssignableTo(crawlerClass);
        CrawlResult result = crawler.crawl(config.getStartPages());
        assertThat(result.getUrlsVisited()).isEqualTo(2);
        assertThat(result.getWordCounts().entrySet())
                .containsExactly(
                        Map.entry("the", 4),
                        Map.entry("jumped", 2),
                        Map.entry("brown", 2))
                .inOrder();
    }

    @ParameterizedTest
    @MethodSource("provideTestParameters")
    public void respectsIgnoredWords(Class<?> crawlerClass) {
        CrawlerConfiguration config =
                new CrawlerConfiguration.Builder()
                        .setImplementationOverride(crawlerClass.getName())
                        .setMaxDepth(10)
                        .setPopularWordCount(3)
                        .addStartPages(Paths.get(DATA_DIR, "test-page.html").toUri().toString())
                        .addIgnoredWords("^...$")
                        .addIgnoredWords("^......$")
                        .build();
        Guice.createInjector(new WebCrawlerModule(config), new NoOpProfilerModule())
                .injectMembers(this);
        assertThat(crawler.getClass()).isAssignableTo(crawlerClass);
        CrawlResult result = crawler.crawl(config.getStartPages());
        assertThat(result.getUrlsVisited()).isEqualTo(3);
        assertThat(result.getWordCounts().entrySet())
                .containsExactly(
                        Map.entry("brown", 2),
                        Map.entry("quick", 2),
                        Map.entry("lazy", 2))
                .inOrder();
    }

    @ParameterizedTest
    @MethodSource("provideTestParameters")
    public void respectsMaxDepth(Class<?> crawlerClass) {
        CrawlerConfiguration config =
                new CrawlerConfiguration.Builder()
                        .setImplementationOverride(crawlerClass.getName())
                        .setMaxDepth(1)
                        .setPopularWordCount(20)
                        .addStartPages(Paths.get(DATA_DIR, "test-page.html").toUri().toString())
                        .build();
        Guice.createInjector(new WebCrawlerModule(config), new NoOpProfilerModule())
                .injectMembers(this);
        assertThat(crawler.getClass()).isAssignableTo(crawlerClass);
        CrawlResult result = crawler.crawl(config.getStartPages());
        assertThat(result.getUrlsVisited()).isEqualTo(1);
        assertThat(result.getWordCounts().entrySet())
                .containsExactly(
                        Map.entry("the", 2),
                        Map.entry("jumped", 1),
                        Map.entry("brown", 1),
                        Map.entry("quick", 1),
                        Map.entry("lazy", 1),
                        Map.entry("link", 1),
                        Map.entry("over", 1),
                        Map.entry("dog", 1),
                        Map.entry("fox", 1))
                .inOrder();
    }

    @ParameterizedTest
    @MethodSource("provideTestParameters")
    public void pageNotFoundStillCountsAsVisited(Class<?> crawlerClass) {
        CrawlerConfiguration config =
                new CrawlerConfiguration.Builder()
                        .setImplementationOverride(crawlerClass.getName())
                        .setMaxDepth(10)
                        .setPopularWordCount(20)
                        .addStartPages(Paths.get(DATA_DIR, "link-1.html").toUri().toString())
                        .build();
        Guice.createInjector(new WebCrawlerModule(config), new com.udacity.webcrawler.NoOpProfilerModule())
                .injectMembers(this);
        assertThat(crawler.getClass()).isAssignableTo(crawlerClass);
        CrawlResult result = crawler.crawl(config.getStartPages());
        assertThat(result.getUrlsVisited()).isEqualTo(2);
        assertThat(result.getWordCounts().entrySet())
                .containsExactly(
                        Map.entry("the", 2),
                        Map.entry("jumped", 1),
                        Map.entry("brown", 1),
                        Map.entry("quick", 1),
                        Map.entry("lazy", 1),
                        Map.entry("link", 1),
                        Map.entry("over", 1),
                        Map.entry("dog", 1),
                        Map.entry("fox", 1))
                .inOrder();
    }

    @ParameterizedTest
    @MethodSource("provideTestParameters")
    public void infiniteLoop(Class<?> crawlerClass) {
        CrawlerConfiguration config =
                new CrawlerConfiguration.Builder()
                        .setImplementationOverride(crawlerClass.getName())
                        .setMaxDepth(10)
                        .setPopularWordCount(3)
                        .setTimeoutSeconds(1)
                        .addStartPages(Paths.get(DATA_DIR, "infinite-loop.html").toUri().toString())
                        .build();
        Guice.createInjector(new WebCrawlerModule(config), new NoOpProfilerModule())
                .injectMembers(this);
        assertThat(crawler.getClass()).isAssignableTo(crawlerClass);
        CrawlResult result = crawler.crawl(config.getStartPages());
        assertThat(result.getUrlsVisited()).isEqualTo(2);
        assertThat(result.getWordCounts().entrySet())
                .containsExactly(
                        Map.entry("the", 4),
                        Map.entry("jumped", 2),
                        Map.entry("brown", 2))
                .inOrder();
    }

    @ParameterizedTest
    @MethodSource("provideTestParameters")
    public void multipleStartingUrls(Class<?> crawlerClass) {
        CrawlerConfiguration config =
                new CrawlerConfiguration.Builder()
                        .setImplementationOverride(crawlerClass.getName())
                        .setMaxDepth(10)
                        .setPopularWordCount(3)
                        .addStartPages(Paths.get(DATA_DIR, "test-page.html").toUri().toString())
                        .addStartPages(Paths.get(DATA_DIR, "link-1.html").toUri().toString())
                        .addStartPages(Paths.get(DATA_DIR, "infinite-loop.html").toUri().toString())
                        .build();
        Guice.createInjector(new WebCrawlerModule(config), new com.udacity.webcrawler.NoOpProfilerModule())
                .injectMembers(this);
        assertThat(crawler.getClass()).isAssignableTo(crawlerClass);
        CrawlResult result = crawler.crawl(config.getStartPages());
        assertThat(result.getUrlsVisited()).isEqualTo(5);
        assertThat(result.getWordCounts().entrySet())
                .containsExactly(
                        Map.entry("the", 8),
                        Map.entry("jumped", 4),
                        Map.entry("brown", 4))
                .inOrder();
    }
}