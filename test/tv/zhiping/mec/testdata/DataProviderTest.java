/**
 * zhiping.tv Inc.
 * Copyright (c) 2010-2014 All Rights Reserved.
 */
package tv.zhiping.mec.testdata;

/**
 * 
 * @author kaifeng.shi
 * @version $Id: JsonUtil.java, v 0.1 2014年8月24日 下午10:52:14  Exp $
 */
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.testng.annotations.DataProvider;

import au.com.bytecode.opencsv.CSVReader;

public class DataProviderTest {

    //定义csv文件路径
    public static String       csvPath          = "/home/thinkpad-x230/workspace/rca.mec.server/test/tv/zhiping/mec/testdata/test.csv";
    //定义分隔符
    public static char         csvSeprator      = ',';
    public static final String dataProviderName = "test1";

    @DataProvider(name = dataProviderName)
    public static Object[][] createData1() throws IOException {
        //从CSV文件中读取数据
        CSVReader reader = new CSVReader(new FileReader(csvPath), csvSeprator);

        //不读第一行,第一行统一为参数的字段名字
        reader.readNext();

        //csv中每行的数据都是一个string数组
        String[] csvRow = null;
        ArrayList<Object[]> csvList = new ArrayList<Object[]>();
        //将读取的数据，按行存入到csvList中
        while ((csvRow = reader.readNext()) != null) {
            csvList.add(csvRow);
        }

        //定义返回值
        Object[][] results = new Object[csvList.size()][];
        //设置二维数组每行的值，每行是个object对象
        for (int i = 0; i < csvList.size(); i++) {
            results[i] = csvList.get(i);
        }

        return results;
    }
}
