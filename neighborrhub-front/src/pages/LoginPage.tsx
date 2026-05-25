import { useState } from "react"
import { useAuth } from "../context/AuthContext";
import { Link, useNavigate } from "react-router-dom";
import api from "../api/axios";
import type { AuthResponse } from "../types/index.ts";
import { Eye, EyeOff } from "lucide-react";

export default function LoginPage() {

    const [mail, setMail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);

    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const response = await api.post<AuthResponse>('/auth/login', { mail, password });
            login(response.data.token);
            navigate('/');
        } catch (err: any) {
            setError(err.response?.data.error ?? 'Identifiants incorrects');
        } finally {
            setLoading(false);
        }
    } 


    return (
        <div>
            <h1>Connexion</h1>

            { error && <p style={{ color: 'red' }}>{ error }</p> }
            
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Email</label>
                    <input type="email" value={mail} onChange={(e) => setMail(e.target.value)} required />
                </div>

                <div style={{position: 'relative'}}>
                    <label>Mot de passe</label>
                    <input
                        type={showPassword ? "text" : "password"}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required />
                    <button
                        type="button"
                        onClick={() => setShowPassword(!showPassword)}
                        style={{
                            position: 'absolute',
                            right: '8px',
                            top: '50%',
                            transform: 'translateY(-50%)',
                            background: 'none',
                            border: 'none',
                            cursor: 'pointer'
                        }}>
                        {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                         </button>
                </div>

                <button type="submit" disabled={loading}>
                    {loading ? 'Connexion...' : 'Se connecter'}
                </button>
            </form>

            <p>
                Pas encore de compte ? <Link to="/register">S'inscrire</Link>
            </p>
        </div>);
}