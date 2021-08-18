package core.graph.road.osm;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UtilsTest {

	@Test
	void setOSMRoadNetworkIntoNeo4jTest() throws Exception {
		core.graph.road.osm.Utils.setOSMRoadNetworkIntoNeo4j("france2");
	}

}
