package projects.CTAP;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import config.Config;
import controller.Controller;

class CityFacStatNodeTest {

	@Test
	void deleteCityFacStatNodeTest() throws Exception {
		Config config = Config.of (Paths.get("/home/clivings/Documents/EU_TRANSIT/Data/inputFiles_cvl_tests/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		core.graph.geo.Utils.deleteCityFacStatNode();
		System.out.println();
	}
	
	@Test
	void addCityFacStatNodeWithHistoricalTest() throws Exception {
		Config config = Config.of (Paths.get("/home/clivings/Documents/EU_TRANSIT/Data/inputFiles_cvl_tests/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		core.graph.geo.Utils.addCityFacStatNodeWithHistorical();
		System.out.println();
	}
	
	

}
