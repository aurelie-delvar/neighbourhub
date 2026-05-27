import { useEffect, useState } from "react";
import api from "../api/axios";
import type { Ad } from "../types";
import { useNavigate, useParams } from "react-router-dom";

export default function AdFormPage() {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();
    const { id } = useParams();
    const isEditing = !!id;

    useEffect(() => {
        if (isEditing) {
            const fetchAd = async () => {
                try {
                    const ad = await api.get(`/ads/${id}`); 
                    setTitle(ad.data.title);
                    setContent(ad.data.content);
                } catch {
                    setError('Impossible de récupérer les informations de l\'annonce');
                }
            }
            fetchAd();
        }
    }, [id]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError(null);
        const ad = {
            title,
            content
        };

        try {
            if (!isEditing) { // Creation
                await api.post<Ad>('/ads', ad);
                navigate('/');
            } else {
                await api.put<Ad>(`/ads/${id}`, ad);
                navigate(`/ads/${id}`);
            }
        } catch (error: any) {
            setError(error.response.data.error ?? 'Une erreur est survenue');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div>
            <h1>{isEditing ? 'Modification' : 'Création' } d'une annonce</h1>

            {error && <p style={{color: 'red'}}>{error}</p>}

            <form onSubmit={handleSubmit}>
                <div>
                    <label>Titre</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                    />

                    <label>Contenu</label>
                    <input
                        type="text"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                    />

                    <button type="submit" disabled={isLoading}>
                        {isLoading ? 'En cours...' : isEditing ? 'Modifier' : 'Publier'}
                    </button>
                </div>
            </form>
        </div>
    );
};