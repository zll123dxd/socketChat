sudo apt-get install mysql-server
sudo apt-get install mysql-client

#登录
sudo mysql -u [username] -p
#默认用户名密码
sudo cat /etc/mysql/debian.cnf  

#创建用户  user : password
CREATE USER 'user'@'localhost' IDENTIFIED BY 'password';


#mysql所有命令需要跟分号 ;

#创建数据库
create database xxx;

#删除数据库
drop xxx;

#使用数据库
use xxx;

#创建表
create table ACCOUNT account(user varchar(20), password varchar(30));

#表插入数据
insert into 表名 (a,b,c) values (a1,b1,c1);   string类型values使用''
insert into account (user,password) values ('root', '12345678');
#查看所有数据库
show databases;

#使用数据库
use base_name;

#展示所有表
show databases;
