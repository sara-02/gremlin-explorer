import java.io.StringReader
import java.util.Properties

import com.thinkaurelius.titan.core.util.TitanCleanup
import com.thinkaurelius.titan.graphdb.database.StandardTitanGraph
import org.apache.log4j.BasicConfigurator
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__._

import org.apache.tinkerpop.gremlin.structure.Vertex
import com.thinkaurelius.titan.core._
import com.thinkaurelius.titan.example.GraphOfTheGodsFactory
import org.apache.commons.configuration.BaseConfiguration
import scala.collection.JavaConversions._

import org.apache.tinkerpop.gremlin.console.Console

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object DynamoDBGraphExample {
  val log = LoggerFactory.getLogger(DynamoDBGraphExample.getClass)

  // BasicConfigurator.configure()

  def main(args: Array[String]) {
    log.info("Setting up TitanGraph Instance...")

    val configFile = if (args.length >= 1) {
      args(0)
    }
    else if (System.getProperties.contains("config.file")) {
      System.getProperty("config.file")
    } else {
      val url = getClass.getResource("dynamodb.properties")
      println(url)
      url.getPath()
    }

    println(configFile)

    val titanGraph = TitanFactory.open(configFile)

    val g = titanGraph.traversal()
    println("Node count:")
    val results = g.V().count().toList()
    println(results)

    println(g.V().hasLabel("name", "tuxdna").out("knows").count().toList())

    // val mgmt = titanGraph.openManagement()
    // mgmt.makePropertyKey("")

    g.close()
    titanGraph.close()
    System.exit(0)

  }
}
