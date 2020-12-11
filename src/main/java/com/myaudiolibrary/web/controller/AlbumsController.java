package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.AlbumRepository;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Controller
@RequestMapping(value = "albums")
public class AlbumsController {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RedirectView addAlbums(Album album, @RequestParam("artistId")Integer id){

        // verifier si le nom de album n'est pas vide

        Optional<Artist> optionalArtist =  artistRepository.findById(id);

        if(optionalArtist.isEmpty())
            throw new EntityNotFoundException("il n'existe pas d'artiste avec l'id " + id);

        album.setArtist(optionalArtist.get());
        albumRepository.save(album);

        return new RedirectView("/artists/" + album.getArtist().getId());
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public RedirectView deleteAlbums(@PathVariable("id") Integer id){

        Optional<Album> optionalAlbum =  albumRepository.findById(id);

        if(optionalAlbum.isEmpty())
            throw  new EntityNotFoundException("L'album avec l'id " + id + " n'existe pas");

        Integer artistId = optionalAlbum.get().getArtist().getId();
        albumRepository.deleteById(id);

        return new RedirectView("/artists/" + artistId);
    }
}
