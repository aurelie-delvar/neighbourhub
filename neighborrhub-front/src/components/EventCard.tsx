import { useNavigate } from "react-router-dom";
import type { Event } from "../types";

interface EventCardProps {
    event: Event
}

export default function EventCard({ event }: EventCardProps) {
    const navigate = useNavigate();

    const goToDetails = (id: number) => {
        navigate(`/events/${id}`);
    };

    return (
        <div onClick={() => goToDetails(event.id)}>
            <h1>{event.title}</h1>
            <p>Le {new Date(event.startsAt).toLocaleDateString('fr-FR')}</p>
            <p> à {event.location}</p>
            <p>Evénement organisé par <span>{event.creator.name}</span></p>
            <p>Nombre maximal de participants : {event.capacityMax ?? 'Illimitée'}</p>
        </div>
    );
}