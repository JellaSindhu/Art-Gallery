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

public interface ArtRepository {
    List<Art> getArts();

    Art getArtById(int artId);

    Art addArt(Art art);

    Art updateArt(int artId, Art art);

    void deleteArt(int artId);
    Artist getArtArtist(int artId);
}