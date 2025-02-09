/*
 * Copyright 2018 FZI Forschungszentrum Informatik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
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
package org.streampipes.processors.transformation.jvm.processor.task;

import org.streampipes.model.graph.DataProcessorInvocation;
import org.streampipes.wrapper.params.binding.EventProcessorBindingParams;

public class TaskDurationParameters extends EventProcessorBindingParams {

  private String taskFieldSelector;
  private String timestampFieldSelector;
  private Double outputDivisor;

  public TaskDurationParameters(DataProcessorInvocation graph, String taskFieldSelector,
                                String timestampFieldSelector, Double outputDivisor) {
    super(graph);
    this.taskFieldSelector = taskFieldSelector;
    this.timestampFieldSelector = timestampFieldSelector;
    this.outputDivisor = outputDivisor;
  }

  public String getTaskFieldSelector() {
    return taskFieldSelector;
  }

  public String getTimestampFieldSelector() {
    return timestampFieldSelector;
  }

  public Double getOutputDivisor() {
    return outputDivisor;
  }
}
