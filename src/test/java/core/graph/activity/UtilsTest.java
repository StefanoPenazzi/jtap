package core.graph.activity;

import java.io.File;
import org.junit.jupiter.api.Test;
import config.Config;
import controller.Controller;
import projects.CTAP.graphElements.ActivityCityLink;

class UtilsTest {

	@Test
	void test() throws Exception {
		Config config = Config.of(new File("/home/stefanopenazzi/projects/jtap/config_.xml")); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		//activities first
		core.graph.Activity.Utils.insertActivitiesLocFromCsv(ActivityCityLink.class);
		
	}

}
