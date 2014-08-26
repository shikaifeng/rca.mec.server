package tv.zhiping.common.main;

import com.jfinal.core.JFinal;

public class MainStart {
	public static void main(String[] args) {
		JFinal.start("WebRoot", 8888, "/", 5);
	}
}