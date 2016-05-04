package com.instrumentalapp;

import org.junit.*;
import java.io.*;
import java.util.Scanner;

public class NoticeTest {

  @Test
  public void messageValidity() {
    Assert.assertTrue(new Notice("hello world", 1, 1).isValid());

    Assert.assertFalse(new Notice("hello\nworld", 1, 1).isValid());
  }
}
