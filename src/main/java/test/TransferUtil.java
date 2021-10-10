package test;

import javassist.*;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author 发现更多精彩  关注公众号：木子的昼夜编程
 * 一个生活在互联网底层，做着增删改查的码农,不谙世事的造作
 * @create 2021-10-10 20:10
 */
public class TransferUtil {
    // 这里用到了javaassist
    // 这个就是有近似于写死代码的性能 有近似于反射的适配性 如果再加字段 这里是不用修改的
    // 判空什么的就先不做了 主要讲使用方式
    public static AbstractTransferHelper getTransferHelper(Class clazz) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException, IOException {
        ClassPool pool = ClassPool.getDefault();
        pool.appendSystemPath();

        // 导包
        //import java.util.HashMap;
        //import java.util.Map;
        pool.importPackage("java.util.Map");
        pool.importPackage("java.util.HashMap");
        //import test.AbstractTransferHelper
        pool.importPackage("test.AbstractTransferHelper");
        //import test.TestPO;
        pool.importPackage(clazz.getName());
        pool.importPackage(AbstractTransferHelper.class.getName());

        // 父类
        CtClass superClass = pool.getCtClass(AbstractTransferHelper.class.getName());

        // 自定义动态创建的类名
        String className = clazz.getName()+"TransferHelper";
        // 创建类 指定父类superClass
        // Class XXXTransferHelper extends AbstractTransferHelper
        CtClass myclass = pool.makeClass(className, superClass);

        // 构造函数 public XXXTransferHelper(){}
        CtConstructor ctConstructor = new CtConstructor(new CtClass[0], myclass);
        ctConstructor.setBody("{}");
        myclass.addConstructor(ctConstructor);

        // 方法---
        StringBuilder sb = new StringBuilder();
        sb.append("public Object transfer(Map map) throws Exception {\n");
        // 类似:TestPO obj = new TestPO();
        sb.append(clazz.getName() +" obj = new "+clazz.getName()+"();\n");
        // 设置属性值
        Field[] fields = clazz.getFields();
        String strF = "obj.%s = map.get(\"%s\") == null ? null : String.valueOf(map.get(\"%s\"));\n";
        String strI = "obj.%s = map.get(\"%s\") == null ? null : Integer.valueOf(map.get(\"%s\").toString());\n";
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String name = field.getName();
            Class<?> type = field.getType();
            // 这里只写String Integer 类型 其他我就不写了
            if (type == String.class) {
                // 类似obj.name = map.get("name") == null ? null : String.valueOf(map.get("name"));
                String format = String.format(strF, field.getName(), field.getName(), field.getName());
                sb.append(format);
            } else if (type == Integer.class) {
                // 类似obj.name = map.get("name") == null ? null : Integer.valueOf(map.get("name").toString());
                String format = String.format(strI, field.getName(), field.getName(), field.getName());
                sb.append(format);
            }
        }

        sb.append("return obj;\n");
        sb.append("}");

        // 创建方法
        CtMethod method = CtMethod.make(sb.toString(), myclass);
        myclass.addMethod(method);
        // 创建实体
        Class aClass = myclass.toClass();

        // myclass.writeFile("E:\\MyNote\\test");
        System.out.println(aClass);
        return (AbstractTransferHelper)aClass.newInstance();
    }
}
