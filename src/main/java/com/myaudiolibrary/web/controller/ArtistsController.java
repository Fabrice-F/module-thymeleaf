package com.myaudiolibrary.web.controller;


import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @GetMapping(value = "/{id}")
    public String getArtistById(@PathVariable("id") Integer id, final ModelMap model){

        // catch erreur de conversion
        Optional<Artist> artistOptional = artistRepository.findById(id);
        if(artistOptional.isEmpty()){
            throw new EntityNotFoundException("L'employé d'identifiant " + id + " n'a pas été trouvé !");
        }
        model.put("artist",artistOptional.get());
        return "detailArtist";
    }

    @GetMapping(params = {"name"})
    public String searchByName(
            @RequestParam("name") String name,
            @RequestParam(defaultValue = "0", name = "page") Integer page,
            @RequestParam(defaultValue = "10",name = "size") Integer size,
            @RequestParam(defaultValue = "name" ,name = "sortProperty") String sortProperty,
            @RequestParam(defaultValue = "ASC",name = "sortDirection") String sortDirection,
            final ModelMap model){

        if(name.isBlank())
            throw new IllegalArgumentException("Le nom ne peut pas être vide");

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

    @GetMapping()
    public String getAllArtist(
            @RequestParam(defaultValue = "0", name = "page") Integer page,
            @RequestParam(defaultValue = "10",name = "size") Integer size,
            @RequestParam(defaultValue = "name" ,name = "sortProperty") String sortProperty,
            @RequestParam(defaultValue = "ASC",name = "sortDirection") String sortDirection,
            final ModelMap model){


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

    @GetMapping(value = "/new")
    public String createArtist(final ModelMap model){

        Artist artist = new Artist();
        model.put("artist",artist);
        return "detailArtist";
    }


    @PostMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView saveArtist(Artist artist){
        // TODO: Traiter si artist n'as pas de nom renseigné


        if (artistRepository.existsByNameIgnoreCase(artist.getName()))
            throw new EntityExistsException("L'artiste " + artist.getName() + " existe déja dans la bdd");

        Artist artistSave=  artistRepository.save(artist);

        return new RedirectView("/artists/" + artistSave.getId());
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/{id}")
    public RedirectView updateArtist(Artist artist, @PathVariable("id")Integer id){

        if (!artistRepository.existsById(id))
            throw  new EntityNotFoundException("L'artiste avec l'id: " + id + " n'existe pas");

        Artist artistSave=  artistRepository.save(artist);
        return new RedirectView("/artists/" + artistSave.getId());
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/{id}")
    public String deleteArtist(@PathVariable("id")Integer id){

        if(!artistRepository.existsById(id))
            throw new EntityNotFoundException("L'artiste avec l'id: " + id + " n'as pas été trouvé");

        artistRepository.deleteById(id);

        return "accueil";
    }

}
