/*
 * Copyright 2019 FZI Forschungszentrum Informatik
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
package org.streampipes.processors.siddhi.stop;

import org.streampipes.wrapper.siddhi.engine.SiddhiEventEngine;

import java.util.Arrays;
import java.util.List;

public class StreamStop extends SiddhiEventEngine<StreamStopParameters> {

  public StreamStop() {
    super();
  }

  @Override
  protected String fromStatement(List<String> inputStreamNames, StreamStopParameters params) {
    return "define stream Test(timestamp LONG,message STRING);\n" +
            "from every not " + inputStreamNames.get(0) + " for " + params.getDuration() + " sec";
  }

  @Override
  protected String selectStatement(StreamStopParameters params) {
      setSortedEventKeys(Arrays.asList("timestamp", "message"));
    return "select currentTimeMillis() as timestamp, 'Event stream has stopped' as message";
  }



}
