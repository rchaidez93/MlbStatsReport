package MlbStatsProject.datasource;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MyDataSourceFactory {

    public static DataSource getMySQLDataSource() {
        Properties props = new Properties();
        FileInputStream fin = null;
        MysqlDataSource mysqlDS = null;


        try {
            String currentDirectory = System.getProperty("user.dir");
            fin = new FileInputStream(currentDirectory + "/src/main/resources/dbConfig.properties");
            props.load(fin);
            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
            mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return mysqlDS;
    }
}
