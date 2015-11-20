# Apache Zeppelin R

This adds [R](http://cran.r-project.org) interpeter to the [Apache Zeppelin notebook](http://zeppelin.incubator.apache.org).

It support R, SparkR, Scala to R binding, R to Scala binding, cross paragraph variables and R plot (ggplot2...).

## Simple R

[![Simple R](https://raw.githubusercontent.com/datalayer/zeppelin-R/rscala/_Rimg/simple-r.png)](https://raw.githubusercontent.com/datalayer/zeppelin-R/rscala/_Rimg/simple-r.png)

## Plot

[![Plot](https://raw.githubusercontent.com/datalayer/zeppelin-R/rscala/_Rimg/plot.png)](https://raw.githubusercontent.com/datalayer/zeppelin-R/rscala/_Rimg/plot.png)

## Scala R Binding

[![Scala R Binding](https://raw.githubusercontent.com/datalayer/zeppelin-R/rscala/_Rimg/scala-r.png)](https://raw.githubusercontent.com/datalayer/zeppelin-R/rscala/_Rimg/scala-r.png)

## R Scala Binding

[![R Scala Binding](https://raw.githubusercontent.com/datalayer/zeppelin-R/rscala/_Rimg/r-scala.png)](https://raw.githubusercontent.com/datalayer/zeppelin-R/rscala/_Rimg/r-scala.png)

## SparkR

[![SparkR](https://raw.githubusercontent.com/datalayer/zeppelin-R/rscala/_Rimg/sparkr.png)](https://raw.githubusercontent.com/datalayer/zeppelin-R/rscala/_Rimg/sparkr.png)

# Prerequisitec

You need to have R (with Rserve, ggplot2 knitr) available on the host running the notebook.

+ For Centos: `yum install R R-devel`
+ For Ubuntu: `apt-get install r-base r-cran-rserve`

Launch R commands tos install the needed packages:

```
R CMD BATCH install.packages("Rserve")
R CMD BATCH install.packages("ggplot2")
R CMD BATCH install.packages("knitr")
```

You also need a [compiled version of Spark 1.5.0](http://archive.apache.org/dist/spark/spark-1.5.0/spark-1.5.0-bin-hadoop2.6.tgz)

# Build and Run

```
mvn clean install -Pspark-1.5 -Dspark.version=1.5.0 -Dhadoop.version=2.7.1 -Phadoop-2.6 -Ppyspark -Dmaven.findbugs.enable=false -Drat.skip=true -Dcheckstyle.skip=true -DskipTests -pl '!flink,!ignite,!phoenix,!postgresql,!tajo,!hive,!cassandra,!lens,!kylin'
SPARK_HOME=/opt/spark-1.5.0-bin-hadoop2.6 ./bin/zeppelin.sh
```

Go to [http://localhost:8080](http://localhost:8080) and test the .

## Get the image from the Docker Repository

[Datalayer](http://datalayer.io) provides an up-to-date Docker image for [Apache Zeppelin](http://zeppelin.incubator.apache.org), the WEB Notebook for Big Data Science.

In order to get the image, you can run with the appropriate rights:

`docker pull datalayer/zeppelin-rscala`

Run the Zeppelin notebook with:

`docker run -it -p 2222:22 -p 8080:8080 -p 4040:4040 datalayer/zeppelin-rscala`

and go to [http://localhost:8080](http://localhost:8080).

# License

Copyright 2015 Datalayer http://datalayer.io

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[![R](http://datalayer.io/ext/images/logo-R-200.png)](http://cran.r-project.org)

[![Apache Zeppelin](http://datalayer.io/ext/images/logo-zeppelin-small.png)](http://zeppelin.incubator.apache.org)

[![Datalayer](http://datalayer.io/ext/images/logo_horizontal_072ppi.png)](http://datalayer.io)
