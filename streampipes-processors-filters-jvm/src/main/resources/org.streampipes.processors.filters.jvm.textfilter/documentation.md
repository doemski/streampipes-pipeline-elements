<!--

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

-->

## Text Filter

<p align="center"> 
    <img src="icon.png" width="150px;" class="pe-image-documentation"/>
</p>

***

## Description
The Text Filter processor filters text values based on a given string.

***

## Required input
The processor works with any input event that has one field containing a text.

***

## Configuration

### Text Field
The field containing the text that should be filtered.


### Operation
The operation used by the filter processor (equals or matches)

## Output
The processor outputs the input event if it satisfies the filter expression.