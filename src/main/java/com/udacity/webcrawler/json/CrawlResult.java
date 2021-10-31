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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Data class representing the final result of a web crawl.
 */
public final class CrawlResult {

  private final Map<String, Integer> wordCounts;
  private final int urlsVisited;

  /**
   * Constructs a {@link CrawlResult} with the given word counts and visited URL count.
   */
  private CrawlResult(Map<String, Integer> wordCounts, int urlsVisited) {
    this.wordCounts = wordCounts;
    this.urlsVisited = urlsVisited;
  }

  /**
   * Returns an unmodifiable {@link Map}. Each key in the map is a word that was encountered
   * during the web crawl. Each value is the total number of times a word was seen.
   *
   * <p>When computing these counts for a given crawl, results from the same page are never
   * counted twice.
   *
   * <p>The size of the returned map is the same as the {@code "popularWordCount"} option in the
   * crawler configuration. For example,  if {@code "popularWordCount"} is 3, only the top 3 most
   * frequent words are returned.
   *
   * <p>If multiple words have the same frequency, prefer longer words rank higher. If multiple
   * words have the same frequency and length, use alphabetical order to break ties (the word that
   * comes first in the alphabet ranks higher).
   */
  public Map<String, Integer> getWordCounts() {
    return wordCounts;
  }

  /**
   * Returns the number of distinct URLs the web crawler visited.
   *
   * <p>A URL is considered "visited" if the web crawler attempted to crawl that URL, even if the
   * HTTP request to download the page returned an error.
   *
   * <p>When computing this value for a given crawl, the same URL is never counted twice.
   */
  public int getUrlsVisited() {
    return urlsVisited;
  }

  /**
   * A package-private builder class for constructing web crawl {@link CrawlResult}s.
   */
  public static final class Builder {
    private Map<String, Integer> wordFrequencies = new HashMap<>();
    private int pageCount;

    /**
     * Sets the word counts. See {@link #getWordCounts()}
     */
    public Builder setWordCounts(Map<String, Integer> wordCounts) {
      this.wordFrequencies = Objects.requireNonNull(wordCounts);
      return this;
    }

    /**
     * Sets the total number of URLs visited. See {@link #getUrlsVisited()}.
     */
    public Builder setUrlsVisited(int pageCount) {
      this.pageCount = pageCount;
      return this;
    }

    /**
     * Constructs a {@link CrawlResult} from this builder.
     */
    public CrawlResult build() {
      return new CrawlResult(Collections.unmodifiableMap(wordFrequencies), pageCount);
    }
  }
}