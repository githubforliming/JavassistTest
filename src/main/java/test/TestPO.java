package test;
/**
 * @author 发现更多精彩  关注公众号：木子的昼夜编程
 * 一个生活在互联网底层，做着增删改查的码农,不谙世事的造作
 * @create 2021-10-10 16:55
 */
public class TestPO {
    public Integer id;
    public String name;
    public Integer age;

    @Override
    public String toString() {
        return "TestPO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
