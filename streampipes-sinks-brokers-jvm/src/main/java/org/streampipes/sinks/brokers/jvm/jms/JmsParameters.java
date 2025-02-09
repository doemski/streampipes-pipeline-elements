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

package org.streampipes.sinks.brokers.jvm.jms;

import org.streampipes.model.graph.DataSinkInvocation;
import org.streampipes.wrapper.params.binding.EventSinkBindingParams;

public class JmsParameters extends EventSinkBindingParams {

  private String jmsHost;
  private Integer jmsPort;
  private String topic;

  public JmsParameters(DataSinkInvocation graph, String jmsHost, Integer jmsPort, String topic) {
    super(graph);
    this.jmsHost = jmsHost;
    this.jmsPort = jmsPort;
    this.topic = topic;
  }

  public String getJmsHost() {
    return jmsHost;
  }

  public Integer getJmsPort() {
    return jmsPort;
  }

  public String getTopic() {
    return topic;
  }
}
