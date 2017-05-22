package com.dafy.dev.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by chunxiaoli on 5/19/17.
 */
public class ObjectConvertUtilTest {
    @Test
    public void testObjectConvert(){
        User source = new User("chunxiao",29,"");
        User target = new User(null,0,"lichunxiao@dafy.com");
        User merge = ObjectConvertUtil.merge(source,target);
        System.out.println("user merge:"+merge);
        Assert.assertTrue(merge.getEmail().equals("lichunxiao@dafy.com"));
        Assert.assertTrue(source.getName().equals("chunxiao"));
        Assert.assertTrue(source.getAge()==29);
    }

    public static class User{
        private String name;
        private int age;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public User(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email=email;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", email='" + email + '\'' +
                    '}';
        }
    }
}
