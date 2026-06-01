import { useEffect, useState } from "react";
import type { Event, Rsvp, RsvpStatus } from "../types";
import api from "../api/axios";

interface RsvpFromProps {
    eventId: number;
    isFull: boolean;
    userRsvp: Rsvp | null;
    onSuccess: (event: Event, rsvp: Rsvp | null, message: string) => void;
    onError: (message: string) => void;
}

export default function RsvpForm({ eventId, isFull, userRsvp, onSuccess, onError }: RsvpFromProps) {
    const [status, setStatus] = useState<RsvpStatus>(userRsvp?.status ?? 'CONFIRMED');

    useEffect(() => {
        if (userRsvp) {
            setStatus(userRsvp.status)
        }
    }, [userRsvp]);

    const handleSubscription = async () => {
        try {
            let updatedRsvp: Rsvp | null = null;
            let message: string = '';

            if (!userRsvp) { // inscription
                const response = await api.post<Rsvp>(`/events/${eventId}/rsvp`, { status });
                updatedRsvp = response.data;
                message = 'Inscription Réussie';

            } else if (status === "DECLINED") { // désinscription
                await api.delete<void>(`/events/${eventId}/rsvp`);
                updatedRsvp = null;
                message = 'Désinscription réussie';

            } else { // modification de l'inscription
                const response = await api.put<Rsvp>(`/events/${eventId}/rsvp`, { status });
                updatedRsvp = response.data;
                message = 'Réponse mise à jour !';
            }

            const updated = await api.get<Event>(`/events/${eventId}`);
            onSuccess(updated.data, updatedRsvp, message);

        } catch (error) {
            onError("Erreur lors de l'inscription");
        }
    }

    return (
        <>
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
        </>
    );
}