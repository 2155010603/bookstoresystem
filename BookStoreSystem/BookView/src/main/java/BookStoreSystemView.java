import java.util.Scanner;

public class BookStoreSystemView {
    /**
     * 面向用户，输入账户密码进行登录
     */
    public boolean loginView(){
        //键盘输入
        Scanner in = new Scanner(System.in);
        System.out.print("用户名: ");
        String username = in.nextLine();
        System.out.print("密码: ");
        String password = in.nextLine();
        //创建对象，调用service方法
        BookStoreSystemService service = new BookStoreSystemService();
        return service.loginService(username, password);
    }

    /**
     * 注册功能
     */
    public void registerView(){
        //键盘输入
        Scanner in = new Scanner(System.in);
        System.out.print("用户名: ");
        String username = in.nextLine();
        System.out.print("密码: ");
        String password = in.nextLine();

        //调service层方法
        BookStoreSystemService service = new BookStoreSystemService();
        service.registerService(username, password);
    }

    /**
     * 添加书籍
     */
    public void addBookView(){
        //键盘输入
        Scanner in = new Scanner(System.in);
        System.out.print("书籍名: ");
        String name = in.nextLine();
        System.out.print("书籍价格: ");
        double price = in.nextDouble();
        System.out.print("书籍作者: ");
        String author = in.next();
        System.out.print("添加数量: ");
        int quantity = in.nextInt();

        //调service层方法
        BookStoreSystemService service = new BookStoreSystemService();
        service.addBookService(name,price,author,quantity);
    }

    /**
     *查询书籍，按作者名查
     */
    public void searchBookView(){
        Scanner in = new Scanner(System.in);
        System.out.print("查询的书籍名");
        String name = in.nextLine();

        //调service层方法
        BookStoreSystemService service = new BookStoreSystemService();
        service.searchBookService(name);
    }

    /**
     * 更新数据
     */
    public void updateBookView(){
        //键盘输入
        Scanner in = new Scanner(System.in);
        System.out.print("书籍名");
        String name = in.nextLine();
        System.out.print("书籍价格");
        double price = in.nextDouble();
        System.out.print("书籍作者");
        String author = in.next();
        System.out.print("书籍数量");
        int quantity = in.nextInt();

        //调service层方法
        BookStoreSystemService service = new BookStoreSystemService();
        service.updateBookService(name,price,author,quantity);
    }

    /**
     * 删除书籍
     */
    public void deleteBookView(){
        //键盘输入
        Scanner in = new Scanner(System.in);
        System.out.print("输入要删除的书籍名:");
        String name = in.nextLine();

        //调service层方法
        BookStoreSystemService service = new BookStoreSystemService();
        service.deleteBookService(name);
    }

    /**
     * 这是一个界面打印函数
     */
    public void menuPrint(){
        System.out.println("-----图书管理系统-----");
        System.out.println("-----1.用户登录");
        System.out.println("-----2.用户注册");
        System.out.println("-----3.添加书籍");
        System.out.println("-----4.查询书籍");
        System.out.println("-----5.修改书籍");
        System.out.println("-----6.删除书籍");
        System.out.println("-----7.退出");
    }

    /**
     * 主菜单界面
     * 实现每按一个菜单键调用各自的功能
     */
    public void showBookView(){
        //键盘输入
        Scanner in = new Scanner(System.in);
        //循环终止条件
        boolean isrun = false;
        menuPrint();
        int choice1 = in.nextInt();

        /**
         * 如果输入的是1则进入登录功能，
         * 输入的是2则进入注册功能，但必须要管理员账户才能注册
         * 输入7退出
         * 输入别的按钮，提示先登录
         */
        if (choice1 == 1) {
            if(loginView()){
                isrun = true;
                while (isrun){
                    menuPrint();
                    int choice2 = in.nextInt();
                    switch (choice2) {
                        case 1:
                            System.out.println("你已经登录过了");
                            break;
                        case 2:
                            System.out.println("你已有账户，不要重复注册");
                            break;
                        case 3:
                            addBookView();
                            break;
                        case 4:
                            searchBookView();
                            break;
                        case 5:
                            updateBookView();
                            break;
                        case 6:
                            deleteBookView();
                            break;
                        case 7:
                            isrun = false;
                            System.out.println("退出！！！");
                            break;
                    }
                }
            }
        }else if(choice1 == 2){
            System.out.print("输入管理员账户名");
            String account = in.next();
            System.out.print("输入管理员密码:");
            String pwd = in.next();
            BookStoreSystemService service = new BookStoreSystemService();
            //在进行注册前，先调用accountMatching进行匹配
            //是管理员才能进行注册，否则不允许
            if(service.accountMatching(account,pwd)){
                registerView();
            }else System.out.println("您需要管理员账户才能注册");
        }else if(choice1 == 7){
            System.out.println("退出");
            return;
        //一开始，没有登录，不允许进行操作
        }else {
            System.out.println("请先登陆后再操作");
            showBookView();
        }
    }
    public static void main(String[] args) {

        BookStoreSystemView bookStoreSystemView = new BookStoreSystemView();
        bookStoreSystemView.showBookView();
    }
}
