import java.net.*;
import java.io.*;

class java2003 {
  public static void main( String[] args ) {
    String cadena;

    try {
      // Creamos un objeto de tipo URL
      URL url = new URL("http://xoap.weather.com/search/search?where=vina+del+mar" );
          
      // Abrimos una conexióacia esa URL, que devolverán canal de
      // entrada por el cual se podráeer todo lo que llegue
      BufferedReader paginaHtml = 
        new BufferedReader( new InputStreamReader( url.openStream() ) );

      // Leemos y presentamos el contenido del fichero en pantalla
      // lía a lía
      while( (cadena = paginaHtml.readLine()) != null ) {
        System.out.println( cadena );
      }
    } catch( UnknownHostException e ) {
      e.printStackTrace();
      System.out.println( 
        "Debes estar conectado para que esto funcione bien." );
    } catch( MalformedURLException e ) {
      e.printStackTrace();
    } catch( IOException e ) { 
      e.printStackTrace();
    }
  }
}
