package org.bigBrotherBooks;

import io.quarkus.test.junit.QuarkusTest;
//import org.apache.tinkerpop.gremlin.driver.Cluster;
//import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
//import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource;
//import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
//import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
//import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ResourceRestApiTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("Hello from Quarkus REST"));
    }

//    @Test
//    void testTinkerPopEndpoint() {
//        Cluster cluster = Cluster.build()
//                .addContactPoint("db-harshil-neptune.cluster-ro-ch22yeqgiuw7.eu-north-1.neptune.amazonaws.com")
//                .port(8182)
//                .create();
//
//        GraphTraversalSource g = AnonymousTraversalSource.traversal().withRemote(DriverRemoteConnection.using(cluster));
//        // check if the database is accessible or not
//        GraphTraversal<Vertex, Vertex> t = g.V().hasLabel("person");
//        System.out.println("Accessed it");
//        System.out.println(t.hasNext());
//        System.out.println("Accessed it");
//        cluster.close();
//    }

}