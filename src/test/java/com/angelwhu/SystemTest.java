package com.angelwhu;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemTest {
    public static final int SLEEP_TIME = 3000;
    public static void dosExit() throws InterruptedException {
        while (true) {
            System.out.println("Try to exit application");
            for (int i = 10; i > 0; i--) {
                System.out.println(i);
                Thread.sleep(SLEEP_TIME);
            }
            System.exit(1);  // 这个真的可以~ @angelwhu
        }

    }

    public static void dosRuntimeHalt() throws InterruptedException {
        while (true) {
            System.out.println("Try to exit application");
            for (int i = 10; i > 0; i--) {
                System.out.println(i);
                Thread.sleep(SLEEP_TIME);
            }
            Runtime.getRuntime().halt(1);  // 这个真的可以~ @angelwhu
        }
    }

    public static void dosRuntimeExec() throws InterruptedException, IOException {
        while (true) {
            System.out.println("Try to exec cmd~");

            Thread.sleep(SLEEP_TIME);

            Process p = Runtime.getRuntime().exec("id");

            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            while ((lineStr = inBr.readLine()) != null)
                //获得命令执行后在控制台的输出信息
                System.out.println(lineStr);// 打印输出信息
            //检查命令是否执行失败。
            if (p.waitFor() != 0) {
                if (p.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束
                    System.err.println("命令执行失败!");
            }
            inBr.close();
            in.close();
        }
    }

    public static void main(String[] args) throws Exception {
        //dosExit();
        //dosRuntimeHalt();
        dosRuntimeExec();
    }

}
