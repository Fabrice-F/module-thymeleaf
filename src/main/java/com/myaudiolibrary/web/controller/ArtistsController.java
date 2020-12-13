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

    @GetMapping(value = "/{id}")
    public String getArtistById(@PathVariable("id") Integer id, final ModelMap model){

        // catch erreur de conversion


        if(!artistRepository.existsById(id)){
            throw new EntityNotFoundException("L'employé d'identifiant " + id + " n'a pas été trouvé !");
        }

        Optional<Artist> artistOptional = artistRepository.findById(id);


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
            throw new IllegalArgumentException("La recherche ne peut pas avoir un nom vide");

        paramsIsOk(page,size,sortDirection);

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

        paramsIsOk(page,size,sortDirection);

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
    public RedirectView saveArtist(final ModelMap model,Artist artist){

        if(artist.getName().isBlank())
            throw new IllegalArgumentException("Le nom de l'artiste ne peut pas être vide");

        if (artistRepository.existsByNameIgnoreCase(artist.getName()))
            throw new EntityExistsException("L'artiste " + artist.getName() + " existe déja dans la bdd");

        Artist artistSave=  artistRepository.save(artist);

        return new RedirectView("/artists/" + artistSave.getId());
    }

    @PostMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE, value = "/update")
    public RedirectView updateArtist(Artist artist){

        if(artist.getName().isBlank())
            throw new IllegalArgumentException("Le nom de l'artiste ne peut pas être vide");

        if (!artistRepository.existsByNameIgnoreCase(artist.getName()))
            throw  new EntityNotFoundException("L'artiste n'existe pas");

        Artist artistSave=  artistRepository.save(artist);
        return new RedirectView("/artists/"+artistSave.getId() );
    }

    @GetMapping(value = "/{id}/delete")
    @ResponseStatus(code = HttpStatus.OK)
    public String deleteArtist(@PathVariable Integer id){

        if(!artistRepository.existsById(id))
            throw new EntityNotFoundException("Aucun artiste trouvé avec l'id: " + id );

        artistRepository.deleteById(id);
        return "accueil";
    }

    private void paramsIsOk(Integer page, Integer size , String sortDirection){

        if (page < 0) {
            throw new IllegalArgumentException("Le paramètre page doit être positif ou nul !");
        }

        if(size <= 0 || size > 50){
            throw new IllegalArgumentException("Le paramètre size doit être compris entre 0 et 50");
        }

        if(!"ASC".equalsIgnoreCase(sortDirection) &&
                !"DESC".equalsIgnoreCase(sortDirection)){
            throw new IllegalArgumentException("Le paramètre sortDirection doit valoir ASC ou DESC");
        }
    }
}
