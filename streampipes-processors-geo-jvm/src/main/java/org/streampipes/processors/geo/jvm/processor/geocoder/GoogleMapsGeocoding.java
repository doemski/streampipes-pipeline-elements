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
package org.streampipes.processors.geo.jvm.processor.geocoder;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.streampipes.commons.exceptions.SpRuntimeException;
import org.streampipes.model.runtime.Event;
import org.streampipes.wrapper.context.EventProcessorRuntimeContext;
import org.streampipes.wrapper.routing.SpOutputCollector;
import org.streampipes.wrapper.runtime.EventProcessor;

import java.io.IOException;

public class GoogleMapsGeocoding implements EventProcessor<GoogleMapsGeocodingParameters> {

  private GeoApiContext context;
  private String placeField;

  @Override
  public void onInvocation(GoogleMapsGeocodingParameters parameters, SpOutputCollector spOutputCollector, EventProcessorRuntimeContext runtimeContext) throws SpRuntimeException {
    this.placeField = parameters.getPlaceField();
    String googleMapsApiKey = parameters.getGoogleMapsApiKey();

    if (googleMapsApiKey == null || googleMapsApiKey.equals("")) {
      throw new SpRuntimeException("Could not start Geocoder. Did you forget to add a Google Maps" +
              " API key?");
    }

    this.context = new GeoApiContext.Builder()
            .apiKey(googleMapsApiKey)
            .build();
  }

  @Override
  public void onEvent(Event event, SpOutputCollector collector) throws SpRuntimeException {
    String placename = event.getFieldBySelector(placeField).getAsPrimitive().getAsString();

    try {
      GeocodingResult[] results =  GeocodingApi.geocode(context,
              placename).await();

      Double latitude = results[0].geometry.location.lat;
      Double longitude = results[0].geometry.location.lng;

      event.addField("latitude", latitude);
      event.addField("longitude", longitude);

      collector.collect(event);

    } catch (ApiException | InterruptedException | IOException e) {
      e.printStackTrace();
      throw new SpRuntimeException("Could not fetch geocoding result");
    }



  }

  @Override
  public void onDetach() throws SpRuntimeException {

  }
}
