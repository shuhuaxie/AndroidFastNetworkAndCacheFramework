package com.frame.FastNetworkAndCacheFramework.data.response;

public class StudentResponse  {

  public Student student;

  public static class Student {
    public String name;
    public String age;

    @Override
    public String toString() {
      return "Student{" +
          "name='" + name + '\'' +
          ", age='" + age + '\'' +
          '}';
    }
  }
}
