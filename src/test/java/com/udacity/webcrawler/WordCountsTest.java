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

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertWithMessage;

public final class WordCountsTest {
  @Test
  public void testBasicOrder() {
    Map<String, Integer> unsortedCounts = new HashMap<>();
    unsortedCounts.put("the", 2);
    unsortedCounts.put("quick", 1);
    unsortedCounts.put("brown", 1);
    unsortedCounts.put("fox", 1);
    unsortedCounts.put("jumped", 1);
    unsortedCounts.put("over", 1);
    unsortedCounts.put("lazy", 1);
    unsortedCounts.put("dog", 1);

    Map<String, Integer> result = WordCounts.sort(unsortedCounts, 4);

    assertWithMessage("Returned the wrong number of popular words")
        .that(result)
        .hasSize(4);

    assertWithMessage("Returned the correct number of popular words, but the wrong words or counts")
        .that(result)
        .containsEntry("the", 2);
    assertWithMessage("Returned the correct number of popular words, but the wrong words or counts")
        .that(result)
        .containsEntry("jumped", 1);
    assertWithMessage("Returned the correct number of popular words, but the wrong words or counts")
        .that(result)
        .containsEntry("brown", 1);
    assertWithMessage("Returned the correct number of popular words, but the wrong words or counts")
        .that(result)
        .containsEntry("quick", 1);
    assertWithMessage("Returned the correct words, but they are in the wrong order")
        .that(result.entrySet())
        .containsExactly(
            Map.entry("the", 2),
            Map.entry("jumped", 1),
            Map.entry("brown", 1),
            Map.entry("quick", 1))
        .inOrder();
  }

  @Test
  public void testNotEnoughWords() {
    Map<String, Integer> unsortedCounts = new HashMap<>();
    unsortedCounts.put("the", 2);
    unsortedCounts.put("quick", 1);
    unsortedCounts.put("brown", 1);
    unsortedCounts.put("fox", 1);

    Map<String, Integer> result = WordCounts.sort(unsortedCounts, 5);

    assertWithMessage("Returned the wrong number of popular words")
        .that(result)
        .hasSize(4);

    assertWithMessage("Returned the correct number of popular words, but the wrong words or counts")
        .that(result)
        .containsEntry("the", 2);
    assertWithMessage("Returned the correct number of popular words, but the wrong words or counts")
        .that(result)
        .containsEntry("brown", 1);
    assertWithMessage("Returned the correct number of popular words, but the wrong words or counts")
        .that(result)
        .containsEntry("quick", 1);
    assertWithMessage("Returned the correct number of popular words, but the wrong words or counts")
        .that(result)
        .containsEntry("fox", 1);
    assertWithMessage("Returned the correct words, but they are in the wrong order")
        .that(result.entrySet())
        .containsExactly(
            Map.entry("the", 2),
            Map.entry("brown", 1),
            Map.entry("quick", 1),
            Map.entry("fox", 1))
        .inOrder();  }
}
