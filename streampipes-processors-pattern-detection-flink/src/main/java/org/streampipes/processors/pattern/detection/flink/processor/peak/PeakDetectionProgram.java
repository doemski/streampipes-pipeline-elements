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

package org.streampipes.processors.pattern.detection.flink.processor.peak;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.streampipes.model.runtime.Event;
import org.streampipes.processors.pattern.detection.flink.AbstractPatternDetectionProgram;
import org.streampipes.processors.pattern.detection.flink.processor.peak.utils.SlidingBatchWindow;

import java.util.List;

/**
 * Created by riemer on 20.04.2017.
 */
public class PeakDetectionProgram extends AbstractPatternDetectionProgram<PeakDetectionParameters> {

  public PeakDetectionProgram(PeakDetectionParameters params, boolean debug) {
    super(params, debug);
  }

  @Override
  protected DataStream<Event> getApplicationLogic(DataStream<Event>[] messageStream) {

    Integer lag = params.getLag();
    String groupBy = params.getGroupBy();
    String valueToObserve = params.getValueToObserve();
    Double threshold = params.getThreshold();
    Double influence = params.getInfluence();
    Integer countWindowSize = params.getCountWindowSize();

    return messageStream[0]
            .keyBy(getKeySelector())
            .transform
                    ("sliding-batch-window-shift",
                            TypeInformation.of(new TypeHint<List<Event>>() {
                            }), new SlidingBatchWindow<>(countWindowSize))
            .flatMap(new PeakDetectionCalculator(groupBy,
                    valueToObserve,
                    lag,
                    threshold,
                    influence));
  }

  private KeySelector<Event, String> getKeySelector() {
    String groupBy = params.getGroupBy();
    return new KeySelector<Event, String>() {
      @Override
      public String getKey(Event in) throws Exception {
        return in.getFieldBySelector(groupBy).getAsPrimitive().getAsString();
      }
    };
  }
}
