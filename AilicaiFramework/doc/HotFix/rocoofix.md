1. 创建branch,修复代码
2. 登陆生成apk的服务器,下载rocoofix生成文件(如:rocoofix20160927-4.8-3),重命名为rocoofix,放在app目录下.
3. 将rocoofix文件强制添加入git,提交代码
4. 在总模块创建修复tag
5. 使用新tag生成apk包,重新在apk服务器的rocoofix目录下找到生成的patch.jar文件
apk服务器地址:\\192.168.1.82\qa
6. 修改补丁文件,上传补丁文件,配置补丁生效等,参考"热修复.html"