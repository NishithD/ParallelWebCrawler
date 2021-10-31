package com.udacity.webcrawler;

import com.udacity.webcrawler.parser.PageParser;

import javax.inject.Inject;
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
    private final List<String> startingUrls;
    private final int maxDepth;
    private final Clock clock;
    @Inject
    private com.udacity.webcrawler.parser.PageParserFactory parserFactory;

    public CrawlerRecursiveAction(Instant deadline, List<String> startingUrls, int maxDepth, Clock clock, List<Pattern> ignoredUrls) {
        this.deadline = deadline;
        this.startingUrls = startingUrls;
        this.maxDepth = maxDepth;
        this.clock = clock;
        this.ignoredUrls = ignoredUrls;
    }

    @Override
    protected void compute() {
        if (this.startingUrls.size() > 1) {
            List<CrawlerRecursiveAction> subtasks = new ArrayList<>(createSubtasks());
            for (RecursiveAction subtask : subtasks) {
                subtask.fork();
            }
        } else {
            if (maxDepth == 0 || clock.instant().isAfter(deadline)) {
                return;
            }
            String url = startingUrls.get(0);
            for (Pattern pattern : ignoredUrls) {
                if (pattern.matcher(url).matches()) {
                    return;
                }
            }
            if (visitedUrls.contains(url)) {
                return;
            }
            visitedUrls.add(url);
            PageParser.Result result = parserFactory.get(url).parse();
            for (Map.Entry<String, Integer> e : result.getWordCounts().entrySet()) {
                if (counts.containsKey(e.getKey())) {
                    counts.put(e.getKey(), e.getValue() + counts.get(e.getKey()));
                } else {
                    counts.put(e.getKey(), e.getValue());
                }
            }
        }
    }

    private List<CrawlerRecursiveAction> createSubtasks() {
        List<CrawlerRecursiveAction> subtasks = new ArrayList<>();
        int size = this.startingUrls.size();
        CrawlerRecursiveAction subtask1 = new CrawlerRecursiveAction(this.deadline, new ArrayList<>(
                this.startingUrls.subList(0, size / 2)), this.maxDepth - 1, clock, ignoredUrls);
        CrawlerRecursiveAction subtask2 = new CrawlerRecursiveAction(this.deadline, new ArrayList<>(
                this.startingUrls.subList(size / 2, size)), this.maxDepth - 1, clock, ignoredUrls);
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        return subtasks;
    }

    public Set<String> getVisitedUrls() {
        return visitedUrls;
    }

    public Map<String, Integer> getCounts() {
        return counts;
    }
}