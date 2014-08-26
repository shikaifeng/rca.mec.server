package shell;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 图片转换 格式：
 * 1：program cover 1920 *
 * 2: episode cover 1920 *
 * 3: person avatar 360  
 * 4: element 无 450
 * 5: baike  无  340
 * 6: album  无  340
 * 
 *电视端
 *  音乐：340 * 160
 *  百科：340 * 180
 *
 *手机端
 *   音乐：285 * 210
 *   百科：339 * 390
 * 
 * 发布到正式服务器: scp -r /home/ups/java/mec/java_sh/* acr01:/data/weitv/java/java_sh/
 * 假设在新增的图片：都是不重名的情况下运行
 * 
 * 进入：cd /data/weitv/java/java_sh/
 * 运行：java -Dfile.encoding=GBK shell/CoverPic > java.log &
 * 生成脚本 ，生成完毕
 * 
 * 运行脚本 bash cover_pic.sh & > cover.log
 * 查看最后执行的日志
 * 
 * 复制到：cp cover_pic.sh /data/weitv/java/tomcat_v8.0.5_p8080/webapps/ROOT/
 * 
 * 跳转到acr04: 进入 cd /data/weitv/java/java_sh
 * 下载：wget http://218.108.129.142:8888/cover_pic.sh
 * 
 * 运行脚本 bash cover_pic.sh & > cover.log
 * 查看最后执行的日志
 * 
 * 脚本都运行完毕：往cover_pic_db.db 写个时间戳 echo 1405330562592 > cover_pic_db.db
 * 
 * 执行完毕，重新命名:  mv cover_pic.sh cover_pic_20140709.sh
 * 
 * 如果中间失败，重新运行，生成脚本，再重复执行
 * @author 张有良
 */
public class CoverPic {
	//文件总数
	public static AtomicLong file_count = new AtomicLong();
	//文件大小总数
	public static Long file_size_sum = 0L;
	
	//待转换文件总数
	public static AtomicLong cover_file_count = new AtomicLong();
	//待转换文件大小总数
	public static Long cover_file_size_sum = 0L;
		
	
	//转换命令 convert $orig -resize 200x $target
	public static final String COVER_CMD= "convert #orig# -resize #width#x #target#";
	
	//复制命令 "cp /home/acr/java/tmp/t.sh /home/acr/java/tmp/t1.sh ";
	public static final String COPY_CMD = "cp #orig#  #target#";

	
	public static final String SH_APTH = "cover_pic.sh";
	
	//经常变换的
	public static final String COVER_PIC_DB_PATH = "cover_pic_db.db";
	
	/**
	 * 以下参数是需要匹配前动态配置的
	 */
	//输出的目录
	private static PrintWriter shell_out = null;
		
	//要扫描的文件路径
	private String path_folder;
	
	//文件路径规则
	private String path_rule;
	
	//要压缩的图片大小
	private int pic_width;
	
	//每次操作完毕，记录到文件中
	private Long prev_last_time = 0L;
	
	private Long start_time = 0L;
	
	public static void main(String[] args) throws Exception {
		System.out.println("echo "+System.currentTimeMillis()+" > cover_pic_db.db");
		
		CoverPic pro = new CoverPic();
		
		pro.start_time = System.currentTimeMillis();
		pro.readLastTime();
		
//		//成产生脚本
		pro.createCoverShell();
		
//		//运行脚本
//	    pro.exec("bash "+SH_APTH+" & > cover_pic.log");
//		
//		//记录最后的修改时间
		pro.writeLastTime();
	}
	
