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
package org.streampipes.processors.imageprocessing.jvm.processor.imageenrichment;

import org.streampipes.processors.imageprocessing.jvm.processor.commons.ImageTransformer;
import org.streampipes.wrapper.context.EventProcessorRuntimeContext;
import org.streampipes.wrapper.routing.SpOutputCollector;
import org.streampipes.wrapper.runtime.EventProcessor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ImageEnricher implements EventProcessor<ImageEnrichmentParameters> {

  private ImageEnrichmentParameters params;

  @Override
  public void onInvocation(ImageEnrichmentParameters params, SpOutputCollector spOutputCollector, EventProcessorRuntimeContext runtimeContext) {
    this.params = params;
  }

  @Override
  public void onEvent(org.streampipes.model.runtime.Event in, SpOutputCollector out) {
    ImageTransformer imageTransformer = new ImageTransformer(in, params);

    Optional<BufferedImage> imageOpt =
            imageTransformer.getImage();

    if (imageOpt.isPresent()) {
      BufferedImage image = imageOpt.get();
      List<Map<String, Object>> allBoxesMap = imageTransformer.getAllBoxCoordinates();

      for (Map<String, Object> box : allBoxesMap) {

        BoxCoordinates boxCoordinates = imageTransformer.getBoxCoordinates(image, box);

        Graphics2D graph = image.createGraphics();
        int alpha = 180;
        Color color = new Color(133, 148, 229, alpha);
        graph.setColor(color);
        graph.fill(new Rectangle(boxCoordinates.getX(), boxCoordinates.getY(), boxCoordinates.getWidth(),
                boxCoordinates.getHeight()));
        graph.dispose();

      }

      Optional<byte[]> finalImage = imageTransformer.makeImage(image);

      if (finalImage.isPresent()) {
        org.streampipes.model.runtime.Event event = new org.streampipes.model.runtime.Event();
        event.addField("image", Base64.getEncoder().encodeToString(finalImage.get()));
        out.collect(event);
      }
    }

  }

  @Override
  public void onDetach() {

  }
}
