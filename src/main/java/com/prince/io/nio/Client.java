package com.prince.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Test client for Non Blocking IO server
 * <a href="https://medium.com/coderscorner/tale-of-client-server-and-socket-a6ef54a74763">Reference</a>
 */
public class Client {

  public static void main(String[] args) {
    Runnable client = new Runnable() {
      @Override
      public void run() {
        try {
          new Client().startClient();
        } catch (IOException | InterruptedException e) {
          e.printStackTrace();
        }

      }
    };

    new Thread(client, "client-A").start();
    new Thread(client, "client-B").start();
  }

  public void startClient() throws IOException, InterruptedException {

    String hostName = "localhost";
    int portNumber = 9093;
    InetSocketAddress hostAddress = new InetSocketAddress(hostName, portNumber);
    String threadName = Thread.currentThread().getName();
    String[] messages = new String[] { threadName + " > msg1", threadName + " > msg2", threadName + " > msg3", threadName +
        " > Done" };

    try(SocketChannel client = SocketChannel.open(hostAddress)) {
      for (String message : messages) {
        ByteBuffer buffer = ByteBuffer.allocate(74);
        buffer.put(message.getBytes());
        buffer.flip();

        client.write(buffer);
        System.out.println("echo: " + message);

        buffer.clear();
        Thread.sleep(5000);
      }
    }
  }
}
