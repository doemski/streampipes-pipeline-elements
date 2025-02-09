/*
Copyright 2019 FZI Forschungszentrum Informatik

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.streampipes.sinks.brokers.jvm.pulsar;

import org.streampipes.model.graph.DataSinkInvocation;
import org.streampipes.wrapper.params.binding.EventSinkBindingParams;

public class PulsarParameters extends EventSinkBindingParams {

  private String pulsarHost;
  private Integer pulsarPort;
  private String topic;

  public PulsarParameters(DataSinkInvocation graph, String pulsarHost, Integer pulsarPort,
                          String topic) {
    super(graph);
    this.pulsarHost = pulsarHost;
    this.pulsarPort = pulsarPort;
    this.topic = topic;
  }

  public String getPulsarHost() {
    return pulsarHost;
  }

  public Integer getPulsarPort() {
    return pulsarPort;
  }

  public String getTopic() {
    return topic;
  }
}
