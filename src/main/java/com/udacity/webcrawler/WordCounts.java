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

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


final class WordCounts {

    private WordCounts() {
        // This class cannot be instantiated
    }

    /**
     * Given an unsorted map of word counts, returns a new map whose word counts are sorted according
     * to the provided {@link WordCountComparator}, and includes only the top
     * {@param popluarWordCount} words and counts.
     *
     * @param wordCounts       the unsorted map of word counts.
     * @param popularWordCount the number of popular words to include in the result map.
     * @return a map containing the top {@param popularWordCount} words and counts in the right order.
     */
    static Map<String, Integer> sort(Map<String, Integer> wordCounts, int popularWordCount) {

        return wordCounts.entrySet().stream().
                sorted(new WordCountComparator()).
                limit(Math.min(popularWordCount, wordCounts.size())).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (u, v) -> u,
                        LinkedHashMap::new));
    }

    /**
     * A {@link Comparator} that sorts word count pairs correctly:
     *
     * <p>
     * <ol>
     *   <li>First sorting by word count, ranking more frequent words higher.</li>
     *   <li>Then sorting by word length, ranking longer words higher.</li>
     *   <li>Finally, breaking ties using alphabetical order.</li>
     * </ol>
     */
    private static final class WordCountComparator implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
            if (!a.getValue().equals(b.getValue())) {
                return b.getValue() - a.getValue();
            }
            if (a.getKey().length() != b.getKey().length()) {
                return b.getKey().length() - a.getKey().length();
            }
            return a.getKey().compareTo(b.getKey());
        }
    }
}