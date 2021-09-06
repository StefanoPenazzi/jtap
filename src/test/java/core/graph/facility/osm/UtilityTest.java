package core.graph.facility.osm;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import core.graph.NodeGeoI;
import core.graph.geo.City;

class UtilityTest {

	@Test
	void neo4jOSMFacilityTest() throws Exception {
		//core.graph.facility.osm.Utils.facilitiesIntoNeo4j("france2");
		
		Map<Class<? extends NodeGeoI>,String> facilityConnMap = new HashMap<>();
		facilityConnMap.put(City.class,"city");
		core.graph.Utils.setShortestDistCrossLink("france2", "/home/stefanopenazzi/projects/jtap/temp/",FacilityNode.class,"node_osm_id",facilityConnMap,true);
	}

}
