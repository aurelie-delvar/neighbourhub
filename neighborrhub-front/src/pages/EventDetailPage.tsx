import { useEffect, useState } from "react";
import api from "../api/axios";
import { useNavigate, useParams } from "react-router-dom";
import type { Event } from "../types";
import { useAuth } from "../context/AuthContext";

export default function EventDetailPage() {
    const [event, setEvent] = useState<Event>();
    const [isLoading, setisLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const { id } = useParams();
    const { currentUser } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchEvent = async () => {
            try {
                const response = await api.get(`events/${id}`);
                setEvent(response.data);
            } catch (error) {
                setError('Il y a eu une erreur dans la récupération de l\'événement');
            } finally {
                setisLoading(false);
            }
        }
        fetchEvent();
    }, [id]);

    const handleDelete = async (id: number) => {
        if (!window.confirm("Supprimer l'événement ?")) return;

        try {
            await api.delete<void>(`/events/${id}`);
            navigate('/events');
        } catch (error) {
            setError('La suppression a échoué');
        }
    };

    const handleUpdate = (id: number) => {
        navigate(`/events/form/${id}`);
    };

    if (isLoading) return <p>Chargement...</p>;

    return (
        <div>
            {error && <p>{error}</p>}
            
            {event && 
                <>
                    <h1>{event.title}</h1>
                    <p>{event.description}</p>
                    <p>Le {new Date(event.startsAt).toLocaleDateString('fr-FR')}</p>
                    <p> à {event.location}</p>
                    <p>Evénement organisé par <span>{event.creator.name}</span></p>
                    <p>Nombre maximal de participants : {event.capacityMax ?? 'Illimitée'}</p>
                
                    <button>S'inscrire</button>

                    {
                        currentUser && event.creator.id === currentUser.id &&
                        <>
                            <button type="button" onClick={() => handleDelete(event.id)}>Supprimer</button>
                            <button type="button" onClick={() => handleUpdate(event.id)}>Modifier</button>
                        </>

                    }
                </>
            }
        </div>
    );
}