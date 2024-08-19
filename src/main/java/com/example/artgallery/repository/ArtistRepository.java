/*
 * You can use the following import statements
 *
 * import java.util.ArrayList;
 * import java.util.List;
 * 
 */

// Write your code here
package com.example.artgallery.repository;

import java.util.*;
import com.example.artgallery.model.*;

public interface ArtistRepository {
    List<Artist> getArtists();

    Artist getArtistById(int artistId);

    Artist addArtist(Artist artist);

    Artist updateArtist(int artistId, Artist artist);

    void deleteArtist(int artistId);
    List<Art> getArtistArts(int artistId);
    List<Gallery> getArtistGalleries(int artistId);
}