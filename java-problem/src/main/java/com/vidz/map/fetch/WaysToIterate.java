package com.vidz.map.fetch;

import java.util.Iterator;
import java.util.Map;

public class WaysToIterate {

  public static void main(String[] args) {
    Map<Integer, String> employee = Map.of(1, "John Doe",2, "Jane Doe",3, "Jill Doe",4, "Jack Doe");
    System.out.println("Using foreach in Java 8");
    keyValueIteration(employee);
    forLoopIteration(employee);
    entrySetIteration(employee);
    entrySetItrIteration(employee);
    keySetIteratorIteration(employee);
  }

  static void keyValueIteration(Map<Integer, String> employee) {
    employee.forEach((id, name) -> {
      System.out.println("Key : " + id + " value : " + name);
    });
  }

  static void forLoopIteration(Map<Integer, String> employee) {
    for (Integer key : employee.keySet()) {
      System.out.println("Key : " + key + " value : " + employee.get(key));
    }
  }

  static void entrySetIteration(Map<Integer, String> employee) {
    for (Map.Entry<Integer, String> entry : employee.entrySet()) {
      System.out.println("Key : " + entry.getKey() + " value : " + entry.getValue());
    }
  }

  static void entrySetItrIteration(Map<Integer, String> employee) {
    Iterator<Map.Entry<Integer, String>> iterator = employee.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = iterator.next();
      System.out.println("Key : " + entry.getKey() + " value : " + entry.getValue());
    }
  }


  static void keySetIteratorIteration(Map<Integer, String> employee) {
    Iterator<Map.Entry<Integer, String>> iterator = employee.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = iterator.next();
      System.out.println("Key : " + entry.getKey() + " value : " + entry.getValue());
    }
  }

}