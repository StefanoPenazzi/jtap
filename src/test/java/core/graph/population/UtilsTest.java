package core.graph.population;

import java.io.File;
import org.junit.jupiter.api.Test;
import config.Config;
import controller.Controller;
import core.graph.Activity.ActivityNode;

class UtilsTest {

	@Test
	void test() throws Exception {
		Config config = Config.of(new File("/home/stefanopenazzi/projects/jtap/config_.xml")); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		//activities first
		core.graph.Activity.Utils.insertActivitiesFromCsv(ActivityNode.class);
		Utils.insertStdPopulationFromCsv(StdAgentNodeImpl.class);
	}
	
	@Test
	void testDeletePop() throws Exception {
		Config config = Config.of(new File("/home/stefanopenazzi/projects/jtap/config_.xml")); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		Utils.deletePopulation();
		core.graph.Activity.Utils.deleteActivities();
	}
}
