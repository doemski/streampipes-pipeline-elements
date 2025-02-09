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

package org.streampipes.processors.transformation.jvm.processor.booloperator.counter;

import org.streampipes.model.graph.DataProcessorDescription;
import org.streampipes.model.graph.DataProcessorInvocation;
import org.streampipes.model.schema.PropertyScope;
import org.streampipes.sdk.builder.ProcessingElementBuilder;
import org.streampipes.sdk.builder.StreamRequirementsBuilder;
import org.streampipes.sdk.extractor.ProcessingElementParameterExtractor;
import org.streampipes.sdk.helpers.*;
import org.streampipes.sdk.utils.Assets;
import org.streampipes.wrapper.standalone.ConfiguredEventProcessor;
import org.streampipes.wrapper.standalone.declarer.StandaloneEventProcessingDeclarer;

public class BooleanCounterController extends StandaloneEventProcessingDeclarer<BooleanCounterParameters> {

  public static final String FIELD_ID = "field";
  public static final String FLANK_ID = "flank";
  public static final String COUNT_FIELD_ID = "countField";

  public static final String COUNT_FIELD_RUNTIME_NAME = "counter";

  private static final String FLANK_UP = "FALSE -> TRUE";
  private static final String FLANK_DOWN = "TRUE -> FALSE";
  private static final String BOTH = "BOTH";

  @Override
  public DataProcessorDescription declareModel() {
    return ProcessingElementBuilder.create("org.streampipes.processors.transformation.jvm.booloperator.counter")
            .withLocales(Locales.EN)
            .withAssets(Assets.DOCUMENTATION, Assets.ICON)
            .requiredStream(StreamRequirementsBuilder.create()
                    .requiredPropertyWithUnaryMapping(
                            EpRequirements.booleanReq(),
                            Labels.withId(FIELD_ID),
                            PropertyScope.NONE)
                    .build())

            .requiredSingleValueSelection(Labels.withId(FLANK_ID), Options.from(BOTH, FLANK_UP, FLANK_DOWN))
            .outputStrategy(OutputStrategies.append(
                    EpProperties.numberEp(Labels.withId(COUNT_FIELD_ID), COUNT_FIELD_RUNTIME_NAME, "http://schema.org/Number")
            ))
            .build();
  }

  @Override
  public ConfiguredEventProcessor<BooleanCounterParameters> onInvocation(DataProcessorInvocation graph, ProcessingElementParameterExtractor extractor) {

    String invertFieldName = extractor.mappingPropertyValue(FIELD_ID);
    String flank = extractor.selectedSingleValue(FLANK_ID, String.class);

    int flankUp = 0;
    if (flank.equals(FLANK_DOWN)) {
      flankUp = 1;
    } else if (flank.equals(FLANK_UP)) {
      flankUp = 2;
    }

    BooleanCounterParameters params = new BooleanCounterParameters(graph, invertFieldName, flankUp);
    return new ConfiguredEventProcessor<>(params, BooleanCounter::new);
  }
}
