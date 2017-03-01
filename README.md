# Gremlin Graph Explorer

## Quickstart

### Update configuation

Copy the file to some folder and change as per your requirments

```
cp src/main/resources/dynamodb.properties dynamodb.properties.prod
```

You may need to change following keys in `dynamodb.properties.prod` file

```
storage.dynamodb.client.credentials.class-name=com.amazonaws.auth.BasicAWSCredentials
storage.dynamodb.client.credentials.constructor-args=$AWS_KEY,$AWS_SECRET
storage.dynamodb.client.endpoint=https://dynamodb.us-east-1.amazonaws.com
storage.dynamodb.prefix=your_db_prefix
```

### Using Scala Console


    $ mvn scala:console
    
    Welcome to Scala version 2.11.6 (OpenJDK 64-Bit Server VM, Java 1.8.0_111).
    Type in expressions to have them evaluated.
    Type :help for more information.
    
    scala> import com.thinkaurelius.titan.core._
    import com.thinkaurelius.titan.core._
    
    scala> val titanGraph = TitanFactory.open("dynamodb.properties.prod")
    titanGraph: com.thinkaurelius.titan.core.TitanGraph = standardtitangraph[com.amazon.titan.diskstorage.dynamodb.DynamoDBStoreManager:[127.0.0.1]]
    
    scala> val g = titanGraph.traversal()
    g: org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource = graphtraversalsource[standardtitangraph[com.amazon.titan.diskstorage.dynamodb.DynamoDBStoreManager:[127.0.0.1]], standard]
    
    scala> val results = g.V().count().toList()
    results: java.util.List[Long] = [0]
    
    scala> println(results)
    [0]
    
    scala> println(g.V().hasLabel("name", "tuxdna").out("knows").count().toList())
    [0]
    
    scala> System.exit(0)


### Using CLI App

    $ mvn clean compile
    $ export CP=`mvn dependency:build-classpath | grep -A1 'Dependencies classpath:' | tail -1`:target/classes 
    $ java -cp $CP DynamoDBGraphExample dynamodb.properties.prod
    Setting up TitanGraph Instance...
    dynamodb.properties.prod
    Node count:
    [0]
    [0]


# Setting up the Gremlin Server

Let us assume our base working dir is BASE_PATH


## Build Titan

```
cd $BASE_PATH
git clone https://github.com/tuxdna/titan.git
cd titan
git checkout local-setup
mvn clean install -DskipTests=true -Dgpg.skip=true -Paurelius-release
```

Target zip is here: 

```
titan-dist/titan-dist-hadoop-2/target/titan-1.1.0-SNAPSHOT-hadoop2.zip
```

# Build DynamoDB plugin for Titan

Build plugin

```
cd $BASE_PATH
git clone https://github.com/tuxdna/dynamodb-titan-storage-backend.git
cd dynamodb-titan-storage-backend
git checkout local-setup
mvn install
```


Update dependency path

```
cp $BASE_PATH/titan/titan-dist/titan-dist-hadoop-2/target/titan-1.1.0-SNAPSHOT-hadoop2.zip .

sed -i "s#TITAN_VANILLA_SERVER_ZIP=.*#TITAN_VANILLA_SERVER_ZIP=`pwd`/titan-1.1.0-SNAPSHOT-hadoop2.zip#" src/test/resources/install-gremlin-server.sh
```

Now install gremlin server

```
src/test/resources/install-gremlin-server.sh
```

Install `gremlin-python`

```
cd $BASE_PATH
cd dynamodb-titan-storage-backend
cd server/dynamodb-titan100-storage-backend-1.0.0-hadoop1
bin/gremlin-server.sh -i org.apache.tinkerpop gremlin-python 3.2.3
```


## GremlinServer configuration file

Update properties in the file: `conf/gremlin-server/gremlin-server.yaml`

```
serializedResponseTimeout: 
scriptEvaluationTimeout: 
gremlinPool: 
threadPoolWorker: 
```

If you want to run HTTP instead of WebSocket change the following 

```
channelizer: org.apache.tinkerpop.gremlin.server.channel.HttpChannelizer
```

## DynamoDB configuration file

Update properties in the file: `conf/gremlin-server/dynamodb.properties`

```
storage.dynamodb.client.credentials.class-name=com.amazonaws.auth.BasicAWSCredentials
storage.dynamodb.client.credentials.constructor-args=$AWS_KEY,$AWS_SECRET
storage.dynamodb.client.endpoint=https://dynamodb.us-east-1.amazonaws.com
storage.dynamodb.prefix=your_db_prefix
```

# Now run the server

```
cd $BASE_DIR
cd dynamodb-titan-storage-backend/server/dynamodb-titan100-storage-backend-1.0.0-hadoop1/
bin/gremlin-server.sh conf/gremlin-server/gremlin-server.yaml
```



### References

 * [Titan and TinkerPop 3.2](https://groups.google.com/forum/#!msg/gremlin-users/ajs0C4-0vzY/SEIDhczrAgAJ)
 * [Running Titan + Tinkerpop 3.2 with AWS DynamoDB](https://groups.google.com/forum/#!topic/gremlin-users/O2mP8GOljDY)
 * [AWS Lambda + Tinkerpop/Gremlin + TitanDB on EC2 + AWS DynamoDB in cloud](https://stackoverflow.com/questions/38592053/aws-lambda-tinkerpop-gremlin-titandb-on-ec2-aws-dynamodb-in-cloud)
 * [Titan: Connect to Elasticsearch from another machine](https://stackoverflow.com/questions/36458614/titan-connect-to-elasticsearch-from-another-machine)
