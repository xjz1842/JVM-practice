package com.jvm.practice;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MyCallSite {
    public static void target(int i) {
    }

    public final MethodHandle mh;

    public MyCallSite() {
        mh = findTarget();
    }
    private static MethodHandle findTarget() {
        try {
            MethodHandles.Lookup l = MethodHandles.lookup();
            MethodType t = MethodType.methodType(void.class, int.class);
            return l.findStatic(MyCallSite.class, "target", t);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MyCallSite myCallSite = new MyCallSite();

    public static void main(String[] args) throws Throwable{
        long current = System.currentTimeMillis();
        for (int i = 1; i <= 2_000_000_000; i++) {
            if (i % 100_000_000 == 0) {
                long temp = System.currentTimeMillis();
                System.out.println(temp - current);
                current = temp;
            }

            myCallSite.mh.invokeExact(128);
        }
    }
}
