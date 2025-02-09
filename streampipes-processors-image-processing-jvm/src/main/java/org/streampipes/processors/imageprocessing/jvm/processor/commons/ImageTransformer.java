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
package org.streampipes.processors.imageprocessing.jvm.processor.commons;

import org.streampipes.model.runtime.Event;
import org.streampipes.model.runtime.field.AbstractField;
import org.streampipes.processors.imageprocessing.jvm.processor.imageenrichment.BoxCoordinates;
import org.streampipes.processors.imageprocessing.jvm.processor.imageenrichment.ImageEnrichmentParameters;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

public class ImageTransformer extends PlainImageTransformer<ImageEnrichmentParameters> {

  public ImageTransformer(Event in, ImageEnrichmentParameters params) {
    super(in, params);
  }

  public Optional<BufferedImage> getImage() {

    return getImage(params.getImageProperty());
  }

  public List<Map<String, Object>> getAllBoxCoordinates() {
    List<Map<String, AbstractField>> allBoxes = in.getFieldBySelector(params.getBoxArray())
            .getAsList()
            .parseAsCustomType(value -> value.getAsComposite().getRawValue());

    List<Map<String, Object>> allBoxesMap = new ArrayList<>();
    allBoxes.forEach(box -> {
      Map<String, Object> boxMap = new HashMap<>();
      box.forEach((key, value) -> boxMap.put(value.getFieldNameIn(), value.getRawValue()));
      allBoxesMap.add(boxMap);
    });

    return allBoxesMap;
  }

  public BoxCoordinates getBoxCoordinates(BufferedImage image, Map<String, Object> box) {
    Float x = toFloat(box.get(params.getBoxX()));
    Float y = toFloat(box.get(params.getBoxY()));
    Float width = toFloat(box.get(params.getBoxWidth()));
    Float height = toFloat(box.get(params.getBoxHeight()));

    return BoxCoordinates.make(width, height, x, y);
  }

  private Float toFloat(Object obj) {
    return Float.parseFloat(String.valueOf(obj));
  }


  public Optional<byte[]> makeImage(BufferedImage image) {

    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(image, "jpg", baos);
      baos.flush();
      byte[] finalImage = baos.toByteArray();
      baos.close();
      return Optional.of(finalImage);
    } catch (IOException e) {
      e.printStackTrace();
      return Optional.empty();
    }

  }
}
