一、开发环境的安装
（1）磁盘分区

	* D：APPS -200GB 用于安装软件
	* E：WORK -413GB 用于存放代码、文档资料
	* F : SOFT  -200GB  用于存放测试工具、虚拟机

（2）常用软件

	* jdk
	*  python2.7

	*  版本控制工具：Git SVN

	*  文本编辑器：Ultraedit

	*  文档对比工具：WinMerge

	*  ASN编码工具

	*  抓包工具 wireshark fidder
	*  文件搜索工具 everything

二、git实验
（1）实验一

 - 用户名和邮箱设置：

<pre>
$ git config --global user.name "name"
$ git config --global user.email "email@host.com"
</pre>

 - 创建本地仓库:
    cd进入创建地址
<pre>
$ mkdir GitTest
$ cd GitTest
$ pwd    //显示当前目录
$ git init
</pre>
 - 分支
<pre>
$ git branch branchname
$ git checkout branchname   //切换分支
</pre>
以上两行等价于
<pre>
$ git checkout -b branchname //创建并切换
$ git branch //察看当前分支
</pre>
合并分支：
<pre>
$ git checkout master//切换到某一分支
$ git merge branch//将被合并分支合并到目前分支上
</pre>
 - 添加并提交文件

<pre>
$ git add <filename>   //文件由工作区加入暂存区
$ git commit -m "explian text"
</pre>

 - 修改文件

    
$ vim <filename> 如果文件存在则打开现有文件，如果文件不存在则新建文件
键盘输入字母 “i”或“Insert”键进入插入编辑模式，编辑完毕后ESC键退出编辑模式，切换到命令模式， 在命令模式下键入"ZZ"或者":wq"保存修改并且退出
按下ESC键进入命令模式后键入 ":q!" 回车可以放弃修改并退出vim

 - 提交修改

$ git status 察看有几项修改
$ git diff <filename> 察看具体修改内容
$ git commit 提交修改

 - 撤回修改

如果修改没有提交
<pre>
$ git chekout --<filename> //放弃工作区或暂存区的修改，即回退到最近一次commit或add后的结果
//如果修改已经提交
$ git log //察看commit纪录
$ git reset --hard HEAD~n //n表示由目前版本向上回溯的版本数，如果较近的版本也可以用HEAD^^,每个^表示回溯一个版本

</pre>

 - 删除与恢复

在文件管理器或者用rm filename删除文件后
<pre>
$ git rm <filename> 确认删除
$ git checkout --<filename> 撤销删除
</pre>

 - 标签

<pre>
$ git tag <tagname> <commit ID>
$ git tag -a <tagname> -m <explain text> 
$ git tag -d <tagname>             //删除
$ git tag show <tagname>           //察看标签内容
$ git push origin <tagname>        //推送到远程
</pre>
（2）实验二

 - 获得ssh key

<pre>
$cd ~/.ssh //检查是否已经存在ssh key
$cd ~      //保证在~目录下
$ssh -keygen -t -rsa -C "email@host.com"
</pre>
到C:/user/username/.ssh/id_rsa.pub 察看公钥
在gitlab个人用户的ssh标签中输入公钥

 - 建立远程仓库并推送

<pre>
$git remote add origin git@git.koal.com:<account>/<reponame>
$git add.
$git commit
$git push -u origin <BranchName>
</pre>

 - 推送标签

<pre>
$git push -u origin <tagname>
</pre>

 - 分支保护

在gitlab的project界面setting-〉protectedbranches中设置，分支保护可以使分支不可push

