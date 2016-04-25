package demo;

import java.io.IOException;

public class test {
    
    public void fun() {
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
        for (long i = 0; i < 1000000000; i++) {
            i += 1;
        }      
    }
    
    public long doone () {
        long startTime = System.currentTimeMillis();
        try {
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
            for (long i = 0; i < 1000000000; i++) {
                i += 1;
            }      
        } catch(Exception e) {
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
    
    public long dotwo () {
        long startTime = System.currentTimeMillis();
        try {
            fun();
        } catch(Exception e) {
            
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
    
    public long dothree () {
        long startTime = System.currentTimeMillis();
        fun();    
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
    
    public static void main(String[] a) {
        test t = new test();
        System.out.println(t.doone());
        System.out.println(t.dotwo());
        System.out.println(t.dothree());
        System.out.println(t.doone());
        System.out.println(t.dotwo());
        System.out.println(t.dothree());
        System.out.println(t.doone());
        System.out.println(t.dotwo());
        System.out.println(t.dothree());
        System.out.println(t.doone());
        System.out.println(t.dotwo());
        System.out.println(t.dothree());
    }
    
}
