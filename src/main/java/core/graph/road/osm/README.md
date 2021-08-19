
<html>
<head>
  
</head>
<body>
  
<h1>Requirements</h1>
 <ul>
  <li>An installation of Neo4j (>4.2.5) <a href="https://neo4j.com/download/"> Neo4j download</a> </li>
  <li>Last version of osmium-tool <a href="https://osmcode.org/osmium-tool/manual.html"> osmium-tool</a></li>
</ul>
  
<div align="justify">
  The Road network model is generated and inmported into a Neo4J database using an external library. 
  The repository and the original tutorial can be found here <a href="https://github.com/neo4j-contrib/osm"> neo4j-contrib/osm </a>.
</div>

<h1>Osmium tool</h1>
<div align="justify">
  Before starting using the <a href="https://github.com/neo4j-contrib/osm"> neo4j-contrib/osm </a>, it is necessary to obtain an OSM file
  containing the road network. 
  Usually the OSM file comes with a huge amount of things that are not related with the road network it self. It is then necessary 
  filter only what we need based on the level of detail of the road network we want to import in Neo4j. In order to do that, we can rely on 
  the <a href="https://osmcode.org/osmium-tool/manual.html"> osmium-tool</a>
  
  Ubuntu:

   	sudo apt install osmium-tool
   
  The first thing to do is dowloading the region/country we are interested into. This is possible using  <a href="https://download.geofabrik.de/europe.html"> geofabrik </a> .
	There are no restriction on the format.

Open the terminal in the same directory in which you saved the OSM file (e.g. france-latest.osm.pbf). The following line shows an example considering only trunk, trunk_link, motorway, motorway_junction, motorway_link. A new file called france-road-latest.osm.bz2 (with osm.bz2 format) is saved in the same directory of france-latest.osm.pbf. 

```
osmium tags-filter france-latest.osm.pbf w/highway=primary w/highway=primary_link w/highway=trunk w/highway=trunk_link w/highway=motorway w/highway=motorway_junction w/highway=motorway_link r/type=restriction -o france-road-latest.osm.bz2 -f osm.bz2,add_metadata=falseD
```
  
</div>

	
<h1>OSM Model in Neo4j</h1>
<div align="justify">
 After you have cloned  <a href="https://github.com/neo4j-contrib/osm"> neo4j-contrib/osm </a> open the teminal in the repository directory (the same directory in which there is the folder /target).
	
```	
java -Xms1280m -Xmx1280m -cp "target/osm-0.2.3-neo4j-4.1.3.jar:target/dependency/*" org.neo4j.gis.osm.OSMImportTool --skip-duplicate-nodes --delete --into "/home/stefanopenazzi/.config/Neo4j Desktop/Application/relate-data/dbmss/dbms-0000000-0000-4493-97b5-0000b6260aa" --database france2 /home/stefanopenazzi/projects/transit/neo4j/Scenarios/map
```	


<ul>
  <li><a href="https://github.com/neo4j-contrib/osm#building"> osm-0.2.3-neo4j-4.1.3-procedures.jar </a></li>
  <li>--into (directory of the dbms. This can be found opening the  <a href="https://neo4j.com/developer/neo4j-desktop/#desktop-open-terminal">dbms terminal</a>) </li>
  <li>--database (name of an already created database in the dbms) </li>
  <li> the last argument is the OSM file you want to import into Neo4J </li>
</ul>

To check that the previous steps have been successful:

<ul>
  <li> Run the dbms </li>
  <li> Use the database containing the OSM model (:use dbname)</li>
  <li> Visualize the schema (call db.schema.visualization())</li>
  <li> Check if the schema contains the following nodes: OSMNode, OSMWayNode, OSMTags, OSMWay, OSM, Bounds</li>
</ul>
	
If the schema is wrong or a new OSM model is required it is possible to follow the same procedure after deleting the relations of the previous one
```
Call apoc.periodic.iterate("cypher runtime=slotted Match (n)-[r:NODE|FIRST_NODE|TAGS|MEMBER|BBOX]->(m) RETURN r limit 10000000", "delete r",{batchSize:100000});
```
and the nodes of the previous one
```
Call apoc.periodic.iterate("cypher runtime=slotted Match (n:OSMNode:OSMWayNode:OSMTags:OSMWay:OSM:Bounds) RETURN n limit 10000000", "delete n",{batchSize:100000});
```
The two previous queries take a while...
	
</div>
  
<h1>Routable road network in Neo4j</h1>
<div align="justify">
To help build graphs that can be used for routing, two procedures can be used:

<ul>
<li>spatial.osm.routeIntersection(node,false,false,false) </li>
<li>spatial.osm.routePointOfInterest(node,ways) </li>
</ul>

These can be installed into an installation of Neo4j by copying the  <a href="https://github.com/neo4j-contrib/osm#building"> osm-0.2.3-neo4j-4.1.3-procedures.jar </a> file into the plugins folder, and restarting the database.

The routable road network can be generated using the static method setOSMRoadNetworkIntoNeo4j in the Utils class 


</div>
  

</body>
</html>
