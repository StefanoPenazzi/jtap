
<html>
<head>
  
</head>
<body>
  
<h1>Requirements</h1>
 <ul>
  <li>An installation of Neo4j (>4.2.5) <a href="https://neo4j.com/download/"> neo4j download</a> </li>
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

	

	2) osmium tags-filter liechtenstein-latest.osm.pbf w/highway r/type=restriction -o filtered.osm.pbf -f pbf,add_metadata=false
        2.1) osmium tags-filter mayotte-latest.osm.bz2 w/highway=primary w/highway= w/highway=trunk w/highway=motorway r/type=restriction -o mayotte-road-latest.osm.bz2 -f osm.bz2,add_metadata=false
        2.2) osmium tags-filter france-latest.osm.pbf w/highway=primary w/highway=primary_link w/highway=trunk w/highway=trunk_link w/highway=motorway w/highway=motorway_junction w/highway=motorway_link r/type=restriction -o france-road-latest.osm.bz2 -f osm.bz2,add_metadata=falseD
  
</div>
  

  
  

  
</body>
</html>
