-- Quartiers
INSERT INTO app_neighbourhood (name, city, zipcode) VALUES
('Montmartre', 'Paris', '75018'),
('Belleville', 'Paris', '75020'),
('La Croix-Rousse', 'Lyon', '69004')
ON CONFLICT (name, zipcode) DO NOTHING;

-- Rôles
INSERT INTO app_role (name) VALUES
('ROLE_USER'),
('ROLE_MODERATOR'),
('ROLE_ADMIN')
ON CONFLICT DO NOTHING;

-- Users (mot de passe = "password" en clair pour le dev, on hashera plus tard)
INSERT INTO app_user (name, mail, password, neighbourhood_id, created_at) VALUES
('Alice Martin', 'alice@test.com', 'password', 1, NOW()),
('Bob Dupont', 'bob@test.com', 'password', 1, NOW()),
('Clara Admin', 'clara@test.com', 'password', 2, NOW())
ON CONFLICT DO NOTHING;