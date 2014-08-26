package shell;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 图片 存在校验 查询
 * java -Dfile.encoding=GBK shell/OutPicFilePath > java.log &
 * @author 张有良
 */
public class OutPicFilePath {
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
	
	/**
	 * 以下参数是需要匹配前动态配置的
	 */
	//输出的目录
	private static PrintWriter shell_out = null;
		
	//要扫描的文件路径
	private static String path_folder = "";
	
    public static void main(String[] args) throws Exception {
        List<String> path_list = getFile_path();
        
        System.out.println(path_list.size());
        shell_out = new PrintWriter(FILENAME_APTH);
        
        outFile_path(path_list);
        
        System.out.println("成功");
        
        shell_out.close();
    }
    
    
    public static void outFile_path(List<String> path_list){
    	for(int i =0;i<path_list.size();i++){
    		shell_out.print(path_list.get(i));
    		shell_out.print("\n");
    	}
    }

    public static void checkFile_path(List<String> path_list){
    	for(int i =0;i<path_list.size();i++){
    		String str = path_folder+path_list.get(i);
    		
    		File f = new File(str);
    		if(!f.exists()){
    			System.out.println(str);
    			shell_out.println(str);
    		}
    	}
    }
    

	private static List<String> getFile_path() throws ClassNotFoundException,
			SQLException {
		Connection con = null; //表示数据库的连接对象  
        Class.forName(DBDRIVER); //1、使用CLASS 类加载驱动程序  
        con = DriverManager.getConnection(DBURL,DBUSER,DBPASS); //2、连接数据库  
                
        Statement st = null;
        ResultSet res = null;
        List<String> path_list = new ArrayList<String>();
        
        try {
			st = con.createStatement();
			res = st.executeQuery("select distinct filename from down_history order by created_at"); 

			while(res.next()){
				path_list.add(res.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			res.close();
			st.close();
			con.close(); // 3、关闭数据库  			
		}
		return path_list;
	} 
	
	 public static final String DBDRIVER = "com.mysql.jdbc.Driver";  
    //连接地址是由各个数据库生产商单独提供的，所以需要单独记住  
    public static final String DBURL = "jdbc:mysql://192.168.0.101:3306/rca_mec_server";  
    //连接数据库的用户名  
    public static final String DBUSER = "root";  
    //连接数据库的密码  
    public static final String DBPASS = "123456";  
}
