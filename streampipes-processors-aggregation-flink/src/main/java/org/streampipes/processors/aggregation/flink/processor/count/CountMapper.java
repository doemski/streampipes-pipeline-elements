/*
 * Copyright 2018 FZI Forschungszentrum Informatik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.streampipes.processors.aggregation.flink.processor.count;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.streampipes.model.runtime.Event;

public class CountMapper implements MapFunction<Event, Tuple3<String, String, Integer>> {

  private String fieldToCount;

  public CountMapper(String fieldToCount) {
    this.fieldToCount = fieldToCount;
  }

  @Override
  public Tuple3<String, String, Integer> map(Event stringObjectMap) throws Exception {
    return new Tuple3<>(fieldToCount, stringObjectMap.getFieldBySelector(fieldToCount)
            .getAsPrimitive().getAsString(), 1);
  }
}
