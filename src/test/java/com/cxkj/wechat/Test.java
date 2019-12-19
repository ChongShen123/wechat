package com.cxkj.wechat;

import com.cxkj.wechat.mapper.UserMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.*;
import java.lang.reflect.Field;

/**
 * @author tiankong
 * @date 2019/12/13 17:59
 */
public class Test {
/*    public static void main(String[] args) throws IllegalAccessException {
     *//*   User user = new User("小明");*//*
        initUser(user);
        printResult(checkUser(user));
        user.setAge(22);
        printResult(checkUser(user));
    }*/

    private static void printResult(boolean checkResult) {
        System.out.println(checkResult ? "检测通过" : "检测未通过");
    }

    private static boolean checkUser(User user) throws IllegalAccessException {
        // 获取User类中所有的属性(getFields无法获得private属性)
        Field[] fields = User.class.getDeclaredFields();
        String username = null;
        boolean result = true;
        // 遍历所有属性
        for (Field field : fields) {
            // 如果属性上有此注解，则进行赋值操作
            if (field.getName().equals("username")) {
                field.setAccessible(true);
                username = field.get(user).toString();
            }
            if (field.isAnnotationPresent(ValidateAge.class)) {
                ValidateAge validateAge = field.getAnnotation(ValidateAge.class);
                field.setAccessible(true);
                int age = (int) field.get(user);
                if (age < validateAge.min() || age > validateAge.max()) {
                    result = false;
                    System.out.println(username + "年龄值不符合条件");
                }
            }
        }
        return result;
    }

    private static void initUser(User user) throws IllegalAccessException {
        // 获取User类中所有属性(getFields无法获得private属性)
        Field[] fields = User.class.getDeclaredFields();
        // 遍历所有属性
        for (Field field : fields) {
            // 如果属性上有此注解，则进行赋值操作
            if (field.isAnnotationPresent(InitSex.class)) {
                InitSex init = field.getAnnotation(InitSex.class);
                field.setAccessible(true);
                field.set(user, init.sex().toString());
                System.out.println("完成属性值的修改，修改值为：" + init.sex().toString());
            }
        }
    }

}

/**
 * 性别赋值
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Inherited
@interface InitSex {
    enum SEX_TYPE {MAN, WOMAN}

    SEX_TYPE sex() default SEX_TYPE.MAN;
}

/**
 * 年龄校验
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Inherited
@interface ValidateAge {
    /**
     * 最小值
     */
    int min() default 18;

    /**
     * 最大值
     */
    int max() default 99;

    /**
     * 默认值
     */
    int value() default 24;
}

@Data
class User {
    private String username;
    @ValidateAge(min = 20, max = 35, value = 22)
    private int age;
    @InitSex(sex = InitSex.SEX_TYPE.WOMAN)
    private String sex;

/*    public User(String s) {
        username = s;
    }*/

    @Autowired
    UserMapper userMapper;
    public void Users(){



    }
}
