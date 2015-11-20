/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zeppelin.spark;

import org.apache.zeppelin.interpreter.*;
import org.apache.zeppelin.scheduler.Scheduler;
import org.apache.zeppelin.scheduler.SchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;

/**
 * R and SparkR interpreter.
 */
public class SparkRInterpreter extends Interpreter {
  private static final Logger logger = LoggerFactory.getLogger(SparkRInterpreter.class);

  static {
    Interpreter.register(
      "r",
      "spark",
      SparkRInterpreter.class.getName(),
      new InterpreterPropertyBuilder()
        .add("spark.master",
                SparkInterpreter.getSystemDefault("MASTER", "spark.master", "local[*]"),
                "Spark master uri. ex) spark://masterhost:7077")
        .add("spark.home",
                SparkInterpreter.getSystemDefault("SPARK_HOME", "spark.home", "/opt/spark"),
                "Spark distribution location")
        .build());
  }

  public SparkRInterpreter(Properties property) {
    super(property);
  }

  @Override
  public void open() {
    ZeppelinR.open(getProperty("spark.master"), getProperty("spark.home"));
  }

  @Override
  public InterpreterResult interpret(String lines, InterpreterContext contextInterpreter) {
    BufferedWriter writer = null;
    try {
      File in = File.createTempFile("forKnitR-" +
              contextInterpreter.getParagraphId(), ".Rmd");
      String inPath = in.getAbsolutePath().substring(0, in.getAbsolutePath().length() - 4);
      File out = new File(inPath + ".html");

      writer = new BufferedWriter(new FileWriter(in));
      writer.write("\n```{r comment=NA, echo=FALSE}\n" + lines + "\n```");
      writer.close();

      String rcmd = "knit2html('" + in.getAbsolutePath() + "', output = '"
              + out.getAbsolutePath() + "')" + "\n";

      ZeppelinR.eval(rcmd);

      String html = new String(Files.readAllBytes(out.toPath()));

      // Only keep the bare results.
      String htmlOut = html.substring(html.indexOf("<body>") + 7, html.indexOf("</body>") - 1)
              .replaceAll("<code>", "").replaceAll("</code>", "")
              .replaceAll("\n\n", "")
              .replaceAll("\n", "<br>")
              .replaceAll("<pre>", "<p class='text'>").replaceAll("</pre>", "</p>");

      return new InterpreterResult(InterpreterResult.Code.SUCCESS, "%html\n" + htmlOut);
    } catch (Exception e) {
      logger.error("Exception while connecting to R", e);
      return new InterpreterResult(InterpreterResult.Code.ERROR, e.getMessage());
    } finally {
      try {
        writer.close();
      } catch (Exception e) {
        // Do nothing...
      }
    }
  }

  @Override
  public void close() {
    ZeppelinR.close();
  }

  @Override
  public void cancel(InterpreterContext context) {}

  @Override
  public FormType getFormType() {
    return FormType.NONE;
  }

  @Override
  public int getProgress(InterpreterContext context) {
    return 0;
  }

  @Override
  public Scheduler getScheduler() {
    return SchedulerFactory.singleton().createOrGetFIFOScheduler(
            SparkRInterpreter.class.getName() + this.hashCode());
  }

  @Override
  public List<String> completion(String buf, int cursor) {
    return new ArrayList<String>();
  }

}
