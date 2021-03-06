package com.prince.multithreading.nonblocking;

/**
 * @author Prince Raj
 */
public class NonBlockingDataStructure {

    public class DoubleWriterCounter {

        // volatile is simple non blocking data structure
        private volatile long countA = 0;

        private volatile long countB = 0;

        /**
         * Only one (and the same from thereon) thread may ever call this method, or it will lead to race conditions.
         */
        public void incA() {
            this.countA++;
        }

        /**
         * Only one (and the same from thereon) thread may ever call this method, or it will lead to race conditions.
         */
        public void incB() {
            this.countB++;
        }

        /**
         * Many reading threads may call this method
         */
        public long countA() {
            return this.countA;
        }

        /**
         * Many reading threads may call this method
         */
        public long countB() {
            return this.countB;
        }
    }
}
