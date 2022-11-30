package com.prince.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Simple NonBlocking IO Server
 * <a href="https://medium.com/coderscorner/tale-of-client-server-and-socket-a6ef54a74763">Reference</a>
 */
public class Server {

  private final static int PORT = 9093;
  private Selector selector;
  private InetSocketAddress listenAddress;

  public Server(String address, int port) throws IOException {
    listenAddress = new InetSocketAddress(address, port);
  }

  public static void main(String[] args) throws Exception {
    try {
      new Server("localhost", PORT).startServer();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Start the server
   *
   * @throws IOException
   */
  private void startServer() throws IOException {
    this.selector = Selector.open();
    ServerSocketChannel serverChannel = ServerSocketChannel.open();
    serverChannel.configureBlocking(false);

    // bind server socket channel to port
    serverChannel.socket().bind(listenAddress);
    serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

    System.out.println("Waiting on port : " + PORT + "...");
    boolean listening = true;

    while (listening) {
      // wait for events
      int readyCount = selector.select();
      if (readyCount == 0) {
        continue;
      }

      // process selected keys...
      Set<SelectionKey> readyKeys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = readyKeys.iterator();
      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();

        // Remove key from set so we don't process it twice
        iterator.remove();

        if (!key.isValid()) {
          continue;
        }

        if (key.isAcceptable()) { // Accept client connections
          this.accept(key);
        } else if (key.isReadable()) { // Read from client
          this.read(key);
        } else if (key.isWritable()) {
          // write data to client...
        }
      }
    }
  }

  // accept client connection
  private void accept(SelectionKey key) throws IOException {
    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
    SocketChannel channel = serverChannel.accept();
    channel.configureBlocking(false);
    Socket socket = channel.socket();
    SocketAddress remoteAddr = socket.getRemoteSocketAddress();
    System.out.println("Connected to: " + remoteAddr);

    /*
     * Register channel with selector for further IO (record it for read/write
     * operations, here we have used read operation)
     */
    channel.register(this.selector, SelectionKey.OP_READ);
  }

  // read from the socket channel
  private void read(SelectionKey key) throws IOException {
    SocketChannel channel = (SocketChannel) key.channel();
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    int numRead = -1;
    numRead = channel.read(buffer);

    if (numRead == -1) {
      Socket socket = channel.socket();
      SocketAddress remoteAddr = socket.getRemoteSocketAddress();
      System.out.println("Connection closed by client: " + remoteAddr);
      channel.close();
      key.cancel();
      return;
    }

    byte[] data = new byte[numRead];
    System.arraycopy(buffer.array(), 0, data, 0, numRead);
    System.out.println("Got: " + new String(data));
  }
}
