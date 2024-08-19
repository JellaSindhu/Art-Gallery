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
import com.example.artgallery.repository.GalleryRepository;

import java.util.*;

@Service
public class GalleryJpaService implements GalleryRepository {
    @Autowired
    private GalleryJpaRepository galleryJpaRepository;

    @Autowired
    private ArtistJpaRepository artistJpaRepository;

    @Override
    public List<Gallery> getGalleries() {
        try {
            List<Gallery> galleryList = galleryJpaRepository.findAll();
            ArrayList<Gallery> galleries = new ArrayList<>(galleryList);
            return galleries;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Gallery getGalleryById(int galleryId) {
        try {
            return galleryJpaRepository.findById(galleryId).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Gallery addGallery(Gallery gallery) {
        List<Integer> artistIds = new ArrayList<>();
        for (Artist artist : gallery.getArtists()) {
            artistIds.add(artist.getArtistId());
        }
        try {
            List<Artist> complete_artists = artistJpaRepository.findAllById(artistIds);
            if (artistIds.size() != complete_artists.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid artist ids");
            }
            gallery.setArtists(complete_artists);

            galleryJpaRepository.save(gallery);
            return gallery;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Gallery updateGallery(int galleryId, Gallery gallery) {
        try {
            Gallery newGallery = galleryJpaRepository.findById(galleryId).get();
            if (gallery.getGalleryName() != null) {
                newGallery.setGalleryName(gallery.getGalleryName());
            }
            if (gallery.getLocation() != null) {
                newGallery.setLocation(gallery.getLocation());
            }
            if (gallery.getArtists() != null) {
                List<Integer> artistIds = new ArrayList<>();
                for (Artist artist : gallery.getArtists()) {
                    artistIds.add(artist.getArtistId());
                }
                List<Artist> complete_artists = artistJpaRepository.findAllById(artistIds);
                if (artistIds.size() != complete_artists.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid artist ids");
                }
                newGallery.setArtists(complete_artists);
            }
            return galleryJpaRepository.save(newGallery);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteGallery(int galleryId) {
        try {
            // Fetch the author entity
            Gallery gallery = galleryJpaRepository.findById(galleryId).get();

            // Remove the associations
            List<Artist> artists = gallery.getArtists();
            for (Artist artist : artists) {
                artist.getGalleries().remove(gallery);
            }

            // Update the book entity after removing the association
            artistJpaRepository.saveAll(artists);

            // Delete the author
            galleryJpaRepository.deleteById(galleryId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Artist> getGalleryArtists(int galleryId) {
        try {
            Gallery gallery = galleryJpaRepository.findById(galleryId).get();
            return gallery.getArtists();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}