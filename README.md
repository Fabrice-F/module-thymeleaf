# myaudiolibrary version thymeleaf:
#### VERSION 1: 
Dans cette version, j'ai du rajouté dans **application.properties** la valeur :
```
spring.mvc.hiddenmethod.filter.enabled=true
```
afin de pouvoir utilisé comme demandé dans l'énoncé de `l'exercice 5 - Modification d'un artiste` et `6 - Suppression d'un artiste` les méthodes :
```sh
PUT /artists/4
DELETE /artists/5
```

Cela permet dans le html de rajouter la notion **put** et **delete** à un formulaire grace thymeleaf en précisant la méthode put/delete a l'attribut **action** :

*exemple :*

```
<form  id="saveForm" th:method="PUT">
```

Cela permets de génèré automatiquent un champs **input hidden** qui permettra d'appeler la route d'un controller **put** ou **delete** :

  *exemple d'un input généré pour un ***action delete:****
```
<input type="hidden" name="_method" value="delete">
```

### commit réalisé :



- Creation de la branche eval pour le projet thymeleaf
- Creation de la pull request
- Adaptation de la page accueil et page details artisteé
- Fin artist => raf: albums
- Implementation delete et ajout album ok
- Update entity ajout override method et update existbyId
- Ajout des sécurité
- Ajout des commentaires





###### Merci de votre lecture et du temps passé sur le projet
