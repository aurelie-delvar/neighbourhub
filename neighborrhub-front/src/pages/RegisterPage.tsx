import { useEffect, useState } from "react"
import api from "../api/axios";
import type { Neighbourhood } from "../types";
import { Link, useNavigate } from "react-router-dom";

export default function RegisterPage() {
    const [mail, setMail] = useState('');
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    const [neighbourhoods, setNeighbourhoods] = useState<Neighbourhood[]>([]);
    const [neighbourhoodId, setNeighbourhoodId] = useState<number | null>(null);
    const navigate = useNavigate();


    useEffect(() => {
        const fetchNeighbourhoods = async () => {
            const neighbourhoods = await api.get<Neighbourhood[]>('/neighbourhoods');
            setNeighbourhoods(neighbourhoods.data);
        };
        fetchNeighbourhoods();
    }, []);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        if (password.length < 8) {
            setError('Le mot de passe doit contenir au moins 8 caractères');
            setLoading(false);
            return;
        }

        try {
            await api.post('/auth/register', { name, mail, password, neighbourhoodId });
            navigate('/login');
        } catch (err: any) {
            setError(err.response.data.error ?? 'Une erreur est survenue');
        } finally {
            setLoading(false);
        }
    }

    return (
        <div>
            <h1>Inscription</h1>

            {error && <p style={{ color: 'red' }}>{error}</p>}
            
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Nom</label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                    />

                    <label>Mail</label>
                    <input
                        type="email"
                        value={mail}
                        onChange={(e) => setMail(e.target.value)}
                    />

                    <label>Mot de passe</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />

                    <label>Quartier</label>
                    <select
                        value={neighbourhoodId ?? ''}
                        onChange={(e) => setNeighbourhoodId(Number(e.target.value))}
                    >
                        <option value="" disabled>Choisir un quartier</option>
                        {neighbourhoods.map(n => 
                            <option value={n.id} key={n.id}>{n.name} - {n.city}</option>
                        )}
                    </select>
                </div>

                <button type="submit" disabled={loading}>
                    {loading ? 'Inscription en cours' : 'S\'inscrire'}
                </button>
            </form>

            <p>Déjà un compte ? <Link to="/login">Se connecter</Link></p>
        </div>);
}