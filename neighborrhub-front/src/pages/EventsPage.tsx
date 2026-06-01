import { useEffect, useState } from "react";
import api from "../api/axios";
import type { Event } from "../types";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import EventCard from "../components/EventCard";

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
                <EventCard
                    key={e.id}
                    event={e}
                />
            )}

            <Link to="/events/form">Créer un événement</Link>
        </div>
    );
}