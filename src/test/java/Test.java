import test.AbstractTransferHelper;
import test.TestPO;
import test.TransferUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 发现更多精彩  关注公众号：木子的昼夜编程
 * 一个生活在互联网底层，做着增删改查的码农,不谙世事的造作
 * @create 2021-10-10 16:24
 */
public class Test {

    public static void main(String[] args) throws Exception {
        // 参数
        /*Map<String,Object> map =  new HashMap<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            TestPO res = Method02(map, TestPO.class);
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);*/

        Map<String,Object> map =  new HashMap<>();
        AbstractTransferHelper helper = TransferUtil.getTransferHelper(TestPO.class);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            TestPO res = Method03(map, helper);
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }



    // 手动编码 一百万次30毫秒左右
    private static TestPO Method01(Map<String, Object> map, Class<TestPO> testPOClass) {
        TestPO res =  new TestPO();
        res.id = map.get("id") == null ? null : Integer.valueOf(map.get("id").toString()) ;
        res.name = map.get("name") == null ? null : String.valueOf(map.get("name").toString()) ;
        res.age = map.get("age") == null ? null : Integer.valueOf(map.get("age").toString()) ;
        return res;
    }

    // 反射 一百万次200~300毫秒
    // 这个有什么好处呢 如果添加字段 这个方法是不需要修改的 而Method01的硬编码是需要修改的
    private static <PO> PO Method02(Map<String, Object> map, Class<PO> clazz) throws Exception {
        Object res = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            // 获取字段名称
            String name = field.getName();
            // 获取字段类型
            Class<?> type = field.getType();
            // 从Map中获取值
            if (type ==  Integer.class) {
                field.set(res, map.get(name) == null ? null : Integer.valueOf(Integer.valueOf(map.get(name).toString())));
            } else if(type ==  String.class){
                field.set(res, map.get(name) == null ? null : String.valueOf(String.valueOf(map.get(name))));
            }
        }
        return (PO) res;
    }

    // 反射 高级版 --> 软变硬 一百万次40毫秒左右
    private static <PO> PO Method03(Map<String, Object> map, AbstractTransferHelper helper) throws Exception {
        return  (PO) helper.transfer(map);
    }




}