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

    /**
     * appeler quand on souhaite ajouter un album a un artiste
     * @param album album a ajouter
     * @param artistId identifiant de l'artiste à ajouter.
     * @return vers la vue details.artist.html
     */
    @PostMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView addAlbum(Album album,@RequestParam("artistId") Integer artistId){

        // si le nom de l'artiste est vide:
        if (album.getTitle().isBlank())
            throw new IllegalArgumentException("Le nom de l'album ne peut pas être vide");

        // si le nom l'album existe déja dans la bdd (pas forcement pertinent peut-être retiré au besoin)
        if (albumRepository.existsByTitleIgnoreCase(album.getTitle()))
            throw new EntityExistsException("L'album \"" + album.getTitle() + "\" existe déja");

        // vérifie si l'artiste auquel on souhaite rajouter l'album existe bien:
        if(!artistRepository.existsById(artistId))
            throw new EntityNotFoundException("il n'existe pas d'artiste avec l'id " + artistId);

        Optional<Artist > optionalArtist = artistRepository.findById(artistId);

        album.setArtist(optionalArtist.get());
        albumRepository.save(album);

        return new RedirectView("/artists/" + optionalArtist.get().getId());
    }


    /**
     * Appeler quand on souhaite supprimer l'albulm
     * @param id de l'album que l'on souhaite supprimé.
     * @return
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public RedirectView deleteAlbum(@PathVariable("id") Integer id) {

        // es que l'album existe réellement:
        if(!albumRepository.existsById(id))
            throw  new EntityNotFoundException("L'album avec l'id " + id + " n'existe pas");

        Optional<Album> optionalAlbum = albumRepository.findById(id);
        
        Integer idArtist = optionalAlbum.get().getArtist().getId();

        albumRepository.deleteById(id);

        return new RedirectView("/artists/" +idArtist);
    }

}
