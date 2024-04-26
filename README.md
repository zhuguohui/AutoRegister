# 背景
在开发过程有些时候我们会需要收集一些类信息。比如要知道某个子类下的所有实现类。可以通过反射的方式实现。但是这种方法有性能问题，因为在运行时，所有类都会包含在dex文件中。这个文件中的类可能有几十万个。而且在实际开发中会发现，新写出来的类，可能没有打包进apk，通过反射无法及时的发现。于是想通过自定义注解处理器来实现。

# 实现
只要在需要收集的类上面配置 **@AutoRegister()** 注解，在注解中写上你要把这个类的类名，生成到那个类的全类名。就可以实现自动收集。


![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/b9bbd9fc4f444e4c82991ce98b1ac38c.png)
## 例如：
我在A,B,C,D 四个类中都配置了这个注解
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/10d05944c0974747829009bd466aa6f8.png)
最后会生成注解中配置的类，把类名保存到**CLASSES**字段中，后面业务中就可以通过反射来实现相应的业务。将注册这个功能，做到对修改关闭，对拖着开发。
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/29b3daef31614f25a8b60075a4834ec2.png)
## 支持多个配置
比如下面 RouteA,RouteB,RouteC 都配置了 **AutoRegister** 注解




![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/1c5957056dd84b28a62124cbd2b306b0.png)
注解中配置的事其他目标类，就会自动生成

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/1cd83a7b6f94420d863a8f4fe0070b66.png)

# 依赖引入
```grovy
    //以下依赖用于自动注册--start
    annotationProcessor 'com.github.zhuguohui.AutoRegister:auto_register_annotation_processer:1.0.0'
    implementation 'com.github.zhuguohui.AutoRegister:auto_register_annotation:1.0.0'
    //以下依赖用于自动注册--end
```

# 源码

[github.com/zhuguohui/AutoRegister](https://github.com/zhuguohui/AutoRegister)
