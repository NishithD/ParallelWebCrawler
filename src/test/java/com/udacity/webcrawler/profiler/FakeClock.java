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

package com.udacity.webcrawler.profiler;

import javax.inject.Inject;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

/**
 * A fake, mutable {@link Clock} implementation for tests.
 */
public final class FakeClock extends Clock {

  private Instant now;
  private ZoneId zoneId;

  @Inject
  public FakeClock() {
    this(Instant.now(), ZoneId.systemDefault());
  }

  public FakeClock(Instant now, ZoneId zoneId) {
    this.now = Objects.requireNonNull(now);
    this.zoneId = Objects.requireNonNull(zoneId);
  }

  @Override
  public ZoneId getZone() {
    return zoneId;
  }

  @Override
  public Clock withZone(ZoneId zone) {
    return new FakeClock(now, zone);
  }

  @Override
  public Instant instant() {
    return now;
  }

  /**
   * Increments the time of the fake clock by the given amount.
   */
  public void tick(Duration duration) {
    now = now.plus(Objects.requireNonNull(duration));
  }

  /**
   * Sets the time of the fake clock.
   */
  public void setTime(Instant instant) {
    this.now = Objects.requireNonNull(instant);
  }

  /**
   * Sets the zone of the fake clock.
   */
  public void setZone(ZoneId zoneId) {
    this.zoneId = Objects.requireNonNull(zoneId);
  }
}
