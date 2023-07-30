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
## leetcode
数组中重复的数
```java
class Solution {
    public int findRepeatNumber(int[] nums) {
        int [] a = new int[nums.length];
        int pointer;
        for (int i=0;i<nums.length;i++){
            pointer = nums[i];
            a[pointer]++;
            if (a[pointer]>1){return pointer;}
        }
        return -1;
    }

    public static void main(){
        Solution solution = new Solution();
        int [] nums = {2,3,1,0,2,5,3};
        solution.findRepeatNumber(nums);
    }
}
```
------------------------------------------------------------------------------------------------------------------------
# 6.29学习记录
## 项目操作
session会话可以存一些信息，比如用户名，密码啥的，登陆的时候就不用重复填写了，应该是不错的存储容器，但又不限于存储。
有机会可以在弄下response这个，可以结合一下（挖个坑）
## leetcode
二维数组中的查找（二分）
```java
class Solution {
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0){
            return false;
        }
        int m = matrix[0].length;
        int n = matrix.length;
        int row = 0;
        int col = m-1;
        while (matrix[row][col] != target){
            if (matrix[row][col] < target){
                row++;
            }else{
                col--;
            }
            if (row>=n || col<0){
                return false;
            }
        }
        return true;
    }
}
```
------------------------------------------------------------------------------------------------------------------------
# 6.30学习记录
## leetcode
------------------------------------------------------------------------------------------------------------------------
# 7.01学习记录
## leetcode
两数之和
```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        int []index = new int[2];
        HashMap<Integer, Integer> hash = new HashMap<Integer,Integer>();
        for (int i=0; i<nums.length; i++){
            if(hash.containsKey(nums[i])){
                index[0] = i;
                index[1] = hash.get(nums[i]);
                return index;
            }
            hash.put(target-nums[i], i);

        }
        return index; 
    }
}
```
------------------------------------------------------------------------------------------------------------------------
# 7.02学习记录
回学校咯。
------------------------------------------------------------------------------------------------------------------------
# 7.03学习记录
note: 今天虚拟机挂起出了个error，吓死个人，赶紧搞个git上传下项目。
## git操作
#### ubuntu安装git
git被包含在 Ubuntu 默认的软件源仓库中，并且可以使用 apt 包管理工具安装。直接暴力安装
```shell
sudo apt update
sudo apt install git
```
查看git版本
```shell
git --version
```
登陆本地git仓库
```shell
git config –global user.name "yourname"
git config -global user.email "your@email"
```
#### git上传项目操作
测试SSH
```shell
# 如果没有权限被拒绝，需要建立公钥
ssh -T git@github.com
```
```shell
# 登陆本地git 获取密钥，从而连接远程仓库
ssh-keygen -C "zsl2021@mail.dlut.edu.cn" -f ~/.ssh/github
```
获取密钥后在github上建立github链接，然后开始上传项目
```shell
git init #把这个目录变成Git可以管理的仓库
git add README.md #文件添加到仓库
git add . #不但可以跟单一文件，还可以跟通配符，更可以跟目录。一个点就把当前目录下所有未追踪的文件全部add了 
git commit -m "first commit" #把文件提交到仓库
git remote add origin git@github.com:{远程参考古地址} #关联远程仓库
git push -u origin master #把本地库的所有内容推送到远程库上
```
------------------------------------------------------------------------------------------------------------------------
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
问题2：Closing non-transactional SqlSession org.apache.ibatis.session.defaults.DefaultSqlSession@7263a8ba

