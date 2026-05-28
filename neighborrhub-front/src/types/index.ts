export interface UserSummary {
    id: number;
    name: string;
}

export interface User {
    id: number;
    name: string;
    mail: string;
    neighbourhoodId: number;
}

export interface Ad {
    id: number;
    title: string;
    content: string;
    creationDate: string;
    updateDate: string | null;
    author: UserSummary;
}

export interface Neighbourhood {
    id: number;
    name: string;
    city: string;
    zipcode: string;
}

export interface Event {
    id: number;
    title: string;
    description: string;
    startsAt: string;
    location: string;
    capacityMax: number | null;
    createdAt: string;
    neighbourhood: Neighbourhood;
    creator: UserSummary;
    rsvps: Rsvp[];
}

export interface Rsvp {
    id: number;
    status: RsvpStatus;
    userId: number;
    eventId: number;
}

export type RsvpStatus = 'CONFIRMED' | 'MAYBE' | 'DECLINED';

export interface Message {
    id: number;
    content: string;
    isRead: boolean;
    sentAt: string;
    sender: UserSummary;
    receiver: UserSummary;
}

export interface AuthResponse {
    token: string;
}