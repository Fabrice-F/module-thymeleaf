package com.myaudiolibrary.web.controller;


import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.awt.print.Pageable;
import java.util.Optional;

@Controller
@RequestMapping("/artists")
public class ArtistsController {

    @Autowired
    ArtistRepository artistRepository;

    /**
     * Appeler lorque l'on souhaite afficher un artiste
     * @param id De l'artiste
     * @param model artiste retourner a la vue (detailArtist.html)
     * @return
     */
    @GetMapping(value = "/{id}")
    public String getArtistById(@PathVariable("id") Integer id, final ModelMap model){
        // catch erreur de conversion ( si artist/A ... )

        // erreur 404 not found
        if(!artistRepository.existsById(id)){
            throw new EntityNotFoundException("L'employé d'identifiant " + id + " n'a pas été trouvé !");
        }

        Optional<Artist> artistOptional = artistRepository.findById(id);

        model.put("artist",artistOptional.get());
        return "detailArtist";
    }

    /**
     * Recherche artiste par nom:
      * @param name de l'artiste rechercher
     * @param page numéro de la recherche
     * @param size nombre de résultat retourné par page
     * @param sortProperty recherche par (ici name)
     * @param sortDirection ordre ascendant ou descendant
     * @param model pageArtist renvoye a la vue (listeArtists.html)
     * @return
     */
    @GetMapping(params = {"name"})
    public String searchByName(
            @RequestParam("name") String name,
            @RequestParam(defaultValue = "0", name = "page") Integer page,
            @RequestParam(defaultValue = "10",name = "size") Integer size,
            @RequestParam(defaultValue = "name" ,name = "sortProperty") String sortProperty,
            @RequestParam(defaultValue = "ASC",name = "sortDirection") String sortDirection,
            final ModelMap model){

        // erreur si recherche sans nom:
        if(name.isBlank())
            throw new IllegalArgumentException("La recherche ne peut pas avoir un nom vide");

        // vérifie si les paramètres sont valides
        paramsIsOk(page,size,sortDirection,sortProperty);

        // récupère lkes résultats
        PageRequest pageRequest = PageRequest.of(page,size, Sort.Direction.valueOf(sortDirection),sortProperty);
        Page<Artist> artistPage = artistRepository.findAllByNameIgnoreCaseContaining(name,pageRequest);


        model.put("page",page);
        model.put("size",size);
        model.put("sortDirection",sortDirection);
        model.put("sortProperty",sortProperty);
        model.put("artistPage",artistPage);
        model.put("name", name);
        model.put("pageNumber", page + 1);
        model.put("previousPage", page - 1);
        model.put("nextPage", page + 1);
        model.put("start", page * size + 1);
        model.put("end", (page) * size + artistPage.getNumberOfElements());

        return "listeArtists";
    }

    /**
     * Recherche artiste par nom:
     * @param page numéro de la recherche
     * @param size nombre de résultat retourné par page
     * @param sortProperty recherche par (ici name)
     * @param sortDirection ordre ascendant ou descendant
     * @param model pageArtist renvoye a la vue (listeArtists.html)
     * @return
     */
    @GetMapping()
    public String getAllArtist(
            @RequestParam(defaultValue = "0", name = "page") Integer page,
            @RequestParam(defaultValue = "10",name = "size") Integer size,
            @RequestParam(defaultValue = "name" ,name = "sortProperty") String sortProperty,
            @RequestParam(defaultValue = "ASC",name = "sortDirection") String sortDirection,
            final ModelMap model){

        paramsIsOk(page,size,sortDirection,sortProperty);

        PageRequest pageRequest = PageRequest.of(page,size, Sort.Direction.valueOf(sortDirection),sortProperty);
        Page<Artist> artistPage = artistRepository.findAll(pageRequest);


        model.put("page",page);
        model.put("size",size);
        model.put("sortDirection",sortDirection);
        model.put("sortProperty",sortProperty);
        model.put("artistPage",artistPage);
        model.put("pageNumber", page + 1);
        model.put("previousPage", page - 1);
        model.put("nextPage", page + 1);
        model.put("start", page * size + 1);
        model.put("end", (page) * size + artistPage.getNumberOfElements());

        return "listeArtists";
    }

