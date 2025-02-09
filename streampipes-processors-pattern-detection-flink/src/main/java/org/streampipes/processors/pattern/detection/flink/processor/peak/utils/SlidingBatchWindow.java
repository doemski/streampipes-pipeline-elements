/*
 * Copyright 2017 FZI Forschungszentrum Informatik
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
 */

package org.streampipes.processors.pattern.detection.flink.processor.peak.utils;

import org.apache.flink.streaming.api.operators.AbstractStreamOperator;
import org.apache.flink.streaming.api.operators.OneInputStreamOperator;
import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by riemer on 21.04.2017.
 */
public class SlidingBatchWindow<IN> extends AbstractStreamOperator<List<IN>> implements
        OneInputStreamOperator<IN, List<IN>> {

  private Integer windowSize;
  private List<IN> currentEvents;

  public SlidingBatchWindow(Integer windowSize) {
    super();
    this.windowSize = windowSize;
    this.currentEvents = new ArrayList<>();
  }

  @Override
  public void processElement(StreamRecord<IN> in) throws Exception {
    currentEvents.add(in.getValue());
    if (currentEvents.size() > windowSize) {
      currentEvents.remove(0);
    }

    output.collect(new StreamRecord<>(currentEvents, System.currentTimeMillis()));

  }
}
