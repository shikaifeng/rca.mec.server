package shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 图片 存在校验 查询
 * java -Dfile.encoding=GBK shell/CheckPicFilePath > java.log &
 * @author 张有良
 */
public class CheckPicFilePath {
	//文件总数
	public static AtomicLong file_count = new AtomicLong();
	//文件大小总数
	public static Long file_size_sum = 0L;
	
	//待转换文件总数
	public static AtomicLong cover_file_count = new AtomicLong();
	//待转换文件大小总数
	public static Long cover_file_size_sum = 0L;
	
	//复制命令 "cp /home/acr/java/tmp/t.sh /home/acr/java/tmp/t1.sh ";
	public static final String COPY_CMD = "cp #orig#  #target#";

	public static final String FILENAME_APTH = "pic_filename.db";
	
	public static Long start_time = System.currentTimeMillis();
	
	/**
	 * 以下参数是需要匹配前动态配置的
	 */
	//输出的目录
	private static PrintWriter shell_out = null;
		
	//要扫描的文件路径
	private static String path_folder = "/data/weitv/static/upload/";
	
    public static void main(String[] args) throws Exception {
        List<String> path_list = getFile_path();
        
        System.out.println(path_list.size());
        shell_out = new PrintWriter("error.log");
        
        checkFile_path(path_list);
        
        System.out.println("执行完毕");
        
        shell_out.close();
    }
 
    public static void checkFile_path(List<String> path_list){
    	for(int i =0;i<path_list.size();i++){
    		String str = path_folder+path_list.get(i);
    		file_count.incrementAndGet();
    		File f = new File(str);
    		if(!f.exists()){
    			System.out.println("出错="+str);
    			shell_out.println(path_list.get(i));
    		}
    		if(file_count.longValue()%1000 == 0){
    			System.out.println(file_count+" time="+(System.currentTimeMillis() - start_time));
    		}
    	}
    }
    

	private static List<String> getFile_path() throws ClassNotFoundException,
			SQLException, IOException {
		BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream("pic_filename.db")));
		List<String> list = new ArrayList<String>();
		String str = null;
		while((str=read.readLine())!=null){
			list.add(str);
		}
		read.close();
		return list;
	} 
	
	 public static final String DBDRIVER = "com.mysql.jdbc.Driver";  
    //连接地址是由各个数据库生产商单独提供的，所以需要单独记住  
    public static final String DBURL = "jdbc:mysql://192.168.0.101:3306/rca_mec_server";  
    //连接数据库的用户名  
    public static final String DBUSER = "root";  
    //连接数据库的密码  
    public static final String DBPASS = "123456";  
}
