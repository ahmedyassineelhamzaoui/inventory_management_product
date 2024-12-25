# Système de Gestion d'Inventaire

Application de gestion d'inventaire client-serveur permettant de gérer des produits avec une interface graphique Java Swing.

## Prérequis

### Base de données
- MySQL Server 5.7+ ou MariaDB 10.x+
- Un utilisateur MySQL avec les droits nécessaires

### Environnement de développement
- JDK 8 ou supérieur
- Maven 3.9 ou supérieur
- IDE (Eclipse, IntelliJ IDEA, ou NetBeans)

## Installation

### 1. Configuration de la base de données



Via MySQL Workbench/phpMyAdmin :
- Ouvrez le fichier `src/main/resources/inventory.sql`
- Exécutez le script dans 

### 2. Configuration du projet

1. Clonez ou téléchargez le projet

2. Configurez les paramètres de connexion à la base de données :
    - Ouvrez le fichier `src/main/resources/config.properties`
    - Modifiez les paramètres selon votre configuration :
    - si vous avez la meme configuration que moi vous pouvez laisser les parametres par defaut
   
```properties
db.url=jdbc:mysql://localhost:3306/inventory
db.user=votre_utilisateur
db.password=votre_mot_de_passe
```

## Exécution

1. Méthode 1 : Exécuter le fichier JAR directement :

```bash
cd release
java -jar inventory_management-1.0-SNAPSHOT.jar
```
2. Méthode 2 : Exécuter le projet depuis le code source (en cas de problème):

```bash
cd chemin/vers/le/projet
java -cp target/classes org.example.Launcher
```
3. Exécuter depuis un IDE :

```bash
   Importez le projet dans l'IDE.
   Localisez la classe Launcher dans le répertoire src/main/java.
   Faites un clic droit sur la classe et sélectionnez Run ou Exécuter.
```

## Fonctionnalités

- Ajout de nouveaux produits
- Modification des produits existants
- Suppression de produits
- chercher des produits
- Visualisation de l'inventaire
- Gestion des stocks
- Interface graphique intuitive

## Structure du Projet

```
inventory_management/
├── release/
│   └── inventory_management-1.0-SNAPSHOT.jar
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/
│   │   │       └── example/
│   │   │           ├── client/
│   │   │           ├── server/
│   │   │           ├── model/
│   │   │           ├── filter/
│   │   │           ├── DAO/
│   │   │           ├── database/
│   │   │           └── Launcher.java
│   │   └── resources/
│   │       ├── config.properties
│   │       └── inventory.sql
│   └── test/
├── pom.xml
├── inventory_logs.log
├── README.md
└── .gitignore
```

## Dépannage

### Le serveur ne démarre pas
- Vérifiez que le port 5000 est disponible
- Vérifiez les logs dans la console
- Vérifiez la connexion à la base de données

### Erreur de connexion client
- Assurez-vous que le serveur est démarré
- Vérifiez que le pare-feu n'empêche pas la connexion
- Vérifiez les paramètres de connexion

### Erreur de base de données
- Vérifiez que MySQL est en cours d'exécution
- Vérifiez les identifiants de connexion
- Vérifiez que la base de données existe

