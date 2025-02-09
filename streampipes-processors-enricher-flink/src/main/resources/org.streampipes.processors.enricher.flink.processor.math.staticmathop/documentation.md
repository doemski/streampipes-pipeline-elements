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

## Static Math

<p align="center"> 
    <img src="icon.png" width="150px;" class="pe-image-documentation"/>
</p>

***

## Description

Performs calculation on an event property with a static value (+, -, *, /, %).

***

## Required input
The math processor works with any event that has at least one field containing a numerical value.

***

## Configuration

### Left operand
The field from the input event that should be used as the left operand.

### Right operand value
Specify the value of the right operand.

### Operation
The math operation that should be performed.

## Output
The processor appends the calculation result to each input event.