package tv.zhiping.mec.testbase;


import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.jfinal.core.JFinal;

public class MecTestBase {
	public final static int port = 8888;
	public static boolean init_success = false;

	@BeforeTest
	public void start() throws Exception {
		if (init_success) {
			return;
		}

		new Thread() {
			public void run() {
				JFinal.start("WebRoot", port, "/", 5);
			}
		}.start();

		Thread.sleep(5000L);
		init_success = true;
	}

	@AfterTest
	public void destory() {
		JFinal.stop();
	}
}
