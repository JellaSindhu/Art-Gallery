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
import java.util.*;
import com.example.artgallery.repository.*;
import com.example.artgallery.model.*;

@Service
public class ArtistJpaService implements ArtistRepository {
    @Autowired
    private ArtistJpaRepository artistJpaRepository;

    @Autowired
    private GalleryJpaRepository galleryJpaRepository;

    @Autowired
    private ArtJpaRepository artJpaRepository;

    @Override
    public List<Artist> getArtists() {
        try {
            List<Artist> artistsList = artistJpaRepository.findAll();
            ArrayList<Artist> artists = new ArrayList<>(artistsList);
            return artists;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Artist getArtistById(int artistId) {
        try {
            return artistJpaRepository.findById(artistId).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Artist addArtist(Artist artist) {
        List<Integer> galleryIds = new ArrayList<>();
        for (Gallery gallery : artist.getGalleries()) {
            galleryIds.add(gallery.getGalleryId());
        }
        try {
            List<Gallery> complete_galleries = galleryJpaRepository.findAllById(galleryIds);
            if (galleryIds.size() != complete_galleries.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid galleryids");
            }
            artist.setGalleries(complete_galleries);

            // Add the author to all the books
            for (Gallery gallery : complete_galleries) {
                gallery.getArtists().add(artist);
            }

            Artist savedArtist = artistJpaRepository.save(artist);

            galleryJpaRepository.saveAll(complete_galleries);

            return savedArtist;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Artist updateArtist(int artistId, Artist artist) {
        try {
            Artist newArtist = artistJpaRepository.findById(artistId).get();
            if (artist.getArtistName() != null) {
                newArtist.setArtistName(artist.getArtistName());
            }
            if (artist.getGenre() != null) {
                newArtist.setGenre(artist.getGenre());
            }
            if (artist.getGalleries() != null) {
                List<Gallery> galleries = newArtist.getGalleries();
                for (Gallery gallery : galleries) {
                    gallery.getArtists().remove(newArtist);
                }
                galleryJpaRepository.saveAll(galleries);

                List<Integer> galleryIds = new ArrayList<>();
                for (Gallery gallery : artist.getGalleries()) {
                    galleryIds.add(gallery.getGalleryId());
                }
                List<Gallery> complete_galleries = galleryJpaRepository.findAllById(galleryIds);
                if (galleryIds.size() != complete_galleries.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid galleryids");
                }
                for (Gallery gallery : complete_galleries) {
                    gallery.getArtists().add(newArtist);
                }
                galleryJpaRepository.saveAll(complete_galleries);
                newArtist.setGalleries(complete_galleries);
            }
            return artistJpaRepository.save(newArtist);

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteArtist(int artistId) {
        try {
            // Fetch the author entity
            Artist artist = artistJpaRepository.findById(artistId).get();

            List<Art> arts = artJpaRepository.findByArtist(artist);

            for (Art art : arts) {
                art.setArtist(null);
            }
            artJpaRepository.saveAll(arts);

            // Remove the associations
            List<Gallery> galleries = artist.getGalleries();
            for (Gallery gallery : galleries) {
                gallery.getArtists().remove(artist);
            }

            // Update the book entity after removing the association
            galleryJpaRepository.saveAll(galleries);

            // Delete the author
            artistJpaRepository.deleteById(artistId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Art> getArtistArts(int artistId) {
        try {
            Artist artist = artistJpaRepository.findById(artistId).get();
            return artJpaRepository.findByArtist(artist);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public List<Gallery> getArtistGalleries(int artistId) {
        try {
            Artist artist = artistJpaRepository.findById(artistId).get();
            return artist.getGalleries();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}