/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * 
 * import java.util.*;
 *
 */

// Write your code here
package com.example.artgallery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.artgallery.model.*;
import com.example.artgallery.repository.*;
import com.example.artgallery.repository.ArtRepository;
import java.util.*;

@Service
public class ArtJpaService implements ArtRepository {
    @Autowired
    private ArtJpaRepository artJpaRepository;

    @Autowired
    private ArtistJpaRepository artistJpaRepository;

    @Autowired
    private GalleryJpaRepository galleryJpaRepository;

    @Override
    public List<Art> getArts() {
        try {
            List<Art> artsList = artJpaRepository.findAll();
            ArrayList<Art> arts = new ArrayList<>(artsList);
            return arts;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Art getArtById(int artId) {
        try {
            return artJpaRepository.findById(artId).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Art addArt(Art art) {
        try {
            int artistId = art.getArtist().getArtistId();
            Artist artist = artistJpaRepository.findById(artistId).get();
            art.setArtist(artist);
            return artJpaRepository.save(art);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Art updateArt(int artId, Art art) {
        try {
            Art newArt = artJpaRepository.findById(artId).get();
            if (art.getArtTitle() != null) {
                newArt.setArtTitle(art.getArtTitle());
            }
            if (art.getTheme() != null) {
                newArt.setTheme(art.getTheme());
            }
            if (art.getArtist() != null) {
                int artistId = art.getArtist().getArtistId();
                Artist artist = artistJpaRepository.findById(artistId).get();
                newArt.setArtist(artist);
            }
            return artJpaRepository.save(newArt);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteArt(int artId) {
        try {
            artJpaRepository.deleteById(artId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);

    }
    
    @Override
    public Artist getArtArtist(int artId) {
        try {
            Art art = artJpaRepository.findById(artId).get();
            // return countryJpaRepository.findByAthlete(athlete);
            Artist artist = art.getArtist();
            return artist;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

}
