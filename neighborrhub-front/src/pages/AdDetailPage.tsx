import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../api/axios";
import type { Ad } from "../types";
import { useAuth } from "../context/AuthContext";

export default function AdDetailPage() {
    const { id } = useParams<{ id: string }>();
    const [ad, setAd] = useState<Ad | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const { currentUser } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchAd = async () => {
            setError(null);

            try {
                const reponse = await api.get<Ad>(`/ads/${id}`);
                setAd(reponse.data);
            } catch {
                setError('Il y a un problème de récupération de l\'annonce.');
            } finally {
                setIsLoading(false);
            }
        };
        fetchAd();
    }, [id]);

    const handleDelete = async () => {
        if (!window.confirm('Supprimer cette annonce ?')) return;

        try {
            await api.delete(`/ads/${id}`);
            navigate('/');
        } catch {
            setError("Impossible de supprimer l'annonce");
        }
    };

    const handleUpdate = async () => {
        navigate(`/ad/form/${id}`);
    };

    if (isLoading) return <p>Chargement en cours...</p>;
    
    return (
        <>
            {error && <p>{ error }</p>}
            { ad && 
                <div>
                    <h5>{ad.title}</h5>
                    <p>{ad.content}</p>
                    <p>{ad.author.name} - {new Date(ad.creationDate).toLocaleDateString('fr-FR')}</p>

                    {ad.updateDate && <p>Mis à jour le : {new Date(ad.updateDate).toLocaleDateString('fr-FR')}</p>}
                </div>
            }
            {currentUser && ad && currentUser.id === ad.author.id &&
                <>
                    <button type="button" onClick={handleDelete}>Supprimer</button>
                    <button type="button" onClick={handleUpdate}>Modifier</button>
                </>
            }
        </>
    );
}