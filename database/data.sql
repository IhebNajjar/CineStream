-- CineStream Database Initial Data - Sprint 1
USE cinestream_db;

-- 1. Genres
INSERT INTO genre (id, name, description) VALUES
(1, 'Action', 'Films et séries centrés sur des cascades, des combats et du rythme intense.'),
(2, 'Science-Fiction', 'Œuvres basées sur des technologies futuristes, l''espace ou des réalités alternatives.'),
(3, 'Drame', 'Récits réalistes et émotionnels mettant l''accent sur le développement des personnages.'),
(4, 'Comédie', 'Œuvres humoristiques conçues pour divertir et amuser le public.'),
(5, 'Thriller', 'Intrigues captivantes générant du suspense, de la tension et de l''angoisse.'),
(6, 'Aventure', 'Voyages épiques, découvertes de mondes inconnus et défis héroïques.'),
(7, 'Animation', 'Films et séries utilisant des techniques d''animation 2D ou 3D pour tous publics.'),
(8, 'Fantastique', 'Univers intégrant des éléments surnaturels, de la magie ou du mythe.'),
(9, 'Policier / Crime', 'Enquêtes criminelles, mystères et univers policier ou mafieux.');

-- 2. Acteurs
INSERT INTO actor (id, name, bio, birth_date, photo_url) VALUES
(1, 'Leonardo DiCaprio', 'Acteur et producteur américain primé aux Oscars, célèbre pour ses rôles intenses et variés.', '1974-11-11', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300'),
(2, 'Cillian Murphy', 'Acteur irlandais renommé pour ses performances saisissantes au cinéma et à la télévision.', '1976-05-25', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=300'),
(3, 'Matthew McConaughey', 'Acteur américain oscarisé, connu pour son charisme et sa versatilité dramatique.', '1969-11-04', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=300'),
(4, 'Anne Hathaway', 'Actrice américaine reconnue pour sa présence à l''écran et ses récompenses cinématographiques.', '1982-11-12', 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300'),
(5, 'Christian Bale', 'Acteur britannique réputé pour ses transformations physiques et ses rôles marquants.', '1974-01-30', 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=300'),
(6, 'Heath Ledger', 'Acteur australien de légende dont l''incarnation du Joker reste gravée dans l''histoire.', '1979-04-04', 'https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=300'),
(7, 'Bryan Cranston', 'Acteur américain exceptionnel, icône de la télévision dramatique moderne.', '1956-03-07', 'https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=300'),
(8, 'Aaron Paul', 'Acteur américain lauréat de plusieurs Emmy Awards pour son jeu émotionnel intense.', '1979-08-27', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300'),
(9, 'Pedro Pascal', 'Acteur chilien-américain ultra populaire, figure de proue des grandes séries récentes.', '1975-04-02', 'https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?w=300'),
(10, 'Bella Ramsey', 'Jeune interprète britannique acclamée pour son authenticité et son intensité dramatique.', '2003-09-25', 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=300'),
(11, 'Millie Bobby Brown', 'Actrice et productrice britannique, révélation mondiale de la pop culture.', '2004-02-19', 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=300'),
(12, 'David Harbour', 'Acteur américain généreux au jeu puissant et expressif.', '1975-04-10', 'https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=300'),
(13, 'Keanu Reeves', 'Acteur canadien iconique du cinéma d''action et de science-fiction contemporain.', '1964-09-02', 'https://images.unsplash.com/photo-1463453091185-61582044d556?w=300'),
(14, 'Zendaya', 'Actrice et chanteuse américaine talentueuse, lauréate de prestigieuses distinctions.', '1996-09-01', 'https://images.unsplash.com/photo-1531746020798-e6953c6e8e04?w=300'),
(15, 'Timothée Chalamet', 'Acteur franco-américain emblématique du cinéma contemporain d''auteur et de blockbusters.', '1995-12-27', 'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=300'),
(16, 'Morgan Freeman', 'Légende vivante du cinéma hollywoodien à la voix inimitable et aux rôles inoubliables.', '1937-06-01', 'https://images.unsplash.com/photo-1513956589380-bad6acb9b9d4?w=300'),
(17, 'Al Pacino', 'Monstre sacré du cinéma américain, interprète de chefs-d''œuvre intemporels.', '1940-04-25', 'https://images.unsplash.com/photo-1501196354995-cbb51c65aaea?w=300'),
(18, 'Margot Robbie', 'Actrice et productrice australienne brillante, acclamée internationalement.', '1990-07-02', 'https://images.unsplash.com/photo-1548142813-c348350df52b?w=300');

-- 3. Réalisateurs
INSERT INTO director (id, name, bio, photo_url) VALUES
(1, 'Christopher Nolan', 'Cinéaste visionnaire britanno-américain adepte des structures narratives complexes et du réalisme pratique.', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300'),
(2, 'Denis Villeneuve', 'Réalisateur canadien reconnu pour son esthétique visuelle grandiose et ses fresques de science-fiction.', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=300'),
(3, 'Vince Gilligan', 'Scénariste et réalisateur américain acclamé pour la précision chirurgicale de ses séries dramatiques.', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=300'),
(4, 'The Duffer Brothers', 'Duo de créateurs et réalisateurs américains passionnés par la culture et le fantastique des années 80.', 'https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=300'),
(5, 'Craig Mazin', 'Scénariste et réalisateur américain salué pour la qualité documentaire et émotionnelle de ses créations.', 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=300'),
(6, 'Chad Stahelski', 'Réalisateur américain et maître des chorégraphies d''action modernes.', 'https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=300'),
(7, 'Frank Darabont', 'Cinéaste américain maître des adaptations émouvantes et humanistes.', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300'),
(8, 'Francis Ford Coppola', 'Pilier du Nouvel Hollywood et créateur de fresques majeures de l''histoire du 7ème art.', 'https://images.unsplash.com/photo-1513956589380-bad6acb9b9d4?w=300'),
(9, 'Quentin Tarantino', 'Auteur et réalisateur iconique au style stylisé, aux dialogues cultes et aux hommages passionnés.', 'https://images.unsplash.com/photo-1463453091185-61582044d556?w=300'),
(10, 'Greta Gerwig', 'Réalisatrice et scénariste américaine reconnue pour sa modernité et son regard féministe subtil.', 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300');

-- 4. Médias (32 Titres équilibrés entre Films et Séries)
INSERT INTO media (id, title, description, release_year, type, duration, language, country, minimum_age, poster_url, trailer_url, rental_price_per_day) VALUES
(1, 'Inception', 'Dom Cobb est un voleur expérimenté dans l''art périlleux de l''extraction : sa spécialité consiste à s''approprier les secrets les plus précieux d''un individu, enfouis au plus profond de son subconscient pendant qu''il rêve.', 2010, 'FILM', 148, 'Français', 'États-Unis', 12, 'https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=600', 'https://www.youtube.com/watch?v=YoHD9XEInc0', 3.99),
(2, 'Interstellar', 'Une équipe d''explorateurs franchit une faille récemment découverte dans l''espace-temps pour repousser les limites de l''humanité et explorer un nouveau monde habitable alors que la Terre se meurt.', 2014, 'FILM', 169, 'Français', 'États-Unis', 10, 'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600', 'https://www.youtube.com/watch?v=zSWdZVtXT7E', 4.50),
(3, 'The Dark Knight', 'Batman entreprend de démanteler les dernières organisations criminelles de Gotham avec l''aide du lieutenant Gordon et du procureur Harvey Dent, jusqu''à l''arrivée d''un criminel machiavélique : le Joker.', 2008, 'FILM', 152, 'Français', 'États-Unis', 12, 'https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600', 'https://www.youtube.com/watch?v=EXeTwQWrcwY', 3.50),
(4, 'Oppenheimer', 'Le parcours captivant de J. Robert Oppenheimer, physicien brillant qui dirigea le projet Manhattan visant à créer la première bombe atomique de l''histoire.', 2023, 'FILM', 180, 'Français', 'États-Unis', 12, 'https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?w=600', 'https://www.youtube.com/watch?v=uYPbbksJxIg', 4.99),
(5, 'Dune : Première Partie', 'Paul Atreides, un jeune homme brillant et doué au destin hors du commun, doit se rendre sur la planète la plus dangereuse de l''univers pour assurer l''avenir de sa famille et de son peuple.', 2021, 'FILM', 155, 'Français', 'États-Unis', 12, 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=600', 'https://www.youtube.com/watch?v=8g18jFHCLXk', 4.20),
(6, 'Dune : Deuxième Partie', 'Paul Atreides s''unit à Chani et aux Fremen tout en préparant sa revanche contre les conspirateurs qui ont détruit sa famille. Face à un choix entre l''amour de sa vie et le destin de l''univers, il combat pour empêcher un avenir terrifiant.', 2024, 'FILM', 166, 'Français', 'États-Unis', 12, 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600', 'https://www.youtube.com/watch?v=Way9Dexny3w', 5.50),
(7, 'Breaking Bad', 'Walter White, professeur de chimie au lycée atteint d''un cancer du poumon en phase terminale, s''associe à un ancien élève pour fabriquer et vendre de la méthamphétamine afin d''assurer l''avenir financier de sa famille.', 2008, 'SERIE', NULL, 'Français', 'États-Unis', 16, 'https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=600', 'https://www.youtube.com/watch?v=HhesaQXLuRY', 6.00),
(8, 'The Last of Us', 'Vingt ans après la destruction de la civilisation moderne, Joel, un survivant endurci, est engagé pour faire sortir clandestinement Ellie, une jeune fille de 14 ans, d''une zone de quarantaine oppressante.', 2023, 'SERIE', NULL, 'Français', 'États-Unis', 16, 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600', 'https://www.youtube.com/watch?v=uLtkt8BonwM', 6.50),
(9, 'Stranger Things', 'Dans une petite ville de l''Indiana en 1983, la disparition soudaine d''un jeune garçon dévoile un mystère impliquant des expériences secrètes, des forces surnaturelles terrifiantes et une étrange fillette.', 2016, 'SERIE', NULL, 'Français', 'États-Unis', 12, 'https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600', 'https://www.youtube.com/watch?v=b9EkMc79ZSU', 5.00),
(10, 'John Wick : Chapitre 4', 'John Wick découvre un moyen de vaincre la Grande Table. Mais avant de pouvoir gagner sa liberté, Wick doit affronter un nouvel ennemi aux alliances puissantes à travers le monde.', 2023, 'FILM', 169, 'Français', 'États-Unis', 16, 'https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600', 'https://www.youtube.com/watch?v=qEVUtrk8_B4', 4.50),
(11, 'Les Évadés', 'Condamné à perpétuité pour un double meurtre qu''il n''a pas commis, le banquier Andy Dufresne s''adapte à la vie carcérale de Shawshank et se lie d''amitié avec Red, un détenu influent.', 1994, 'FILM', 142, 'Français', 'États-Unis', 10, 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600', 'https://www.youtube.com/watch?v=PLl99DlL6b4', 2.99),
(12, 'Le Parrain', 'L''histoire de la famille Corleone, l''une des plus puissantes familles de la mafia italo-américaine de New York, alors que le patriarche Don Vito transmet son empire à son plus jeune fils.', 1972, 'FILM', 175, 'Français', 'États-Unis', 16, 'https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=600', 'https://www.youtube.com/watch?v=sY1S349g33U', 3.20),
(13, 'Pulp Fiction', 'Les vies de deux hommes de main, d''un boxeur, de la femme d''un gangster et de deux braqueurs s''entrecroisent dans quatre récits de violence et de rédemption à Los Angeles.', 1994, 'FILM', 154, 'Français', 'États-Unis', 16, 'https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9?w=600', 'https://www.youtube.com/watch?v=s7EdQ4FqbhY', 3.00),
(14, 'Barbie', 'Vivre à Barbie Land, c''est être parfait dans un monde parfait. À moins d''avoir une crise existentielle... ou d''être Ken ! Barbie part alors pour le monde réel à la recherche de réponses.', 2023, 'FILM', 114, 'Français', 'États-Unis', 0, 'https://images.unsplash.com/photo-1514565131-fce0801e5785?w=600', 'https://www.youtube.com/watch?v=pBk4NYhWNMM', 4.00),
(15, 'Better Call Saul', 'Avant d''être l''avocat véreux de Walter White, Jimmy McGill était un avocat débutant qui peinait à joindre les deux bouts et cherchait désespérément à prouver sa valeur à son frère aîné.', 2015, 'SERIE', NULL, 'Français', 'États-Unis', 12, 'https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?w=600', 'https://www.youtube.com/watch?v=HN4oydykJFc', 5.00),
(16, 'Le Prestige', 'À la fin du XIXe siècle à Londres, deux magiciens surdoués et autrefois amis se livrent une guerre impitoyable après un accident tragique, poussant l''obsession jusqu''à l''irréparable.', 2006, 'FILM', 130, 'Français', 'États-Unis', 12, 'https://images.unsplash.com/photo-1514565131-fce0801e5785?w=600', 'https://www.youtube.com/watch?v=ijXruSzfGEc', 3.20),
(17, 'Blade Runner 2049', 'En 2049, la société est fragilisée par les tensions entre les humains et leurs esclaves créés par bioingénierie. L''officier K découvre un secret susceptible de bouleverser le monde.', 2017, 'FILM', 164, 'Français', 'États-Unis', 12, 'https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600', 'https://www.youtube.com/watch?v=gCcx85zbxz4', 3.80),
(18, 'Premier Contact', 'Lorsque de mystérieux vaisseaux spatiaux atterrissent aux quatre coins du monde, une équipe d''experts est réunie sous la direction de la linguiste Louise Banks pour tenter de communiquer.', 2016, 'FILM', 116, 'Français', 'États-Unis', 10, 'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600', 'https://www.youtube.com/watch?v=tFMo3UJ4B4g', 3.50),
(19, 'Django Unchained', 'Dans le Sud des États-Unis deux ans avant la guerre civile, Django, un esclave au lourd passé, fait équipe avec le chasseur de primes allemand Dr King Schultz pour libérer sa femme.', 2012, 'FILM', 165, 'Français', 'États-Unis', 16, 'https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600', 'https://www.youtube.com/watch?v=0fUCuvNlOCg', 3.99),
(20, 'Inglourious Basterds', 'Durant la Seconde Guerre mondiale en France occupée, un commando de soldats juifs américains mène des actions punitives sanglantes contre les nazis tandis qu''une jeune femme prépare sa vengeance.', 2009, 'FILM', 153, 'Français', 'États-Unis', 16, 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600', 'https://www.youtube.com/watch?v=KnrRy6kSFF0', 3.60),
(21, 'Chernobyl', 'Le 26 avril 1986, l''explosion du réacteur de la centrale nucléaire de Tchernobyl provoque une catastrophe sans précédent. Les scientifiques et les secouristes luttent pour sauver l''Europe.', 2019, 'SERIE', NULL, 'Français', 'États-Unis', 16, 'https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?w=600', 'https://www.youtube.com/watch?v=s9APLXM9Ei8', 4.80),
(22, 'The Mandalorian', 'Après la chute de l''Empire et avant l''émergence du Premier Ordre, un chasseur de primes mandalorien solitaire opère dans les confins de la galaxie, loin de l''autorité de la Nouvelle République.', 2019, 'SERIE', NULL, 'Français', 'États-Unis', 10, 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600', 'https://www.youtube.com/watch?v=aOC8E8R_N5c', 5.50),
(23, 'Le Loup de Wall Street', 'L''ascension fulgurante et la chute spectaculaire de Jordan Belfort, courtier en bourse flamboyant de New York consumé par l''ambition, l''argent facile et les excès en tous genres.', 2013, 'FILM', 180, 'Français', 'États-Unis', 16, 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600', 'https://www.youtube.com/watch?v=iszwuX1AK6A', 3.80),
(24, 'Fight Club', 'Un employé de bureau insomniaque et désabusé fait la rencontre d''un vendeur de savon charismatique nommé Tyler Durden. Ensemble, ils fondent un club de combat clandestin.', 1999, 'FILM', 139, 'Français', 'États-Unis', 16, 'https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600', 'https://www.youtube.com/watch?v=qtRKDV93gk8', 3.20),
(25, 'Le Voyage de Chihiro', 'Chihiro, une fillette de dix ans, se retrouve piégée avec ses parents dans un monde d''esprits magiques gouverné par la sorcière Yubaba. Elle doit faire preuve de courage pour sauver sa famille.', 2001, 'FILM', 125, 'Français', 'Japon', 0, 'https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=600', 'https://www.youtube.com/watch?v=ByXuk9QqQkk', 2.80),
(26, 'Spider-Man : New Generation', 'Miles Morales, un adolescent de Brooklyn, est mordu par une araignée radioactive et développe des super-pouvoirs, avant de découvrir que plusieurs Spider-Héros existent dans le multivers.', 2018, 'FILM', 117, 'Français', 'États-Unis', 0, 'https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600', 'https://www.youtube.com/watch?v=tg52up16eq0', 3.50),
(27, 'Peaky Blinders', 'À Birmingham en 1919, Thomas Shelby dirige le gang redouté des Peaky Blinders au lendemain de la Première Guerre mondiale, bien décidé à bâtir un empire commercial et criminel légitime.', 2013, 'SERIE', NULL, 'Français', 'Royaume-Uni', 16, 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600', 'https://www.youtube.com/watch?v=oVzVdvGIC7U', 5.50),
(28, 'Succession', 'La famille Roy contrôle l''un des plus grands conglomérats médiatiques mondiaux. Lorsque le patriarche envisage de se retirer, ses enfants s''affrontent impitoyablement pour le trône.', 2018, 'SERIE', NULL, 'Français', 'États-Unis', 16, 'https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?w=600', 'https://www.youtube.com/watch?v=OzYxJV_rmE8', 5.80),
(29, 'Seven', 'Deux inspecteurs de police, l''un vétéran blasé et l''autre jeune recrue fougueuse, traquent un tueur en série méthodique qui choisit ses victimes en fonction des sept péchés capitaux.', 1995, 'FILM', 127, 'Français', 'États-Unis', 16, 'https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9?w=600', 'https://www.youtube.com/watch?v=znmZoVkCjpI', 3.00),
(30, 'Gladiator', 'Le général romain Maximus est trahi lorsque Commode s''empare du pouvoir et assassine sa famille. Réduit à l''esclavage, Maximus devient gladiateur pour accomplir sa vengeance.', 2000, 'FILM', 155, 'Français', 'États-Unis', 12, 'https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600', 'https://www.youtube.com/watch?v=owK1qxDselE', 3.40),
(31, 'Avatar : La Voie de l''eau', 'Plus d''une décennie après les événements du premier film, Jake Sully et Neytiri ont fondé une famille et font tout pour rester soudés face à une nouvelle menace destructrice.', 2022, 'FILM', 192, 'Français', 'États-Unis', 10, 'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600', 'https://www.youtube.com/watch?v=d9MyW72ELq0', 4.50),
(32, 'Arcane', 'Au cœur du conflit entre les cités jumelles de Piltover et Zaun, deux sœurs se retrouvent dans des camps opposés au milieu d''une guerre technologique et mystique.', 2021, 'SERIE', NULL, 'Français', 'États-Unis', 12, 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600', 'https://www.youtube.com/watch?v=fXmAurh012s', 5.00);

-- 5. Media_Genre (Relations)
INSERT INTO media_genre (media_id, genre_id) VALUES
(1, 1), (1, 2), (1, 5), -- Inception: Action, SciFi, Thriller
(2, 2), (2, 3), (2, 6), -- Interstellar: SciFi, Drama, Aventure
(3, 1), (3, 5), (3, 9), -- The Dark Knight: Action, Thriller, Crime
(4, 3), -- Oppenheimer: Drame
(5, 2), (5, 6), (5, 3), -- Dune 1: SciFi, Aventure, Drame
(6, 2), (6, 6), (6, 1), -- Dune 2: SciFi, Aventure, Action
(7, 3), (7, 5), (7, 9), -- Breaking Bad: Drame, Thriller, Crime
(8, 1), (8, 3), (8, 2), -- The Last of Us: Action, Drame, SciFi
(9, 2), (9, 5), (9, 8), -- Stranger Things: SciFi, Thriller, Fantastique
(10, 1), (10, 5), (10, 9), -- John Wick 4: Action, Thriller, Crime
(11, 3), (11, 9), -- Les Evades: Drame, Crime
(12, 3), (12, 9), -- Le Parrain: Drame, Crime
(13, 3), (13, 9), (13, 4), -- Pulp Fiction: Drame, Crime, Comedie
(14, 4), (14, 6), (14, 8), -- Barbie: Comedie, Aventure, Fantastique
(15, 3), (15, 9), (15, 4), -- Better Call Saul: Drame, Crime, Comedie
(16, 3), (16, 5), (16, 8), -- Le Prestige: Drame, Thriller, Fantastique
(17, 2), (17, 5), (17, 3), -- Blade Runner 2049: SciFi, Thriller, Drame
(18, 2), (18, 3), -- Premier Contact: SciFi, Drame
(19, 1), (19, 3), -- Django: Action, Drame
(20, 1), (20, 3), (20, 4), -- Inglourious Basterds: Action, Drame, Comedie
(21, 3), (21, 5), -- Chernobyl: Drame, Thriller
(22, 1), (22, 2), (22, 6), -- The Mandalorian: Action, SciFi, Aventure
(23, 3), (23, 4), (23, 9), -- Le Loup de Wall Street: Drame, Comedie, Crime
(24, 3), (24, 5), -- Fight Club: Drame, Thriller
(25, 7), (25, 8), (25, 6), -- Le Voyage de Chihiro: Animation, Fantastique, Aventure
(26, 7), (26, 1), (26, 2), -- Spider-Man: Animation, Action, SciFi
(27, 3), (27, 9), -- Peaky Blinders: Drame, Crime
(28, 3), -- Succession: Drame
(29, 5), (29, 9), (29, 3), -- Seven: Thriller, Crime, Drame
(30, 1), (30, 3), (30, 6), -- Gladiator: Action, Drame, Aventure
(31, 2), (31, 1), (31, 6), -- Avatar 2: SciFi, Action, Aventure
(32, 7), (32, 1), (32, 2); -- Arcane: Animation, Action, SciFi

-- 6. Media_Actor (Acteurs avec leur rôle dans le titre)
INSERT INTO media_actor (media_id, actor_id, character_name) VALUES
(1, 1, 'Dom Cobb'),
(1, 2, 'Robert Fischer'),
(2, 3, 'Joseph Cooper'),
(2, 4, 'Dr. Amelia Brand'),
(3, 5, 'Bruce Wayne / Batman'),
(3, 6, 'Le Joker'),
(3, 2, 'Dr. Jonathan Crane / L''Épouvantail'),
(4, 2, 'J. Robert Oppenheimer'),
(5, 15, 'Paul Atreides'),
(5, 14, 'Chani'),
(6, 15, 'Paul Atreides'),
(6, 14, 'Chani'),
(7, 7, 'Walter White / Heisenberg'),
(7, 8, 'Jesse Pinkman'),
(8, 9, 'Joel Miller'),
(8, 10, 'Ellie Williams'),
(9, 11, 'Eleven'),
(9, 12, 'Jim Hopper'),
(10, 13, 'John Wick'),
(11, 16, 'Ellis Boyd ''Red'' Redding'),
(12, 17, 'Michael Corleone'),
(14, 18, 'Barbie Stéréotypée'),
(15, 7, 'Walter White (Apparition)'),
(16, 5, 'Alfred Borden'),
(19, 1, 'Calvin J. Candie'),
(22, 9, 'Din Djarin / Le Mandalorien'),
(23, 1, 'Jordan Belfort'),
(23, 3, 'Mark Hanna'),
(23, 18, 'Naomi Lapaglia'),
(27, 2, 'Thomas Shelby'),
(29, 16, 'Inspecteur William Somerset');

-- 7. Media_Director (Réalisateurs des titres)
INSERT INTO media_director (media_id, director_id) VALUES
(1, 1), -- Inception -> Christopher Nolan
(2, 1), -- Interstellar -> Christopher Nolan
(3, 1), -- The Dark Knight -> Christopher Nolan
(4, 1), -- Oppenheimer -> Christopher Nolan
(5, 2), -- Dune 1 -> Denis Villeneuve
(6, 2), -- Dune 2 -> Denis Villeneuve
(7, 3), -- Breaking Bad -> Vince Gilligan
(8, 5), -- The Last of Us -> Craig Mazin
(9, 4), -- Stranger Things -> The Duffer Brothers
(10, 6), -- John Wick 4 -> Chad Stahelski
(11, 7), -- Les Evades -> Frank Darabont
(12, 8), -- Le Parrain -> Francis Ford Coppola
(13, 9), -- Pulp Fiction -> Quentin Tarantino
(14, 10), -- Barbie -> Greta Gerwig
(15, 3), -- Better Call Saul -> Vince Gilligan
(16, 1), -- Le Prestige -> Christopher Nolan
(17, 2), -- Blade Runner 2049 -> Denis Villeneuve
(18, 2), -- Premier Contact -> Denis Villeneuve
(19, 9), -- Django Unchained -> Quentin Tarantino
(20, 9), -- Inglourious Basterds -> Quentin Tarantino
(21, 5); -- Chernobyl -> Craig Mazin
