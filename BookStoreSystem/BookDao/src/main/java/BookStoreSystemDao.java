import org.apache.commons.dbutils.QueryRunner;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import utils.DruidUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BookStoreSystemDao {

    //用户登录
    public ResultSet loginDao(String userName, String password) throws SQLException {
        //DruidUtils是自定义的一个连接数据库的工具类，使用的是druid连接池技术
        Connection conn = DruidUtils.getConnection();
        //?是占位符，放在sql注入攻击
        String sql = "select * from user where name = ? and password = ?";
        //获取执行对象
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        //手动设置占位符的值
        preparedStatement.setObject(1,userName);
        preparedStatement.setObject(2,password);

        //执行sql语句，获取查询结果
        return preparedStatement.executeQuery();
    }

    //用户注册
    public void registerDao(String userName, String password) throws SQLException {
        //DBUtils工具包中的QueryRunner类
        //有参构造可以自动从连接池中获取连接对象
        QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
        BookStoreSystemDao bookStoreSystemDao = new BookStoreSystemDao();

        String sql = "insert into user values(?,?)";

        //调用loginDao方法查询数据库中是否以存在输入的账户，没有就更新
        if(bookStoreSystemDao.loginDao(userName,password).next()){
            System.out.println("账户已存在，请直接登录");
        }else queryRunner.update(sql,userName,password);
    }

    //添加书籍
    public void addBookDao(Connection conn, String bookname, double bookprice, String bookauthor, int bookquantity) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
        String sql = "insert into book values(?,?,?,?)";
        queryRunner.update(sql,bookname,bookprice,bookauthor,bookquantity);
    }

    //查询书籍
    public ResultSet searchBookDao(String bookname) throws SQLException {
        //---注意---
        //这里我没有用QueryRunner,是因为我通过beanhandler获取的book对象为null
        //我尝试了很久，找不到问题，就没有使用javabean来存储，而是使用了resultset
        Connection con = DruidUtils.getConnection();
        String sql = "select * from book where bookname = ?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1,bookname);

        return preparedStatement.executeQuery();

    }

    //更新书籍
    public void updateBookDao(Connection conn,String bookname, double bookprice,String author,int bookquantity) throws SQLException {
        //这里一样的，都是借助DButils来简化代码
        QueryRunner queryRunner = new QueryRunner();
        String sql = "update book set bookprice = ?,bookauthor = ?, bookquantity = ? where bookname = ?";
        queryRunner.update(conn,sql,bookprice,author,bookquantity,bookname);
    }

    //删除书籍
    public void deleteBookDao(String bookname) throws SQLException {
        //这里一样的，都是借助DButils来简化代码
        QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
        String sql = "delete from book where bookname = ?";
        queryRunner.update(DruidUtils.getConnection(),sql,bookname);
    }

}