    /**
     * Appeler lorque l'on souhaite créer un nouvelle artiste
     * @param model artiste vide renvoye a la vue (detailArtist.html)
     * @return
     */
    @GetMapping(value = "/new")
    public String createArtist(final ModelMap model){

        Artist artist = new Artist();
        model.put("artist",artist);
        return "detailArtist";
    }


    /**
     * Appeler lorsque l'on sauvegarde un nouvelle artiste
     * @param artist à sauvegarger
     * @return vers la vue le détails de l'artiste
     */
    @PostMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView saveArtist(Artist artist){

        // vérifie si le nom est vide
        if(artist.getName().isBlank())
            throw new IllegalArgumentException("Le nom de l'artiste ne peut pas être vide");

        // verifie si l'artiste existe déja :
        if (artistRepository.existsByNameIgnoreCase(artist.getName()))
            throw new EntityExistsException("L'artiste " + artist.getName() + " existe déja dans la bdd");

        Artist artistSave=  artistRepository.save(artist);

        return new RedirectView("/artists/" + artistSave.getId());
    }

    /**
     * Appeler lorsque l'on met à jour un artiste
     * @param artist que l'on mets a jour
     * @return vers la vue le détails de l'artiste
     */
    @PostMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE, value = "/update")
    public RedirectView updateArtist(Artist artist){

        // vérifie si le nom est vide
        if(artist.getName().isBlank())
            throw new IllegalArgumentException("Le nom de l'artiste ne peut pas être vide");

        // verifie si lors de la modif l'artiste existe déja en bdd :
        if (artistRepository.existsByNameIgnoreCase(artist.getName()))
            throw  new EntityNotFoundException("L'artiste existe déja en bdd");

        Artist artistSave=  artistRepository.save(artist);
        return new RedirectView("/artists/"+artistSave.getId() );
    }

    /**
     * Appeler quand on supprime un artiste
     * @param id de l'artiste a supprimer
     * @return vers la page d'accueil
     */
    @GetMapping(value = "/{id}/delete")
    @ResponseStatus(code = HttpStatus.OK)
    public String deleteArtist(@PathVariable Integer id){

        // si l'artiste existe bien dans la bdd
        if(!artistRepository.existsById(id))
            throw new EntityNotFoundException("Aucun artiste trouvé avec l'id: " + id );

        artistRepository.deleteById(id);
        return "accueil";
    }

    /**
     * Methode appeler pour vérifier les paramètres lorsque l'on fait une recherche d'artiste
     * ou que l'on affiche la liste des artiste.
     * @param page numéro de la page
     * @param size  nombre de résultat par page
     * @param sortDirection ordre ascendant ou descandant.
     */
    private void paramsIsOk(Integer page, Integer size , String sortDirection,String sortProperty){

        // la page ne peut pas être à négative
        if (page < 0) {
            throw new IllegalArgumentException("Le paramètre page doit être positif ou nul !");
        }

        // le nombre de résultat par page ne peut pas être à négatif ou excessif.
        if(size <= 0 || size > 50){
            throw new IllegalArgumentException("Le paramètre size doit être compris entre 0 et 50");
        }

        // on ne peut pas avoir une valeur différente de ASC ou DESC:
        if(!"ASC".equalsIgnoreCase(sortDirection) &&
                !"DESC".equalsIgnoreCase(sortDirection)){
            throw new IllegalArgumentException("Le paramètre sortDirection doit valoir ASC ou DESC");
        }


        // on ne peut pas avoir une valeur différente de null et de name
        if(sortProperty!= null && sortProperty.compareToIgnoreCase("name")!=0) {
            throw new IllegalArgumentException("Le paramètre sortProperty doit avoir la valeur name");
        }



    }
}
