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

package org.streampipes.sources.watertank.simulator.watertank.streams;

import org.streampipes.model.SpDataStream;
import org.streampipes.model.graph.DataSourceDescription;
import org.streampipes.model.schema.PropertyScope;
import org.streampipes.sdk.builder.DataStreamBuilder;
import org.streampipes.sdk.builder.PrimitivePropertyBuilder;
import org.streampipes.sdk.helpers.EpProperties;
import org.streampipes.sdk.helpers.Formats;
import org.streampipes.sdk.helpers.Locales;
import org.streampipes.sdk.helpers.Protocols;
import org.streampipes.sdk.utils.Assets;
import org.streampipes.sdk.utils.Datatypes;
import org.streampipes.sources.AbstractAlreadyExistingStream;
import org.streampipes.sources.watertank.simulator.config.WatertankSimulatorConfig;
import org.streampipes.sources.watertank.simulator.vocabulary.WaterTankVocabulary;

public class FlowRate2Stream extends AbstractAlreadyExistingStream {


  @Override
  public SpDataStream declareModel(DataSourceDescription sep) {
    return DataStreamBuilder.create("org.streampipes.sources.simulator.flowrate2")
            .withLocales(Locales.EN)
            .withAssets(Assets.DOCUMENTATION, Assets.ICON)
            .property(EpProperties.timestampProperty("timestamp"))
            .property(PrimitivePropertyBuilder
                    .create(Datatypes.String, "sensorId")
                    .label("Sensor ID")
                    .description("The ID of the sensor")
                    .domainProperty(WaterTankVocabulary.HAS_SENSOR_ID)
                    .scope(PropertyScope.DIMENSION_PROPERTY)
                    .build())
            .property(PrimitivePropertyBuilder
                    .create(Datatypes.Float, "mass_flow")
                    .label("Mass Flow")
                    .description("Denotes the current mass flow in the sensor")
                    .domainProperty(WaterTankVocabulary.HAS_MASS_FLOW)
                    .scope(PropertyScope.MEASUREMENT_PROPERTY)
                    .build())
            .property(PrimitivePropertyBuilder
                    .create(Datatypes.Float, "volume_flow")
                    .label("Volume Flow")
                    .description("Denotes the current volume flow")
                    .domainProperty(WaterTankVocabulary.HAS_VOLUME_FLOW)
                    .scope(PropertyScope.MEASUREMENT_PROPERTY)
                    .build())
            .property(PrimitivePropertyBuilder
                    .create(Datatypes.Float, "density")
                    .label("Density")
                    .description("Denotes the current density of the fluid")
                    .domainProperty(WaterTankVocabulary.HAS_DENSITY)
                    .scope(PropertyScope.MEASUREMENT_PROPERTY)
                    .build())
            .property(PrimitivePropertyBuilder
                    .create(Datatypes.Float, "fluid_temperature")
                    .label("Fluid Temperature")
                    .description("Denotes the current temperature of the fluid")
                    .domainProperty(WaterTankVocabulary.HAS_TEMPERATURE)
                    .scope(PropertyScope.MEASUREMENT_PROPERTY)
                    .build())
            .property(PrimitivePropertyBuilder
                    .create(Datatypes.Float, "sensor_fault_flags")
                    .label("Sensor Fault Flags")
                    .description("Any fault flags of the sensors")
                    .domainProperty(WaterTankVocabulary.HAS_SENSOR_FAULT_FLAGS)
                    .scope(PropertyScope.MEASUREMENT_PROPERTY)
                    .build())
            .format(Formats.jsonFormat())
            .protocol(Protocols.kafka(WatertankSimulatorConfig.INSTANCE.getKafkaHost(), WatertankSimulatorConfig.INSTANCE.getKafkaPort(),
                    "org.streampipes.examples.flowrate2"))
            .build();
  }

}
