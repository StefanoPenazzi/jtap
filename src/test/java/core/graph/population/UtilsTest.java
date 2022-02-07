package core.graph.population;

import java.io.File;
import org.junit.jupiter.api.Test;
import config.Config;
import controller.Controller;

class UtilsTest {

	@Test
	void test() throws Exception {
		Config config = Config.of(new File("/home/stefanopenazzi/projects/jtap/config_.xml")); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		Utils.insertStdPopulationFromCsv("france2",controller.getInjector().getInstance(Config.class),StdAgentImpl.class);
	}
	
	@Test
	void testDeletePop() throws Exception {
		Config config = Config.of(new File("/home/stefanopenazzi/projects/jtap/config_.xml")); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		Utils.deletePopulation("france2");
	}
}
