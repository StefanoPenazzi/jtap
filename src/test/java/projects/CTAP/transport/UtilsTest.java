package projects.CTAP.transport;

import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import config.Config;
import controller.Controller;

class UtilsTest {

	@Test
	void CTAPTransportLinkFactoryTest() throws Exception {
		Config config = Config.of (Paths.get("/home/stefanopenazzi/projects/jtap/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();	
		projects.CTAP.transport.Utils.insertCTAPTransportLinkFactory();
	}
}
