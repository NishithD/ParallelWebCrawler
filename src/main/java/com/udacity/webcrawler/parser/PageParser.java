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

import com.udacity.webcrawler.profiler.Profiled;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Parses and processes remote and local HTML pages to return a parse {@link Result}.
 */
public interface PageParser {

  /**
   * Processes the HTML page and returns a {@link Result} for the page.
   */
  @Profiled
  Result parse();

  /**
   * A data class that represents the outcome of processing an HTML page.
   */
  final class Result {
    private final Map<String, Integer> wordCounts;
    private final List<String> links;

    private Result(Map<String, Integer> wordCounts, List<String> links) {
      this.wordCounts = Objects.requireNonNull(wordCounts);
      this.links = Objects.requireNonNull(links);
    }

    /**
     * Returns an unmodifiable {@link Map} containing the words and word frequencies encountered
     * when parsing the web page.
     */
    public Map<String, Integer> getWordCounts() {
      return wordCounts;
    }

    /**
     * Returns an unmodifiable {@link List} of the hyperlinks encountered when parsing the web page.
     */
    public List<String> getLinks() {
      return links;
    }

    /**
     * A builder class for the parse {@link Result}. This builder keeps track of word counts and
     * hyperlinks encountered while parsing a web page.
     */
    static final class Builder {
      private final Map<String, Integer> wordCounts = new HashMap<>();
      private final Set<String> links = new HashSet<>();

      /**
       * Increments the frequency counter for the given word.
       */
      void addWord(String word) {
        Objects.requireNonNull(word);
        wordCounts.compute(word, (k, v) -> (v == null) ? 1 : v + 1);
      }

      /**
       * Adds the given link, if it has not already been added.
       */
      void addLink(String link) {
        links.add(Objects.requireNonNull(link));
      }

      /**
       * Constructs a {@link Result} from this builder.
       */
      Result build() {
        return new Result(
            Collections.unmodifiableMap(wordCounts),
            links.stream().collect(Collectors.toUnmodifiableList()));
      }
    }
  }
}
