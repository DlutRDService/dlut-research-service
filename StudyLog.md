# 6.28学习记录
## docker操作
notes:虚拟机挂起重连后，IDEA的docker连接出了权限问题，查询连接没有问题，用户权限也没有问题，重启电脑自动解决。 
### docker指令复习
  1. 'systemctl restart docker  docker服务重置'
  2. 'systemctl status docker  查看docker状态'
  3. 'docker ps  显示当前进程的状态' 
## 项目操作
note:Mybatis-Plus报错依旧没有解决，sql实现仍然有问题，去掉Mybatis-Plus插件后仍然报错
### 类学习
* HttpSession接口  
    jakarta.servlet.http包中的接口对象，接口内一些get，set方法，存有客户端连接相关的信息
* 
### 概念学习
* session会话

# 6.29学习记录

# 6.30学习记录

# 7.01学习记录

# 7.02学习记录

# 7.03学习记录
note: 今天虚拟机挂起出了个error，吓死个人，赶紧搞个git上传下项目。
## git操作
#### ubuntu安装git
git被包含在 Ubuntu 默认的软件源仓库中，并且可以使用 apt 包管理工具安装。直接暴力安装
```agsl
sudo apt update
sudo apt install git
```
查看git版本
```agsl
git --version
```
#### git上传项目操作
测试SSH
```agsl
// 如果没有权限被拒绝，需要建立公钥
ssh -T git@github.com
```
```agsl
// 登陆本地git 获取密钥
ssh-keygen -C "xx@xx.com" -f ~/.ssh/github
```

# 7.04学习记录
## AOP常用事件通知
* Before
* After
* Around
## 项目操作
note:艹，bug终于找到了14写成15。(补充：修改后依旧没用，怀疑是数据库连接问题)
可能存在问题的相关描述
问题1：Creating a new SqlSession
SqlSession org.apache.ibatis.session.defaults.DefaultSqlSession@7263a8ba was not registered for synchronization because synchronization is not active
问题2：Closing non transactional SqlSession org.apache.ibatis.session.defaults.DefaultSqlSession@7263a8ba

JDBC Connection will not be managed by Spring。
Spring不对其进行事务监视，可以添加@Trantional注解
## Leetcode
罗马数字转化问题，可以用while循环，也可以用hash存值。

# 7.05学习记录
## 基础学习
note:连赋值都忘了hhh
```
int [] a = new int[4]; 
int a[] = new int[4];
int a[] = {0,0,0,0}
```
```agsl
a = {0,0,0,0}
```
#### 基础数据类型与引用数据类型
* 基本数据类型，分为boolean、byte、int、char、long、short、double、float；
* 引用数据类型 ，分为数组、类、接口。

基本数据类型: boolean，char，byte，short，int，long，float，double
封装类类型：Boolean，Character，Byte，Short，Integer，Long，Float，Double
基本数据类型存放栈内存区，封装类的实例对象存在堆内存区。JDK1.5之后引入了数据类型的自动装箱与拆箱
（感觉就是可以动态赋值）例如
```agsl
// 直接把基本数据类型转成封装类型，这倒是在String类赋值中最常见，
// 原理是调用valueOf静态方法，静态方式是可以在不new一个对象时直接被调用的。
// 挖个坑，该天瞅瞅valueOf的源码。
Integer a = 9;
// 拆箱操作
a--;
```
## 项目操作
note:我的Bug依旧没有解决，气死个人。
## Leetcode
* 归并排序，巧妙得很，时间复杂度nlog(n),空间复杂度O(n)
* 打印数字问题，复习下Math.pow指数运算
# 7.06 学习记录
## Leetcode
贪心算法
## 基础学习
#### BufferImage类，实现抽象类Image。
主要作用是将一幅图片加载到内存中（BufferedImage生成的图片在内存里有一个图像缓冲区，利用缓冲区可以很方便地操作这个图片），
提供获得绘图对象、图像缩放、选择图像平滑度等功能，通常用来做图片大小变换、图片变灰、设置透明不透明等。（验证码设置）
Java将一张图片加载到内存的方法,可以使用javax.imageio中的ImageIO类
```agsl
String imgPath = "/home/zsl/Desktop/***.jpg";  
BufferedImage image = ImageIO.read(new FileInputStream(imgPath));
```
#### Graphics类
提供基本绘图和显示格式化文字的方法，画图用的坐标系原点在左上角，纵轴向下。主要有画线段、矩形、圆、椭圆、圆弧、多边形等各种颜色的图形、线条。
具体操作可以调用getGraphics()方法，用于获得在图像上绘图的Graphics对象，示例
```agsl
BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
Graphics g = bufferedImage.getGraphics();
// 设置背景色
g.setColor(int R, int B, int G);
// 填充
g.fillRect(0,0,width, height);
// 字体设置
Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
g.setFont(font);
```
#### 匿名类
啥时候用啥时候写，示例：Animal的子类Cat与Dog，完全可以用匿名类来代替。
正常需要创建Girl与Boy子类，匿名类直接省去了子类创建。
```agsl
class Animal(){
    public void Eat(){
        System.out.prinln("吃饭");
    }
}
class Demo(){
    // 匿名类实现
    Animal a = new Animal(){
        @Override
        public void Eat(){
            System.out.prinln("Eating");
        }
    };
    a.Eat();
}
```
#### -> 箭头运算符
Java8中增加了lambda表达式，出现了箭头运算符用于形成lambda表达式，上述匿名类的实现可改为
```agsl
Animal a = () -> System.out.prinln("Eating")
```
# 7.07 学习记录
## Leetcode
快排
## 项目操作
note:吐了，mysql连接问题还是没解决，麻了。
# 7.12学习记录
## 项目操作
那破问题先放放，直接把用户登陆的业务逻辑添加到知识图谱这个项目上。开始写接口。
# 7.13学习记录
## 基础学习
* 计算机网络
今天先简单了解网络传输的一些基本指标 丢包、时延、带宽、吞吐量、速率等
* 操作系统

## 项目操作
今天差不多把用户登陆相关的业务逻辑梳理完成，准备封装函数。
# 7.14学习记录
## 基础学习
* 计算机网络
今天了解网络协议的基本内容，物理层、链路层、网络层、运输层、应用层。
* 操作系统

## 项目操作
今天算是完成了用户登陆的业务逻辑，邮箱验证码还需要在深入了解下，下一步准备开始字符串检索
## Java基础学习
Java中各个类型对应关系还是比较严的，大多使用null值来进行逻辑判断，不像python中int类型的0、1可以当true、false用
StringBuild类可以进行字符串的构建，里面有比较常用的方法append，append与""+""这种字符串拼接相比存有内存地址上的区别。