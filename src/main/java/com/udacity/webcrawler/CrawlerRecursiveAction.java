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

import com.udacity.webcrawler.parser.PageParser;
import com.udacity.webcrawler.parser.PageParserFactory;

import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.RecursiveAction;
import java.util.regex.Pattern;

public class CrawlerRecursiveAction extends RecursiveAction {
    private static final Map<String, Integer> counts = Collections.synchronizedMap(new HashMap<>());
    private static final Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<>());
    private final List<Pattern> ignoredUrls;
    private final Instant deadline;
    private final Clock clock;
    private final PageParserFactory parserFactory;
    private List<String> startingUrls;
    private int maxDepth;

    private CrawlerRecursiveAction(Instant deadline,
                                   List<String> startingUrls,
                                   int maxDepth,
                                   Clock clock,
                                   List<Pattern> ignoredUrls,
                                   PageParserFactory parserFactory) {
        this.deadline = deadline;
        this.startingUrls = startingUrls;
        this.maxDepth = maxDepth;
        this.clock = clock;
        this.ignoredUrls = ignoredUrls;
        this.parserFactory = parserFactory;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public List<String> getStartingUrls() {
        return startingUrls;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public Clock getClock() {
        return clock;
    }

    public List<Pattern> getIgnoredUrls() {
        return ignoredUrls;
    }

    public PageParserFactory getParserFactory() {
        return parserFactory;
    }

    public Set<String> getVisitedUrls() {
        return visitedUrls;
    }

    public Map<String, Integer> getCounts() {
        return counts;
    }

    @Override
    protected void compute() {
        while (!startingUrls.isEmpty()) {
            if (isMaxDepthReached() || isTimeOut() || isStartEmpty()) return;
            if (startingUrls.size() > 1) {
                List<CrawlerRecursiveAction> subtasks = new ArrayList<>(createSubtasks());
                for (RecursiveAction subtask : subtasks) {
                    subtask.fork();
                }
            } else {
                String url = startingUrls.get(0);
                if (isUrlIgnored(url)) return;
                if (isVisited(url)) {
                    return;
                }
                synchronized (visitedUrls) {
                    if (isVisited(url)) {
                        return;
                    }
                    visitedUrls.add(url);
                }
                PageParser.Result result = parserFactory.get(url).parse();
                synchronized (counts) {
                    for (Map.Entry<String, Integer> e : result.getWordCounts().entrySet()) {
                        counts.put(e.getKey(), counts.containsKey(e.getKey())
                                ? counts.get(e.getKey()) + e.getValue()
                                : e.getValue());
                    }

                }
                startingUrls = result.getLinks();
                maxDepth -= 1;
            }
        }
    }

    private boolean isUrlIgnored(String url) {
        for (Pattern pattern : ignoredUrls) {
            if (pattern.matcher(url).matches()) {
                return true;
            }
        }
        return false;
    }

    private boolean isVisited(String url) {
        return visitedUrls.contains(url);
    }

    private boolean isStartEmpty() {
        return startingUrls.isEmpty();
    }

    private boolean isTimeOut() {
        return clock.instant().isAfter(deadline);
    }

    private boolean isMaxDepthReached() {
        return maxDepth == 0;
    }

    private List<CrawlerRecursiveAction> createSubtasks() {
        List<CrawlerRecursiveAction> subtasks = new ArrayList<>();
        int size = startingUrls.size();
        for (int i = 0; i < size; i++) {
            subtasks.add(new CrawlerRecursiveAction
                    .Builder()
                    .setDeadline(deadline)
                    .setStartingUrls(startingUrls.subList(i, i + 1))
                    .setMaxDepth(maxDepth - 1)
                    .setClock(clock)
                    .setIgnoredUrls(ignoredUrls)
                    .setParserFactory(parserFactory)
                    .build());
        }
        return subtasks;
    }

    public static final class Builder {
        private List<Pattern> ignoredUrls;
        private Instant deadline;
        private List<String> startingUrls;
        private int maxDepth;
        private Clock clock;
        private PageParserFactory parserFactory;

        public Builder setIgnoredUrls(List<Pattern> ignoredUrls) {
            this.ignoredUrls = ignoredUrls;
            return this;
        }

        public Builder setDeadline(Instant deadline) {
            this.deadline = deadline;
            return this;
        }

        public Builder setStartingUrls(List<String> startingUrls) {
            this.startingUrls = startingUrls;
            return this;
        }

        public Builder setClock(Clock clock) {
            this.clock = clock;
            return this;
        }

        public Builder setMaxDepth(int maxDepth) {
            this.maxDepth = maxDepth;
            return this;
        }

        public Builder setParserFactory(PageParserFactory parserFactory) {
            this.parserFactory = parserFactory;
            return this;
        }

        public CrawlerRecursiveAction build() {
            return new CrawlerRecursiveAction(deadline, startingUrls, maxDepth, clock, ignoredUrls, parserFactory);
        }
    }
}