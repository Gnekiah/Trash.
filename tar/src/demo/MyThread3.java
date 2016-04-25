package demo;

import java.io.IOException;

public class MyThread3 extends Thread
{
    private Data data;

    public MyThread3(Data data) {
        this.data = data;
    }
    
    public void run() {
        while (true) {
            /*
            if (data.getValue().substring(data.getValue().length()-1).equals("q")) {
                System.out.println("value==q  " + data.getValue());
                data.setValue(data.getValue()+"w");
            }
            else {
                System.out.println("value==w  " + data.getValue());
                data.setValue(data.getValue()+"q");
            }*/
            try {
                this.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            data.setValue(data.getValue()+"q");
        }
    }
    
    
    
    public static void main(String[] args) {
        Data data = new Data();
        Thread thread = new MyThread3(data);
        thread.start();
        while(true) {/*
            if (data.getValue().substring(data.getValue().length()-1).equals("q")) {
                System.out.println("main value= :" + data.getValue());
                data.setValue(data.getValue()+"z");
            }
            else {
                System.out.println("main value= :" + data.getValue());
                data.setValue(data.getValue()+"w");
            }*/
            data.setValue(data.getValue()+"z");
            System.out.println(data.getValue());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}