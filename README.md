# myaudiolibrary version thymeleaf

! Dans cette version la modification et la suppression d'un artiste s'effectue comme dans le travaux pratique c'est à dire :  

*Création d'un artiste:*  
- L'artiste n'existe pas dans la page détails, "action" du formulaire contient la route "/artists/" en mode "POST". 
- Le controller répondant à cette route a pour objectif d'enregistrer l'artiste reçu dans son requestBody .
- Le bouton suppression dans cette configuration disparait, et le bouton de sauvegarde de l'artiste se nomme "Enregistrer".


*Modification d'un artiste :*  
- Lorsqu'un artiste existe dans la page détails,  l'url "action" lié au "POST" dans le formulaire a été incrémenté du mot "/update", l'url qu'on obtient da,s "action" est alors  => "/artists/update".
- Il est également ajouter l'id de l'artiste dans un input hidden.
- Dans le controller répondant à cette route le même artiste (confirmation via son id) et nous le sauvegardons cela permet ici de changer son nom.
- Le bouton suppression apparait (voir dessous pour son fonctionnement) , et le bouton de modification de l'artiste se nomme dans cette page "Modifier".

*Suppression d'un artiste:*  
- Il es nécessaire pour pouvoir supprimer un artiste qu'on ne soit pas en mode création (sinon le bouton n'apparait pas), le bouton supprimer est un lien <a>
  en HTML donc le href appele une route nommé => "/artists/ id_de_l'artiste /delete" (id_de_l'artiste étant renseigné grace au modelput artist.id) . 
- Le controller répondant à cette route est un "GET" ayant comme value ="/{id}/delete", il récupère ensuite l'id de l'artiste afin de supprimer dans la bdd.
  

- Création de la branche eval pour le projet thymeleaf
- Creation de la pull request
