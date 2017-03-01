import java.io.StringReader
import java.util.Properties

import com.thinkaurelius.titan.core.util.TitanCleanup
import com.thinkaurelius.titan.graphdb.database.StandardTitanGraph
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*

import org.apache.tinkerpop.gremlin.structure.Vertex
import com.thinkaurelius.titan.core.*

println("Connect Example")

url = this.getClass().getResource("dynamodb.properties")

println(url)
configFile = url.getPath()
println(configFile)
titanGraph = TitanFactory.open(configFile)
g = titanGraph.traversal()
c = g.V().count()
println(c)
println(c.toList())

println(g.V().hasLabel("name", "tuxdna").out("knows").count().toList())

// mgmt = titanGraph.openManagement()

// mgmt.makePropertyKey("")

g.close()
titanGraph.close()
System.exit(0)

