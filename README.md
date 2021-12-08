# Rapport pour le TP3

[Sujet trouvable ici](https://www.fil.univ-lille1.fr/~noe/rsx1_portail/files/TP3.pdf).

Compilation de tous les fichiers : 
```sh
$ make
```

### Première expérience

Le code peut être éxécuté de cette façon :

Lancer le serveur :
```sh
$ make run_hello_serv
```

Connecter un client :
```sh
$ make run_one_time_client
```


- Q1  
Une requête est constituée de deux étapes :
  - Demande allant du client au serveur
  - Réponse allant du serveur au client

> Ici, on n'a pas de requête à proprement parler puisque le client ne va 
> rien demander au serveur et celui va directement lui envoyer un message.

- Q2  
Les exceptions à traiter seront les exceptions de type `IOException`
étant donné que ce sont celles qui sont appelées dès que l'on va avoir 
besoin de communiquer d'une façon ou d'une autre.

> `I/O` pour `In/Out` ou en français `Dedans/Dehors`...  

- Q3  
Une fois le programme réalisé, on pourrait tester son bon fonctionnement
en essayant de s'y connecter depuis un client tel que `telnet` ou un client
réalisé en Java (l'option qui va être privilégiée ici).


- Q4  
On pourrait garder la trace de toutes les connections à l'aide d'une des 
façons suivantes :
  - En affichant qui se connecte, quand, sa requête... dans le flux de sorti du serveur
  - Rediriger ce flux vers un 'journal' (fichier de logs)

---

### Serveur de dialogue

Éxécution du code :

Lancement du serveur :
```sh
$ make run_server
```

Connexion d'un client :
```sh
$ make connect_client
```

- Q1  
On va pouvoir créer un nouveau thread à chaque fois que l'on va accepter une socket. 
> Ici le code ressemblerait à ça :
> ```java
> while (true) {
>   Socket s = serverSocket.accept(); // Wait for a socket here
>   MyCustomThread thread = new MyCustomThread(s); // Inject the socket through a constructor
>   thread.start(); // Start the custom thread
> }
> ```
> (Ça ne sera probablement pas le résultat final étant donné qu'on va vouloir faire
> en sorte de gérer une `pool` de thread afin de pouvoir les retrouver)
> 
> ---
> 
> Edit : Après réflexion, le code ressemble maintenant à ça :
> ```java
> while (true) {
>   Socket s = serverSocket.accept(); // Wait for a socket here
>   clientHandler.add(s); // Method that will create a consumer for a client and the related thread
> }
> ```
> ```java
> public class ClientHandler {
>     private final List<ClientConsumer> clients;
> 
>    public void add(Socket s) {
>        ClientConsumer clientConsumer = new ClientConsumer(s, this); //Create a new ClientConsumer
>        // Injecting through the constructor the socket and the handler (this)
>        clientConsumer.start(); // Start the ClientConsumer's thread
>        clients.add(clientConsumer); // Add the ClientConsumer to the List, so we can find it back
>    }
> ```
> La logique reste la même, elle a juste été améliorée de façon  avoir un programme plus solide.


- Q2
Les primitives permettant de récupérer un message sur une socket sont :
  - `ServerSocket#getOutputStream` couplé à `OutputStream#send` (envoie du message depuis le serveur)
  - `Socket#getInputStream` couplé à `InputStream#read` (réception du message)
> Edit : ici on préfèrera utiliser des [`BufferedReader`](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/io/BufferedReader.html)
> pour la réception et des [`PrintWriter`](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/io/PrintWriter.html) pour la sortie.  
> (Plus directs.)

- Q3
On pourrait par exemple se servir d'un patron de conception tel que `Observer/Observable`
> Edit : le patron de conception `Observer/Observable` fourni par Java est devenu [`@Deprecated`](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/lang/Deprecated.html)
> à partir de java 9, j'ai donc préféré opter pour une solution "plus simple" à mettre en place étant :
> - réception du message sur la `Socket` liée
> - ajout d'un "entête" serveur
> - envoie à toutes les `Socket`s à l'aide d'un `forEach`
> 
> Plus d'informations en regardant les classes [`ClientHandler`](./src/tcpChat/ClientHandler.java) (gère la liste de `Socket`s)
> et [`ClientConsumer`](./src/tcpChat/ClientConsumer.java).

- Q4  
C.f. la Q4 de partie précédente.  
Ici, on garde la trace de tout ce qui s'est passé à l'aide d'un [`Logger`](./src/tcpChat/Logger.java) 
qui va tout renvoyer sur la sortie standard du `Server`.


- Q5
On peut soit essayer de se connecter depuis un autre client, soit vérifier depuis 
un client déjà connecté ou encore regarder la trace du programme pour savoir si 
une exception a été levée.
> Edit :  
> Pour le moment, quand un client telnet quitte intempestivement, telnet envoie 
> un message identifié comme `null` au serveur qui va log une NPE non bloquante :
> ```java
> 0|2021-11-03T13:36:32.296955511 ~
> 
> java.lang.NullPointerException: Cannot invoke "String.equals(Object)" because "received" is null
> at tcpChat.ClientConsumer.run(ClientConsumer.java:35)
> ```
