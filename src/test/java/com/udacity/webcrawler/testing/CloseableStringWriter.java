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

package com.udacity.webcrawler.testing;

import java.io.IOException;
import java.io.StringWriter;

/**
 * A {@link StringWriter} that checks if it has already been closed.
 *
 * <p>For other kinds of writers, this could be tested by checking whether {@link #close()} throws
 * an {@link IOException}, but for {@link StringWriter}s the {@link #close()} method does nothing.
 */
public final class CloseableStringWriter extends StringWriter {
  private boolean closed = false;

  @Override
  public void close() throws IOException {
    if (closed) {
      throw new IOException("stream is closed");
    }
    closed = true;
  }

  /**
   * Returns whether this writer has been closed.
   */
  public boolean isClosed() {
    return closed;
  }
}