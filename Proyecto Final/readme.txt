# Aplicación Cliente-Servidor

La Aplicación Cliente-Servidor se compone de las siguientes clases:

- **Cliente**: 
  - Encapsula la información del cliente y se encarga de la mayor parte de la interacción con el usuario.
  - Cada cliente genera un hilo `OyenteServidor`, encargado de recibir los mensajes del servidor.

- **Servidor**: 
  - Encapsula la información del servidor.
  - Los clientes le mandan los archivos que poseen en sus carpetas, y se encarga de la comunicación entre clientes y servidores, enviando toda la información.
  - Por cada cliente conectado al servidor se genera un hilo `OyenteCliente`.

- **OyenteCliente** y **OyenteServidor**:
  - Hilos ya mencionados encargados de escuchar y recibir mensajes.

- **Emisor** y **Receptor**:
  - Hilos encargados de la transmisión de archivos (P2P).

- **Mensaje**: 
  - Sirve como jerarquía de todos los mensajes enviados entre los Clientes y el Servidor.
  - Cada mensaje específico se compone de `Msg` seguido de `C` o `S` dependiendo de quién mande el mensaje, y terminando con un nombre descriptivo sobre la información que transmite.
  - Se utiliza un enumerado `TipoMensaje` para facilitar el tratamiento de los mensajes.

- **Usuario**: 
  - Representa el concepto de usuario, almacenando su nombre, ficheros y su IP.

- **ArchivoEnviado**: 
  - Encapsula la información enviada durante las conexiones P2P (nombre del archivo y contenido en bytes).

- **AsignarPuerto**: 
  - Clase estática empleada para obtener el siguiente puerto disponible para conexiones P2P (o error si no hay disponibles).

- **DatosConexion**: 
  - Encapsula la información de la conexión entre Servidor y Cliente (usuario, `in` y `out`).

- **Otros recursos**: 
  - Recursos de prácticas anteriores (`MonitorRW`, `LockTicket`) y el tipo `Pair`.

**Nota**: Las clases que implementan `Serializable` son aquellas cuyas instancias deben ser transmitidas en algún punto como información de mensaje entre Servidor y Cliente (mensajes, `ArchivoEnviado`, `Usuario`).

## Herramientas utilizadas

- **LockTicket**: 
  - En Servidor se crea una instancia de `LockTicket` que se transmite a los hilos `OyenteCliente`. Cuando un cliente solicita un archivo (`MsgCSolicitudFichero` / `MsgSolicitudEsteFichero`), el correspondiente hilo `OyenteCliente` solicita el cerrojo antes de llamar a los métodos de la clase `AsignarPuerto` y lo libera, para enviar el puerto disponible al cliente mediante el mensaje `MsgSCreaEmisor`. De esta forma, se garantiza la seguridad del atributo estático `puertoActual` de la clase `AsignarPuerto`.

- **Semáforos**: 
  - Se emplean semáforos de Java en dos puntos. Para controlar desde el Servidor el número de Clientes conectados, de forma que un Cliente deba esperar para establecer la conexión con el servidor en caso de que el servidor esté lleno.
  - El otro punto es para controlar las impresiones en la pantalla cliente entre las clases Cliente y `OyenteServidor`.

- **Monitor**: 
  - Se utiliza una modificación del monitor para escritores y lectores de la práctica anterior para controlar el acceso a la información almacenada en el Servidor. Los hilos `OyenteCliente` le piden la información al servidor, que accede en exclusión mutua a la información.

## Funcionamiento

Con el servidor iniciado, los clientes son identificados por su nombre de usuario. Dentro de la base de datos local (`database`) se encuentra un archivo con los nombres de usuario registrados y una carpeta `archivos` que contiene las carpetas con los archivos de cada usuario. Cuando dos clientes se envían un archivo por comunicación P2P, se almacena en la carpeta `downloads`. Si el archivo existe, se añade un identificador numérico.
