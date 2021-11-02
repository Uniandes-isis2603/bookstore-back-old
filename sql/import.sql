--delete from REVIEW_ENTITY;
--delete from BOOK_ENTITY_AUTHORS;
--delete from AUTHOR_ENTITY;
--delete from BOOK_ENTITY;
--delete from EDITORIAL_ENTITY;

insert into EDITORIAL_ENTITY (id, name) values (100,'Oveja Negra');
insert into EDITORIAL_ENTITY (id, name) values (200,'Siruela');

insert into BOOK_ENTITY (id, name, isbn, image, description, publishing_date, editorial_id) values (100, 'The Lord of the Rings', '930330149-8', 'https://images-na.ssl-images-amazon.com/images/I/516GyHY9p6L.jpg', 'Supplement R Tympanic Membrane with Synth Sub, Via Opening', '1996-08-20',100);
insert into BOOK_ENTITY (id, name, isbn, image, description, publishing_date, editorial_id) values (200, 'Harry Potter and the Sorcerer´s Stone', '507119915-7', 'http://m.cdn.blog.hu/ko/kockagyar/image/harry_potter_poster/harry_potter_1.jpg', 'Occlusion of Right Femoral Artery, Percutaneous Approach', '2014-02-09',100);
insert into BOOK_ENTITY (id, name, isbn, image, description, publishing_date, editorial_id) values (300, 'A Game of Thrones', '279453624-9', 'https://cdn.waterstones.com/bookjackets/large/9780/0075/9780007548231.jpg', 'Removal of Spacer from T-lum Jt, Perc Approach', '1998-04-07',100);
insert into BOOK_ENTITY (id, name, isbn, image, description, publishing_date, editorial_id) values (400, 'The Winds of Winter', '744706866-7', 'http://www.darkmediaonline.com/wp-content/uploads/2013/01/WindsofWinter.jpg', 'Reposition Left Femoral Shaft, Perc Endo Approach', '1998-10-10',200);
insert into BOOK_ENTITY (id, name, isbn, image, description, publishing_date, editorial_id) values (500, 'The Slow Regard of Silent Things', '260760424-9', 'http://www.patrickrothfuss.com/images/page/cover-slow-regard_277.jpg', 'Supplement Lower Artery with Autol Sub, Perc Approach', '2013-05-09',200);
insert into BOOK_ENTITY (id, name, isbn, image, description, publishing_date, editorial_id) values (600, 'Harry Potter and the Philosopher´s Stone', '260760424-9', 'https://katemacdonalddotnet.files.wordpress.com/2015/11/potter-1-4.jpg', 'disse accumsan tortor quis turp Perc Approach', '2013-09-05',200);

insert into AUTHOR_ENTITY (id, name,  image, birth_date, description) values (100,'J.K. Rowling', 'http://cdn1us.denofgeek.com/sites/denofgeekus/files/2016/11/rowling.jpg', '1965-07-04','Joanne  Rowling, OBE, FRSL, pen names J. K. Rowling and Robert Galbraith, is a British novelist, screenwriter and film producer best known as the author of the Harry Potter fantasy series.');
insert into AUTHOR_ENTITY (id, name,  image, birth_date, description) values (200, 'J. R. R. Tolkien', 'http://www.biografiasyvidas.com/biografia/t/fotos/tolkien.jpg', '1892-03-01','Joanne  Rowling, OBE, FRSL, pen names J. K. Rowling and Robert Galbraith, is a British novelist, screenwriter and film producer best known as the author of the Harry Potter fantasy series.');
insert into AUTHOR_ENTITY (id, name,  image, birth_date, description) values (300, 'George R. R. Martin','http://static1.businessinsider.com/image/54d3c832eab8eabe028b4569-960/george-rr-martin-2011.jpg','1948-09-20', 'asA smkcxm slklkc n names J. K. Rowling and Robert Galbraith, is a British novelist');
insert into AUTHOR_ENTITY (id, name,  image, birth_date, description) values (400,'Patrick Rothfuss','http://librista.es/blog/wp-content/uploads/2014/05/patrick-rothfuss.jpg/5x10','1973-06-06','Joanne  Rowling, OBE, FRSL, pen names J. K. Rowling and Robert Galbraith, is a British novelist, screenwriter and film producer best known as the author of the Harry Potter fantasy series.');

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

