package cn.sheeva;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

public class CommonTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final Test test=new Test();
        
        for (int i = 0; i < 50; i++) {
            Thread a=new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < 100; j++) {
                        test.a();
                    }
                }
            });
            a.start();
            
            Thread b=new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < 100; j++) {
                        test.b();
                    }
                }
            });
            b.start();
        }
    }
    
    
    static class Test{
        int a=0;
        public synchronized void a(){
            a+=1;
            System.out.println(a);
            a=0;
        }
        
        public synchronized void b(){
            a+=2;
            System.out.println(a);
            a=0;
        }
    }
}


