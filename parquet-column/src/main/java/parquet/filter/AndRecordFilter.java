/**
 * Copyright 2012 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package parquet.filter;

import parquet.Preconditions;
import parquet.column.ColumnReader;

/**
 * Provides ability to chain two filters together. Bear in mind that the first one will
 * short circuit the second. Useful if getting a page of already filtered result.
 *  i.e and( column("manufacturer", equalTo("Volkswagen")), page(100,50))
 *
 * @author Jacob Metcalf
 */
public final class AndRecordFilter implements RecordFilter {

  private final RecordFilter boundFilter1;
  private final RecordFilter boundFilter2;

  /**
   * Returns builder for creating an and filter.
   * @param filter1 The first filter to check.
   * @param filter2 The second filter to check.
   */
  public static final UnboundRecordFilter and( final UnboundRecordFilter filter1, final UnboundRecordFilter filter2 ) {
    Preconditions.checkNotNull( filter1, "filter1" );
    Preconditions.checkNotNull( filter2, "filter2" );
    return new UnboundRecordFilter() {
      @Override
      public RecordFilter bind(Iterable<ColumnReader> readers) {
        return new AndRecordFilter( filter1.bind(readers), filter2.bind( readers) );
      }
    };
  }

  /**
   * Private constructor, use AndRecordFilter.and() instead.
   */
  private AndRecordFilter( RecordFilter boundFilter1, RecordFilter boundFilter2 ) {
    this.boundFilter1 = boundFilter1;
    this.boundFilter2 = boundFilter2;
  }

  @Override
  public boolean isFullyConsumed() {
    return boundFilter1.isFullyConsumed() && boundFilter2.isFullyConsumed();
  }

  @Override
  public boolean isMatch() {
    return boundFilter1.isMatch() && boundFilter2.isMatch();
  }
}