	/**
	 * 1：program cover 1920
	 * 2: episode cover 1920
	 * 3: person avatar 360  
	 * 4: element 无 450
	 * 5: baike  无  339
	 * 6: album  无  285
	 * 
	 * @param args
	 * @throws IOException 
	 */
	private void createCoverShell() throws IOException {
		shell_out = new PrintWriter(new File(SH_APTH));
		
		String static_path = "/home/acr/java/apache-tomcat-8.0.5/webapps/ROOT/upload/images/";//web08
		static_path = "/data/weitv/static/upload/images/"; //正式服务器
		
		//节目
		this.path_folder = static_path+"program/";
		this.path_rule = "cover";
		this.pic_width = 1920;
		coverFile(new File(path_folder));
		System.out.println("节目封面处理完毕。。。。。。。。。。。。。。。。");
		
		//剧集
		this.path_folder = static_path+"episode/";
		this.path_rule = "cover";
		this.pic_width = 1920;
		coverFile(new File(path_folder));
		System.out.println("剧集封面处理完毕。。。。。。。。。。。。。。。。");
		
		//人物头像
		this.path_folder = static_path+"person/";
		this.path_rule = "avatar";
		this.pic_width = 360;
		coverFile(new File(path_folder));
		System.out.println("明星头像处理完毕。。。。。。。。。。。。。。。。");
		
		//场景的元素
		this.path_folder = static_path+"element/";
		this.path_rule = null;
		this.pic_width = 450;
		coverFile(new File(path_folder));
		System.out.println("场景的元素处理完毕。。。。。。。。。。。。。。。。");
		
		//场景的百科
		this.path_folder = static_path+"baike/";
		this.path_rule = null;
		this.pic_width = 340;
		coverFile(new File(path_folder));
		System.out.println("场景的百度百科处理完毕。。。。。。。。。。。。。。。。");
		
		//场景的歌曲专辑
		this.path_folder = static_path+"album/";
		this.path_rule = "cover";
		this.pic_width = 340;
		coverFile(new File(path_folder));
		System.out.println("歌曲专辑封面处理完毕。。。。。。。。。。。。。。。。");
		
		System.out.println("总文件数="+file_count+"  总文件大小="+file_size_sum);
		System.out.println("转换文件数="+cover_file_count+"  转换文件大小="+cover_file_size_sum);
		System.out.println("sh 产生完毕");
		
		if(shell_out!=null){
			shell_out.close();
		}
	}
	
	/**
	 * 转换文件
	 * @param f  待转换的文件
	 * @param sufixMap
	 * @throws IOException 
	 */
	public void coverFile(File f) throws IOException{
		if(f.isFile()){//是文件
			if(f.lastModified() > prev_last_time){//是在上一次的修改时间之后
				String file_name = f.getName();
				if(isPic(file_name)){//是图片文件
					String path = f.getAbsolutePath();
					if(!isNotBlank(path_rule) || path.indexOf(path_rule)>0){//是否要待处理的目录
						if(!isOrigName(file_name)){//源文件,时间后，表示被修改了
							String orig = getOrigName(f);
							String target = getLinuxPath(f.getAbsolutePath());
							
							File orig_file = new File(orig);
							int[] widthHeight = null;
							
							if(orig_file.exists()){//表示存在原来的文件
								cover_file_size_sum += orig_file.length();
								widthHeight = getPicWidthHeight(orig);
							}else{
								String cmd = COPY_CMD.replace("#orig#",target).replace("#target#",orig);
								outShellCmd(cmd);
								cover_file_size_sum += f.length();
								widthHeight = getPicWidthHeight(target);
							}
							if(widthHeight!=null){
								cover_file_count.incrementAndGet();
								String cmd = null;
								if(widthHeight[0] > pic_width){//如果原图是比我的要求大,进行压缩
									cmd = COVER_CMD.replace("#orig#",orig).replace("#width#",String.valueOf(pic_width)).replace("#target#",target);
								}else{//比我小，我直接复制
									cmd = COPY_CMD.replace("#orig#",orig).replace("#target#",target);
								}
								outShellCmd(cmd);
							}
						}
					}
				}
			}
			file_count.incrementAndGet();
			if(file_count.longValue()%500 == 0){
				System.out.println("扫描文件数="+file_count+" times="+(System.currentTimeMillis() - start_time)+"  path="+f.getAbsolutePath());
			}
			file_size_sum += f.length();
		}else{
			File[] fs = f.listFiles();
			if(fs!=null){
				for(int i=0;i<fs.length;i++){
					coverFile(fs[i]);
				}
			}
		}
	}


