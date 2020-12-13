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

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Controller
@RequestMapping(value = "albums")
public class AlbumsController {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @PostMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView addAlbum(Album album,@RequestParam("artistId") Integer artistId){


        if (album.getTitle().isBlank())
            throw new IllegalArgumentException("Le nom de l'album ne peut pas être vide");

        if (albumRepository.existsByTitleIgnoreCase(album.getTitle()))
            throw new EntityExistsException("L'album \"" + album.getTitle() + "\" existe déja");

        if(!artistRepository.existsById(artistId))
            throw new EntityNotFoundException("il n'existe pas d'artiste avec l'id " + artistId);

        Optional<Artist > optionalArtist = artistRepository.findById(artistId);

        album.setArtist(optionalArtist.get());
        albumRepository.save(album);

        return new RedirectView("/artists/" + optionalArtist.get().getId());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public RedirectView deleteAlbum(@PathVariable("id") Integer id) {


        if(!albumRepository.existsById(id))
            throw  new EntityNotFoundException("L'album avec l'id " + id + " n'existe pas");

        Optional<Album> optionalAlbum = albumRepository.findById(id);
        
        Integer idArtist = optionalAlbum.get().getArtist().getId();

        albumRepository.deleteById(id);

        return new RedirectView("/artists/" +idArtist);
    }

}
