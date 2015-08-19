# Android-SMS-Application

Il s'agit d'un projet que j'ai cree pour les etudiants et valide par l'equipe pedagogique durant mon curses a 42.

Le but du sujet est de familiariser les etudiants avec le système Android en réalisant une application de gestion de contacts

## Sujet

### Introduction

Pour ce projet, vous devrez réaliser une application Android permettant de créer un contact et de dialoguer avec par SMS.

Le but ici est de comprendre comment fonctionne une application Android, comment Android gère votre application et comment utiliser le SDK

### Objectifs

Vous allez devoir réaliser diverses tâches qui vous feront comprendre le fonctionnement
d’une application Android en JAVA. Le but est d’avoir une application permettant
de créer un contact (avec au minum 5 informations), de l’éditer et de le supprimer. Une
fois un contact enregistré il devra être possible de dialoguer avec lui en utilisant les SMS.
Les contacts sont enregistrés de manière persistante (base de donnée SQLite, n’utilisez
pas la table de contact partagée, mais créez bien la votre). Un résumé de chaque contact
sera présent sur l’accueil de l’application sous forme de liste, un clic sur un résumé permettra
de voir toutes les informations concernant le contact correspondant.

Votre application devra également fonctionner sous deux langues dont une par défaut
(changez la langue du système pour tester). Lorsque vous êtes sur l’accueil et que vous
passez l’application en arrière-plan, la date sera sauvegardée et affichée dans un toast lors
de votre retour au premier plan. Un menu permettra de changer la couleur du header de
l’application. Et pour terminer, l’icone de l’application sera le logo de 42.

### Consignes générales

• Ce projet ne sera évalué que par des humains.

• Le projet devra être en JAVA.

• Aucune librairie externe (même pour le design) n’est autorisée.

Il est fortement conseillé d’utiliser Android Studio comme IDE.

Faites attention, le plugin ADT pour Eclipse n’est plus supporté par

Google.

### Partie obligatoire

Voici ce que vous devrez réaliser :

• Création d’un contact.

• Edition d’un contact.

• Suppression d’un contact.

• Page d’accueil avec un résumé de chaque contact.

• Pouvoir recevoir les SMS de vos contacts enregistrés.

• Pouvoir envoyer des SMS à vos contacts.

• Un menu doit être disponible pour changer la couleur du header.

• L’application devra supporter deux langues.

• Afficher l’heure de la mise en arrière plan lors du retour sur l’application.

• L’application fonctionne en mode portrait et paysage.

• Le logo de l’application sera celui de 42.

### Partie bonus

• Avoir une photo pour les contacts.

• A la réception d’un SMS, un contact avec le numéro en nom est directement créé

• C’est beau ! Le Material Design c’est cool.

• On peut appeller le contact.

Faites-vous plaisir, plein de choses peuvent améliorer l’application.

### Rendu et peer-évaluation

Attention à votre dépot, plein de fichiers plus ou moins utiles sont générés dans votre
projet. Pensez à bien configurer votre .gitignore.

Pour la correction le projet sera compilé et installé avec :

$./gradlew installDebug

## Screenshot
<img src="/misc/mainScreen.png" alt="alt text" width="50%" height="50%">      <img src="/misc/add.png" alt="alt text" width="50%" height="50%">   <img src="/misc/view.png" alt="alt text" width="50%" height="50%">
