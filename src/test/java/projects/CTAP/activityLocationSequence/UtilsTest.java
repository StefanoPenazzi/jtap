package projects.CTAP.activityLocationSequence;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import config.Config;
import controller.Controller;

class UtilsTest {

	@Test
	void test() throws Exception {
		Config config = Config.of (Paths.get("/home/clivings/Documents/EU_TRANSIT/Data/inputFiles_cvl_tests/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		
		projects.CTAP.activityLocationSequence.Utils.insertDestinationProbIntoNeo4j();
		
		System.out.println();
	}

}
