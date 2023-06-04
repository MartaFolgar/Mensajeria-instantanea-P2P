# Mensajeria-instantanea-P2P
Implementación de una aplicación distribuida mediante Java RMI. La aplicación consiste en un sistema de mensajería instantánea donde el servidor aceptará conexiones de múltiples clientes, notificando a todos ellos ante la llegada/desconexión de cada usuario. 

Se proporcionará un mecanismo de envío/recepción de mensajes que necesariamente se realizará de cliente a cliente, sin pasar por el servidor. Se gestionarán además grupos de amistad entre usuarios, por lo que se implementará un mecanismo de registro en el sistema así como uno que permita solicitar amistad a otro cliente. En el caso de los grupos de amistad, las notificaciones por parte del servidor se realizarán exclusivamente a los clientes que forman parte del mismo grupo.
