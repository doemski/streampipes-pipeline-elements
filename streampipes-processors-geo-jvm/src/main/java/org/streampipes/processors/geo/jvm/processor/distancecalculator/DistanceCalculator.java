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

package org.streampipes.processors.geo.jvm.processor.distancecalculator;

import org.streampipes.model.runtime.Event;
import org.streampipes.wrapper.context.EventProcessorRuntimeContext;
import org.streampipes.wrapper.routing.SpOutputCollector;
import org.streampipes.wrapper.runtime.EventProcessor;

public class DistanceCalculator implements EventProcessor<DistanceCalculatorParameters> {

  private DistanceCalculatorParameters params;

  @Override
  public void onInvocation(DistanceCalculatorParameters numericalFilterParameters, SpOutputCollector spOutputCollector, EventProcessorRuntimeContext
          runtimeContext) {
    this.params = numericalFilterParameters;
  }

  @Override
  public void onEvent(Event event, SpOutputCollector out) {

    float lat1 = event.getFieldBySelector(this.params.getLat1PropertyName()).getAsPrimitive().getAsFloat();
    float long1 = event.getFieldBySelector(this.params.getLong1PropertyName()).getAsPrimitive().getAsFloat();
    float lat2 = event.getFieldBySelector(this.params.getLat2PropertyName()).getAsPrimitive().getAsFloat();
    float long2 = event.getFieldBySelector(this.params.getLong2PropertyName()).getAsPrimitive().getAsFloat();

    double resultDist = dist(lat1, long1, lat2, long2);

    event.addField("distance", resultDist);

    out.collect(event);
  }

  @Override
  public void onDetach() {

  }


  public static float dist(float lat1, float lng1, float lat2, float lng2) {
    double earthRadius = 6371000; //meters
    double dLat = Math.toRadians(lat2-lat1);
    double dLng = Math.toRadians(lng2-lng1);
    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                    Math.sin(dLng/2) * Math.sin(dLng/2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    float dist = (float) (earthRadius * c);

    return dist / 1000;
  }
}
