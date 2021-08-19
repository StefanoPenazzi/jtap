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
The database can be queried using
  
```
try( Neo4jConnection conn = new Neo4jConnection()){  
 List<Record> records = conn.query(database,"your query",AccessMode.READ );
}
```
</div>
  
<h1>Insert nodes</h1>
<div align="justify">
In order to add new nodes to the database it is necessary a class annotated with

```	
@Neo4JNodeElement(labels={"Neo4j Label1","Neo4j Label2"})
```
which implements NodeI. Moreover, each field of the class that needs to be added as a property of the node in neo4j need to be annotated with

```
@Neo4JPropertyElement(key="Neo4J property key",type=Neo4JType.TOSTRING)
```
The second parameter of the annotation is the type of the property in neo4j. 
All the queries that require a consistent transfer of data are performed using a bulk data import. This means that a CSV file representing the list of nodes (and their properties) is used to load the nodes in neo4j. Therefore, each field is also annotated with 

```
@CsvBindByName(column = "property name in CSV")
```
The column name in the CSV file can be differnet from the key name in neo4j. 
  
```
@Neo4JNodeElement(labels={"CityNode"})
public class City implements NodeGeoI{
	
	@CsvBindByName(column = "city")
	@Neo4JPropertyElement(key="city",type=Neo4JType.TOSTRING)
	private String city;
	
	@CsvBindByName(column = "lat")
	@Neo4JPropertyElement(key="lat",type=Neo4JType.TOFLOAT)
	private Double lat;
	
	@CsvBindByName(column = "lng")
	@Neo4JPropertyElement(key="lon",type=Neo4JType.TOFLOAT)
	private Double lon;
	
	@CsvBindByName(column = "country")
	@Neo4JPropertyElement(key="country",type=Neo4JType.TOSTRING)
	private String country;
	
	@CsvBindByName(column = "population")
	@Neo4JPropertyElement(key="population",type=Neo4JType.TOINTEGER)
	private Integer population;
	
	public String getId() {
		return this.city;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public Double getLat() {
		return this.lat;
	}
	
	public Double getLon() {
		return this.lon;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public Integer getPopulation() {
		return this.population;
	}
  
}
```

The class <i>data.external.neo4j</i> contains static methods to add nodes in the database.
Nodes from a List can be added to the database through the static method 
```
public static <T extends NodeI> void insertNodes(String database,String tempDirectory,List<T> nodes)
```
Nodes from a CSV file can be added to the database through the static method 
```
public static <T extends NodeI> void insertNodes(String database,String tempDirectory,String fileCsv)
```
These methods require also a temporary directory in which save the CSV file created for the bulk data import.
	
It is worth noting that the search queries performance is heavily affected by the use of indexes. Create a single-property index for nodes is highly recommended.
This can be don in JTAP using

```
try( Neo4jConnection conn = new Neo4jConnection()){  
    conn.query(database,"CREATE INDEX IndexName FOR (n:NodeLabel) ON (n.NodePropertyKey)",AccessMode.WRITE);
}
```

	
</div>
  
<h1>Insert links</h1>
<div align="justify">
	
In order to add new links to the database it is necessary a class annotated with
```	
@Neo4JLinkElement(label="Neo4J label")
```
which implements LinkI. Only one label is allowed per link, in contrast to the node which allows multiple labels .Moreover, each field of the class that needs to be added as a property of the node in neo4j need to be annotated with

```
@Neo4JPropertyElement(key="Neo4J property key",type=Neo4JType.TOSTRING)
```
The second parameter of the annotation is the type of the property in neo4j. 
All the queries that require a consistent transfer of data are performed using a bulk data import. This means that a CSV file representing the list of nodes (and their properties) is used to load the nodes in neo4j. Therefore, each field is also annotated with 

```
@CsvBindByName(column = "property name in CSV")
```
The column name in the CSV file can be differnet from the key name in neo4j. 
  
```
	
```
@Neo4JLinkElement(label="CrossLink")
public class CrossLink implements LinkI {
	
	@CsvBindByName(column = "from")
	@Neo4JPropertyElement(key="from",type=Neo4JType.TOSTRING)
	private String from;
	
	@CsvBindByName(column = "to")
	@Neo4JPropertyElement(key="to",type=Neo4JType.TOSTRING)
	private String to;
	
	@CsvBindByName(column = "distance")
	@Neo4JPropertyElement(key="distance",type=Neo4JType.TOINTEGER)
	private Integer distance;
	
	@CsvBindByName(column = "avg_travel_time")
	@Neo4JPropertyElement(key="avg_travel_time",type=Neo4JType.TOINTEGER)
	private Integer avgTravelTime;
	
	public CrossLink() {}
	
	public CrossLink( String from,String to,Integer distance,Integer avgTravelTime) {
		this.from = from;
		this.to = to;
		this.distance = distance;
		this.avgTravelTime = avgTravelTime;
	}
	
	public String getFrom() {
		return this.from;
	}
	
	public String getTo(){
		return this.to; 
	}
	
	public Integer getDistance(){
		return this.distance; 
	}
	
	public Integer getAvgTravelTime(){
		return this.avgTravelTime; 
	}
}
```
	
The class <i>data.external.neo4j</i> contains static methods to add links in the database.
Links from a List can be added to the database through the static method 
```
public static <T extends LinkI> void insertLinks(String database,String tempDirectory,List<T> links,Class<? extends LinkI> linkElement, Class<? extends NodeI> sfNodeElement, String sfProperty, String sfCsvProperty,Class<? extends NodeI> stNodeElement, String stProperty, String stCsvProperty) 
```
Links from a CSV file can be added to the database through the static method 
```
public static <T extends LinkI> void insertLinks(String database,String tempDirectory,String fileCsv,Class<? extends LinkI> linkElement, Class<? extends NodeI> sfNodeElement, String sfProperty, String sfCsvProperty,Class<? extends NodeI> stNodeElement, String stProperty, String stCsvProperty)
```
These methods require also a temporary directory in which save the CSV file created for the bulk data import.
	
It is worth noting that if the node property selected to search the start/end node of a link is not a property index this query will be very slow.  
</div>
 
  
</body>
</html>
