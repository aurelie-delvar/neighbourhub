import { useState } from "react";
import api from "../api/axios";
import type { Ad } from "../types";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function CreateAdPage() {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError(null);

        const ad = {
            title,
            content
        };

        try {
            await api.post<Ad>('/ads', ad);
            navigate('/');
        } catch (error: any) {
            setError(error.response.data.error ?? 'Une erreur est survenue');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div>
            <h1>Création d'une annonce</h1>

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

                    <button type="submit" disabled={isLoading}>{ isLoading ? 'Création en cours...' : 'Envoyer' }</button>
                </div>
            </form>
        </div>);
};