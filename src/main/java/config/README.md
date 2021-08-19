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
           <firstChildDirectory>hello</firstChildDirectory>
           <secondChildDirectory>JTAP</secondChildDirectory>
      </newElement>
   </config>
   ```
  </li>
  <li>Create a class that can host the information
 
   ```
  @XmlRootElement(name = "newElement")
  public class NewElementConfig implements Serializable {

      private static final long serialVersionUID = 1L;

      private String firstChildDirectory;
      private String secondChildDirectory;

      public NewElementConfig() {}

      public NewElementConfig(String firstChildDirectory,String secondChildDirectory) {
          this.firstChildDirectory = firstChildDirectory;
      }

      @XmlElement(name = "firstChildDirectory",required = true)
      public String getFirstChildDirectory() {
          return this.firstChildDirectory;
      }

      public void setFirstChildDirectory(String firstChildDirectory) {
          this.firstChildDirectory = firstChildDirectory;
      }
 
      @XmlElement(name = "secondChildDirectory",required = true)
      public String getSecondChildDirectory() {
          return this.secondChildDirectory;
      }

      public void setSecondChildDirectory(String secondChildDirectory) {
          this.secondChildDirectory = secondChildDirectory;
      }
  }
 ```
   </li>

 
  <li></li>
</ul>
 
</div>
</body>
</html>