JDBC Connection will not be managed by Spring。
Spring不对其进行事务监视，可以添加@Trantional注解
## Leetcode
罗马数字转化问题，可以用while循环，也可以用hash存值。
```java
import java.util.HashMap;

class Solution {
    public int romanToInt(String s) {
        HashMap<Character, Integer> hash = new HashMap<>();
        hash.put('I',1);
        hash.put('V',5);
        hash.put('X',10);
        hash.put('L',50);
        hash.put('C',100);
        hash.put('D',500);
        hash.put('M',1000);
        int sum = 0;
        int num;
        int num_next;
        char [] chars = s.toCharArray();
        for (int i=0;i<chars.length;){
            num = hash.get(chars[i]);
            if (i == chars.length - 1){
                sum += num;
                break;
            }
            num_next = hash.get(chars[++i]);
            if (num < num_next){
                i++;
                sum += (num_next - num);
            }else{
                sum += num;
            }
        }
        return sum;
    }
}
```
------------------------------------------------------------------------------------------------------------------------
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
```
// 直接把基本数据类型转成封装类型，这倒是在String类赋值中最常见，
// 原理是调用valueOf静态方法，静态方式是可以在不new一个对象时直接被调用的。
// 挖个坑，改天瞅瞅valueOf的源码。
Integer a = 9;
a--; // 拆箱操作
```
## 项目操作
note:我的Bug依旧没有解决，气死个人。
## Leetcode
* 归并排序，巧妙得很，时间复杂度nlog(n),空间复杂度O(n)
* 打印数字问题，复习下Math.pow指数运算
------------------------------------------------------------------------------------------------------------------------
# 7.06 学习记录
## Leetcode
贪心算法
## 基础学习
#### BufferImage类，实现抽象类Image。
主要作用是将一幅图片加载到内存中（BufferedImage生成的图片在内存里有一个图像缓冲区，利用缓冲区可以很方便地操作这个图片），
提供获得绘图对象、图像缩放、选择图像平滑度等功能，通常用来做图片大小变换、图片变灰、设置透明不透明等。（验证码设置）
Java将一张图片加载到内存的方法,可以使用javax.imageio中的ImageIO类
```
String imgPath = "/home/zsl/Desktop/***.jpg";  
BufferedImage image = ImageIO.read(new FileInputStream(imgPath));
```
#### Graphics类
提供基本绘图和显示格式化文字的方法，画图用的坐标系原点在左上角，纵轴向下。主要有画线段、矩形、圆、椭圆、圆弧、多边形等各种颜色的图形、线条。
具体操作可以调用getGraphics()方法，用于获得在图像上绘图的Graphics对象，示例
```
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
```java
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
    //a.Eat();
}
```
#### -> 箭头运算符
Java8中增加了lambda表达式，出现了箭头运算符用于形成lambda表达式，上述匿名类的实现可改为
```
Animal a = () -> System.out.prinln("Eating")
```
------------------------------------------------------------------------------------------------------------------------
# 7.07 学习记录
## Leetcode
快排
```java
public class QuickSort(){
    public sort(int[] array){
        sortSection(array, 0, array.length - 1);
    }
    private static void sortSection(int[] array, int start, int end){
        if (start >= end){
            return;
        }
        int p = partition(array,start,end);
        sortSection(array,start,p-1);
        sortSection(array,p+1,end);
    }
    private static int partition(int[] array, int start, int end){
        int pivot = array[start];
        int tmp;
        int biggest_smallest = start;
        for (int i = start; i < end; i++) {
            if (array[i] <= pivot){
                biggest_smallest++;
                tmp = array[biggest_smallest];
                array[biggest_smallest] = array[i];
                array[i] = tmp;
            }
        }
        array[start] = array[biggest_smallest];
        array[biggest_smallest] = pivot;
        return biggest_smallest;
    }
}
```
## 项目操作
note:吐了，mysql连接问题还是没解决，麻了。
------------------------------------------------------------------------------------------------------------------------
# 7.08 学习记录
note：开摆，去隔壁玩了一天。进了一下午贡，点贡没少进，记在小本本上。
------------------------------------------------------------------------------------------------------------------------
# 7.09 学习记录
note：马德还要改论文，改不动，真的改不动
------------------------------------------------------------------------------------------------------------------------
# 7.10 学习记录
note：改论文，改论文，改tm个锤子
------------------------------------------------------------------------------------------------------------------------
# 7.11 学习记录
note：终于改完了，提交，请假，拜拜了您勒。
------------------------------------------------------------------------------------------------------------------------
# 7.12学习记录
## 项目操作
那破问题先放放，直接把用户登陆的业务逻辑添加到知识图谱这个项目上。开始写接口。
------------------------------------------------------------------------------------------------------------------------
# 7.13学习记录
## 基础学习
* 计算机网络
今天先简单了解网络传输的一些基本指标 丢包、时延、带宽、吞吐量、速率等
* 操作系统

## 项目操作
今天差不多把用户登陆相关的业务逻辑梳理完成，准备封装函数。
------------------------------------------------------------------------------------------------------------------------
# 7.14学习记录
## 基础学习
* 计算机网络
今天了解网络协议的基本内容，物理层、链路层、网络层、运输层、应用层。
* 操作系统
操作系统核心内容在与管理计算机硬件。管理Cpu，内存，硬盘等。
## 项目操作
今天算是完成了用户登陆的业务逻辑，邮箱验证码还需要在深入了解下，下一步准备开始字符串检索
## Java基础学习
Java中各个类型对应关系还是比较严的，大多使用null值来进行逻辑判断，不像python中int类型的0、1可以当true、false用
StringBuild类可以进行字符串的构建，里面有比较常用的方法append，append与""+""这种字符串拼接相比存有内存地址上的区别。
------------------------------------------------------------------------------------------------------------------------
# 7.15学习记录
note:今天久违的学习快乐，还是丫的学习新知识快乐，业务开发才是无聊地要死，秋招找到工作就转行。
## 基础学习
* 操作系统
1.操作系统通过多进程的方式高效的管理CPU。
2.管理CPU最直接最简单的方式就是设置初值取指执行，但是IO指令的执行时间远远大于计算指令的执行时间， 
  所以实际场景中单线程CPU的利用率无限的接近于0，因此采用并行这种有效的管理方式。
3.进程是正在进行的程序，相较于在磁盘中的静态程序而言。进程
4.进程可以理解为资源与指令序列，资源就是映射表对应的内存地址，指令序列就线程，两个序列两个栈，真的是很巧妙阿。
5.线程
## Java基础
今天啃个Java注解的实现原理，Annotation。注解套到方法上，算是贴了个标签。
注解通过@interface关键字进行定义（注解套注解，6）,注解只有成员变量，没有方法，那就只能无参构造了。
注解的成员变量在注解的定义中以“无形参的方法”形式来声明，其方法名定义了该成员变量的名字，
其返回值定义了该成员变量的类型（动态成员变量？不过我看注解定义中到时没有体现）。
注解通过反射获取（反射？什么记忆中的抽象概念，有点上强度，先复习下反射）
元注解，套在注解上的注解（开始套娃）Aspect中倒是常用（猛地一想，切面不都是注解实现，可不就用元注解）
5个元注解（挖个坑）
* @Retention
* @Repeatable
* @Inherited
* @Target
* @Documented
## 项目操作
趁热打铁，学学两个注解
@Autowired
@Resource
## leetcode
复习快排 、二分
```java
```
------------------------------------------------------------------------------------------------------------------------
# 7.16 学习记录
note：想起来要改论文就闹心
# 7.17 学习记录
## 基础学习
* 操作系统
## 项目操作
## Java基础
# 7.18 学习记录
note:Java莫的指针，不晓得咋实现动态数组。
今天搞了下zilliz，向量数据库真是好东西，还是计算机顶会好，发两篇顶会论文横着走。
zilliz这个开发工作真的好，大厂是什么，真不熟，不过招聘都博士起步，虽说硕士的话我也去不了。
我敲，这个是个云服务器，还是整那个开源Milvus的吧。
## Milvus向量数据库
### 镜像安装
先检查CPU支持不支持，出来一堆汇编，啥也看不懂。好像能跑，能跑那就是支持。
```shell
# lscpu，cpu指令，grep (global regular expression) 查找文件里符合条件的字符串或正则表达式。
lscpu | grep -e sse4_2 -e avx -e avx2 -e avx512
```
检查docker版本，我的是24.02，dock-compose还没安装，奇怪，那我idea怎么跑起来的 o?0
```shell
docker info  # 建议使用 19.03 或以上版本。
docker-compose version  # 建议使用 1.25.1 或以上版本。
```
安装docker-compose，得从github上拉取，我的虚拟机好像就跟github有仇，试试把。
```shell
sudo curl -L "https://github.com/docker/compose/releases/download/1.25.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
```
连接超时，老林能不能赞助我个梯子用用。   --试了几次突然成功了。。。  
验证一下，先授权，我敲1.25就能验证版本，2.2就不行what？
```shell
sudo chmod +x /usr/local/bin/docker-compose
```
```shell
docker-compose -version
```
最后还是安装了，版本1.25.1 
我又回来了虽然说1.25.1以上，但是我看官网都不用1了，安装介绍也没有1，还是要重新安装最新版，又要看运气能不能链接上555。
```shell
# 卸载原先安装的
sudo rm /usr/local/bin/docker-compose
```
下载yml文件拉取docker镜像，又要跟github打交道了，唉，这网络传输就限制的死死的。
```shell
# 下载yaml文件
wget https://github.com/milvus-io/milvus/releases/download/v2.2.11/milvus-standalone-docker-compose.yml -O docker-compose.yml
```
github连不上，调调host文件试试
```shell
# 把代理地址加上
sudo vim /etc/hosts
```
下载文件成功了，显示。
```
2023-07-18 14:35:24 (31.2 MB/s) - ‘docker-compose.yml’ saved [1357/1357]
```
找到docker-compose.yml，欸忘了我的地址了，直接保存到项目目录下了，找了半天。启动。
```shell
# find命令查找文件
sudo find / -type f -name "docker-compose"
```
```shell
# 开始拉取
docker-compose up
```
好慢。。。。拉取10几个小时，怪不得说可以泡杯茶慢慢等了，tmd原来不是在拉取，项目已经启动了，我好想个大厦比
```shell 
# 查看进程
docker-compose ps
```
```shell
# 查看监视接口
docker port milvus-standalone 19530/tcp
```

# 7.28学习记录
## Redis学习
 * Redis的List删除命令：
　　 lrem : lrem mylist 0 "value"    //从mylist中删除全部等值value的元素   0为全部，负值为从尾部开始。
　　 ltrim: ltrim mylist 1 -1     //保留mylist中 1到末尾的值，即删除第一个值。
　　 lpop: lpop mylist
　　 rpop: rpop mylist
    如果想要删除指定index的值：
　   lset mylist index "del"
　   lrem mylist 0 "del"
# 7.30学习记录
note：今天很多的问题来自与依赖注入，对Spring各个注解不是很明白
配置类那个问题解决了，@Component注解改成了@Service莫名其妙。。。(挖个坑留意下)
@Configuration与@Bean 在Springboot中倒是直接注入了不再需要ApplicationContext进行上下文注入
## Redis学习
 * python中，Redis的时间设置采用ex参数,直接在set方法中使用即可。
## SpringBoot注解
## 项目操作
note：很多网页的知识还要在多了解，前端多少还是要了解点的，不然还是挺难的。
解决了推荐问题，猛地一想多条件查询直接字符拼接算了。

