package com.example;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;




public class Main {
   public static void main(String[] args) throws IOException {
      ServerSocket ss = new ServerSocket(8080);
      while (true) {
          Socket s = ss.accept();
          try {
               BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream())) ;
               DataOutputStream out = new DataOutputStream(s.getOutputStream());
               String[] firstLine = in.readLine().split(" ");
               String resource = firstLine[1];
               String header;
               do {
                   header = in.readLine();
               } while (!header.isEmpty());
               if (resource.equals("/")){
                   resource = "index.html";
               }
               File file = new File("htdocs/" + resource);
               if (file.exists()) {
                   out.writeBytes("HTTP/1.1 200 OK\n");
                   out.writeBytes("Content-Length: "+ file.length()+ "\n");
                   out.writeBytes("Content-Type: "+getContentType(file)+"\n");
                   out.writeBytes("\n");
                   InputStream input = new FileInputStream(file);
                   byte[] buf = new byte[8192];
                   int n;
                   while ((n = input.read(buf)) != -1) {
                       out.write(buf,0,n);
                   }
               } else {
                   String msg = "File non trovato";
                   out.writeBytes("HTTP/1.1 404 Not found\n");
                   out.writeBytes("Content-Length: "+ msg.length()+ "\n");
                   out.writeBytes("Content-Type: text/plain\n");
                   out.writeBytes("\n");
                   out.writeBytes(msg);
               }
               out.close();
               in.close();
          } catch (IOException e) {
               System.err.println(e);
          } finally {
               s.close();
          }
      }
  }
   private static String getContentType(File f) {
       String[] s = f.getName().split("\\.");
       String ext = s[s.length - 1];
       switch (ext) {
           case "html":
           case "htm":
               return "text/html";
           case "jpg":
           case "jpeg":
               return "image/jpeg";
           case "css":
               return "text/css";
           case "png":
               return "image/png";
           case "js":
               return "application/javascript";
           default:
               return "";
       }
   }
}
