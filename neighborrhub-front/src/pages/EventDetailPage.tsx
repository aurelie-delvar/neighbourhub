import { useEffect, useState } from "react";
import api from "../api/axios";
import { useNavigate, useParams } from "react-router-dom";
import type { Rsvp, RsvpStatus, Event } from "../types";
import { useAuth } from "../context/AuthContext";

export default function EventDetailPage() {
    const [event, setEvent] = useState<Event>();
    const [isLoading, setisLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [status, setStatus] = useState<RsvpStatus>('CONFIRMED');
    const [message, setMessage] = useState<string | null>(null);
    const { id } = useParams();
    const { currentUser } = useAuth();
    const navigate = useNavigate();
    const [userRsvp, setUserRsvp] = useState<Rsvp | null>(null);
    const participatingCount = event?.rsvps.filter(r => r.status !== 'DECLINED').length ?? 0;
    const isFull = event && event?.capacityMax !== null && participatingCount >= event.capacityMax;

    useEffect(() => {
        const fetchEvent = async () => {
            try {
                const response = await api.get<Event>(`/events/${id}`);
                setEvent(response.data);

                if (currentUser) {
                    const existing = response.data.rsvps.find(r => r.userId === currentUser.id);
                    setUserRsvp(existing ?? null);
                    if (existing) setStatus(existing.status);
                }
            } catch (error) {
                setError('Il y a eu une erreur dans la récupération de l\'événement');
            } finally {
                setisLoading(false);
            }
        }
        fetchEvent();
    }, [id, currentUser]);

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

    const handleSubscription = async () => {
        setError(null);
        setMessage(null);

        try {
            if (!userRsvp) { // inscription
                const response = await api.post<Rsvp>(`/events/${id}/rsvp`, { status });
                setMessage('Inscription réussie :)'); 
                setUserRsvp(response.data)
            } else if (status === "DECLINED") { // désinscription
                await api.delete<void>(`/events/${id}/rsvp`);
                setMessage('Désinscription réussie');
                setUserRsvp(null);
            } else { // modification de l'inscription
                const response = await api.put<Rsvp>(`/events/${id}/rsvp`, { status });
                setMessage('Réponse mise à jour !');
                setUserRsvp(response.data);
            }

            const updated = await api.get<Event>(`/events/${id}`);
            setEvent(updated.data);
        } catch (error) {
            setError("Erreur lors de l'inscription");
        }
    }

    if (isLoading) return <p>Chargement...</p>;

    return (
        <div>
            {error && <p>{error}</p>}
            {message && <p>{message}</p>}

            {event &&
                <>
                    <h1>{event.title}</h1>
                    <p>{event.description}</p>
                    <p>Le {new Date(event.startsAt).toLocaleDateString('fr-FR')}</p>
                    <p> à {event.location}</p>
                    <p>Evénement organisé par <span>{event.creator.name}</span></p>

                    {
                        event.capacityMax &&
                        <p>Nombre de participants : {participatingCount} / {event.capacityMax ?? 'Illimitée'}</p>
                    }

                    <select value={status} onChange={(e) => setStatus(e.target.value as RsvpStatus)}>
                        <option
                            value="CONFIRMED"
                            disabled={isFull || status === 'CONFIRMED'}
                        >
                            Participe
                        </option>
                        <option
                            value="MAYBE"
                            disabled={isFull || status === 'MAYBE'}
                        >
                            Peut-être
                        </option>
                        <option
                            value="DECLINED"
                            disabled={status === 'DECLINED'}
                        >
                            Ne participe pas
                        </option>
                    </select>
                    <button type="button" onClick={handleSubscription}>Confirmer</button>
                
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