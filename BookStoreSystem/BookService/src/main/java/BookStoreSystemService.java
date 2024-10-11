import org.apache.commons.dbutils.DbUtils;
import utils.DruidUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class BookStoreSystemService {

    //用户登录
    public boolean loginService(String username, String password){
        try{
            //创建dao层对象调用dao层
            BookStoreSystemDao dao = new BookStoreSystemDao();
            ResultSet resultSet = dao.loginDao(username,password);
            if(resultSet.next()) {
                //这里获取到了查询结果，说明和数据库数据库中的数据匹配了
                String name = resultSet.getString("name");

                System.out.println("登陆成功");
                System.out.println("你好,"+name+"!!!");
                //关流
                resultSet.close();
                return true;
            }else {
                System.out.println("用户不存在或账户密码错误");
                //关流
                resultSet.close();
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //用户注册
    public void registerService(String username, String password){
        Connection conn = null;
        try{
            conn = DruidUtils.getConnection();
            BookStoreSystemDao dao = new BookStoreSystemDao();
            dao.registerDao(username,password);
            System.out.println("注册成功");

            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //注册前判断是否为管理员
    public boolean accountMatching(String account, String pwd){
        try {
            BookStoreSystemService service = new BookStoreSystemService();
            InputStream in = BookStoreSystemService.class.getClassLoader().getResourceAsStream("root.properties");
            Properties prop = new Properties();
            prop.load(in);
            if (account.equals(prop.getProperty("account"))&&pwd.equals(prop.getProperty("pwd"))){
                return true;
            }else return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //添加书籍
    public void addBookService(String name,double price,String author,int quantity){
        Connection conn = null;
        try{
            conn = DruidUtils.getConnection();
            BookStoreSystemDao dao = new BookStoreSystemDao();
            dao.addBookDao(conn,name,price,author,quantity);
            System.out.println("添加成功");
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //查询书籍
    public void searchBookService(String name){
        try{
            BookStoreSystemDao dao = new BookStoreSystemDao();
            ResultSet resultSet = dao.searchBookDao(name);
            //遍历查询结果集
            if(resultSet.next()){
                String bookname = resultSet.getString(1);
                double price = resultSet.getDouble(2);
                String author = resultSet.getString(3);
                int quantity = resultSet.getInt(4);

                System.out.println("bookname:"+bookname+"price:"+price+"author:"+author+"quantity:"+quantity);
            }else System.out.println("未查到此书籍");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //更新书籍
    public void updateBookService(String name,double price,String author,int quantity){
        BookStoreSystemDao dao = new BookStoreSystemDao();
        Connection conn = null;
        try {
            conn = DruidUtils.getConnection();
            conn.setAutoCommit(false);
            ResultSet resultSet = dao.searchBookDao(name);

            if(resultSet != null){
                dao.updateBookDao(conn,name,price,author,quantity);
            }else {
                dao.addBookDao(conn,name,price,author,quantity);
                System.out.println("数据不存在，已为您添加数据");
            }
            DbUtils.commitAndClose(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            DbUtils.rollbackAndCloseQuietly(conn);
        }
    }

    //删除书籍
    public void deleteBookService(String name){
        BookStoreSystemDao dao = new BookStoreSystemDao();
        try {
            ResultSet resultSet = dao.searchBookDao(name);

            if(resultSet.next()){
                dao.deleteBookDao(name);
                System.out.println("删除成功");
            }else System.out.println("没有这个数据，我怎么删？");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
