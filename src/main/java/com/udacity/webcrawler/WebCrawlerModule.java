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

package com.udacity.webcrawler;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.ProvisionException;
import com.google.inject.multibindings.Multibinder;
import com.udacity.webcrawler.json.CrawlerConfiguration;
import com.udacity.webcrawler.parser.ParserModule;
import com.udacity.webcrawler.profiler.Profiler;

import javax.inject.Qualifier;
import javax.inject.Singleton;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Guice dependency injection module that installs all the required dependencies to run the web
 * crawler application. Callers should use it like this:
 *
 * <pre>{@code
 *   CrawlerConfiguration config = ...;
 *   WebCrawler crawler =
 *       Guice.createInjector(new WebCrawlerModule(config))
 *           .getInstance(WebCrawler.class);
 * }</pre>
 */
public final class WebCrawlerModule extends AbstractModule {

  private final CrawlerConfiguration config;

  /**
   * Installs a web crawler that conforms to the given {@link CrawlerConfiguration}.
   */
  public WebCrawlerModule(CrawlerConfiguration config) {
    this.config = Objects.requireNonNull(config);
  }

  @Override
  protected void configure() {
    // Multibinder provides a way to implement the strategy pattern through dependency injection.
    Multibinder<WebCrawler> multibinder =
        Multibinder.newSetBinder(binder(), WebCrawler.class, Internal.class);
    multibinder.addBinding().to(SequentialWebCrawler.class);
    multibinder.addBinding().to(ParallelWebCrawler.class);

    bind(Clock.class).toInstance(Clock.systemUTC());
    bind(Key.get(Integer.class, MaxDepth.class)).toInstance(config.getMaxDepth());
    bind(Key.get(Integer.class, PopularWordCount.class)).toInstance(config.getPopularWordCount());
    bind(Key.get(Duration.class, Timeout.class)).toInstance(config.getTimeout());
    bind(new Key<List<Pattern>>(IgnoredUrls.class) {
    }).toInstance(config.getIgnoredUrls());

    install(
        new ParserModule.Builder()
            .setTimeout(config.getTimeout())
            .setIgnoredWords(config.getIgnoredWords())
            .build());
  }

  @Provides
  @Singleton
  @Internal
  WebCrawler provideRawWebCrawler(
      @Internal Set<WebCrawler> implementations,
      @TargetParallelism int targetParallelism) {
    String override = config.getImplementationOverride();
    if (!override.isEmpty()) {
      return implementations
          .stream()
          .filter(impl -> impl.getClass().getName().equals(override))
          .findFirst()
          .orElseThrow(() -> new ProvisionException("Implementation not found: " + override));
    }
    return implementations
        .stream()
        .filter(impl -> targetParallelism <= impl.getMaxParallelism())
        .findFirst()
        .orElseThrow(
            () -> new ProvisionException(
                "No implementation able to handle parallelism = \"" +
                    config.getParallelism() + "\"."));
  }

  @Provides
  @Singleton
  @TargetParallelism
  int provideTargetParallelism() {
    if (config.getParallelism() >= 0) {
      return config.getParallelism();
    }
    return Runtime.getRuntime().availableProcessors();
  }

  @Provides
  @Singleton
  WebCrawler provideWebCrawlerProxy(Profiler wrapper, @Internal WebCrawler delegate) {
    return wrapper.wrap(WebCrawler.class, delegate);
  }

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  private @interface Internal {
  }
}
