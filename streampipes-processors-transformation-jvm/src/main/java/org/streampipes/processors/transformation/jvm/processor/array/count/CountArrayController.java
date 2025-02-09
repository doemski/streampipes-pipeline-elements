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

package org.streampipes.processors.transformation.jvm.processor.array.count;

import org.streampipes.model.graph.DataProcessorDescription;
import org.streampipes.model.graph.DataProcessorInvocation;
import org.streampipes.model.schema.PropertyScope;
import org.streampipes.sdk.builder.ProcessingElementBuilder;
import org.streampipes.sdk.builder.StreamRequirementsBuilder;
import org.streampipes.sdk.extractor.ProcessingElementParameterExtractor;
import org.streampipes.sdk.helpers.EpProperties;
import org.streampipes.sdk.helpers.EpRequirements;
import org.streampipes.sdk.helpers.Labels;
import org.streampipes.sdk.helpers.Locales;
import org.streampipes.sdk.helpers.OutputStrategies;
import org.streampipes.sdk.utils.Assets;
import org.streampipes.vocabulary.SO;
import org.streampipes.wrapper.standalone.ConfiguredEventProcessor;
import org.streampipes.wrapper.standalone.declarer.StandaloneEventProcessingDeclarer;

public class CountArrayController extends StandaloneEventProcessingDeclarer<CountArrayParameters> {

  public final static String COUNT_NAME = "countValue";
  public final static String ARRAY_FIELD = "array-field";

  @Override
  public DataProcessorDescription declareModel() {
    return ProcessingElementBuilder.create("org.streampipes.processors.transformation.jvm.count-array")
            .withLocales(Locales.EN)
            .withAssets(Assets.DOCUMENTATION, Assets.ICON)
            .requiredStream(
                    StreamRequirementsBuilder.create()
                            .requiredPropertyWithUnaryMapping(EpRequirements.listRequirement(),
                                    Labels.withId(ARRAY_FIELD), PropertyScope.NONE)
                            .build())
            .outputStrategy(OutputStrategies.append(EpProperties.doubleEp(Labels.empty(), COUNT_NAME,
                    SO.Number)))
            .build();
  }

  @Override
  public ConfiguredEventProcessor<CountArrayParameters> onInvocation(DataProcessorInvocation graph, ProcessingElementParameterExtractor extractor) {
    String arrayField = extractor.mappingPropertyValue(ARRAY_FIELD);

    CountArrayParameters params = new CountArrayParameters(graph, arrayField);
    return new ConfiguredEventProcessor<>(params, CountArray::new);
  }

}
