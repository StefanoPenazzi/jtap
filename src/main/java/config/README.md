<html>
<head>

</head>
<body>
<h1>Config file</h1>
<div align="justify">
Input data as settings and directories are provided to JTAP through the config file. This latter is an .XML file containing all the necessary informations to run run the simulation. 
 
  
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<config>
    <generalConfig>
        <!-- All the temporary files are saved in this directory and deleted when the process is finished -->
        <tempDirectory>/projects/jtap/temp/</tempDirectory>
    </generalConfig>
    <gtfsConfig>
        <gtfsDirectory>/projects/jtap/gtfs/</gtfsDirectory>
    </gtfsConfig>
    <geoLocConfig>
        <citiesFile>/projects/jtap/cities/fr.csv</citiesFile>
    </geoLocConfig>
    <neo4jConfig>
        <neo4jInstallationDirectory>/.config/Neo4j Desktop/Application/relate-data/dbmss/dbms-0000c47-9976-0000-0000-cc4356662698bb</neo4jInstallationDirectory>
    </neo4jConfig>
    <roadOsmConfig>
       <osmFile>/projects/transit/neo4j/Scenarios/map</osmFile>
    </roadOsmConfig>
</config>
```
 
In order to add new information that can be used at runtime in JTAP the following procedure must be respected:
 
<ul>
  <li>Create a new element and its children in the config file
 
   ```
   <config>
      <newElement>
           <firstChild>hello</firstChild>
           <secondChild>JTAP</secondChild>
      </newElement>
   </config>
   ```
  </li>
  <li></li>
  <li></li>
</ul>
 
</div>
</body>
</html>
