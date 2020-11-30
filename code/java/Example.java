// Add your the driver dependency to your pom.xml build.gradle etc.
// Java Driver Dependency: http://search.maven.org/#artifactdetails|org.neo4j.driver|neo4j-java-driver|4.0.1|jar
// Reactive Streams http://search.maven.org/#artifactdetails|org.reactivestreams|reactive-streams|1.0.3|jar
// download jars into current directory
// java -cp "*" Example.java

import org.neo4j.driver.*;
import static org.neo4j.driver.Values.parameters;

public class Example {

  public static void main(String...args) {

    Driver driver = GraphDatabase.driver("neo4j+s://demo.neo4jlabs.com:7687",
              AuthTokens.basic("northwind","northwind"));

    try (Session session = driver.session(SessionConfig.forDatabase("northwind"))) {

      String cypherQuery =
        "MATCH (p:Product)-[:PART_OF]->(:Category)-[:PARENT*0..]->" +
        "(:Category {categoryName:$category})" +
        "RETURN p.productName as product";

      var result = session.readTransaction(
        tx -> tx.run(cypherQuery, 
                parameters("category","Dairy Products"))
            .list());

      for (Record record : result) {
        System.out.println(record.get("product").asString());
      }
    }
    driver.close();
  }
}


