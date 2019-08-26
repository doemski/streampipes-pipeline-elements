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
 */

package org.streampipes.sinks.databases.jvm.iotdb;

import org.streampipes.commons.exceptions.SpRuntimeException;
import org.streampipes.logging.api.Logger;
import org.streampipes.model.runtime.Event;
import org.streampipes.model.schema.EventProperty;
import org.streampipes.model.schema.EventPropertyPrimitive;
import org.streampipes.sinks.databases.jvm.jdbcclient.JdbcClient;
import org.streampipes.vocabulary.XSD;
import org.streampipes.wrapper.context.EventSinkRuntimeContext;
import org.streampipes.wrapper.runtime.EventSink;

import java.sql.SQLException;
import java.sql.Statement;

public class IotDb extends JdbcClient implements EventSink<IotDbParameters> {

  private static Logger LOG;
  int counter = 0;

  @Override
  public void onInvocation(IotDbParameters parameters, EventSinkRuntimeContext runtimeContext) throws SpRuntimeException {
    LOG = parameters.getGraph().getLogger(IotDb.class);

    // get(0) because it is the only input stream of the sink (and not two)
    // tablename is the identifier for the storage group in the IoTDB Adapter (e.g. root.data.table1) in which all
    // time series are written
    //TODO: Add better regular expression
    initializeJdbc(
            parameters.getGraph().getInputStreams().get(0).getEventSchema().getEventProperties(),
            parameters.getIotDbHost(),
            parameters.getIotDbPort(),
            "",         // Database does not exist in  IoTDB model
            "root." + parameters.getDbStorageGroup(),
            parameters.getUsername(),
            parameters.getPassword(),
            ".*",
            "org.apache.iotdb.jdbc.IoTDBDriver",
            "iotdb",
            LOG);
  }

  @Override
  public void onEvent(Event event) {
    try {
      event.addField("timestamp", counter++);
      if (event.getRaw().containsKey("value")) {
        // Renaming value. Very ugly
        event.addField("value_1", event.getFieldBySelector("s0::value").getRawValue());
        event.removeFieldBySelector("s0::value");
      }
      save(event);
    } catch (SpRuntimeException e) {
      LOG.error(e.getMessage());
    }
  }

  @Override
  public void onDetach() throws SpRuntimeException {
    closeAll();
  }

  /*@Override
  protected void save(final Event event) throws SpRuntimeException {
    checkConnected();
    // Needs to enrich the event with a timestamp, if it does not exist
    //TODO: Add batch support
    event.addField("timesstamp", 0);
  }*/

  @Override
  protected void ensureDatabaseExists(String url, String databaseName) throws SpRuntimeException {
    checkRegEx(tableName, "Storage Group name");
    try {
      Statement statement = c.createStatement();
      statement.execute("SET STORAGE GROUP TO " + tableName);
    } catch (SQLException e) {
      // Storage group already exists
      //TODO: Catch other exceptions
    }
  }

  /**
   * Needs to be reimplemented since the IoTDB JDBC implementation does not support the methods used in the
   * JDBC-Client class
   *
   * @param url The JDBC url containing the needed information (e.g. "jdbc:iotdb://127.0.0.1:6667/")
   * @throws SpRuntimeException
   */
  @Override
  protected void ensureTableExists(String url, String databaseName) throws SpRuntimeException {
    StringBuilder statement1 = new StringBuilder("INSERT INTO ").append(tableName).append(" (timestamp, ");
    StringBuilder statement2 = new StringBuilder("VALUES (?, ");
    int index = 1;
    parameters.put("timestamp", new Parameterinfo(index++, SqlAttribute.INTEGER));
    for (EventProperty eventProperty : eventProperties) {
      try {
        Statement statement = null;
        statement = c.createStatement();
        // The identifier cannot be called "value"
        //TODO: Do not simply add a _1 but look instead, if the name is already taken
        String runtimeName = eventProperty.getRuntimeName();
        if (eventProperty.getRuntimeName().equals("value")) {
          runtimeName = "value_1";
        }
        String datatype = extractAndAddEventPropertyRuntimeType(eventProperty, index++);
        statement1.append(eventProperty.getRuntimeName()).append(", ");
        statement2.append("?, ");

        statement.execute("CREATE TIMESERIES "
                + tableName
                + "."
                + runtimeName
                + " WITH DATATYPE="
                + datatype
                + ", ENCODING=PLAIN");
      } catch (SQLException e) {
        // Probably because it already exists
        //TODO: Add better exception handling
        e.printStackTrace();
      }
    }
    // Removing the space and comma at the end
    statement1.setLength(Math.max(0, statement1.length() - 2));
    statement2.setLength(Math.max(0, statement2.length() - 2));
    // Finish preparedStatement
    String finalStatement = statement1.append(") ").append(statement2.append(")")).toString();
    try {
      ps = c.prepareStatement(finalStatement);
    } catch (SQLException e) {
      throw new SpRuntimeException("Could not initialize prepared statement: " + e.getMessage());
    }
    tableExists = true;
  }

  private String extractAndAddEventPropertyRuntimeType(EventProperty eventProperty, int index) {
    // Supported datatypes can be found here: https://iotdb.apache.org/#/Documents/0.8.0/chap2/sec2
    String re;
    if (eventProperty instanceof EventPropertyPrimitive) {
      String runtimeType = ((EventPropertyPrimitive)eventProperty).getRuntimeType();
      if (runtimeType.equals(XSD._integer.toString())) {
        parameters.put(eventProperty.getRuntimeName(), new Parameterinfo(index, SqlAttribute.INTEGER));
        re = "INT32";
      } else if (runtimeType.equals(XSD._long.toString())) {
        parameters.put(eventProperty.getRuntimeName(), new Parameterinfo(index, SqlAttribute.LONG));
        re = "INT64";
      } else if (runtimeType.equals(XSD._float.toString())) {
        parameters.put(eventProperty.getRuntimeName(), new Parameterinfo(index, SqlAttribute.FLOAT));
        re = "FLOAT";
      } else if (runtimeType.equals(XSD._double.toString())) {
        parameters.put(eventProperty.getRuntimeName(), new Parameterinfo(index, SqlAttribute.DOUBLE));
        re = "DOUBLE";
      } else if (runtimeType.equals(XSD._boolean.toString())) {
        parameters.put(eventProperty.getRuntimeName(), new Parameterinfo(index, SqlAttribute.BOOLEAN));
        re = "BOOLEAN";
      } else {
        parameters.put(eventProperty.getRuntimeName(), new Parameterinfo(index, SqlAttribute.STRING));
        re = "TEXT";
      }
    } else {
      // TODO: Add listed and nested items
      re = "TEXT";
    }
    return re;
  }
}
