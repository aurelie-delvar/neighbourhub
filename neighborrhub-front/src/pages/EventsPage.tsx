import { useEffect, useState } from "react";
import api from "../api/axios";
import type { Event } from "../types";
import { useAuth } from "../context/AuthContext";

export default function EventsPage() {
    const [events, setEvents] = useState<Event[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const { currentUser } = useAuth();    

    useEffect(() => {
        const fetchEvents = async () => {
            if (currentUser) {
                try {
                    const response = await api.get<Event[]>(`/events/neighbourhood/${currentUser.neighbourhoodId}`);
                    setEvents(response.data);
                } catch {
                    setError('Impossible de récupérer les événements');
                } finally {
                    setIsLoading(false);
                }               
            }
        }
        fetchEvents();
    }, [currentUser]);

    if (isLoading) return <p>Chargement...</p>;

    return (
        <div>
            {error && <p>{error}</p>}

            {events.map(e => 
                <div key={e.id}>
                    <h1>{e.title}</h1>
                    <p>{e.description}</p>
                    <p>Le {new Date(e.startsAt).toLocaleDateString('fr-FR')}</p>
                    <p> à {e.location}</p>
                    <p>{e.neighbourhood.name}</p>
                    <p>{e.neighbourhood.zipcode} {e.neighbourhood.city}</p>
                    <p>Evénement organisé par <span>{e.creator.name}</span></p>
                    <p>Nombre maximal de participants : {e.capacityMax ?? 'Illimitée'}</p>
                </div>
            )}
        </div>
    );
}