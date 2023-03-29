# socketChat
Socket chat Server/Client
基于java socket进行tcp连接的聊天服务
1.Client端参考 client/SimpleCode.java使用
SocketManager.java需要配置对应的server端IP

2.Server端部署在Linux系统， 执行build_run_java.sh 
前提 安装openJDK8/11 安装mysql

3.当前支持功能: 登录/注册/聊天/发送图片

4.扩展须知:
Client/Server端的SocketData.java内容保持一致
部署环境后运行， 根据日志修改serialVersionUID的值， 以便Server端decode message
  
