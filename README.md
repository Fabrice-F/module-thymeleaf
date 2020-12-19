# myaudiolibrary version thymeleaf:

🖐 Bonjour, je me suis permis de réalisé 2 versions de ce projet, essentiellement car je souhaitais comprendre les différentes organisation du projet si on incluais des requêtes PUT et DELETE et si on ne les incluais pas.

Vous êtes ici dans la version n'incluant pas les requêtes **PUT / DELETE** *(lien vers version 1 en bas de page)* .

#### VERSION 2 sans PUT et DELETE: 
Dans cette version afin de pouvoir comme demandé dans l'énoncé des exercices: 

`5 - Modification d'un artiste` 
`6 - Suppression d'un artiste` 
`8 - Suppression d'un album`

les méthodes :
```sh
PUT /artists/4
DELETE /artists/5
DELETE /albums/X
```

On été transformé en méthode **POST** comme ceci:

###### exercice 4 :
La méhode `PUT /artists/4 ` appelé dans l'exercice 5 pour mettre à jour un artiste a été remplacer par un **POST** répondant à la route:

```
/artists/update
```
La méthode POST produit ici un **application/x-www-form-urlencoded** et ne prend pas un **@RequestBody** mais directement un **Artist** en paramètre. 

###### exercice 5 :

La méhode `DELETE /artists/5 ` appelé dans l'exercice 6 pour supprimer un artiste a été remplacer ici par un **GET** répondant à la route:

```
/artists/{id}/delete
```
``id`` ici représentant l'identifiant de l'ariste à supprimer.

###### exercice 8 :

La méhode `DELETE /albums/X ` appelé dans l'exercice 8 pour supprimer un artiste a été remplacer ici aussi par un **GET** répondant à la route:
```
/albums/{id}
```
``id`` ici représentant l'identifiant de l'album à supprimer.


### Commit réalisé :

- Creation PR eval_V2
- Ajout du controller et ajout de global exeption , exercice 4
- Implementation delete et update avec le post et get
- Ajout et suppression album ok!
- ajout des securite
- Ajout commentaires sur code
- Fin des corrections et fin des commentaires

###### Merci de votre lecture et du temps passé sur le projet:   
---

Je vous partage le lien de la 1e version avec les PUT et DELETE integré :
https://github.com/Fabrice-F/module-thymeleaf/tree/eval

