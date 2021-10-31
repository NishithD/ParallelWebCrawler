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

package com.udacity.webcrawler.parser;

import com.udacity.webcrawler.Timeout;
import com.udacity.webcrawler.profiler.Profiler;

import javax.inject.Inject;
import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A {@link PageParserFactory} that wraps its returned instances using a {@link Profiler}.
 */
final class PageParserFactoryImpl implements PageParserFactory {
  private final Profiler profiler;
  private final List<Pattern> ignoredWords;
  private final Duration timeout;

  @Inject
  PageParserFactoryImpl(
      Profiler profiler, @IgnoredWords List<Pattern> ignoredWords, @Timeout Duration timeout) {
    this.profiler = profiler;
    this.ignoredWords = ignoredWords;
    this.timeout = timeout;
  }

  @Override
  public PageParser get(String url) {
    // Here, parse the page with the initial timeout (instead of just the time remaining), to make
    // the download less likely to fail. Deadline enforcement should happen at a higher level.
    PageParser delegate = new PageParserImpl(url, timeout, ignoredWords);
    return profiler.wrap(PageParser.class, delegate);
  }
}
