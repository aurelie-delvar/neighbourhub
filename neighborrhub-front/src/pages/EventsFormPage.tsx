import { useState } from "react";
import api from "../api/axios";
import { useNavigate } from "react-router-dom";

export default function EventsFormPage() {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [startsAt, setStartsAt] = useState('');
    const [location, setLocation] = useState('');
    const [capacityMax, setCapacityMax] = useState<number | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setIsLoading(true);

        const event = {
            title,
            description,
            startsAt,
            location,
            capacityMax
        };

        try {
            await api.post('/events', event);
            navigate('/events');
        } catch (error) {
            setError(`Il y a eu une erreur : ${error}`);
        } finally {
            setIsLoading(false);
        }
    };

    if (error) return <p>{error}</p>

    return (
        <>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Titre</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                    />

                    <label>Description</label>
                    <input
                        type="text"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                    />

                    <label>Heure de début</label>
                    <input
                        type="datetime-local"
                        value={startsAt}
                        onChange={(e) => setStartsAt(e.target.value)}
                    />

                    <label>Lieu</label>
                    <input
                        type="text"
                        value={location}
                        onChange={(e) => setLocation(e.target.value)}
                    />

                    <label>Nombre maximal de participants</label>
                    <input
                        type="number"
                        value={capacityMax ?? ''}
                        onChange={(e) => setCapacityMax(e.target.value === '' ? null : Number(e.target.value))}
                        placeholder="Illimité"
                    />  
                </div>
                
                <button type="submit" disabled={isLoading}>{ isLoading ? 'Création en cours...' : 'Créer'}</button>
            </form>
        </>
    );
}