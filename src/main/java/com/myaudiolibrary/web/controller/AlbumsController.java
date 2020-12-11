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
    ArtistRepository ArtistRepository;

    @PostMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView addAlbum(Album album,@RequestParam("artistId") Integer artistId){

        Optional<Artist > optionalArtist = ArtistRepository.findById(artistId);
        album.setArtist(optionalArtist.get());
        albumRepository.save(album);

        return new RedirectView("/artists/" + optionalArtist.get().getId());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public RedirectView deleteAlbum(@PathVariable("id") Integer id) {

        Optional<Album> optionalAlbum = albumRepository.findById(id);
        if(optionalAlbum.isEmpty())
            throw new EntityNotFoundException("L'album avec l'id " + id+" n'existe pas");

        Integer idArtist = optionalAlbum.get().getArtist().getId();

        albumRepository.deleteById(id);

        return new RedirectView("/artists/" +idArtist);
    }

}
