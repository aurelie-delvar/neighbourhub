import { useEffect, useState } from "react"
import type { Ad } from "../types"
import api from "../api/axios";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function HomePage() {
    const [ads, setAds] = useState<Ad[]>([]);
    const { logout } = useAuth();
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchAds = async () => {
            try {
                const ads = await api.get<Ad[]>('/ads'); 
                setAds(ads.data);                
            } catch {
                setError('Impossible de charger les annonces');
            } finally {
                setLoading(false);
            }

        }; 
        fetchAds();
    }, []);

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    if (loading) return <p>Chargement...</p>;
    if (error) return <p style={{ color: 'red' }}>{error}</p>;

    return (
        <div>
            <h1>Accueil</h1>

            <button
                type="button"
                onClick={handleLogout}>Déconnexion
            </button>

            <div>
                {ads.map(ad => 
                    <div key={ad.id}>
                        <h5>{ad.title}</h5>
                        <p>{ad.content}</p>
                        <p>{ad.author.name} - {new Date(ad.creationDate).toLocaleDateString('fr-FR')}</p>

                        {ad.updateDate && <p>Mis à jour le : {new Date(ad.updateDate).toLocaleDateString('fr-FR')}</p>}
                    </div>                    
                )}
            </div>
        </div>);
}