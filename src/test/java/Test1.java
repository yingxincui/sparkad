import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class Test1 {
    public static void main(String[] args) {
        //Test1.testJson1();
        //Test1.testJson2();
        Test1.testJson3();


    }

    public static void testJson1() {
        JSONObject js = new JSONObject();
        js.put("name", "xiaoming");
        js.put("age", 23);
        System.out.println(js);

    }

    public static void testJson2() {
        String str = "{\"name\":\"xiaoming\",\"age\":23}";
        //字符串转json对象
        JSONObject jsonObject = JSONObject.parseObject(str);
        //json对象转字符串
        String string = jsonObject.toJSONString();
        System.out.println(string);//{"name":"xiaoming","age":23}
    }

    public static void testJson3() {
        String str = "{\n" +
                "'name':'网站',\n" +
                "'num':3,\n" +
                "'sites':[ 'Google', 'Runoob', 'Taobao' ]\n" +
                "}";
        //字符串转json对象
        JSONObject jsonObject = JSONObject.parseObject(str);
        //将JSON对象转化为字符串
        String sites = jsonObject.getString("sites");
        JSONArray array = JSONObject.parseArray(sites);
        System.out.println(array.get(0));//Google
    }

}
