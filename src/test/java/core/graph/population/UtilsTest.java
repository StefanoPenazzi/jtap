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
		core.graph.Activity.Utils.insertActivitiesFromCsv(config.getNeo4JConfig().getDatabase(),config,ActivityNode.class);
		
		Utils.insertStdPopulationFromCsv(config.getNeo4JConfig().getDatabase(),controller.getInjector().getInstance(Config.class),StdAgentImpl.class);
	}
	
	@Test
	void testDeletePop() throws Exception {
		Config config = Config.of(new File("/home/stefanopenazzi/projects/jtap/config_.xml")); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		Utils.deletePopulation("france2");
		core.graph.Activity.Utils.deleteActivities("france2");
	}
}
