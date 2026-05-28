import { useEffect, useState } from "react";
import api from "../api/axios";
import type { Event } from "../types";
import { useAuth } from "../context/AuthContext";
import { Link } from "react-router-dom";

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

            <h1>Evénements du quartier : {events[0].neighbourhood.name} ({events[0].neighbourhood.zipcode}, {events[0].neighbourhood.city})</h1>

            {events.map(e => 
                <div key={e.id}>
                    <h1>{e.title}</h1>
                    <p>{e.description}</p>
                    <p>Le {new Date(e.startsAt).toLocaleDateString('fr-FR')}</p>
                    <p> à {e.location}</p>
                    <p>Evénement organisé par <span>{e.creator.name}</span></p>
                    <p>Nombre maximal de participants : {e.capacityMax ?? 'Illimitée'}</p>
                </div>
            )}

            <Link to="/events/form">Créer un événement</Link>
        </div>
    );
}