/*

CREATE TABLE artist(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(250),
    genre VARCHAR(250)
);

CREATE TABLE art(
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(250),
    theme VARCHAR(250),
    artistId INT,
    FOREIGN KEY(artistId) REFERENCES artist(id)
);

CREATE TABLE gallery(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(250),
    location VARCHAR(250)
);

CREATE TABLE artist_gallery(
    artistId INT,
    galleryId INT,
    PRIMARY KEY(galleryId, artistId),
    FOREIGN key(galleryId) REFERENCES gallery(id),
    FOREIGN key(artistId) REFERENCES artist(id)
);

*/
