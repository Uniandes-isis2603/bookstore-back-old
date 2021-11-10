delete from REVIEW_ENTITY;
delete from BOOK_ENTITY_authors;
delete from AUTHOR_ENTITY;
delete from BOOK_ENTITY;
delete from EDITORIAL_ENTITY;

insert into EDITORIAL_ENTITY (id, name) values (100,'Oveja Negra');
insert into EDITORIAL_ENTITY (id, name) values (200,'Siruela');

insert into BOOK_ENTITY (id, name, isbn, image, description, publishing_date, editorial_id) values (100, 'The Lord of the Rings', '930330149-8', 'https://i.imgur.com/av09pgE.png', 'Supplement R Tympanic Membrane with Synth Sub, Via Opening', '1996-08-20',100);
insert into BOOK_ENTITY (id, name, isbn, image, description, publishing_date, editorial_id) values (200, 'Harry Potter and the Prisoner of Azkaban', '507119915-7', 'https://i.imgur.com/mfGKn3G.png', 'Occlusion of Right Femoral Artery, Percutaneous Approach', '2014-02-09',100);
insert into BOOK_ENTITY (id, name, isbn, image, description, publishing_date, editorial_id) values (300, 'A Game of Thrones', '279453624-9', 'https://i.imgur.com/anYXxfW.png', 'Removal of Spacer from T-lum Jt, Perc Approach', '1998-04-07',100);
insert into BOOK_ENTITY (id, name, isbn, image, description, publishing_date, editorial_id) values (400, 'The Winds of Winter', '744706866-7', 'https://i.imgur.com/AvAw5xc.png', 'Reposition Left Femoral Shaft, Perc Endo Approach', '1998-10-10',200);
insert into BOOK_ENTITY (id, name, isbn, image, description, publishing_date, editorial_id) values (500, 'The Slow Regard of Silent Things', '260760424-9', 'https://i.imgur.com/1K89Zoe.png', 'Supplement Lower Artery with Autol Sub, Perc Approach', '2013-05-09',200);
insert into BOOK_ENTITY (id, name, isbn, image, description, publishing_date, editorial_id) values (600, 'Harry Potter and the Philosopher´s Stone', '260760424-9', 'https://i.imgur.com/BnXEtbT.png', 'disse accumsan tortor quis turp Perc Approach', '2013-09-05',200);

insert into AUTHOR_ENTITY (id, name,  image, birth_date, description) values (100,'J.K. Rowling', 'https://i.imgur.com/Btjo0JX.jpg', '1965-07-04','She is a British author, philanthropist, film producer, television producer, and screenwriter. She is best known for writing the Harry Potter fantasy series, which has won multiple awards and sold more than 500 million copies.');
insert into AUTHOR_ENTITY (id, name,  image, birth_date, description) values (200, 'J. R. R. Tolkien', 'https://i.imgur.com/av1KAU0.png', '1892-03-01','He was an English writer, poet, philologist, and academic, best known as the author of the high fantasy works The Hobbit and The Lord of the Rings.');
insert into AUTHOR_ENTITY (id, name,  image, birth_date, description) values (300, 'George R. R. Martin','https://i.imgur.com/kvl0nxR.png','1948-09-20', 'He is an American novelist and short story writer, screenwriter, and television producer. He is the author of the series of epic fantasy novels A Song of Ice and Fire.');
insert into AUTHOR_ENTITY (id, name,  image, birth_date, description) values (400,'Patrick Rothfuss','https://i.imgur.com/cezGDGZ.png','1973-06-06','He is an American writer of epic fantasy. He is best known for his projected trilogy The Kingkiller Chronicle, which has won him several awards, including the 2007 Quill Award for his debut novel, The Name of the Wind');

insert into BOOK_ENTITY_AUTHORS (books_id,authors_id ) values (100,200);
insert into BOOK_ENTITY_AUTHORS (books_id,authors_id ) values (200,100); 
insert into BOOK_ENTITY_AUTHORS (books_id,authors_id ) values (300,300);
insert into BOOK_ENTITY_AUTHORS (books_id,authors_id ) values (400,300); 
insert into BOOK_ENTITY_AUTHORS (books_id,authors_id ) values (500,400);   
insert into BOOK_ENTITY_AUTHORS (books_id,authors_id ) values (600,100);      

insert into REVIEW_ENTITY  (id, name,  description, source, book_id) values (100,' ', 'This isnot really like other books, even its imitators, though the best of them are similarly long, variable in pace and diverse in language and location. The early part of the story was meant to be a follow-up  ', ' ', 100);
insert into REVIEW_ENTITY  (id, name,  description, source, book_id) values (200, ' ','The trilogy is worth reading once. For readers who like fast paced action , this is not a series I would recommend. It progresses quite slowly and the descriptions are lengthy - not really my type. But if you have the patience, the plot is worth it.', ' ', 100);
insert into REVIEW_ENTITY  (id, name,  description, source, book_id) values (300,' ','This isnt really like other books, even its imitators, though the best of them are similarly long, variable in pace and diverse in language and location. The early part of the story was meant to be a follow-up to The Hobbit, u ',' ', 200);
insert into REVIEW_ENTITY  (id, name,  description, source, book_id) values (400,' ', 'The trilogy is worth reading once. For readers who like fast paced action , this is not a series I would recommend. It progresses quite slowly and the descriptions are lengthy - not really my type. But if you have the patience, the plot is worth it.',' ', 300);
