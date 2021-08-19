<html>
<head>
  
</head>
<body>
  
<p align="center">
  <img width="1000" height="250" src="/src/main/resources/images/graph/gen_graph.PNG">
</p>
  
<h1>Agents environment</h1>
<div align="justify">
Agents interact with their environment in addition to other agents. The environment in jtap is a network. Agents move in the network from one node to an other using links.
In order to exploit the maximum potential (efficiency and reusability) of the network, a graph database is used to create, store, and query the network. We decided to use neo4j to fulfil these tasks. It is therefore necessary to install and connect neo4j to the library.
</div>
  
<h1>Install Neo4J</h1>
<div align="justify">
You can download neo4j desktopt from <a href=""https://neo4j.com/download/"> here </a>
  
If you want to get familiar with neo4j you can find a nice tutorial <a href="https://neo4j.com/developer/get-started/"> here </a>
</div>
  
<h1>Create a new database</h1>
<div align="justify">
If you are using neo4j desktop, we suggest to create a new project and dbms. In the dbms you are able to create a new database that we call here HelloJTAP. It is foundamenta that in the next steps HelloJTAP is running when we perform the queries on it through the API. 
Using neo4j browser you can verify that HelloJTAP is been created and it is empty.

```
:USE HelloJTAP
```
```
CALL db.schema.visualization()
```

</div>
  
<h1>Query Neo4j from JTAP</h1>
<div align="justify">

To query neo4j from JTAP a java driver is used <i> org.neo4j.driver </i>. The first thing to do is create a new properties file named <i> database.properties </i> in src/main/resources . In the file you need to add the neo4j_uri, neo4j_username, neo4j_password.
```
neo4j_uri=neo4j://localhost:5763
neo4j_username=hello
neo4j_password=jtap
```
This informations are used in the class <i> data.external.neo4j.Neo4jConnection </i> to access the database. 
  
```
try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,"your query",AccessMode.READ );
}
```
  
</div>
 
  
</body>
</html>
