# FKnife
要交一个java的课设，于是模仿Behinder(https://github.com/rebeyond/Behinder)做了一个动态二进制加密的webshell管理器。

代码主要参考了菜刀的源码(https://github.com/Chora10/Cknife)

因为不知道java怎么写没有iv向量的AES-128-CBC加密，所以把Behinder的一句话改成了AES-128-ECB加密

目前功能有限，只支持php的webshell，只有phpinfo查看，命令执行和文件管理这三个功能。以后再慢慢补上，先拿这个v1.0交课设