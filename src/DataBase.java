import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DataBase {
	protected static Connection conn = null;
	
	/*连接sqlite数据库,如果webshell不存在则新建一个webshell表*/
    public static void connect() {
        //Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:date.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }
    
    /*如果不存在webshell表的话新建该表*/
    public static void create_table() {
    	 String sql = "CREATE TABLE IF NOT EXISTS webshell (\n"
                 + "	id integer PRIMARY KEY,\n"
                 + "	url text NOT NULL,\n"
                 + "	pass text NOT NULL,\n"
                 + "    type text NOT NULL\n"
                 + ");";
    	 try {
    		 Statement stmt = conn.createStatement();
    		 stmt.execute(sql);
    	 }catch (SQLException e) {
    		 System.out.println(e.getMessage());
    	 }
    }
    
    public static int nextId() {
    	String sql = "select count(*) from webshell";
    	int new_id = 1;
    	
    	try(Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql)){
    		new_id = rs.getInt(0)+1;
    	}catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	return new_id;
    }
    
    /*把新的webshell写到数据库里*/
    public static void insert(WebShell new_webshell) {
    	String url = new_webshell.getUrl();
    	String password = new_webshell.getPassword();
    	String type = new_webshell.getType();
    	
    	String sql = "INSERT INTO webshell(url,pass,type) VALUES(?,?,?)";
    	//System.out.println(sql);
    	
    	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, url);
            pstmt.setString(2, password);
            pstmt.setString(3, type);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /*查询webshell表中的内容，每一条记录都是一个WebShell对象，返回一个ArrayList<WebShell>*/
    public static ArrayList<WebShell> selectAll(){
    	ArrayList<WebShell> result = new ArrayList<WebShell>();
        String sql = "SELECT id,url, pass, type FROM webshell";
        
        try(Statement stmt  = conn.createStatement();
        	ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
            	WebShell tmp  = new WebShell(rs.getInt("id"),rs.getString("url"),rs.getString("pass"));
            	result.add(tmp);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    
    /*新建一个webshell的时候需要获取一个独一无二的id值，所以先把它插到表中，再去看sqlite中最后一条记录是多少*/
    public static int getNextId() {
    	String sql = "SELECT id FROM webshell ORDER BY id DESC LIMIT 1";
    	int next_id = 1;
    	
    	try(Statement stmt  = conn.createStatement();
            	ResultSet rs    = stmt.executeQuery(sql)){
                next_id = rs.getInt(1)+1;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    	return next_id;
    }
    
    public static void DeleteWebShell(WebShell dieone) {
    	 int id = dieone.getId();
    	 String sql = "DELETE FROM webshell WHERE id = ?";
    	 
    	 try(PreparedStatement pstmt = conn.prepareStatement(sql)){
             pstmt.setInt(1, id);
             pstmt.executeUpdate();
    	 }catch(SQLException e) {
             System.out.println(e.getMessage());
         }
    }
    
    public static void update(WebShell update_one) {
        String sql = "UPDATE webshell SET url = ? , "
                + "pass = ? ," 
        		+ "type = ? "
                + "WHERE id = ?";
 
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, update_one.getUrl());
            pstmt.setString(2, update_one.getPassword()); 
            pstmt.setString(3, update_one.getType());
            pstmt.setInt(4,update_one.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}