package org.glycoinfo.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ThreadLocalTest.class})
public class ThreadLocalTest {
	
	public class IntegerClass {
		Integer intHolder;

		public Integer getIntHolder() {
			return intHolder;
		}

		public void setIntHolder(Integer intHolder) {
			this.intHolder = intHolder;
		}
	}
	
    public static class MyRunnable implements Runnable {

        private ThreadLocal<Integer> threadLocal =
               new ThreadLocal<Integer>();

        
        
        @Override
        public void run() {
            threadLocal.set( (int) (Math.random() * 100D) );
            System.out.println("threadLocal.get()" + threadLocal + "\nnum:>" + threadLocal.get());
    
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
    
            System.out.println("threadLocal.get()" + threadLocal + "\nnum:>" + threadLocal.get());
        }
    }
    
    @Test
    public void test() throws InterruptedException {
        MyRunnable sharedRunnableInstance = new MyRunnable();

        Thread thread1 = new Thread(sharedRunnableInstance);
        Thread thread2 = new Thread(sharedRunnableInstance);

        thread1.start();
        thread2.start();

        thread1.join(); //wait for thread 1 to terminate
        thread2.join(); //wait for thread 2 to terminate
    }

}