	private void outShellCmd(String cmd) {
		shell_out.print(cmd);
		shell_out.print("\n");
	}
	
	
	/**
	 * 获取linux的路径表示方式
	 * @param path
	 * @return
	 */
	private static String getLinuxPath(String path){
		return path.replace("\\","/");
	}
	/**
	 * 生成orig的文件名
	 * @param file_name
	 * @return
	 */
	private static String getOrigName(File f) {
		String str = f.getAbsolutePath();
		String base = str.substring(0,str.lastIndexOf("."));
		String ext = getFileSuffix(str);
		return getLinuxPath(base+".orig."+ext);
	}
	
	/**
	 * 根据orig的文件名生成缩小图文件名
	 * @param file_name
	 * @return
	 */
	private static String getTargetByOrigName(File f) {
		String str = f.getAbsolutePath();
		
		String base = str.substring(0,str.lastIndexOf("."));
		base = base.replace(".orig","");
		String ext = getFileSuffix(str);
		return getLinuxPath(base+"."+ext);
	}

	
	/**
	 * 是否是 orig 文件
	 * @param file_name
	 * @return
	 */
	private static boolean isOrigName(String file_name) {
		if(file_name.indexOf(".orig.")>-1){
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是图片
	 * @param sufix
	 * @return
	 */
	private static boolean isPic(String sufix){
		if(isNotBlank(sufix) && (sufix.indexOf("jpg")>-1 || sufix.indexOf("png")>-1)){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean isNotBlank(String str){
		return str != null && str.trim().length()>0;
	}
	
	/**
	 * 判断文件后缀
	 * @param fileName
	 * @return
	 */
	public static String getFileSuffix(String fileName){
		int index = fileName.lastIndexOf(".");
		if(index>0 && index<fileName.length()){
			fileName = fileName.substring(fileName.lastIndexOf(".")+1);
		}
		index = fileName.indexOf("?");
		if(index>-1){
			fileName = fileName.substring(0,index);
		}
		return fileName;
	}
	
	
	public void exec(String message) throws Exception {
        Process process = Runtime.getRuntime().exec(message);
        
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getInputStream()));  
        String line = null;  
        while ((line = errorReader.readLine()) != null) {  
            System.err.println(line);  
        }  
        errorReader.close();  
        BufferedReader infoReader = new BufferedReader(new InputStreamReader(  
                process.getErrorStream()));  
        while ((line = infoReader.readLine()) != null) {  
            System.out.println(line);  
        }  
        infoReader.close();
    }
	
	/**
	 * 记录最后的最后时间
	 * @throws IOException 
	 */
	public void readLastTime() throws IOException{
		BufferedReader read = null;
		try {
			File f = new File(COVER_PIC_DB_PATH);
			if(f.exists()){
				read = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				String str = read.readLine();
				if(isNotBlank(str)){
					prev_last_time = Long.parseLong(str);
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}finally{
			if(read!=null){
				read.close();				
			}
		}
	}
	
	/**
	 * 记录最后的最后时间
	 * @throws FileNotFoundException 
	 */
	public void writeLastTime() throws FileNotFoundException{
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(COVER_PIC_DB_PATH);
			pw.print(System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(pw!=null){
				pw.close();				
			}
		}
	}
	
	//得到图片的长宽
	public static int[] getPicWidthHeight(String srcPath){
		Image src = null;
		try {
			src = javax.imageio.ImageIO.read(new File(srcPath)); // 构造Image对象
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(src!=null){
			int width = src.getWidth(null); // 得到源图宽
			int height = src.getHeight(null); // 得到源图长
			return new int[]{width,height};
		}else{
			System.out.println("图片文件有问题="+srcPath);
		}
		return null;
	}
}
