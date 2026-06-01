import { useEffect, useState } from "react";
import api from "../api/axios";
import { useNavigate, useParams } from "react-router-dom";
import type { Rsvp, Event } from "../types";
import { useAuth } from "../context/AuthContext";
import RsvpForm from "../components/RsvpForm";

export default function EventDetailPage() {
    const [event, setEvent] = useState<Event>();
    const [isLoading, setisLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
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

                <RsvpForm
                    eventId={event.id}
                    isFull={!!isFull}
                    userRsvp={userRsvp}
                    onSuccess={(updatedEvent, updatedRsvp, message) => {
                        setEvent(updatedEvent);
                        setUserRsvp(updatedRsvp);
                        setMessage(message);
                    }}
                    onError={(message) => {
                        setError(message);
                    }}
                />
                
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