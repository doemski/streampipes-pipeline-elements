/*
Copyright 2018 FZI Forschungszentrum Informatik

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
package org.streampipes.processors.siddhi.filter;

import org.streampipes.wrapper.siddhi.engine.SiddhiEventEngine;

import java.util.List;

public class NumericalFilter extends SiddhiEventEngine<NumericalFilterParameters> {

  @Override
  protected String fromStatement(List<String> inputStreamNames, NumericalFilterParameters params) {
    String filterProperty = prepareName(params.getFilterProperty());
    return "from " + inputStreamNames.get(0) +"[" + filterProperty +"<" + params.getThreshold() +"]";
  }

  @Override
  protected String selectStatement(NumericalFilterParameters bindingParameters) {
    return "select *";
  }

}
